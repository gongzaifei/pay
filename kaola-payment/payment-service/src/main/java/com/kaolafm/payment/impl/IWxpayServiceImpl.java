package com.kaolafm.payment.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.dao.WxpayDao;
import com.kaolafm.payment.dto.WxpayDto;
import com.kaolafm.payment.entity.PayTradeWeixinResult;
import com.kaolafm.payment.service.IOrderService;
import com.kaolafm.payment.service.IWxpayService;
@Service("wxpayServiceImpl")
public class IWxpayServiceImpl implements IWxpayService {

	private Logger logger = LoggerFactory.getLogger(IWxpayServiceImpl.class);

	@Autowired
	private WxpayDao wxpayDao;

	@Autowired
	private IOrderService iOrderService;

	@Override
	public PayTradeWeixinResult getWeixinResultByOutTradeNo(String outTradeNo) {
		PayTradeWeixinResult result = wxpayDao.getTradeWeixinResultByOutTradeNo(outTradeNo);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean closeTradeWxResult(PayTradeWeixinResult tradeWeixinResult) throws Exception {
		PayTradeWeixinResult dbWxResult = wxpayDao.getTradeWeixinResultByOutTradeNo(tradeWeixinResult.getOutTradeNo());
		if (dbWxResult != null) {
			BeanUtils.copyProperties(tradeWeixinResult, dbWxResult);
			Integer result = wxpayDao.updateTradeWeixinResult(dbWxResult);
			if (result > 0) {
				return iOrderService.closeTrade(tradeWeixinResult.getOutTradeNo());
			} else {
				logger.info("save weixin pay notify result error,outTradeNo:{}",tradeWeixinResult.getOutTradeNo());
				throw new RuntimeException();
			}
		} else {
			logger.info("save weixin pay notify result error,why:查无此单");
			throw new RuntimeException();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean updateOkTradeWxResult(PayTradeWeixinResult tradeWeixinResult) throws Exception {

		PayTradeWeixinResult dbWxResult = wxpayDao.getTradeWeixinResultByOutTradeNo(tradeWeixinResult.getOutTradeNo());
		if (dbWxResult != null) {
			BeanUtils.copyProperties(tradeWeixinResult, dbWxResult);
			dbWxResult.setCashFeeType(dbWxResult.getFeeType());
			Integer result = wxpayDao.updateTradeWeixinResult(dbWxResult);
			if (result > 0) {
				return iOrderService.doUserAccount(tradeWeixinResult.getOutTradeNo(), Constants.PAY_TYPE.WEIXIN.code());
			} else {
				logger.info("save weixin pay notify result error");
				throw new RuntimeException();
			}
		} else {
			logger.info("save weixin pay notify result error,why:查无此单");
			throw new RuntimeException();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean savePrePayTradeWxResult(WxpayDto dto) throws Exception {
		PayTradeWeixinResult result = new PayTradeWeixinResult();
		Integer total_fee = (int) (dto.getFee() * 100);
		result.setOutTradeNo(dto.getOrderId());
		result.setTotalFee(total_fee);
		result.setFeeType(dto.getFeeType());
		result.setAppid(dto.getAppid());
		result.setMchId(dto.getMchId());
		result.setNonceStr(dto.getNonceStr());
		result.setSign(dto.getSign());
		result.setResultCode(dto.getResultCode());
		result.setTradeType(dto.getTradeType());
		result.setCashFee(total_fee);
		result.setCashFeeType(dto.getFeeType());
		result.setTransactionId(dto.getPrepayid());
		result.setCreateDate(new Date());
		result.setUpdateDate(new Date());
		Integer record = wxpayDao.saveTradeWeixinResult(result);
		if (record > 0) {
			return iOrderService.saveTrade(dto);
		} else {
			logger.info("save weixin get prepay fail");
			throw new RuntimeException();
		}
	}

}
