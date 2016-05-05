package com.kaolafm.payment.dao;

import com.kaolafm.payment.entity.PayRechargePlan;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author gongzf
 * @date 2016/3/16
 */
public interface RechargePlanDao {

    PayRechargePlan getPlanById(Integer planId);

    PayRechargePlan getPlanAll(Integer planId);

    List<PayRechargePlan> getRechargePlanList(Integer deviceType);

	Integer save(PayRechargePlan rechargePlan);

	Integer update(PayRechargePlan rechargePlan);

	PayRechargePlan getPlanByName(String name);

	Integer batchUpdatePlanStatus(@Param("planids") List<Long> planIds, @Param("status") Integer status);

	Integer count(@Param("deviceType") Integer deviceType,@Param("status") Integer status,@Param("name") String name);
	List<PayRechargePlan> pageRechargePlan(@Param("deviceType") Integer deviceType,@Param("status") Integer status,
							 @Param("name") String name,@Param("limitStart") Integer limitStart,@Param("limitEnd") Integer limitEnd);

}
