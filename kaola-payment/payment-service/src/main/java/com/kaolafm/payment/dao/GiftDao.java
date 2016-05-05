package com.kaolafm.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kaolafm.payment.entity.PayGift;

public interface GiftDao {

	public Long getGiftCount(@Param("status") Integer status, @Param("name") String name);

	public List<PayGift> getGiftList(@Param("status") Integer status, @Param("name") String name, @Param("limitStart") Integer limitStart, @Param("limitEnd") Integer limitEnd);

	public PayGift getGift(Long giftId);

	public PayGift getGiftUse(Long giftId);

	public PayGift getGiftByName(String giftName);

	public Integer savePayGift(PayGift payGift);

	public Integer updatePayGift(PayGift payGift);

	public Integer getGiftMaxOrder();

	public boolean batchUpdateGift(@Param("giftIds") List<Long> giftIds,@Param("status") Integer status);

}
