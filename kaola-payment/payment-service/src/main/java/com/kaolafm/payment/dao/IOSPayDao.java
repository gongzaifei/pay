package com.kaolafm.payment.dao;

import com.kaolafm.payment.entity.PayIosResult;

/**
 * @author gongzf
 * @date 2016/3/24
 */
public interface IOSPayDao {
    /**
     * 创建一条交易记录
     * @param result
     * @return
     */
    public Integer save(PayIosResult result);

    /**
     * 更新交易记录
     * @param result
     * @return
     */
    public Integer update(PayIosResult result);

    /**
     * 根据订单号查充值
     * @param outTradeNo
     * @return
     */
    public PayIosResult getIosResult(String outTradeNo);

    /**
     * 根据md5凭证查询充值
     * @param receiptMd5
     * @return
     */
    public PayIosResult getIosResultByMd5(String receiptMd5);



}
