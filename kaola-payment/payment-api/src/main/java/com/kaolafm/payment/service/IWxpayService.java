package com.kaolafm.payment.service;

import com.kaolafm.payment.dto.WxpayDto;
import com.kaolafm.payment.entity.PayTradeWeixinResult;

public interface IWxpayService {

	/**
	 * 获取微信通知记录
	 * @param out_trade_no
	 * @return
	 */
	public PayTradeWeixinResult getWeixinResultByOutTradeNo(String out_trade_no);

	/**
	 * 保存微信支付通知结果
	 * @param result
	 * @return 
	 * @throws Exception 
	 */
	public boolean closeTradeWxResult(PayTradeWeixinResult result) throws Exception;

	public boolean updateOkTradeWxResult(PayTradeWeixinResult result) throws Exception;

	public boolean savePrePayTradeWxResult(WxpayDto dto) throws Exception;

}
