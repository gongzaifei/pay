package com.kaolafm.payment.dao;

import com.kaolafm.payment.entity.PayAlipayResult;

/**
 * @author gongzf
 * @date 2016/3/12
 */
public interface AlipayDao {

    /**
     * 保存支付宝结果
     * @param alipayResult
     * @return
     */
    Integer save(PayAlipayResult alipayResult);

    /**
     * 根据订单号查询充值状态
     * @param outTradeNo
     * @return
     */
    PayAlipayResult getByOutTradeNo(String outTradeNo);

    /**
     * 支付宝回调成功后 更新表中支付宝回调信息
     * @param alipayResult
     * @return
     */
    Integer update(PayAlipayResult alipayResult);
}
