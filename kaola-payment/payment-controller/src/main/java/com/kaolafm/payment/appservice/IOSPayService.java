package com.kaolafm.payment.appservice;

import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.user.exception.UserServiceException;

import java.util.Map;

/**
 * @author gongzf
 * @date 2016/3/22
 */
public interface IOSPayService {

    /**
     * 创建苹果支付订单
     * @param planId
     * @param commonParams
     * @return
     */
    public Map<String,String> getOrderId(Integer planId, CommonParams commonParams) throws ServiceException, UserServiceException;

    /**
     * ios支付验证接口
     * @param receipt
     * @param orderId
     * @param commonParams
     * @return
     */
    public boolean verifyIosReceipt(String receipt,String orderId,CommonParams commonParams);


}
