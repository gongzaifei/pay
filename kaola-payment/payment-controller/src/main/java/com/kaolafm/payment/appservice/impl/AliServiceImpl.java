package com.kaolafm.payment.appservice.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.user.entity.UserInfo;
import com.kaolafm.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaolafm.counter.api.CounterService;
import com.kaolafm.payment.appservice.AliService;
import com.kaolafm.payment.constants.AlipayConstant;
import com.kaolafm.payment.constants.CounterConstants;
import com.kaolafm.payment.dto.AlipayDto;
import com.kaolafm.payment.dto.FillOrderDto;
import com.kaolafm.payment.entity.PayRechargePlan;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.request.OrderHandler;
import com.kaolafm.payment.service.IAlipayService;
import com.kaolafm.payment.service.IOrderService;
import com.kaolafm.payment.service.IUserCenterService;
import com.kaolafm.payment.utils.AlipayCore;
import com.kaolafm.payment.utils.AlipayUtils;
import com.kaolafm.payment.utils.IdBuildUtils;

/**
 * @author gongzf
 * @date 2016/3/15
 */
@Service
public class AliServiceImpl implements AliService {

	private Logger logger = LoggerFactory.getLogger(AliServiceImpl.class);

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private IAlipayService rpcAliService;
	@Autowired
	private IOrderService rpcOrderService;
	@Autowired
	private IUserCenterService rpcUserCenterService;
	@Autowired
	private CounterService rpcCounterService;

	@Autowired
	private UserService rpcUserSerivce;

	@Override
	public Map<String, String> alipaySignAndParams(Integer planId, CommonParams commonParam) {
		try {
			// 获取套餐费用
			PayRechargePlan rechargePlan = rpcOrderService.getPlan(planId);
			if (rechargePlan == null) {
				throw new ServiceException(Code.PLAN_NULL);
			}
			Double fee = rechargePlan.getCashFee();
			if (rechargePlan.getPlanLimitType() == 1) {
				PayTrade trade = rpcUserCenterService.getTradeByUidAndPlanId(
						Long.valueOf(commonParam.getUid()), planId);
				if (trade != null) {
					throw new ServiceException(Code.FIRST_RECHARGE_GIFT_REPEAT_USE);
				}
			}
			// String orderId = IdBuildUtils.buildFillId(commonParam.getUid());

			String orderId = null;
			try {
				orderId = IdBuildUtils.buildFillIdPrefix(commonParam.getUid()) + CounterConstants.PAY_MODULE_START_ID
						+ String.valueOf(rpcCounterService.incr(CounterConstants.PAY_MODULE_UNI_KEY, 1));
			} catch (Exception e) {
				logger.info("计数器获取订单号失败");
				orderId = OrderHandler.getKaolaOrderId(commonParam.getUid());
				e.printStackTrace();
			}

			if (orderId == null) {
				throw new ServiceException(Code.SERVER_ERROR);
			}
			UserInfo userInfo =  rpcUserSerivce.getUserInfoByUid(Long.valueOf(commonParam.getUid()));
			if(userInfo == null){
				throw new ServiceException(Code.USER_NOT_EXIST);
			}
			FillOrderDto fillDto = new FillOrderDto();
			fillDto.setUid(userInfo.getUid());
			fillDto.setUserName(userInfo.getNickName());
			fillDto.setFee(fee);
			fillDto.setOrderId(orderId);
			fillDto.setPlanId(planId);
			fillDto.setType(0);
			fillDto.setStatus(0);
			Boolean flag = rpcAliService.doSaveAliOrder(fillDto);
			if (flag) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				map.put("partner", AlipayConstant.PARTERNER);
				map.put("seller_id", AlipayConstant.SELLER_ID);
				map.put("out_trade_no", "\"" + orderId + "\"");
				map.put("subject", AlipayConstant.SUBJECT);
				map.put("body", AlipayConstant.BODY);
				map.put("total_fee", "\"" + fee + "\"");
				map.put("notify_url", AlipayConstant.NOTIFY_URL);
				map.put("service", AlipayConstant.SERVICE);
				map.put("payment_type", AlipayConstant.PAYMENT_TYPE);
				map.put("_input_charset", AlipayConstant._INPUT_CHARSET);
				map.put("it_b_pay", AlipayConstant.IT_B_PAY);
				String content = AlipayCore.createString(map);
				logger.info("content" + content + " uid:" + commonParam.getUid());
				String signContent = AlipayUtils.sign(content, AlipayConstant.PRIVATE_KEY);
				signContent = URLEncoder.encode(signContent, "UTF-8");
				logger.info("signContent" + signContent + " uid:" + commonParam.getUid());
				map.put("sign", "\"" + signContent + "\"");
				map.put("sign_type", AlipayConstant.SIGN_TYPE);
				
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("outTradeNo", orderId);
				resultMap.put("url", AlipayCore.createString(map));
				return resultMap;
			}
		} catch (Exception e) {
			logger.info("alipaySignAndParamsError", e);
			return null;
		}

		return null;
	}

	@Override
	public boolean verifyAlipayNotify(HttpServletRequest request) throws UnsupportedEncodingException {
		Boolean flag = false;
		Map<String, String> paramMap = AlipayCore.dealRequestParams(request);
		String sign = paramMap.get("sign");
		String notify_id = paramMap.get("notify_id");
		String tradeStatus = paramMap.get("trade_status");
		String outTradeNo = paramMap.get("out_trade_no");
		logger.info("outTradeNo:" + outTradeNo + " notifyId:" + notify_id + " tradeStatus:" + tradeStatus);
		try {
			// 验证消息合法性
			String responseText = AlipayUtils.verifyResponse(notify_id);
			// 验证签名是否正确
			Boolean isSign = AlipayUtils.getSignVeryfy(paramMap, sign);
			if ("true".equals(responseText) && isSign) {
				// 交易成功处理逻辑
				if ((AlipayConstant.TRADE_STATUS_SUCCESS.equals(tradeStatus) || AlipayConstant.TRADE_STATUS_FINISHED
						.equals(tradeStatus))) {
					AlipayDto alipayDto = rpcOrderService.getAlipay(outTradeNo);
					if (alipayDto == null) {
						logger.info("alipayResultIsNull" + outTradeNo);
						return true;
					}
					if (tradeStatus.equals(alipayDto.getTradeStatus())) {
						logger.info("alipayResultSuccess" + outTradeNo);
						return true;
					}
					AlipayDto newAlipayDto = mapToAliDto(paramMap);
					flag = rpcAliService.doSaveAliResult(newAlipayDto);
					if (flag) {
						rpcOrderService.doPresentLeaf(outTradeNo);
					}
				}
				// 交易关闭处理逻辑
				if (AlipayConstant.TRADE_STATUS_CLOSED.equals(tradeStatus)) {
					AlipayDto alipayDto = rpcOrderService.getAlipay(outTradeNo);
					if (alipayDto == null) {
						logger.info("alipayResultIsNull" + outTradeNo);
						return true;
					}
					AlipayDto newAlipayDto = mapToAliDto(paramMap);
					flag = rpcAliService.doSaveClosedAliTrade(newAlipayDto);
				}
			} else {
				logger.info("alipaySignFail:" + outTradeNo + " responseText:" + responseText);
				flag = true;

			}
		} catch (Exception e) {
			logger.info("verifyAlipayNotifyError", e);
			flag = false;
		}
		return flag;
	}

	private AlipayDto mapToAliDto(Map<String, String> map) {
		try {
			AlipayDto alipayDto = new AlipayDto();
			String notifyTime = map.get("notify_time");
			String notifyType = map.get("notify_type");
			String notifyId = map.get("notify_id");
			String signType = map.get("sign_type");
			String sign = map.get("sign");
			String outTradeNo = map.get("out_trade_no");
			String subject = map.get("subject");
			String paymentType = map.get("payment_type");
			String tradeNo = map.get("trade_no");
			String tradeStatus = map.get("trade_status");
			String sellerId = map.get("seller_id");
			String sellerEmail = map.get("seller_email");
			String buyerId = map.get("buyer_id");
			String buyerEmail = map.get("buyer_email");
			String totalFee = map.get("total_fee");
			String quantity = map.get("quantity");
			String price = map.get("price");
			String body = map.get("body");
			String gmtCreate = map.get("gmt_create");
			String gmtPayment = map.get("gmt_payment");
			Date notifyTimeD = format.parse(notifyTime);
			alipayDto.setNotifyTime(notifyTimeD);
			if (StringUtils.isNotBlank(gmtPayment)) {
				alipayDto.setGmtPayment(format.parse(gmtPayment));
			}
			if (StringUtils.isNotBlank(gmtCreate)) {
				alipayDto.setGmtCreate(format.parse(gmtCreate));
			}
			alipayDto.setOutTradeNo(outTradeNo);
			alipayDto.setTradeNo(tradeNo);
			alipayDto.setSignType(signType);
			alipayDto.setSign(sign);
			alipayDto.setBuyerEmail(buyerEmail);
			alipayDto.setBuyerId(buyerId);
			alipayDto.setNotifyId(notifyId);
			alipayDto.setTotalFee(Double.valueOf(totalFee));
			alipayDto.setPrice(Double.valueOf(price));
			alipayDto.setTradeStatus(tradeStatus);
			alipayDto.setQuantity(Integer.valueOf(quantity));
			alipayDto.setPaymentType(paymentType);
			alipayDto.setSellerEmail(sellerEmail);
			alipayDto.setNotifyType(notifyType);
			return alipayDto;

		} catch (Exception e) {
			logger.info("mapToAliDtoError", e);
			return null;
		}
	}

}
