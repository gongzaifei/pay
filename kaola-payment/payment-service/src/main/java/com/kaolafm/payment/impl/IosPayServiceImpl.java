package com.kaolafm.payment.impl;

import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.dao.IOSPayDao;
import com.kaolafm.payment.dto.FillOrderDto;
import com.kaolafm.payment.entity.PayIosResult;
import com.kaolafm.payment.service.IOrderService;
import com.kaolafm.payment.service.IosPayService;
import org.omg.CORBA.REBIND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gongzf
 * @date 2016/3/24
 */
@Service
public class IosPayServiceImpl implements IosPayService {

    private Logger logger = LoggerFactory.getLogger(IosPayServiceImpl.class);
    @Autowired
    private IOrderService orderServiceImpl;

    @Autowired
    private IOSPayDao iosPayDao;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Boolean doSaveIosPay(FillOrderDto fillOrderDto) {
        try {
            PayIosResult result = new PayIosResult();
            result.setOutTradeNo(fillOrderDto.getOrderId());
            result.setStatus(fillOrderDto.getStatus());
            result.setTotalFee(fillOrderDto.getFee());
            Integer record = iosPayDao.save(result);
            if(record == 1){
                return  orderServiceImpl.saveTrade(fillOrderDto);
            }
        }catch (Exception e){
            logger.info("doSaveIosPayError",e);
            throw new RuntimeException();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Boolean doSaveIosPayResult(PayIosResult result) {
        if(result == null){
            logger.info("doSaveIosPayResultIsNull");
            throw new RuntimeException();
        }
        Integer record = iosPayDao.update(result);
        if(record != 1){
            logger.info("doUpdateIosPayResultError:"+result.getOutTradeNo());
            throw new RuntimeException();
        }
        return orderServiceImpl.doUserAccount(result.getOutTradeNo(), Constants.PAY_TYPE.IOSPAY.code());
    }

    @Override
    public PayIosResult getIosResult(String outTradeNo) {
        try{
           return iosPayDao.getIosResult(outTradeNo);
        }catch (Exception e){
            logger.info("getIosResultError",e);
        }
        return null;
    }

    @Override
    public PayIosResult getIosResultByMd5(String receiptMd5) {
        try{
            return iosPayDao.getIosResultByMd5(receiptMd5);
        }catch (Exception e){
            logger.info("getIosResultByMd5Error",e);
        }
        return null;
    }
}
