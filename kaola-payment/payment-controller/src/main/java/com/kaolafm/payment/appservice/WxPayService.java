package com.kaolafm.payment.appservice;

import com.kaolafm.payment.dto.WxAppPayDto;
import com.kaolafm.payment.exception.ServiceException;

public interface WxPayService {

	/**
	 * 微信支付回调
	 * 
	 * @param info
	 * @return
	 * @throws ServiceException
	 */
	public boolean NotifyHandleResult(String info) throws ServiceException;

	/**
	 * 获取预支付订单
	 * 
	 * @param planid
	 * @return
	 * @throws Exception
	 */
	public WxAppPayDto getOrderId(Integer planid) throws Exception;

}
