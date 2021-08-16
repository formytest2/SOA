package com.github.bluecatlee.gs4d.gateway.model;

import java.io.Serializable;

public class ServiceRequest implements Serializable {

	private static final long serialVersionUID = 2192790275261533775L;

	/**
	 * 租户id
	 */
	private Long tenantNumId;

	/**
	 * 数据标识 0正式 1测试
	 */
	private Long dataSign;

	/**
	 * 参数
	 */
	private String plainParams;

	/**
	 * 服务名称 实际是beanId
	 */
	private String serviceName;

	/**
	 * 方法名称
	 */
	private String methodName;

	private Object attach;// 员工内部编号,客户编号、微信号等
	
	private String accessToken;

	public Long getTenantNumId() {
		return tenantNumId;
	}

	public void setTenantNumId(Long tenantNumId) {
		this.tenantNumId = tenantNumId;
	}

	public Long getDataSign() {
		return dataSign;
	}

	public void setDataSign(Long dataSign) {
		this.dataSign = dataSign;
	}

	public String getPlainParams() {
		return plainParams;
	}

	public void setPlainParams(String plainParams) {
		this.plainParams = plainParams;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object getAttach() {
		return attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
