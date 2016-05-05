package com.kaolafm.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kaolafm.payment.entity.PayUserAccount;

/**
 * @author gongzf
 * @date 2016/3/17
 */
public interface UserAccountDao {

    Integer save(PayUserAccount userAccount);

    Integer update(PayUserAccount userAccount);

    PayUserAccount selectLock(Long uid);

	PayUserAccount selectUserAccountByUid(Long uid);

	List<PayUserAccount> getUsreAccountPage(@Param("limitStart") Integer limitStart, @Param("limitEnd") Integer limitEnd);
}
