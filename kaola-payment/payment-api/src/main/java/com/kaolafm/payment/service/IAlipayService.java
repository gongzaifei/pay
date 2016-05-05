package com.kaolafm.payment.service;

import com.kaolafm.payment.dto.AlipayDto;
import com.kaolafm.payment.dto.FillOrderDto;

/**
 * @author gongzf
 * @date 2016/3/16
 */
public interface IAlipayService {
    /**
     * 生成支付账单
     * @param fillOrderDto
     * @return
     * @throws Exception
     */
    public Boolean doSaveAliOrder(FillOrderDto fillOrderDto) throws Exception;

    /**
     * 支付宝回调保存支付成功的交易
     * @param alipayDto
     * @return
     */
    public Boolean doSaveAliResult(AlipayDto alipayDto);

    /**
     * 保存交易关闭的支付账单
     * @param alipayDto
     * @return
     */
    public Boolean doSaveClosedAliTrade(AlipayDto alipayDto);
}
