package com.kaolafm.payment.appservice.impl;

import org.springframework.stereotype.Service;

import com.kaolafm.payment.appservice.WxPayService;
import com.kaolafm.payment.request.OrderHandler;
import com.kaolafm.payment.utils.HttpClientUtils;
import com.kaolafm.payment.utils.PropertiesUtils;

@Service
public class WxPayServiceImpl implements WxPayService {


	@Override
	public String getOrderId() {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public static String getOrderIddd() {

		String klOrder = OrderHandler.getKaolaOrderId();
		
		OrderHandler orderHandler = new OrderHandler();
		orderHandler.setParameter("appid", PropertiesUtils.getValue("wxAppid"));
		orderHandler.setParameter("mch_id", PropertiesUtils.getValue("wxMchId"));
		orderHandler.setParameter("nonce_str", PropertiesUtils.generateString(16).toUpperCase());
		orderHandler.setParameter("notify_url", PropertiesUtils.getValue("wxNotifyUrl"));
		orderHandler.setParameter("trade_type", PropertiesUtils.getValue("wxTradeType"));
		orderHandler.setParameter("out_trade_no", klOrder);
		orderHandler.setParameter("attach", "test");
		orderHandler.setParameter("body", "JSAPItest");
		orderHandler.setParameter("spbill_create_ip", "14.23.150.211");
		orderHandler.setParameter("total_fee", "1");
		
		String paramXML = orderHandler.getWXPayXml();
		System.out.println("xml=========" + paramXML);
		String result = HttpClientUtils.sendPost(PropertiesUtils.getValue("wxOrderUrl"), paramXML);
		System.out.println("result=========" + result);
		return "";
	}
	
	
	public static void main(String[] args) {
		getOrderIddd();
	}

	@Override
	public boolean updateWxOrder(StringBuffer info) {
		// TODO Auto-generated method stub
		return false;
	}
}
