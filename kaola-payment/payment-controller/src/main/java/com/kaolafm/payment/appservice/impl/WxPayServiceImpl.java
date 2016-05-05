package com.kaolafm.payment.appservice.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.user.entity.UserInfo;
import com.kaolafm.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaolafm.counter.api.CounterService;
import com.kaolafm.payment.appservice.WxPayService;
import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.constants.CounterConstants;
import com.kaolafm.payment.dto.WxAppPayDto;
import com.kaolafm.payment.dto.WxpayDto;
import com.kaolafm.payment.entity.PayRechargePlan;
import com.kaolafm.payment.entity.PayTradeWeixinResult;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.request.OrderHandler;
import com.kaolafm.payment.service.IOrderService;
import com.kaolafm.payment.service.IUserCenterService;
import com.kaolafm.payment.service.IWxpayService;
import com.kaolafm.payment.utils.DateUtils;
import com.kaolafm.payment.utils.HttpClientUtils;
import com.kaolafm.payment.utils.IdBuildUtils;
import com.kaolafm.payment.utils.PropertiesUtils;
import com.kaolafm.payment.utils.ThreadLocalUtil;

@Service
public class WxPayServiceImpl implements WxPayService {

	public Logger log = LoggerFactory.getLogger(WxPayServiceImpl.class);
	public static Logger errorLog = LoggerFactory.getLogger("errorLog");

	public static String WAIT_PAY = "WAIT_PAY";
	public static String PAY_SUCCESS = "SUCCESS";
	public static String PAYERROR = "PAYERROR";
	private static String PAY_CLOSED = "CLOSED";

	@Autowired
	private IWxpayService rpcWxpayService;
	@Autowired
	private IOrderService rpcOrderService;
	@Autowired
	private IUserCenterService rpcUserCenterService;

	@Autowired
	private CounterService rpcCounterService;

	@Autowired
	private UserService rpcUserSerivce;

	@Override
	public WxAppPayDto getOrderId(Integer planid) throws Exception {
		WxAppPayDto appPayDto = null;
		String prepay_id = null;
		CommonParams params = ThreadLocalUtil.getCommonParams();
		Long uid = Long.valueOf(params.getUid());
		// 获取套餐费用
		PayRechargePlan rechargePlan = rpcOrderService.getPlan(planid);
		if (rechargePlan != null) {
			Double planFee = rechargePlan.getCashFee();
			if (rechargePlan.getPlanLimitType() == 1) {
				PayTrade trade = rpcUserCenterService.getTradeByUidAndPlanId(uid, planid);
				if (trade != null) {
					throw new ServiceException(Code.FIRST_RECHARGE_GIFT_REPEAT_USE);
				}
			}
			Integer total_fee = (int) (planFee * 100);
			String klOrder = null;
			try {
				klOrder = IdBuildUtils.buildFillIdPrefix(uid.toString()) + CounterConstants.PAY_MODULE_START_ID
						+ String.valueOf(rpcCounterService.incr(CounterConstants.PAY_MODULE_UNI_KEY, 1));
			} catch (Exception e) {
				log.info("计数器获取订单号失败");
				klOrder = OrderHandler.getKaolaOrderId(params.getUid());
				e.printStackTrace();
			}

			if (klOrder == null) {
				throw new ServiceException(Code.SERVER_ERROR);
			}

			UserInfo userInfo =  rpcUserSerivce.getUserInfoByUid(uid);
			if(userInfo == null){
				throw new ServiceException(Code.USER_NOT_EXIST);
			}

			String nonce_str = OrderHandler.generateString(16);
			Long timestamp = new Date().getTime() / 1000;
			OrderHandler orderHandler = new OrderHandler();
			orderHandler.setParameter("appid", PropertiesUtils.getValue("wxAppid"));
			orderHandler.setParameter("mch_id", PropertiesUtils.getValue("wxMchId"));
			orderHandler.setParameter("nonce_str", nonce_str);
			orderHandler.setParameter("notify_url", PropertiesUtils.getValue("wxNotifyUrl"));
			orderHandler.setParameter("trade_type", PropertiesUtils.getValue("wxTradeType"));
			orderHandler.setParameter("wxKey", PropertiesUtils.getValue("wxKey"));
			orderHandler.setParameter("out_trade_no", klOrder);
			orderHandler.setParameter("body", "考拉虚拟商品");
			orderHandler.setParameter("spbill_create_ip", params.getIp());
			orderHandler.setParameter("total_fee", total_fee.toString());
			orderHandler.setParameter("time_expire", DateUtils.getStrDateByMinute(10, new Date(), "yyyyMMddHHmmss"));

			String paramXML = orderHandler.getWXPayXml();
			String result = HttpClientUtils.sendPost(PropertiesUtils.getValue("wxOrderUrl"), paramXML);
			if (StringUtils.isNotBlank(result)) {
				try {
					Document document = DocumentHelper.parseText(result);
					if (document != null) {
						Element rootElement = document.getRootElement();
						if (rootElement != null && rootElement.elementText("return_code") != null
								&& rootElement.elementText("return_code").equals("SUCCESS")
								&& rootElement.elementText("result_code") != null
								&& rootElement.elementText("result_code").equals("SUCCESS")) {
							prepay_id = rootElement.elementText("prepay_id");
							log.info("获取预支付订单号,uid:{},预支付订单号:{}", uid, prepay_id);
							if (StringUtils.isNotBlank(prepay_id)) {
								// 保存微信预支付信息
								WxpayDto wxpayDto = new WxpayDto();
								wxpayDto.setFee(planFee);
								wxpayDto.setOrderId(klOrder);
								wxpayDto.setPlanId(planid);
								wxpayDto.setType(Constants.PAY_TYPE.WEIXIN.code());
								wxpayDto.setUid(uid);
								wxpayDto.setUserName(userInfo.getNickName());
								wxpayDto.setStatus(Constants.RECHARGE_STATUS.WAIT.code());
								wxpayDto.setAppid(PropertiesUtils.getValue("wxAppid"));
								wxpayDto.setMchId(PropertiesUtils.getValue("wxMchId"));
								wxpayDto.setSign(orderHandler.getWxSign());
								wxpayDto.setNonceStr(nonce_str);
								wxpayDto.setPrepayid(prepay_id);
								wxpayDto.setResultCode(WAIT_PAY);
								rpcWxpayService.savePrePayTradeWxResult(wxpayDto);

								// 返回客户端信息
								OrderHandler clientHandler = new OrderHandler();
								clientHandler.setParameter("appid", PropertiesUtils.getValue("wxAppid"));
								clientHandler.setParameter("partnerid", PropertiesUtils.getValue("wxMchId"));
								clientHandler.setParameter("wxKey", PropertiesUtils.getValue("wxKey"));
								clientHandler.setParameter("package", PropertiesUtils.getValue("wxPackage"));
								clientHandler.setParameter("noncestr", nonce_str);
								clientHandler.setParameter("timestamp", timestamp.toString());
								clientHandler.setParameter("prepayid", prepay_id);
								String clienSign = clientHandler.getWxSign();

								appPayDto = new WxAppPayDto(PropertiesUtils.getValue("wxAppid"),
										PropertiesUtils.getValue("wxMchId"), nonce_str,
										PropertiesUtils.getValue("wxPackage"), timestamp.toString(), prepay_id,
										clienSign);
								appPayDto.setOutTradeNo(klOrder);
							}
						} else {
							log.info("get orderid request parse document fail,return info:{}", result);
							throw new ServiceException(Code.SERVER_ERROR);
						}
					}
				} catch (DocumentException e) {
					log.info("get orderid request wxinterface ducoment exception,return info:{}", result);
					throw new ServiceException(Code.SERVER_ERROR);
				}
			} else {
				log.info("get orderid request wxinterface result null,return info:{}", result);
				throw new ServiceException(Code.SERVER_ERROR);
			}
		} else {
			throw new ServiceException(Code.PLAN_NULL);
		}
		return appPayDto;
	}

	@Override
	public boolean NotifyHandleResult(String resultInfo) throws ServiceException {
		boolean flag = false;
		try {
			if (StringUtils.isNotBlank(resultInfo)) {
				boolean returnXml = false; // 该变量用来验证通知的xml格式是否正确
				Document document = DocumentHelper.parseText(resultInfo);
				if (document != null) {
					Element rootElement = document.getRootElement();
					if (rootElement != null && rootElement.getName().equals("xml")) {
						if (rootElement.elementText("return_code") != null
								&& rootElement.elementText("return_code").equals("SUCCESS")) {
							returnXml = true;
							PayTradeWeixinResult result = this.convertToWeixinResult(rootElement);
							// 查询是否处理过
							PayTradeWeixinResult dbResult = rpcWxpayService.getWeixinResultByOutTradeNo(result
									.getOutTradeNo());
							if (dbResult != null && dbResult.getId() != null
									&& !dbResult.getResultCode().equals(WAIT_PAY)) {
								flag = true;
							} else {
								// 当result_code为成功时,验签
								if (result.getResultCode().equals("SUCCESS")) {
									if (validateSign(result)) {
										// 验证签名 通过则更新账户信息
										rpcWxpayService.updateOkTradeWxResult(result);
										flag = true;
										if (flag) {
											rpcOrderService.doPresentLeaf(result.getOutTradeNo());
										}
									} else {
										log.info("wxpay notify sign validate fail,notify resultInfo:{}", resultInfo);
										throw new ServiceException(Code.SIGN_ERROR);
									}
								} else {
									// 否则直接保存失败记录
									rpcWxpayService.closeTradeWxResult(result);
									flag = true;
								}
							}
						}
					}
				}
				if (!returnXml) {
					errorLog.info("wxpay notify xml format error,notify resultInfo:{}", resultInfo);
					throw new ServiceException(Code.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			errorLog.info("wxpay notify xml is null");
			throw new ServiceException(Code.SERVER_ERROR);
		}

		return flag;
	}

	private boolean validateSign(PayTradeWeixinResult result) {
		OrderHandler orderHandler = new OrderHandler();
		orderHandler.setParameter("appid", PropertiesUtils.getValue("wxAppid"));
		orderHandler.setParameter("mch_id", PropertiesUtils.getValue("wxMchId"));
		orderHandler.setParameter("wxKey", PropertiesUtils.getValue("wxKey"));
		orderHandler.setParameter("device_info", result.getDeviceInfo());
		orderHandler.setParameter("nonce_str", result.getNonceStr());
		orderHandler.setParameter("return_code", result.getReturnCode());
		orderHandler.setParameter("result_code", result.getResultCode());
		orderHandler.setParameter("err_code", result.getErrCode());
		orderHandler.setParameter("err_code_des", result.getErrCodeDes());
		orderHandler.setParameter("openid", result.getOpenId());
		orderHandler.setParameter("is_subscribe", result.getIsSubscribe());
		orderHandler.setParameter("trade_type", result.getTradeType());
		orderHandler.setParameter("bank_type", result.getBankType());
		orderHandler.setParameter("total_fee", result.getTotalFee() == null ? null : result.getTotalFee().toString());
		orderHandler.setParameter("fee_type", result.getFeeType());
		orderHandler.setParameter("cash_fee", result.getCashFee() == null ? null : result.getCashFee().toString());
		orderHandler.setParameter("cash_fee_type", result.getCashFeeType());
		orderHandler
				.setParameter("coupon_fee", result.getCouponFee() == null ? null : result.getCouponFee().toString());
		orderHandler.setParameter("coupon_count", result.getCouponCount() == null ? null : result.getCouponCount()
				.toString());
		orderHandler.setParameter("coupon_id_$n", result.getCouponIdN());
		orderHandler.setParameter("coupon_fee_$n", result.getCouponFeeN() == null ? null : result.getCouponFeeN()
				.toString());
		orderHandler.setParameter("transaction_id", result.getTransactionId());
		orderHandler.setParameter("out_trade_no", result.getOutTradeNo());
		orderHandler.setParameter("attach", result.getAttach());
		orderHandler.setParameter("time_end", result.getTimeEnd());

		String sign = orderHandler.getWxSign();
		String resultSign = result.getSign();
		if (sign.equals(resultSign)) {
			return true;
		} else {
			return false;
		}
	}

	private PayTradeWeixinResult convertToWeixinResult(Element rootElement) {
		String appid = rootElement.elementText("appid");
		String mch_id = rootElement.elementText("mch_id");
		String device_info = rootElement.elementText("device_info");
		String nonce_str = rootElement.elementText("nonce_str");
		String sign = rootElement.elementText("sign");
		String returnCode = rootElement.elementText("return_code");
		String result_code = rootElement.elementText("result_code");
		String err_code = rootElement.elementText("err_code");
		String err_code_des = rootElement.elementText("err_code_des");
		String openid = rootElement.elementText("openid");
		String is_subscribe = rootElement.elementText("is_subscribe");
		String trade_type = rootElement.elementText("trade_type");
		String bank_type = rootElement.elementText("bank_type");
		String total_fee = rootElement.elementText("total_fee");
		String fee_type = rootElement.elementText("fee_type");
		String cash_fee = rootElement.elementText("cash_fee");
		String cash_fee_type = rootElement.elementText("cash_fee_type");
		String coupon_fee = rootElement.elementText("coupon_fee");
		String coupon_count = rootElement.elementText("coupon_count");
		String coupon_id_n = rootElement.elementText("coupon_id_$n");
		String coupon_fee_n = rootElement.elementText("coupon_fee_$n");
		String transaction_id = rootElement.elementText("transaction_id");
		String out_trade_no = rootElement.elementText("out_trade_no");
		String attach = rootElement.elementText("attach");
		String time_end = rootElement.elementText("time_end");

		PayTradeWeixinResult result = new PayTradeWeixinResult(out_trade_no, appid, mch_id, device_info, nonce_str,
				sign, returnCode, result_code, err_code, err_code_des, openid, is_subscribe, trade_type, bank_type,
				Integer.valueOf(total_fee), fee_type, Integer.valueOf(cash_fee), cash_fee_type,
				coupon_fee == null ? null : Integer.valueOf(coupon_fee), coupon_count == null ? null
						: Integer.valueOf(coupon_count), coupon_id_n, coupon_fee_n == null ? null
						: Integer.valueOf(coupon_fee_n), transaction_id, attach, time_end);

		return result;
	}

	public static void main(String[] args) {
		try {
			FileInputStream fl = new FileInputStream(new File("C:\\Users\\kaolafm\\Desktop\\notify.txt"));

			StringBuffer info = new StringBuffer();
			BufferedInputStream buf = new BufferedInputStream(fl);
			byte[] buffer = new byte[1024];
			int iRead;
			while ((iRead = buf.read(buffer)) != -1) {
				info.append(new String(buffer, 0, iRead, "utf-8"));
			}
			String result = HttpClientUtils.sendPost("http://localhost:8080/payment-controller/payapi/wx/order/notify",
					info.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
