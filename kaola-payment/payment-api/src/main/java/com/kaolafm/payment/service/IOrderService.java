package com.kaolafm.payment.service;

import com.kaolafm.payment.dto.AlipayDto;
import com.kaolafm.payment.dto.FillOrderDto;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.entity.PayRechargePlan;
import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.payment.entity.PayUserGift;

/**
 * @author gongzf
 * @date 2016/3/15
 */
public interface IOrderService {

	/**
	 * 充值保存一条交易记录
	 * 
	 * @param fillOrderDto
	 * @return
	 */
	public boolean saveTrade(FillOrderDto fillOrderDto) throws Exception;

	/**
	 * 获取套餐充值金额
	 * 
	 * @param planId
	 * @return
	 */
	public Double getPlanFee(Integer planId);

	/**
	 * 获取支付宝订单信息
	 * 
	 * @param outTradeNo
	 * @return
	 */
	public AlipayDto getAlipay(String outTradeNo);

	/**
	 * 账户加钱
	 * 
	 * @param outTradeNo
	 * @param payType
	 * @return
	 */
	public boolean doUserAccount(String outTradeNo, Integer payType);

	/**
	 * 超时交易关闭
	 * 
	 * @param outTradeNo
	 * @return
	 */
	public boolean closeTrade(String outTradeNo);

	/**
	 * 获取充值套餐对象
	 * 
	 * @param planId
	 * @return
	 */
	public PayRechargePlan getPlan(Integer planId);

	/**
	 * 充值送叶子
	 * @param outTradeNo
	 */
	public void doPresentLeaf(String outTradeNo);

	/**
	 * 获取一条交易订单信息
	 * 
	 * @param outTradeNo
	 * @return
	 */
	public PayTrade getTrade(String outTradeNo);

	/**
	 * 获取一条打赏信息
	 * @param id
	 * @return
     */
	public PayUserGift getUserGift(Integer id);



	/**
	 * 获取交易记录列表
	 * @param payType
	 * @param payStatus
	 * @param orderid
	 * @param nickName
	 * @param planName
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public PageDto<PayTrade> getTradeRecordList(Integer payType, Integer payStatus, String orderid
												,String nickName,String planName, Integer pagenum, Integer pagesize);


	/**
	 * 礼物订单列表
	 * @param orderId
	 * @param giftName
	 * @param nickName
	 * @param anchorName
	 * @param pageNum
	 * @param pageSize
     * @return
     */
	public PageDto<PayUserGift> getUserGiftList(Integer orderId,String giftName,String nickName,String anchorName
											,Integer pageNum,Integer pageSize);


}
