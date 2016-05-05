package com.kaolafm.payment.dao;

import com.kaolafm.payment.entity.PayAccountBill;
import com.kaolafm.payment.entity.PayTrade;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @author gongzf
 * @date 2016/3/16
 */
public interface TradeDao {
	/**
	 * 保存交易记录
	 * 
	 * @param trade
	 * @return
	 */
	Integer save(PayTrade trade);

	/**
	 * 获取交易记录
	 * 
	 * @param tradeId
	 * @return
	 */
	PayTrade getTradeByTradeId(String tradeId);

	/**
	 * 更新交易状态
	 * 
	 * @param trade
	 * @return
	 */
	Integer update(PayTrade trade);

	/**
	 * 查询用户交易记录列表
	 * 
	 * @param uid
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<PayTrade> getUserTradeRecordList(Map<String, Object> paramMap);

	/**
	 * 获取用户交易记录总数
	 * 
	 * @param uid
	 * @return
	 */
	Long getUserTradeRecordCount(Map<String, Object> paramMap);

	Integer getTradeRecordCount(@Param("payType") Integer payType,@Param("payStatus") Integer payStatus,
								@Param("orderid") String orderid,@Param("nickName") String nickName,
								@Param("planName") String planName);

	List<PayTrade> getTradeRecordList(@Param("payType") Integer payType, @Param("payStatus") Integer payStatus,
									  @Param("orderid") String orderid,@Param("nickName") String nickName,
									  @Param("planName") String planName,
									  @Param("limitStart") Integer limitStart,@Param("limitEnd") Integer limitEnd);

	/**
	 * 新人套餐查询是否使用
	 * @param map
	 * @return
     */
	PayTrade getByUidAndPlanId(Map<String, Object> map);
}
