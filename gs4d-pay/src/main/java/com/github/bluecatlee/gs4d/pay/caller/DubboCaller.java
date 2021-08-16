package com.github.bluecatlee.gs4d.pay.caller;

import com.alibaba.fastjson.JSON;
import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.common.bean.AbstractUserOptionalSessionRequest;
import com.github.bluecatlee.gs4d.common.bean.AbstractUserSessionRequest;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;
import com.github.bluecatlee.gs4d.pay.caller.bean.MethodAndRequestClass;
import com.github.bluecatlee.gs4d.pay.caller.bean.ServiceMethod;
import com.github.bluecatlee.gs4d.pay.caller.bean.ServiceRequest;
import com.github.bluecatlee.gs4d.pay.constant.RespEnum;
import com.github.bluecatlee.gs4d.pay.entity.PayOrderInfo;
import com.github.bluecatlee.gs4d.pay.utils.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 调用业务回调方法demo (dubbo方式)
 *      具体调用过程与网关的实现类似 参考gs4d-web项目
 */
@Service
@Slf4j
@Deprecated
public class DubboCaller implements CallbackCaller {

    private static Map<String/*method/cmd*/, ServiceMethod> serviceMethodMap=new ConcurrentHashMap<String,ServiceMethod>();
    private static Map<String, MethodAndRequestClass> methodAndRequestTypeMap=new ConcurrentHashMap<String,MethodAndRequestClass>();
    private static MyJsonMapper mapper = MyJsonMapper.nonDefaultMapper();
    private static MyJsonMapper camelMapper = MyJsonMapper.nonDefaultMapper();
    private final static String SUB_SYSTEM="paymentplatform";

//    @Resource
//    private BaseInfoCommonService baseInfoCommonService;

    @Override
    public BaseResponse call(PayOrderInfo orderInfo, String businessCallback) throws Exception {

        BaseResponse response  = new BaseResponse();
        MessagePack messagePack = new MessagePack();

        try {
            String params = createCallBackRequestParam(orderInfo);
            ServiceRequest serviceRequest = getServiceRequest("",businessCallback,params);
            messagePack = callMethodForNormal(serviceRequest,false);

            if(messagePack.getCode() == MessagePack.OK){
                response.setCode(RespEnum.SUCCESS.getStatus());
                response.setMessage(messagePack.getMessage());
            } else {
                response.setCode(RespEnum.ERROR_992.getStatus());
                response.setMessage(messagePack.getMessage());
            }

        } catch (Exception e) {
            log.error(" PayOrderInfo : {} , businessCallback : {} ",orderInfo,businessCallback,e);
            response.setCode(RespEnum.ERROR_992.getStatus());
            response.setMessage(e.getMessage()+","+RespEnum.ERROR_992.getMsg()+"("+System.currentTimeMillis()+")");
        }

        return response;
    }



    //用户及密码换sid及salt或router/plain入参解析
    public ServiceRequest getServiceRequest(String cmd, String method, String params){
        log.info("cmd:{},method:{},params:{}",cmd,method,params);
        if (StringUtils.isEmpty(cmd) && StringUtils.isEmpty(method)) {
            throw new ValidateClientException(SUB_SYSTEM, ExceptionType.VCE10027,"未传入cmd或method");
        }

        if (StringUtils.isEmpty(params)) {
            throw new ValidateClientException(SUB_SYSTEM, ExceptionType.VCE10027,"未传入params");
        }
        ServiceMethod serviceMethod=getServiceMethodByCmdOrMethod(0L, cmd, method);

        ServiceRequest sr=new ServiceRequest();
        sr.setTenantNumId(6L);
        sr.setDataSign(0L);
        sr.setPlainParams(params);
        sr.setServiceName(serviceMethod.getServiceName());
        sr.setMethodName(serviceMethod.getServiceMethod());
        sr.setAttach(null);
        return sr;
    }

    //先从缓存取，取不到再按cmd获取服务及方法
    private ServiceMethod getServiceMethodByCmdOrMethod(Long dataSign, String cmd, String method){
        String serviceMethodKey=null;
        if(StringUtils.isEmpty(cmd)){
            serviceMethodKey=method+"_"+dataSign;
        }else{
            serviceMethodKey=cmd+"_"+dataSign;
        }
        ServiceMethod serviceMethod = serviceMethodMap.get(serviceMethodKey);
        if(serviceMethod==null){
            serviceMethod=doGetServiceMethodByCmdOrMethod(dataSign,cmd,method);
            serviceMethodMap.put(serviceMethodKey, serviceMethod);
        }
        return serviceMethod;
    }

    private ServiceMethod doGetServiceMethodByCmdOrMethod(Long dataSign,String cmd,String method){
//        if (!StringUtils.isEmpty(method)){
//            ServiceMethodByMethodGetRequest request=new ServiceMethodByMethodGetRequest();
//            request.setTenantNumId(0L);
//            request.setDataSign(dataSign);
//            request.setMethod(method);
//            ServiceMethodByMethodGetResponse response = baseInfoCommonService.getServiceMethodByMethod(request);
//            ExceptionUtil.checkDubboException(response);
//            return response.getServiceMethod();
//        } else {
//            ServiceMethodByCmdGetRequest request = new ServiceMethodByCmdGetRequest();
//            request.setTenantNumId(0L);
//            request.setDataSign(dataSign);
//            request.setCmd(cmd);
//            ServiceMethodByCmdGetResponse response = baseInfoCommonService.getServiceMethodByCmd(request);
//            ExceptionUtil.checkDubboException(response);
//            return response.getServiceMethod();
//        }
        return null;
    }

    //普通执行带转换开关(apiTools、router、router/plain)
    public MessagePack callMethodForNormal(ServiceRequest serviceRequest,boolean isCamelParam){
        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        MessagePack messagePack = new MessagePack();
        try {
            Long tenantNumId = serviceRequest.getTenantNumId();
            Long dataSign = serviceRequest.getDataSign();
            Object o = applicationContext.getBean(serviceRequest.getServiceName());
            if (o == null) {
                throw new ValidateClientException(SUB_SYSTEM,ExceptionType.VCE10027,"未定义的服务:" + serviceRequest.getServiceName());
            }
            String methodAndRequestTypeKey = serviceRequest.getServiceName() + "_" + serviceRequest.getMethodName();
            MethodAndRequestClass mp=methodAndRequestTypeMap.get(methodAndRequestTypeKey);
            if(mp == null) {
                mp = getMethodAndRequestClass(o.getClass(), serviceRequest.getMethodName());
                if (mp == null) {
                    throw new ValidateClientException(SUB_SYSTEM, ExceptionType.VCE10027, "未定义的方法:" + serviceRequest.getServiceName() + ":" + serviceRequest.getMethodName());
                }
                methodAndRequestTypeMap.put(methodAndRequestTypeKey, mp);
            }
            Method m = mp.getMethod();
            Class<? extends AbstractRequest> requestType=mp.getRequestClass();
            AbstractRequest myrequest;
            if (isCamelParam) {
                myrequest = camelMapper.fromJson(serviceRequest.getPlainParams(), requestType);
            } else {
                myrequest = mapper.fromJson(serviceRequest.getPlainParams(), requestType);
            }
            myrequest.setTenantNumId(tenantNumId);
            myrequest.setDataSign(dataSign);
            messagePack = (MessagePack)m.invoke(o, myrequest);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ie) {
            ExceptionUtil.processException(ie, messagePack);
        } catch (Exception ex) {
            ExceptionUtil.processException(ex, messagePack);
        }
        return messagePack;
    }

    public MethodAndRequestClass getMethodAndRequestClass(Class<?> clazz, String name) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = searchType.getMethods();
            for (Method method : methods) {
                if (name.equals(method.getName())) {
                    if (method.getParameterTypes().length == 1) {
                        Class<?> parameterType=method.getParameterTypes()[0];
                        if (AbstractUserSessionRequest.class.isAssignableFrom(parameterType)) {
                            @SuppressWarnings("unchecked")
                            Class<? extends AbstractUserSessionRequest> requestClass = (Class<? extends AbstractUserSessionRequest>)parameterType;
                            MethodAndRequestClass mp = new MethodAndRequestClass();
                            mp.setMethod(method);
                            mp.setRequestClass(requestClass);
                            mp.setTypeNumId(1);
                            return mp;
                        } else if (AbstractUserOptionalSessionRequest.class.isAssignableFrom(parameterType)) {
                            @SuppressWarnings("unchecked")
                            Class<? extends AbstractUserOptionalSessionRequest> requestClass = (Class<? extends AbstractUserOptionalSessionRequest>)parameterType;
                            MethodAndRequestClass mp = new MethodAndRequestClass();
                            mp.setMethod(method);
                            mp.setRequestClass(requestClass);
                            mp.setTypeNumId(2);
                            return mp;
                        } else if (AbstractUserSessionRequest.class.isAssignableFrom(parameterType)) {
                            @SuppressWarnings("unchecked")
                            Class<? extends AbstractUserSessionRequest> requestClass = (Class<? extends AbstractUserSessionRequest>)parameterType;
                            MethodAndRequestClass mp = new MethodAndRequestClass();
                            mp.setMethod(method);
                            mp.setRequestClass(requestClass);
                            mp.setTypeNumId(3);
                            return mp;
                        } else if (AbstractRequest.class.isAssignableFrom(parameterType)) {
                            @SuppressWarnings("unchecked")
                            Class<? extends AbstractRequest> requestClass = (Class<? extends AbstractRequest>)parameterType;
                            MethodAndRequestClass mp = new MethodAndRequestClass();
                            mp.setMethod(method);
                            mp.setRequestClass(requestClass);
                            mp.setTypeNumId(0);
                            return mp;
                        }
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    private String createCallBackRequestParam(PayOrderInfo orderInfo) throws Exception {
        Map<String,Object> paramMap = new HashMap<>();
        String outTradeNo =  orderInfo.getOutTradeNo();
        Double totalFee = orderInfo.getTotalFee(); // 单位分
//        if(StringUtils.isNotBlank(orderInfo.getExt1())){
//            String  couponFee = MathUtil.yuanToCent(orderInfo.getExt1());
//            paramMap.put("couponFee", Double.valueOf(couponFee));
//        }
//
//        String  totalFeeStr = MathUtil.yuanToCent(String.valueOf(totalFee));
        String tmlNumId = orderInfo.getTransactionId();// 其实是第三方单号
        paramMap.put("outTradeNo", outTradeNo);
        paramMap.put("tmlNumId", tmlNumId);
        paramMap.put("totalFee", totalFee);
        paramMap.put("tenantNumId", 6);
        paramMap.put("dataSign", 0);
//        if(TradeTypeEnum.PAY.getStatus().equals(String.valueOf(orderInfo.getTradeType()))){
//            paramMap.put("tradeStatus", Constants.APP_CALLBACK_TRADE_SUCCESS);
//        } else if(TradeTypeEnum.REFUND.getStatus().equals(String.valueOf(orderInfo.getTradeType()))){
//            paramMap.put("tradeStatus", Constants.APP_CALLBACK_REFUND_SUCCESS);
//        } else {
//            throw new BussinessException(RespEnum.ERROR_995);
//        }

        return JSON.toJSONString(paramMap);
    }
}
