package com.github.bluecatlee.gs4d.gateway.service;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.gateway.bean.*;
import com.github.bluecatlee.gs4d.gateway.model.ServiceRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ManageSecurityService {

    /**
     * 验证前端参数并转换成服务请求参数 (不需要会话信息，如登陆，api工具等)
     * @param cmd       命令名称
     * @param method    方法名称
     * @param appKey    应用秘钥 适用于多租户系统来区分租户，也可以用来区分多应用
     * @param params    请求参数(base64加密)
     * @return
     */
    ServiceRequest getServiceRequest(String cmd, String method, String appKey, String params);

    /**
     * 验证前端参数并转换成服务请求参数 (不需要会话信息，如登陆，api工具等)
     * @param cmd
     * @param method
     * @param appKey
     * @param params
     * @param decodeParams  参数是否需要base64解码
     * @return
     */
    ServiceRequest getServiceRequest(String cmd, String method, String appKey, String params, boolean decodeParams);

    /**
     * 验证前端参数并转换成服务请求参数 (需要会话信息)
     * @param cmd       命令名称
     * @param method    方法名称
     * @param params    请求参数(base64加密)
     * @param timestamp 时间戳
     * @param sign      签名
     * @param sid       会话id
     * @return
     */
    ServiceRequest getServiceRequest(String cmd, String method, String params, String timestamp, String sign, String sid);

    /**
     * 根据sid获取租户、测试标识、用户编号、salt
     * @param sid 会话标识sessionId
     * @return
     */
    UserIdAndSalt getUserIdAndSaltBySid(String sid);

    /**
     * 封装请求class类和方法
     * @param clazz serviceBean的Class类型
     * @param name
     * @return
     */
    MethodAndRequestClass getMethodAndRequestClass(Class<?> clazz, String name);

    /**
     * 服务调用(网关用)
     * @param serviceRequest
     * @return
     */
    MessagePack callMethodForGateway(ServiceRequest serviceRequest);

    /**
     * 服务调用(可用于api工具、路由)
     * @param serviceRequest
     * @return
     */
    MessagePack callMethodForNormal(ServiceRequest serviceRequest);

    /**
     * 服务调用(可用于api工具、路由)
     * @param serviceRequest
     * @param isCamelParam      参数是否是驼峰
     * @return
     */
    MessagePack callMethodForNormal(ServiceRequest serviceRequest, boolean isCamelParam);

    /**
     * 是否授权ip
     * @param request
     * @return
     */
    boolean isAuthorizeIp(HttpServletRequest request);

    /**
     * 是否授权ip
     * @param request
     * @param whiteIpList   白名单
     * @return
     */
    boolean isAuthorizeIp(HttpServletRequest request, List<String> whiteIpList);

    /**
     * 获取系统列表
     * @return
     */
    List<ExArcSystem> getExArcSystemList();

    /**
     * 根据cmd获取接口
     * @param cmd
     * @return
     */
    CommonCallTableInfoByCmd getCommonCallTableInfoByCmd(String cmd);

    /**
     * 根据系统编号获取接口列表
     * @param sysNumId
     * @return
     */
    List<CommonCallTableInfoBySysNumId> getcommonCallTableInfosBySysNumId(Integer sysNumId);

    /**
     * 根据funcname获取接口
     * @param funcname
     * @return
     */
    CommonCallTableInfoByFuncname getCommonCallTableInfoByFuncname(String funcname);

    /**
     * 清除缓存的appKey与appSecret映射
     */
    void clearAppSecretMap();

}
