package com.kaolafm.payment.dao;

import com.kaolafm.payment.entity.PayBatchWithdraw;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gongzf
 * @date 2016/4/20
 */
public interface BatchWithdrawDao {

    public Integer batchInsert(@Param("withdrawList") List<PayBatchWithdraw> withdrawList);


    public List<PayBatchWithdraw> getWithdrawInfo(@Param("batchNo") String batchNo);
}
