package com.kaolafm.payment.service;

import com.kaolafm.payment.dto.FillOrderDto;
import com.kaolafm.payment.entity.PayIosResult;

/**
 * @author gongzf
 * @date 2016/3/24
 */
public interface IosPayService {

    /**
     * 创建交易订单
     * @param fillOrderDto
     * @return
     */
    public Boolean  doSaveIosPay(FillOrderDto fillOrderDto);

    /**
     * 保存交易结果并更新交易状态
     * @param result
     * @return
     */
    public Boolean  doSaveIosPayResult(PayIosResult result);

    /**
     * 根据订单号查询充值
     * @param outTradeNo
     * @return
     */
    public PayIosResult getIosResult(String outTradeNo);

    /**
     * 凭证md5查询
     * @param receiptMd5
     * @return
     */
    public PayIosResult getIosResultByMd5(String receiptMd5);


}
