package com.github.bluecatlee.gs4d.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.bluecatlee.gs4d.common.model.ValidationResult;
import com.github.bluecatlee.gs4d.common.valid.ValidationUtil;
import com.github.bluecatlee.gs4d.common.utils.id.IdGenerator;
import com.github.bluecatlee.gs4d.pay.api.PayService;
import com.github.bluecatlee.gs4d.pay.bean.BaseRequest;
import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;
import com.github.bluecatlee.gs4d.pay.config.PaymentConfig;
import com.github.bluecatlee.gs4d.pay.constant.*;
import com.github.bluecatlee.gs4d.pay.exception.BizException;
import com.github.bluecatlee.gs4d.pay.mapper.PayOrderInfoMapper;
import com.github.bluecatlee.gs4d.pay.mapper.PayParamLogMapper;
import com.github.bluecatlee.gs4d.pay.entity.PayOrderInfo;
import com.github.bluecatlee.gs4d.pay.entity.PayParamLogWithBLOBs;
import com.github.bluecatlee.gs4d.pay.service.PaymentService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.github.bluecatlee.gs4d.pay.constant.RequestMethodEnum.getType;

/**
 * 支付通用服务实现
 */
@Slf4j
@com.alibaba.dubbo.config.annotation.Service(retries = 0, timeout = 300000, interfaceClass = PayService.class)
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TaskExecutor taskExecutor;

    /**
     * 支付配置
     */
    @Autowired
    private PaymentConfig paymentConfig;

    /**
     * 支付信息服务
     */
    @Autowired
    private CallBackService callBackService;
    @Autowired
    private PayOrderInfoMapper payOrderInfoMapper;

    /**
     * 参数记录服务
     */
    @Autowired
    private PayParamLogMapper payParamLogMapper;

//    /**
//     * 订单通用服务
//     */
//    @Autowired
//    private MemorderCommonMapper memorderCommonMapper;

    // 支付类型id -> 支付实现service的映射关系
    @Autowired  // 注意这里有个spring的特性：spring装配Map类型时，会自动找到所有实现bean, 以beanName为key, bean为value存到map中
    private Map<String, PaymentService> payTypeId2ServicesMap;

    private static final Byte INIT_BUSINESS_CALLBACK_STATUS = 0;                        // 业务回调初始状态：0未回调

    private static final Map<String, Type> REQ_TYPE_CACHE = new HashMap<>();
    private static final Map<String, Class> REQUEST_TYPE_CACHE = new HashMap<>();
    private static final Map<String, Class> RESPONSE_TYPE_CACHE = new HashMap<>();

    /**
     * 统一入口
     * @param jsonParam
     * @param requestMethod
     * @return
     * @throws Exception
     */
    @Override
    public String pay(String jsonParam, String requestMethod) throws Exception {
    	
    	log.info(" paramMap = {}  , requestMethod = {} ", jsonParam, requestMethod);

        long startTime = System.currentTimeMillis();

        // 检查是否重复支付 TODO
        // request 重复 订单号_支付方式 重复
        // 支付处理 TODO 超时处理

        BaseRequest req = new BaseRequest();
        BaseResponse res = new BaseResponse();

        // 是否记录日志的标识
        boolean recodeFlag = false;

        PayParamLogWithBLOBs logInfo = new PayParamLogWithBLOBs();
        logInfo.setSeries(IdGenerator.nextId());
        logInfo.setRequestParams(jsonParam);
        logInfo.setRequestDate(new Date());
        String operatType = requestMethod;
        if (requestMethod != null && requestMethod.length() > 40) {
            operatType = operatType.substring(0, 40);
        }
        logInfo.setOperatType(operatType);

        PaymentService paymentService = null;
        String redisKey = "";
        try {
            log.debug("req:{}", req);
            if (StringUtils.isBlank(jsonParam) || StringUtils.isBlank(requestMethod)) {
                throw new BizException(RespEnum.ERROR_21);
            }

            // 取得支付方式
            String platType = getPlatType(jsonParam);
            req.setPlatType(platType);
            // 根据支付类型id取得支付service 【支付类型id->支付service实现的映射配置在配置文件中】
            paymentService = getPaymentService(platType);
            if (paymentService == null) {
                throw new BizException(RespEnum.ERROR_22);
            }

            log.debug("req:{}", req);
            // 解析入参 获取实际请求类型
            req = analyzeParams(jsonParam, paymentService, platType);
            log.debug("req:{}", req);
            if (req == null) {
                throw new BizException(RespEnum.ERROR_21);
            }

            // 获取实际响应类型
            res = getResponseInstance(paymentService, platType);

            log.debug("req:{}", req);
            // check 参数合法，验签等
            redisKey = checkInputParams(req, res, paymentService, requestMethod);
            log.debug("req:{}", req);

            recodeFlag = true;

            // 调用具体方法
            res = executeMethod(req, res, paymentService, requestMethod, redisKey);

        } catch (BizException e) {
            log.info("req:{}", req);
            res.setCode(e.getStatusCode());
            res.setMessage(e.getStatus());
            res.setTradeStatus(TradeStatusEnum.FAIL.getStatus());
            res.setTradeStatusRes(e.getStatus());

            recodeFlag = false;
            log.warn("业务错误  param : {} , method : {}, req : {}", jsonParam, requestMethod, req, e);
        } catch (Throwable e) {
            log.info("req:{}",req);
            res.setCode(RespEnum.ERROR_MINUS_100.getStatus());
            res.setMessage(RespEnum.ERROR_MINUS_100.getMsg() + ":" + e);
            res.setTradeStatus(TradeStatusEnum.ERROR.getStatus());
            res.setTradeStatusRes(TradeStatusEnum.ERROR.getMsg());

            recodeFlag = false;
            log.error("系统错误 param : {} , method : {}, req : {}", jsonParam, requestMethod, req, e);
        } finally {
            log.debug("req:{}", req);
            // 支付退款记录
            if (recodeFlag && (RequestMethodEnum.CREATE.getMethod().equals(requestMethod) || RequestMethodEnum.CANCEL.getMethod().equals(requestMethod))) {
                log.debug("req:{}",req);
                if (RespEnum.SUCCESS.getStatus().equals(res.getCode())) {
                    String key = "";
                    if (RequestMethodEnum.CREATE.getMethod().equals(requestMethod)) {
                        key = generateRedisKey(req, TradeTypeEnum.PAY.getStatus(), Constants.REDIS_KEY_STATUS_COMPLETED);

                    } else if (RequestMethodEnum.CANCEL.getMethod().equals(requestMethod)) {
                        key = generateRedisKey(req, TradeTypeEnum.REFUND.getStatus(), Constants.REDIS_KEY_STATUS_COMPLETED);
                    }
                    // 成功场合记录到redis
                    stringRedisTemplate.opsForValue().set(key, "0", Constants.REDIS_KEY_STATUS_COMPLETED_TIMEOUT, TimeUnit.SECONDS);
                }
                recordRequest(paymentService, req, res, requestMethod);
                log.debug("req:{}", req);
            }

            stringRedisTemplate.delete(redisKey);

            switch (getType(requestMethod)){
                case NOTIFY:
                    // todo 这里可以处理幂等问题(需要callbackNotify返回特定的序列号等信息，这里就不重复发起)， 也可以由业务回调处理逻辑解决幂等问题
                    callBackService.doAfterNotify(res);         // 异步回调之后需要发起业务回调
                    break;
            }

            // 记录请求
            recordRequestLog(req, res, logInfo);
        }

        res.setPlatType(Integer.valueOf(req.getPlatType()));
        res.clearReturn();          // 清除敏感信字段

        return JSON.toJSONString(res);
    }

    @Override
    public String repair() throws Exception {

        // 查询未支付的当天数据
        List<PayOrderInfo> lst = payOrderInfoMapper.selectProcessingData();

        // 处理成功的数据
        List<String> successedLst = new ArrayList<>();
        // 处理失败的数据
        List<String> failedLst = new ArrayList<>();

        // 处理数据，考虑到调用远程，并行处理比较好
        lst.parallelStream().forEach(payOrderInfo -> {

            // 取得支付方式
            String platType = String.valueOf(payOrderInfo.getPlatType());
            PaymentService paymentService = getPaymentService(platType);
            if (paymentService != null) {
                // 生成入参，出参
                BaseRequest req = null;
                BaseResponse res = null;

                try {
                    req = getRequestInstance(paymentService, platType);
                    res = getResponseInstance(paymentService, platType);

                    // 订单号
                    req.setOutTradeNo(payOrderInfo.getOutTradeNo());
                    // 原订单号
                    req.setSrcOutTradeNo(payOrderInfo.getSrcOutTradeNo());
                    // 门店号
                    req.setSubUnitNumId(String.valueOf(payOrderInfo.getSubUnitNumId()));
                    // 支付方式
                    req.setPlatType(platType);

                    // 取得交易类型
                    String tradeType = String.valueOf(payOrderInfo.getTradeType());
                    if (TradeTypeEnum.PAY.getStatus().equals(tradeType)) {
                        res = paymentService.queryPayResult(req, res);
                    } else if (TradeTypeEnum.REFUND.getStatus().equals(tradeType)) {
                        res = paymentService.queryRefundResult(req, res);
                    }

                    // 查询结果成功，则更新数据库
                    if (RespEnum.SUCCESS.getStatus().equals(res.getCode())) {
                        PayOrderInfo info = new PayOrderInfo();
                        info.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());
                        info.setTransactionId(res.getTransactionId());
                        info.setUpdateTime(new Date());
                        info.setSeries(payOrderInfo.getSeries());

                        payOrderInfoMapper.updateTradeStatusByPrimaryKey(info);
                        successedLst.add(String.valueOf(info.getSeries()));
                    } else {
                        failedLst.add(String.valueOf(payOrderInfo.getSeries()));
                    }

                } catch (IllegalAccessException | InstantiationException e) {
                    log.error("批处理支付中数据失败:" + e);
                    failedLst.add(String.valueOf(payOrderInfo.getSeries()));

                } catch (Exception e) {
                    log.error("批处理支付中数据失败:" + e);
                    failedLst.add(String.valueOf(payOrderInfo.getSeries()));
                }
            }
        });

        // 组织返回参数
        Map<String, List<String>> result = new HashMap();
        result.put("successed", successedLst);
        result.put("failed", failedLst);

        return JSONObject.toJSONString(result);
    }

    /**
     * 记录请求日志
     * @param req
     * @param res
     * @param logInfo
     * @throws Exception
     */
    private void recordRequestLog(BaseRequest req, BaseResponse res, PayParamLogWithBLOBs logInfo) throws Exception {
        log.info("req: {}", req);
        logInfo.setResponseDate(new Date());
        logInfo.setOutTradeNo(req.getOutTradeNo());
        logInfo.setResponseParams(res.getTradeStatusRes());
        logInfo.setChannel(req.getChannel());
        logInfo.setPlatType(Short.valueOf(req.getPlatType()));
        logInfo.setResponseCode(res.getCode());
        String msg = res.getMessage();
        logInfo.setResponseMsg((msg != null && msg.length()>200)?msg.substring(200):msg);
        taskExecutor.execute(() -> {
            payParamLogMapper.insert(logInfo);
        });
    }

    /**
     * 记录支付、退款请求
     * @param paymentService
     * @param req
     * @param res
     * @param requestMethod
     * @throws Exception
     */
    private void recordRequest(PaymentService paymentService, BaseRequest req, BaseResponse res, String requestMethod) throws Exception {

        PayOrderInfo info = new PayOrderInfo();
        long id = IdGenerator.nextId();
        res.setId(String.valueOf(id));
        info.setSeries(id);

        if (RequestMethodEnum.CANCEL.getMethod().equals(requestMethod) && req.getSrcPayOrderInfo() != null) {
            info.setTenantNumId(req.getSrcPayOrderInfo().getTenantNumId());
        } else {
            info.setTenantNumId((long) RandomUtils.nextInt(0, 4));  // 没必要随机吧...
        }
        info.setDataSign((byte) 0);
        info.setMerchantId(res.getMerchantId());
        info.setSubUnitNumId(Long.valueOf(req.getSubUnitNumId()));
        info.setPlatType(Integer.valueOf(req.getPlatType()));
        info.setSrcOutTradeNo(req.getSrcOutTradeNo());
        info.setOutTradeNo(req.getOutTradeNo());
        info.setRequestNo(req.getRequestNo());
        info.setTransactionId(res.getTransactionId());
        info.setTotalFee(res.getTotalFee());
        info.setTradeStatus(res.getTradeStatus());
        info.setTradeType(res.getTradeType());
        info.setDeviceInfo(req.getDeviceInfo());
//        info.setCloseSign();
        Date currentDate = new Date();
        info.setCreateDtme(currentDate);
        info.setUpdateTime(currentDate);
        info.setCreateUserId(0L);
        info.setItemBody(req.getItemBody());
        info.setOpenid(req.getOpenid());
        info.setAttach(req.getAttach());
        info.setSubject(req.getSubject());
//        info.setPayAppKey();
        info.setCreateIp(req.getCreateIp());
        String tradeStatusRes = res.getTradeStatusRes();
        String orgtradeStatusRes = tradeStatusRes;
        if (tradeStatusRes != null && tradeStatusRes.length() > 4000) {
            tradeStatusRes = tradeStatusRes.substring(0, 4000);
        }
        info.setTradeStatusRes(tradeStatusRes);
        info.setChannel(req.getChannel());
        info.setTxndate(res.getTxndate());
        info.setTxntime(res.getTxntime());
        info.setTranid(req.getTranId());
        info.setCancelSign((byte) 0);
        info.setExt1(res.getExt1());
        info.setExt2(res.getExt2());
        info.setExt3(res.getExt3());
        info.setExt4(res.getExt3());
        //业务回调 与 回调状态 0,未创建回调, 1 已经创建回调
        if (StringUtils.isNotBlank(req.getCallback())) {
        	info.setBusinessCallback(req.getCallback());
        	info.setBusinessCallbackStatus(INIT_BUSINESS_CALLBACK_STATUS);  // CallbackStatusEnum.NEW
//            info.setExt3(req.getCallback());
//            info.setExt4(0 + "");
        }
        taskExecutor.execute(() -> {
            try {
                payOrderInfoMapper.insert(info); // 记录订单付款信息 异步处理，防止三方付款成功了，由于数据问题 插入数据库失败导致 返回 错误信息。
                if (RequestMethodEnum.CREATE.getMethod().equals(requestMethod)) {
                    paymentService.afterPay(req, orgtradeStatusRes, id);                    // 支付后处理
                } else if (RequestMethodEnum.CANCEL.getMethod().equals(requestMethod)) {
                    paymentService.afterRefund(req, orgtradeStatusRes, id);                 // 退款后处理
                }
            } catch (Exception e) {
                log.error(" 记录付款订单失败  实体参数 ：  {}", JSON.toJSONString(info), e);
            }
        });
    }

    /**
     * 解析参数
     * @param paramMap
     * @param paymentService
     * @param platType
     * @return
     */
    private BaseRequest analyzeParams(String paramMap, PaymentService paymentService, String platType) {
        Type actualTypeArgument = REQ_TYPE_CACHE.get(platType);

        if (actualTypeArgument == null) {
            actualTypeArgument = ((ParameterizedType) paymentService.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            REQ_TYPE_CACHE.put(platType, actualTypeArgument);
        }

        log.info("end parse type {}", actualTypeArgument);
        return JSON.parseObject(paramMap, actualTypeArgument);
    }

    /**
     * 从参数中解析出支付类型
     * @param jsonParam
     * @return
     */
    private String getPlatType(String jsonParam) {
        int a = jsonParam.indexOf("platType");
        int b = jsonParam.indexOf(':', a);
        int c = jsonParam.indexOf(',', b + 1);
        if (c == -1) {
            c = jsonParam.indexOf('}', b + 1);
        }
        if (c == -1) {
            return null;
        }
        return jsonParam.substring(b + 1, c).replaceAll("\"", "").replaceAll("'", "").trim();

    }

    /**
     * 取得支付方式服务实现bean
     * @param platType
     * @return
     */
    public PaymentService getPaymentService(String platType) {
        return payTypeId2ServicesMap.get(paymentConfig.getPaymentTypes().get(platType));
    }

    /**
     * 通用的验证参数合法性
     * @param req
     * @param res
     * @param paymentService
     * @param requestMethod
     * @return
     * @throws Exception
     */
    private String checkInputParams(BaseRequest req, BaseResponse res, PaymentService paymentService, String requestMethod) throws Exception {
        // todo 基于请求序号的防重校验
//        // 重复提交check消费check， redis5分钟过期
//        String key = "payment.request_" + req.getRequestNo() + "_" + String.valueOf(req.getPlatType());
//
//        // 先查缓存request
//        boolean existRequest = stringRedisTemplate.opsForValue().setIfAbsent( key, "1", 300, TimeUnit.SECONDS);
//        if (existRequest) {
//            // 再查缓存正在处理的订单存在与否
//
//            // 最后检查数据库
//
//        } else {
//            throw new BizException();
//        }

        // 基本参数验证
        ValidationResult result = ValidationUtil.validateEntity(req);
        if (result.isHasErrors()) {
            throw new BizException(String.valueOf(RespEnum.ERROR_30.getStatus()), JSON.toJSONString(result.getErrorMsg()));
        }

        // 业务校验 以及业务层面的重复提交的校验
        String key = "";
        if (RequestMethodEnum.CREATE.getMethod().equals(requestMethod)) {
            if (StringUtils.isEmpty(req.getTranId())) {
                throw new BizException(String.valueOf(RespEnum.ERROR_30.getStatus()), "tranId不能为空");
            }
            if (StringUtils.isEmpty(req.getTotalFee())) {
                throw new BizException(String.valueOf(RespEnum.ERROR_30.getStatus()), "totalFee不能为空");
            }
            key = generateRedisKey(req, TradeTypeEnum.PAY.getStatus(), Constants.REDIS_KEY_STATUS_PROCESSING);

            if (stringRedisTemplate.hasKey(key)) {
                throw new BizException(RespEnum.ERROR_40);
            }
            // todo 视情况校验金额
        } else if (RequestMethodEnum.CANCEL.getMethod().equals(requestMethod)) {
            // todo 退款一般需要原单号
//            if (StringUtils.isEmpty(req.getSrcOutTradeNo())) {
//                throw new BizException(RespEnum.ERROR_30.getStatus(), "srcOutTradeNo不能为空");
//            }
            if (StringUtils.isEmpty(req.getTotalFee())) {
                throw new BizException(String.valueOf(RespEnum.ERROR_30.getStatus()), "totalFee不能为空");
            }
            key = generateRedisKey(req, TradeTypeEnum.REFUND.getStatus(), Constants.REDIS_KEY_STATUS_PROCESSING);
            if (stringRedisTemplate.hasKey(key)) {
                throw new BizException(RespEnum.ERROR_41);
            }
        }

        // 调用具体实现的校验逻辑
        paymentService.checkInputParams(req, res, requestMethod);

        return key;
    }

    /**
     * 执行
     * @param req
     * @param res
     * @param paymentService
     * @param requestMethod
     * @param key
     * @return
     * @throws Exception
     */
    private BaseResponse executeMethod(BaseRequest req, BaseResponse res, PaymentService paymentService, String requestMethod, String key) throws Exception {

        RequestMethodEnum rme = getType(requestMethod);

        switch (rme) {
            case NOTIFY:
                // 服务器通知
                stringRedisTemplate.opsForValue().set(key, "0", Constants.REDIS_KEY_STATUS_PROCESSING_TIMEOUT, TimeUnit.SECONDS);
                paymentService.callbackNotify(req, res);
                break;
            case CREATE:
                // 支付
                res.setTradeType(Byte.valueOf(TradeTypeEnum.PAY.getStatus()));
                stringRedisTemplate.opsForValue().set(key, "0", Constants.REDIS_KEY_STATUS_PROCESSING_TIMEOUT, TimeUnit.SECONDS);
                paymentService.pay(req, res);
                break;
            case CANCEL:
                // 退款
                res.setTradeType(Byte.valueOf(TradeTypeEnum.REFUND.getStatus()));
                stringRedisTemplate.opsForValue().set(key, "0", Constants.REDIS_KEY_STATUS_PROCESSING_TIMEOUT, TimeUnit.SECONDS);

                String srcOutTradeNo = req.getSrcOutTradeNo();

                // 可以在支付中台用单号去查原单
//                if (Strings.isNullOrEmpty(srcOutTradeNo)) {
//                    String outTradeNo = req.getOutTradeNo();
//                    try {
//                        Long outTradeNoL = Long.valueOf(outTradeNo.substring(0,outTradeNo.indexOf("_")));
//                        Map paramMap = new HashMap();
//                        paramMap.put("tmlNumId",outTradeNoL);
//                        paramMap.put("payTypeId",Integer.valueOf(req.getPlatType()));
//                        srcOutTradeNo = memorderCommonMapper.querySrcOutTradeNo(paramMap);
//                        req.setSrcOutTradeNo(srcOutTradeNo);
//                    } catch (NumberFormatException e) {
//                    }
//                }

                if (Strings.isNullOrEmpty(srcOutTradeNo)) {
                     throw new BizException(RespEnum.ERROR_42.getStatus(), "原始订单号为空，根据退款订单号，查询不到原始订单号");
                }

                // 查询原支付相关信息
                PayOrderInfo srcInfo = new PayOrderInfo()
                        .setOutTradeNo(srcOutTradeNo)
                        .setPlatType(Integer.valueOf(req.getPlatType()))
                        .setTradeType(Byte.valueOf(TradeTypeEnum.PAY.getStatus()));

                srcInfo = payOrderInfoMapper.selectPayInfoByOutTradeNo(srcInfo);

                req.setSrcPayOrderInfo(srcInfo);
                paymentService.refund(req, res);
                break;
            case QUERY:
                // 支付结果查询 TODO 查询需要优化 在redis里保留几分钟
                paymentService.queryPayResult(req, res);

                PayOrderInfo info = new PayOrderInfo();
                info.setOutTradeNo(req.getOutTradeNo());
                info.setPlatType(Integer.valueOf(req.getPlatType()));
                info.setTradeType(Byte.valueOf(TradeTypeEnum.PAY.getStatus()));

                info = payOrderInfoMapper.selectByPlatTypeAndOutTradeNo(info);
                // 查询结果成功，且数据库结果不是成功，更新数据库
                if (info != null) {
                    res.setId(String.valueOf(info.getSeries()));

                    if (RespEnum.SUCCESS.getStatus().equals(res.getCode()) && !TradeStatusEnum.SUCCESS.getStatus().equals(info.getTradeStatus())) {
                        info.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());
                        info.setTransactionId(res.getTransactionId());
                        info.setTotalFee(res.getTotalFee());
                        info.setUpdateTime(new Date());
                        payOrderInfoMapper.updateTradeStatusByPrimaryKey(info);
                    }
                }

                break;
            case QUERYR:
                // 退款结果查询 TODO 查询需要优化 在redis里保留几分钟
                paymentService.queryRefundResult(req, res);

                PayOrderInfo infor = new PayOrderInfo();
                infor.setOutTradeNo(req.getOutTradeNo());
                infor.setPlatType(Integer.valueOf(req.getPlatType()));
                infor.setTradeType(Byte.valueOf(TradeTypeEnum.REFUND.getStatus()));

                infor = payOrderInfoMapper.selectByPlatTypeAndOutTradeNo(infor);
                // 查询结果成功，且数据库结果不是成功，更新数据库
                if (infor != null) {
                    res.setId(String.valueOf(infor.getSeries()));

                    if (RespEnum.SUCCESS.getStatus().equals(res.getCode()) && !TradeStatusEnum.SUCCESS.getStatus().equals(infor.getTradeStatus())) {
                        infor.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());
                        infor.setTransactionId(res.getTransactionId());
                        infor.setUpdateTime(new Date());
                        infor.setTotalFee(res.getTotalFee());

                        payOrderInfoMapper.updateTradeStatusByPrimaryKey(infor);
                    }
                }

                break;
            default:
                throw new BizException(RespEnum.ERROR_22);

        }

        return res;

    }

    private BaseResponse getResponseInstance(PaymentService paymentService, String platType) throws IllegalAccessException, InstantiationException {

        Class clazz = RESPONSE_TYPE_CACHE.get(platType);
        if (clazz == null) {
            clazz = (Class) ((ParameterizedType) paymentService.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1];
            RESPONSE_TYPE_CACHE.put(platType, clazz);
        }

        return (BaseResponse)clazz.newInstance();
    }

    private BaseRequest getRequestInstance(PaymentService paymentService, String platType) throws IllegalAccessException, InstantiationException {

        Class clazz = REQUEST_TYPE_CACHE.get(platType);
        if (clazz == null) {
            clazz = (Class) ((ParameterizedType) paymentService.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            RESPONSE_TYPE_CACHE.put(platType, clazz);
        }

        return (BaseRequest)clazz.newInstance();
    }

    public static String generateRedisKey(BaseRequest req, String requestMethod, String type) {

        // payment_ + 方式key(进行中， 或者 完成的) + 支付方式 + 外部订单号 + 方式（支付，退款）
        // payment_plattype_outTradeNo_tradeType

        StringBuffer sb = new StringBuffer();
        sb.append("payment_").append(type).append("_").append(req.getPlatType()).append("_").append(req.getOutTradeNo()).append("_").append(requestMethod);
        return sb.toString();
    }

}
