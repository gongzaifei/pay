package com.kaolafm.payment.appservice;

public interface WxPayService {

	public String getOrderId();

	public boolean updateWxOrder(StringBuffer info);
	

}
