package com.kaolafm.payment.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kaolafm.payment.entity.PayAccountWithdrawal;

public interface AccountWithdrawalDao {

	public Integer saveApplyCash(PayAccountWithdrawal accountWithDrawal);

	public List<PayAccountWithdrawal> getUserWithdrawalInfoDayByUid(Long uid);

	public Long getUserWithdrawalRecordCount(Map<String, Object> paramsMap);

	public List<PayAccountWithdrawal> getUserWithdrawalRecordList(Map<String, Object> paramsMap);

	/**
	 * 查询列表
	 * @param limitStart
	 * @param limitEnd
	 * @param status
	 * @param nickName
	 * @param aliAccount
	 * @param startDate
     * @param endDate
     * @return
     */
	public List<PayAccountWithdrawal> getWithdrawalList(@Param("limitStart") Integer limitStart, @Param("limitEnd") Integer limitEnd,
														@Param("status") Integer status, @Param("nickName")String nickName, @Param("aliAccount")String aliAccount,
														@Param("startDate")Date startDate,@Param("endDate") Date endDate);


	public Integer batchUpdate(@Param("ids") List<String> ids,@Param("reason") String reason,@Param("status") Integer status);

	public List<PayAccountWithdrawal> getUserWithdrawalInfoDayByUidLock(Long uid);

	/**
	 * 获取提现详情
	 * @param id
	 * @return
     */
	public PayAccountWithdrawal getWithdrawDetail(String id);

	/**
	 * 导出全部待转账提现
	 * @return
     */
	public List<PayAccountWithdrawal> getAllWithdraw();

	/**
	 * 更新状态
	 * @param payAccountWithdrawal
	 * @return
     */
	public Integer update(PayAccountWithdrawal payAccountWithdrawal);

	/**
	 * 总数
	 * @param status
	 * @param nickName
	 * @param aliAccount
	 * @param startDate
	 * @param endDate
     * @return
     */
	public Integer getWithdrawalListCount(@Param("status") Integer status, @Param("nickName")String nickName,
									   @Param("aliAccount")String aliAccount,@Param("startDate")Date startDate,
									   @Param("endDate") Date endDate);




	public List<PayAccountWithdrawal> getWithdrawByIds(@Param("ids") List<String> ids);
}
