package com.github.bluecatlee.gs4d.pay.caller.bean;

import java.io.Serializable;

public class ServiceRequest implements Serializable {
	private static final long serialVersionUID = -2438292342339076690L;
	private Long tenantNumId;

	private Long dataSign;

	private String plainParams;

	private String serviceName;

	private String methodName;

	private Object attach;// 员工内部编号,客户编号、微信号等
	
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

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

}
