package com.kaolafm.payment.dao;

import com.kaolafm.payment.entity.PayLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gongzf
 * @date 2016/3/29
 */
public interface WithdrawLogDao {
    /**
     * 批量插入操作日志
     * @param logList
     * @return
     */
    public Integer batchInsert(@Param("logList")List<PayLog> logList);

    /**
     * 某条提现的操作记录
     * @param dataId
     * @return
     */
    public List<PayLog> withdrawLogList(@Param("dataId") String dataId,@Param("dataType") Integer dataType);

    /**
     * 保存操作日志
     * @param log
     * @return
     */
    public Integer save(PayLog log);


    public List<PayLog> getWithdrawLogList(@Param("limitStart") Integer limitStart, @Param("limitEnd") Integer limitEnd);

    public Integer getWithdrawLogCount();
}
