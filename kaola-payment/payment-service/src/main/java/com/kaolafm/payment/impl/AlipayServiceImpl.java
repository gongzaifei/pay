package com.kaolafm.payment.impl;

import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.dao.AlipayDao;
import com.kaolafm.payment.dto.AlipayDto;
import com.kaolafm.payment.dto.FillOrderDto;
import com.kaolafm.payment.entity.PayAlipayResult;
import com.kaolafm.payment.service.IAlipayService;
import com.kaolafm.payment.service.IOrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gongzf
 * @date 2016/3/15
 */
@Service
public class AlipayServiceImpl implements IAlipayService {

    private Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);
    @Autowired
    private AlipayDao alipayDao;
    @Autowired
    private IOrderService orderServiceImpl;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Boolean doSaveAliOrder(FillOrderDto fillOrderDto) throws Exception {
        try{
            PayAlipayResult alipayResult = new PayAlipayResult();
            alipayResult.setOutTradeNo(fillOrderDto.getOrderId());
            alipayResult.setTotalFee(fillOrderDto.getFee());
            alipayResult.setTradeStatus("WAIT_BUYER_PAY");
            alipayResult.setSubject("考拉虚拟商品");
            Integer record = alipayDao.save(alipayResult);
            if(record == 1){
                return  orderServiceImpl.saveTrade(fillOrderDto);
            }
        }catch (Exception e){
            logger.info("doSaveAliOrderError",e);
            throw new RuntimeException();
        }

        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Boolean doSaveAliResult(AlipayDto alipayDto) {
        PayAlipayResult result = aliDtoToAliResult(alipayDto);
        if(result == null){
            logger.info("doSaveAliResultIsNull");
            throw new RuntimeException();
        }
        Integer record = alipayDao.update(result);
        if(record != 1){
            logger.info("doUpdateAliResultError:"+alipayDto.getOutTradeNo());
            throw new RuntimeException();
        }
        return  orderServiceImpl.doUserAccount(alipayDto.getOutTradeNo(),Constants.PAY_TYPE.ALIPAY.code());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Boolean doSaveClosedAliTrade(AlipayDto alipayDto) {
        PayAlipayResult result = aliDtoToAliResult(alipayDto);
        if(result == null){
            logger.info("doSaveAliResultIsNull");
            throw new RuntimeException();
        }
        Integer record = alipayDao.update(result);
        if(record != 1){
            logger.info("doUpdateAliResultError:"+alipayDto.getOutTradeNo());
            throw new RuntimeException();
        }
        return orderServiceImpl.closeTrade(alipayDto.getOutTradeNo());
    }

    private PayAlipayResult aliDtoToAliResult(AlipayDto alipayDto){
        if(alipayDto == null){
            return null;
        }
        PayAlipayResult result = new PayAlipayResult();
        result.setSubject(alipayDto.getSubject());
        result.setTradeStatus(alipayDto.getTradeStatus());
        result.setOutTradeNo(alipayDto.getOutTradeNo());
        result.setDiscount(alipayDto.getDiscount());
        result.setGmtCreate(alipayDto.getGmtCreate());
        result.setGmtPayment(alipayDto.getGmtPayment());
        result.setNotifyId(alipayDto.getNotifyId());
        result.setNotifyTime(alipayDto.getNotifyTime());
        result.setNotifyType(alipayDto.getNotifyType());
        result.setPrice(alipayDto.getPrice());
        result.setQuantity(alipayDto.getQuantity());
        result.setSellerEmail(alipayDto.getSellerEmail());
        result.setSign(alipayDto.getSign());
        result.setSignType(alipayDto.getSignType());
        result.setTradeNo(alipayDto.getTradeNo());
        result.setBuyerId(alipayDto.getBuyerId());
        result.setTotalFee(alipayDto.getTotalFee());
        return result;
    }
}
