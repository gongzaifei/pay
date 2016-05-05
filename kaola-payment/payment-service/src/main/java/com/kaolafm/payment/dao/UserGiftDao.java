package com.kaolafm.payment.dao;

import java.util.List;
import java.util.Map;

import com.kaolafm.payment.entity.PayAccountBill;
import org.apache.ibatis.annotations.Param;

import com.kaolafm.payment.entity.PayUserGift;

public interface UserGiftDao {

	public Long getUserGiftListCount(Map<String, Object> paramsMap);

	public List<PayUserGift> getUserGiftPage(Map<String, Object> paramsMap);

	public Long getUserGiftSum(@Param("uid") Long uid, @Param("type") Integer type);

	public Integer save(PayUserGift userGift);

	public PayUserGift getUserGift(Integer id);

	public Integer count(@Param("orderId") Integer orderId,@Param("giftName") String giftName,
						 @Param("userName") String userName,@Param("anchorName") String anchorName);
	public List<PayUserGift> list(@Param("orderId") Integer orderId,@Param("giftName") String giftName,
								  @Param("userName") String userName,@Param("anchorName") String anchorName,
								  @Param("limitStart") Integer limitStart,@Param("limitEnd") Integer limitEnd);



}
