package com.kaolafm.payment.response;

import java.io.Serializable;

public class CommonResponse<T> implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 355207958304927788L;
	private String code = "10000";// 状态码
	private String message = "success";// 异常消息
	private T result;// 数据列表
	private Long serverTime = System.currentTimeMillis();

	public CommonResponse() {
	}

	public CommonResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

    public CommonResponse(T result) {
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}
	
	public Long getServerTime() {
		return serverTime;
	}

}
