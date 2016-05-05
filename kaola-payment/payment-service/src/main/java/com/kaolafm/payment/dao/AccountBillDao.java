package com.kaolafm.payment.dao;

import com.kaolafm.payment.entity.PayAccountBill;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @author gongzf
 * @date 2016/3/17
 */
public interface AccountBillDao {

	Integer save(PayAccountBill accountBill);


	Long getAccountBillLeafCount(@Param("uid") Long uid, @Param("type") Integer type);
}
