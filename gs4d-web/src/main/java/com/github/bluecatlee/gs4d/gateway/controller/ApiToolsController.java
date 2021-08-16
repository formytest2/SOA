package com.github.bluecatlee.gs4d.gateway.controller;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.gateway.bean.CommonCallTableInfoByCmd;
import com.github.bluecatlee.gs4d.gateway.bean.CommonCallTableInfoByFuncname;
import com.github.bluecatlee.gs4d.gateway.bean.CommonCallTableInfoBySysNumId;
import com.github.bluecatlee.gs4d.gateway.bean.ExArcSystem;
import com.github.bluecatlee.gs4d.gateway.model.ServiceRequest;
import com.github.bluecatlee.gs4d.gateway.service.ManageSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档工具控制器
 */
@Controller
@RequestMapping(value = "/apiTools")
public class ApiToolsController {

    private static final Logger log = LoggerFactory.getLogger(ApiToolsController.class);

    // 白名单 todo外部配置化
    private List<String> whiteList;

    public List<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    @Resource
    ManageSecurityService manageSecurityService;

    /**
     * 接口测试页
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "/", method = {RequestMethod.POST,RequestMethod.GET}, produces = "text/htm;charset=UTF-8")
    public String apiToolsIndex(HttpServletRequest request, ModelMap map) {
//		if (!manageSecurityService.isAuthorizeIp(request, whiteList)){
//			map.put("error", "未许可的访问！");
//			return "templates/error";
//		}
        // 系统列表在页面写死了
        List<ExArcSystem> exArcSystemList = manageSecurityService.getExArcSystemList();
        map.put("exArcSystemList", exArcSystemList);
        return "api/apiTools";
    }

    /**
     * 根据cmd查询接口信息 (返回方法全称)
     * @param httpRequest
     * @param httpResponse
     * @param cmd
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/queryApiNameByCmd", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> queryApiNameByCmd(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestParam(value = "cmd")String cmd) throws IOException {
        if (!manageSecurityService.isAuthorizeIp(httpRequest, whiteList)){
            httpResponse.sendRedirect("/apiTools");
            return null;
        }
        CommonCallTableInfoByCmd commonCallTableInfoByCmd = manageSecurityService.getCommonCallTableInfoByCmd(cmd);
        String method = cmd + "_" + commonCallTableInfoByCmd.getFuncname() + "_" + commonCallTableInfoByCmd.getRemark();
        commonCallTableInfoByCmd.setMethod(method);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("isSuccess", Boolean.TRUE);
        map.put("commonCallTableInfoByCmd", commonCallTableInfoByCmd);
        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }

    /**
     * 根据服务方法查询接口信息
     * @param httpRequest
     * @param httpResponse
     * @param funcname
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/queryApiNameByFuncname", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> queryApiNameByFuncname(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestParam(value = "funcname")String funcname) throws IOException {
        if (!manageSecurityService.isAuthorizeIp(httpRequest, whiteList)) {
            httpResponse.sendRedirect("/apiTools");
            return null;
        }
        CommonCallTableInfoByFuncname commonCallTableInfoByFuncname = manageSecurityService.getCommonCallTableInfoByFuncname(funcname);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("isSuccess", Boolean.TRUE);
        map.put("commonCallTableInfoByFuncname", commonCallTableInfoByFuncname);
        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }

    /**
     * 根据系统名称获取接口信息列表
     * @param httpRequest
     * @param httpResponse
     * @param sysNumId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/findApiNameListBySysNumId", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> findApiNameListBySysNumId(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestParam(value = "sysNumId")Integer sysNumId) throws IOException {
		if (!manageSecurityService.isAuthorizeIp(httpRequest, whiteList)){
			httpResponse.sendRedirect("/apiTools");
			return null;
		}
        List<CommonCallTableInfoBySysNumId> list = manageSecurityService.getcommonCallTableInfosBySysNumId(sysNumId);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("isSuccess", Boolean.TRUE);
        map.put("commonCallTableInfoBySysNumId", list);
        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }

    /**
     * 选择方法全称来查询接口信息
     * @param httpRequest
     * @param httpResponse
     * @param cmd
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/findByCmd", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> findByCmd(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestParam(value = "cmd")String cmd) throws IOException {
        if (!manageSecurityService.isAuthorizeIp(httpRequest, whiteList)){
            httpResponse.sendRedirect("/apiTools");
            return null;
        }
        CommonCallTableInfoByCmd commonCallTableInfoByCmd = manageSecurityService.getCommonCallTableInfoByCmd(cmd);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("isSuccess", Boolean.TRUE);
        map.put("commonCallTableInfoByCmd", commonCallTableInfoByCmd);
        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }

    /**
     * 提交测试(执行)
     * @param cmd
     * @param method
     * @param appKey
     * @param convertFlag
     * @param params
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/execute", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                     @RequestParam(value = "cmd", required = false) String cmd,
                                     @RequestParam(value = "method", required = false) String method,
                                     @RequestParam(value = "app_key", required = false) String appKey,
                                     @RequestParam(value = "convert_flag", required = false) String convertFlag,
                                     @RequestParam(value = "params", required = false) String params) throws IOException {

        if (!manageSecurityService.isAuthorizeIp(httpRequest, whiteList)) {
            httpResponse.sendRedirect("/apiTools");
            return null;
        }
        ServiceRequest serviceRequest = null;
        MessagePack messagePack = new MessagePack();
        long startTime=System.currentTimeMillis();
        try {
            boolean isCamelParam = false;
            if("Y".equals(convertFlag)){
                isCamelParam = true;
            }
            serviceRequest = manageSecurityService.getServiceRequest(cmd, method, appKey, params, false);
            messagePack = manageSecurityService.callMethodForNormal(serviceRequest, isCamelParam);
        } catch (Exception ex) {
            ExceptionUtil.processException(ex, messagePack);
        }
        String json = messagePack.toLowerCaseJson();
        if (log.isDebugEnabled()){
            long invocationTime = System.currentTimeMillis() - startTime;
            log.debug("/apiTools/execute cmd:{}, method:{}, invocationTime:{}ms, request:{}, response:{}", cmd, method, invocationTime,
                    serviceRequest == null ? params : serviceRequest.getPlainParams(), json);
        }
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }

    /**
     * 刷新appKey缓存
     * @param httpRequest
     * @param httpResponse
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/refreshChache", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> refreshChache(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        if (!manageSecurityService.isAuthorizeIp(httpRequest, whiteList)) {
            httpResponse.sendRedirect("/apiTools");
            return null;
        }
        manageSecurityService.clearAppSecretMap();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("isSuccess", Boolean.TRUE);
        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }

    // todo 生成文档接口

}
