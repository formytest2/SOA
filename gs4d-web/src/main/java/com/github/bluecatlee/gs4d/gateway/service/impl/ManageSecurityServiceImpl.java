package com.github.bluecatlee.gs4d.gateway.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.bluecatlee.gs4d.common.bean.*;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.utils.DigestsUtil;
import com.github.bluecatlee.gs4d.common.utils.Encodes;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.gateway.bean.*;
import com.github.bluecatlee.gs4d.gateway.constant.Constant;
import com.github.bluecatlee.gs4d.gateway.model.ServiceRequest;
import com.github.bluecatlee.gs4d.gateway.service.ManageSecurityService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("manageSecurityService")
public class ManageSecurityServiceImpl implements ManageSecurityService {

    private static final Logger log = LoggerFactory.getLogger(ManageSecurityServiceImpl.class);

    // 缓存appKey与appSecret的映射关系
    private static final Map<String, AppSecret> appSecretMap = new ConcurrentHashMap<>();
    // 缓存cmd/method与serviceMethod的映射关系
    private static final Map<String, ServiceMethod> serviceMethodMap = new ConcurrentHashMap<>();
    // 缓存beanId:method与实际class类的映射关系
    private static final Map<String, MethodAndRequestClass> methodAndRequestTypeMap = new ConcurrentHashMap<>();

    private static final MyJsonMapper mapper = MyJsonMapper.nonDefaultMapper();
    private static final MyJsonMapper camelMapper = MyJsonMapper.nonDefaultMapper();
    static {
        mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LowerCaseStrategy.SNAKE_CASE);
        camelMapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LowerCaseStrategy.LOWER_CAMEL_CASE);
    }

    @Resource
    private ApplicationContext ctx;

    @Override
    public ServiceRequest getServiceRequest(String cmd, String method, String appKey, String params) {
        return getServiceRequest(cmd, method, appKey, params, true);
    }

    @Override
    public ServiceRequest getServiceRequest(String cmd, String method, String appKey, String params, boolean decodeParams) {
        log.info("cmd:{}, method:{}, appKey:{}, params:{}", cmd, method, appKey, params);
        if (StringUtils.isEmpty(cmd) && StringUtils.isEmpty(method)) {
            throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未传入cmd或method");
        }
        if (StringUtils.isEmpty(appKey)) {
            throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未传入app_key");
        }
        if (StringUtils.isEmpty(params)) {
            throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未传入params");
        }
        AppSecret appSecret = getAppSecretByAppKey(appKey);

        if (decodeParams) {
            //BASE64解码
            String plainParams;
            try {
                plainParams = new String(new Base64().decode(params.getBytes()), "UTF-8");
            } catch (UnsupportedEncodingException e){
                log.error("参数base64解码失败,params:{}", params);
                log.error(e.getMessage(), e);
                throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "参数base64解码失败");
            }
            if (log.isDebugEnabled()){
                log.debug("param Base64解码后:" + plainParams);
            }
            params = plainParams;
        }

        ServiceMethod serviceMethod = getServiceMethodByCmdOrMethod(appSecret.getDataSign(), cmd, method);

        ServiceRequest sr = new ServiceRequest();
        sr.setTenantNumId(appSecret.getTenantNumId());
        sr.setDataSign(appSecret.getDataSign());
        sr.setPlainParams(params);
        sr.setServiceName(serviceMethod.getServiceName());
        sr.setMethodName(serviceMethod.getServiceMethod());
        sr.setAttach(null);
        return sr;
    }

    @Override
    public ServiceRequest getServiceRequest(String cmd, String method, String params, String timestamp, String sign, String sid) {
        log.info("cmd:{}, method:{}, params:{}, sign:{}, sid:{}, timestamp:{}", new Object[]{cmd, method, params, sign, sid, timestamp});
        if (StringUtils.isEmpty(cmd) && StringUtils.isEmpty(method)) {
            throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未传入cmd或method");
        } else if (StringUtils.isEmpty(params)) {
            throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未传入params");
        } else if (StringUtils.isEmpty(sid)) {
            throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未传入sid");
        } else if (StringUtils.isEmpty(timestamp)) {
            throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未传入时间戳");
        } else if (StringUtils.isEmpty(sign)) {
            throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未传入签名");
        } else {

            // todo 校验时间戳间隔时间不能超过1分钟 因此需要用户保证其客户端时间是几乎准确的
            // 请求时间间隔校验适用于服务端请求 不适用于c端用户请求

            UserIdAndSalt us = this.getUserIdAndSaltBySid(sid);
            Long userNumId = us.getUserId();
            String salt = us.getSalt();
            Long tenantNumId = us.getTenantNumId();
            Long dataSign = us.getDataSign();
            log.debug("salt:{}", salt);
            TreeMap<String, String> treeMap = new TreeMap();
            if (!StringUtils.isEmpty(cmd)) {
                treeMap.put("cmd", cmd);
            } else {
                treeMap.put("method", method);
            }

            treeMap.put("sid", sid);
            treeMap.put("params", params);
            treeMap.put("timestamp", timestamp);
            // todo 此处简化使用了用户密码对应的盐值来作为请求加密的秘钥  最好分开 否则会泄露用户密码的盐值
            String serverSign = sha1Signature(treeMap, salt);
            log.info("serverSign:{}", serverSign);

            // 如果对接口加签名 意味着登陆成功之后除了给前端返回会话id 还需要返回秘钥
            // 内容加签是有必要的 可以一定程度防请求重放攻击
            if (StringUtils.equals(serverSign, sign)) {
                throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "验签失败");
            }

            String plainParams;
            try {
                plainParams = new String((new Base64()).decode(params.getBytes()), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("参数base64解码失败,params:{}", params);
                log.error(e.getMessage(), e);
                throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "参数base64解码失败");
            }

            if (log.isDebugEnabled()) {
                log.debug("param Base64解码后:" + plainParams);
            }

            ServiceMethod serviceMethod = this.getServiceMethodByCmdOrMethod(dataSign, cmd, method);
            ServiceRequest sr = new ServiceRequest();
            sr.setTenantNumId(tenantNumId);
            sr.setDataSign(dataSign);
            sr.setPlainParams(plainParams);
            sr.setServiceName(serviceMethod.getServiceName());
            sr.setMethodName(serviceMethod.getServiceMethod());
            sr.setAttach(userNumId);
            return sr;
        }
    }

    @Override
    public UserIdAndSalt getUserIdAndSaltBySid(String sid) {
        // todo 待实现
        return null;
//        UserNumIdAndSaltGetBySidRequest request10=new UserNumIdAndSaltGetBySidRequest();
//        request10.setTenantNumId(0L);//此参数无用
//        request10.setDataSign(1L);//此参数无用
//        request10.setSid(sid);
//        UserNumIdAndSaltGetBySidResponse response10=userLoginService.getUserNumIdAndSaltBySid(request10);
//        ExceptionUtil.checkDubboException(response10);
//        UserIdAndSalt us=new UserIdAndSalt();
//        us.setTenantNumId(response10.getTenantNumId());
//        us.setDataSign(response10.getDataSign());
//        us.setUserId(response10.getUserNumId());
//        us.setSalt(response10.getSalt());
//        return us;
    }

    @Override
    public MethodAndRequestClass getMethodAndRequestClass(Class<?> clazz, String name) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = searchType.getMethods();
            for (Method method : methods) {
                if (name.equals(method.getName())){
                    if (method.getParameterTypes().length == 1) {
                        Class<?> parameterType = method.getParameterTypes()[0];
                        if (AbstractEmployeeSessionRequest.class.isAssignableFrom(parameterType)){
                            @SuppressWarnings("unchecked")
                            Class<? extends AbstractEmployeeSessionRequest> requestClass = (Class<? extends AbstractEmployeeSessionRequest>)parameterType;
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

    @Override
    public MessagePack callMethodForGateway(ServiceRequest serviceRequest) {
        MessagePack messagePack = new MessagePack();
        try {
            Long tenantNumId = serviceRequest.getTenantNumId();
            Long dataSign = serviceRequest.getDataSign();
            Object o = ctx.getBean(serviceRequest.getServiceName()); // 此处获取的bean是dubbo暴露出来的bean 可以直接调用
            if (o == null) {
                throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未定义的服务:" + serviceRequest.getServiceName());
            }
            String methodAndRequestTypeKey = serviceRequest.getServiceName() + "_" + serviceRequest.getMethodName();
            MethodAndRequestClass mp = methodAndRequestTypeMap.get(methodAndRequestTypeKey);
            if (mp == null) {
                mp = getMethodAndRequestClass(o.getClass(), serviceRequest.getMethodName());
                if (mp == null) {
                    throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未定义的方法:" + serviceRequest.getServiceName() + ":" + serviceRequest.getMethodName());
                }
                methodAndRequestTypeMap.put(methodAndRequestTypeKey, mp);
            }
            Method m = mp.getMethod();
            if (mp.getTypeNumId() == 1) {
                @SuppressWarnings("unchecked")
                Class<? extends AbstractEmployeeSessionRequest> requestType = (Class<? extends AbstractEmployeeSessionRequest>) mp.getRequestClass();
                AbstractEmployeeSessionRequest myrequest = mapper.fromJson(serviceRequest.getPlainParams(), requestType);
                myrequest.setTenantNumId(tenantNumId);
                myrequest.setDataSign(dataSign);
                myrequest.setUserNumId((Long) serviceRequest.getAttach());
                myrequest.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10000);
                messagePack = (MessagePack) m.invoke(o, myrequest);
            } else if (mp.getTypeNumId() == 3) {
                @SuppressWarnings("unchecked")
                Class<? extends AbstractUserSessionRequest> requestType = (Class<? extends AbstractUserSessionRequest>) mp.getRequestClass();
                AbstractUserSessionRequest myrequest = mapper.fromJson(serviceRequest.getPlainParams(), requestType);
                myrequest.setTenantNumId(tenantNumId);
                myrequest.setDataSign(dataSign);
                myrequest.setUsrNumId((Long) serviceRequest.getAttach());
                myrequest.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10000);
                messagePack = (MessagePack) m.invoke(o, myrequest);
            } else if (mp.getTypeNumId() == 2 && serviceRequest.getAttach() != null) {
                @SuppressWarnings("unchecked")
                Class<? extends AbstractUserOptionalSessionRequest> requestType = (Class<? extends AbstractUserOptionalSessionRequest>) mp.getRequestClass();
                AbstractUserOptionalSessionRequest myrequest = mapper.fromJson(serviceRequest.getPlainParams(), requestType);
                myrequest.setTenantNumId(tenantNumId);
                myrequest.setDataSign(dataSign);
                myrequest.setUsrNumId((Long) serviceRequest.getAttach());
                myrequest.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10000);
                messagePack = (MessagePack) m.invoke(o, myrequest);
            } else {
                Class<? extends AbstractRequest> requestType = mp.getRequestClass();
                AbstractRequest myrequest = mapper.fromJson(serviceRequest.getPlainParams(), requestType);
                myrequest.setTenantNumId(tenantNumId);
                myrequest.setDataSign(dataSign);
                myrequest.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10000);
                messagePack = (MessagePack) m.invoke(o, myrequest);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ie) {
            if (ie instanceof InvocationTargetException) {
                Exception exception = (Exception)((InvocationTargetException) ie).getTargetException();
                ExceptionUtil.processException(exception, messagePack);
            } else {
                ExceptionUtil.processException(ie, messagePack);
            }
        } catch (Exception ex) {
            ExceptionUtil.processException(ex, messagePack);
        }
        return messagePack;
    }

    @Override
    public MessagePack callMethodForNormal(ServiceRequest serviceRequest) {
        return callMethodForNormal(serviceRequest,false);
    }

    @Override
    public MessagePack callMethodForNormal(ServiceRequest serviceRequest, boolean isCamelParam) {
        MessagePack messagePack = new MessagePack();
        try {
            Long tenantNumId = serviceRequest.getTenantNumId();
            Long dataSign = serviceRequest.getDataSign();
            Object o = ctx.getBean(serviceRequest.getServiceName());
            if (o == null) {
                throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未定义的服务:" + serviceRequest.getServiceName());
            }
            String methodAndRequestTypeKey = serviceRequest.getServiceName() + "_" + serviceRequest.getMethodName();
            MethodAndRequestClass mp = methodAndRequestTypeMap.get(methodAndRequestTypeKey);
            if (mp == null) {
                mp = getMethodAndRequestClass(o.getClass(), serviceRequest.getMethodName());
                if (mp == null) {
                    throw new ValidateClientException(Constant.SUB_SYSTEM, ExceptionType.VCE10000, "未定义的方法:" + serviceRequest.getServiceName() + ":" + serviceRequest.getMethodName());
                }
                methodAndRequestTypeMap.put(methodAndRequestTypeKey, mp);
            }
            Method m = mp.getMethod();
            Class<? extends AbstractRequest> requestType = mp.getRequestClass();
            AbstractRequest myrequest;
            if (isCamelParam) {
                myrequest = camelMapper.fromJson(serviceRequest.getPlainParams(), requestType);
            } else {
                myrequest = mapper.fromJson(serviceRequest.getPlainParams(), requestType);
            }
            myrequest.setTenantNumId(tenantNumId);
            myrequest.setDataSign(dataSign);
            //myrequest.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10000);
            messagePack = (MessagePack)m.invoke(o, myrequest);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ie) {
            ExceptionUtil.processException(ie, messagePack);
        } catch (Exception ex) {
            ExceptionUtil.processException(ex, messagePack);
        }
        return messagePack;
    }

    @Override
    public boolean isAuthorizeIp(HttpServletRequest request) {
        String ipAddress = getIpAddr(request,false);
        if (log.isDebugEnabled()){
            log.debug("check isAuthorizeIp,ip:{}", ipAddress);
        }
        if (ipAddress.startsWith("192.168.") || ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equals("127.0.0.1"))  {
            return true;
        }
        if (log.isDebugEnabled()){
            log.debug("check isAuthorizeIp false");
        }
        return false;
    }

    @Override
    public boolean isAuthorizeIp(HttpServletRequest request, List<String> whiteIpList) {
        // todo 此处直接放行了
        return true;
    }

    @Override
    public List<ExArcSystem> getExArcSystemList() {
        // todo 获取系统名称列表
        return new ArrayList<>(0);
//        ExArcSystemResponse response = baseInfoCommonService.sysNumIdAndSysName();
//        ExceptionUtil.checkDubboException(response);
//        return response.getExArcSystemList();
    }

    @Override
    public CommonCallTableInfoByCmd getCommonCallTableInfoByCmd(String cmd) {
        return null;
//        CommonCallTableInfoByCmdRequest request = new CommonCallTableInfoByCmdRequest();
//        request.setCmd(cmd);
//        request.setTenantNumId(0L);
//        request.setDataSign(1L);
//        CommonCallTableInfoByCmdResponse response = this.baseInfoCommonService.commonCallTableInfoByCmd(request);
//        ExceptionUtil.checkDubboException(response);
//        return response.getCommonCallTableInfoByCmd();
    }

    @Override
    public List<CommonCallTableInfoBySysNumId> getcommonCallTableInfosBySysNumId(Integer sysNumId){
        // todo
        return new ArrayList<>(0);
//        CommonCallTableInfoBySysNumIdRequest request = new CommonCallTableInfoBySysNumIdRequest();
//        request.setSysNumId(sysNumId);
//        request.setTenantNumId(0L);//此参数无用
//        request.setDataSign(1L);//此参数无用
//        CommonCallTableInfoBySysNumIdResponse response = baseInfoCommonService.commonCallTableInfoBySysNumId(request);
//        ExceptionUtil.checkDubboException(response);
//        List<CommonCallTableInfoBySysNumId> list = response.getCommonCallTableInfoBySysNumId();
//        for (CommonCallTableInfoBySysNumId cc : list) {
//            cc.setFuncname(cc.getCmd() + "_" + cc.getFuncname() + "_" + cc.getRemark());
//        }
//        return list;
    }

    @Override
    public CommonCallTableInfoByFuncname getCommonCallTableInfoByFuncname(String funcname) {
        return null;
    }

    @Override
    public void clearAppSecretMap() {
        appSecretMap.clear();
    }

    /**
     * 根据appKey获取appSecret
     * @param appKey
     * @return
     */
    private AppSecret getAppSecretByAppKey(String appKey) {
        AppSecret appSecret = appSecretMap.get(appKey);
        if (appSecret == null) {
            appSecret = doGetAppSecretByAppKey(appKey);
            appSecretMap.put(appKey, appSecret);
        }
        return appSecret;
    }

    //按appKey获取秘钥
    private AppSecret doGetAppSecretByAppKey(String appKey) {
        // todo 待实现
        return null;
//        AppSecretGetRequest request = new AppSecretGetRequest();
//        request.setAppKey(appKey);
//        request.setTenantNumId(0L);//此两参数实际无用
//        request.setDataSign(1L);
//        AppSecretGetResponse response = baseInfoTenantService.getAppSecret(request);
//        ExceptionUtil.checkDubboException(response);
//        AppSecret appSecret = response.getAppSecret();
//        return appSecret;
    }

    /**
     * 根据cmd或method获取对应的服务及其方法
     * @param dataSign
     * @param cmd
     * @param method
     * @return
     */
    private ServiceMethod getServiceMethodByCmdOrMethod(Long dataSign, String cmd, String method) {
        String serviceMethodKey = null;
        if (StringUtils.isEmpty(cmd)) {
            serviceMethodKey = "method_" + method + "_" + dataSign;
        } else {
            serviceMethodKey = "cmd_" + cmd + "_" + dataSign;
        }
        ServiceMethod serviceMethod = serviceMethodMap.get(serviceMethodKey);
        if (serviceMethod == null) {
            serviceMethod = doGetServiceMethodByCmdOrMethod(dataSign, cmd, method);
            serviceMethodMap.put(serviceMethodKey, serviceMethod);
        }
        return serviceMethod;
    }

    private ServiceMethod doGetServiceMethodByCmdOrMethod(Long dataSign, String cmd, String method) {
        // todo 待实现
        return null;
//        if (!StringUtils.isEmpty(method)) {
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
    }

    /**
     * 生成sha1签名 todo 自定义
     * @param params
     * @param secret
     * @return
     */
    private static String sha1Signature(TreeMap<String, String> params, String secret) {
        String result = null;
        StringBuffer origin = assembleParams(params, new StringBuffer(secret));
        if (origin == null) {
            return result;
        }
        origin.append(secret);
        log.debug("before encode sha1 origin:{}", origin);
        //SHA1加密
        result = Encodes.encodeHex(DigestsUtil.sha1(origin.toString().getBytes()));
        return result;
    }

    /**
     * 组装参数
     * @param params
     * @param secret
     * @return
     */
    private static StringBuffer assembleParams(TreeMap<String, String> params, StringBuffer secret) {
        if (params == null) {
            return null;
        }
        Map<String, String> treeMap = new TreeMap<String, String>();
        treeMap.putAll(params);
        Iterator<String> iter = treeMap.keySet().iterator();
        while (iter.hasNext()) {
            String k = (String) iter.next();
            secret.append(k).append(params.get(k));
        }
        return secret;
    }

    /**
     * 获取请求的ip地址
     * @param request
     * @param convertToExternalIp
     * @return
     */
    private String getIpAddr(HttpServletRequest request, boolean convertToExternalIp) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            } else {
		    	/*
		    	if (log.isDebugEnabled()){
		    		log.debug("ip Proxy-Client-IP:{}",ip);
		    	}
		    	*/
            }
        } else {
	    	/*
	    	if (log.isDebugEnabled()){
	    		log.debug("ip X-Forwarded-For:{}",ip);
	    	}
	    	*/
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            } else {
		    	/*
		    	if (log.isDebugEnabled()){
		    		log.debug("ip WL-Proxy-Client-IP:{}",ip);
		    	}
		    	*/
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            } else {
		    	/*
		    	if (log.isDebugEnabled()){
		    		log.debug("ip HTTP_CLIENT_IP:{}",ip);
		    	}
		    	*/
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            } else {
                if (log.isDebugEnabled()){
                    log.debug("ip HTTP_X_FORWARDED_FOR:{}",ip);
                }
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (log.isDebugEnabled()) {
                log.debug("ip RemoteAddr:{}", ip);
            }
        }
        //内部测试用 todo 内部网段配置外部化
//        if (convertToExternalIp && ip.startsWith("192.168")) {
//            ip = "218.4.214.114";
//            if (log.isDebugEnabled()) {
//                log.debug("ip startWith 192.168, new ip:{}", ip);
//            }
//        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) { //"***.***.***.***".length() = 15
            if(ip.indexOf(",") > 0){
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        log.debug("ip:" + ip);
        return ip;
    }

}
