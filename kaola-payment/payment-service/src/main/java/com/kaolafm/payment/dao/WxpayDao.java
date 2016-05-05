package com.kaolafm.payment.dao;

import com.kaolafm.payment.entity.PayTradeWeixinResult;

public interface WxpayDao {
	
	public PayTradeWeixinResult getTradeWeixinResultByOutTradeNo(String outTradeNo);

	public Integer saveTradeWeixinResult(PayTradeWeixinResult tradeWeixinResult);

	public Integer updateTradeWeixinResult(PayTradeWeixinResult dbWxResult);

}
