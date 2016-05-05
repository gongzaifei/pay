package com.kaolafm.payment.entity;

import java.util.Date;

import com.kaolafm.payment.dto.BaseDto;

public class PayTradeWeixinResult extends BaseDto {

	private Long id;
	private String outTradeNo;
	private String appid;
	private String mchId;
	private String deviceInfo;
	private String nonceStr;
	private String sign;
	private String returnCode;
	private String resultCode;
	private String errCode;
	private String errCodeDes;
	private String openId;
	private String isSubscribe;
	private String tradeType;
	private String bankType;
	private Integer totalFee;
	private String feeType;
	private Integer cashFee;
	private String cashFeeType;
	private Integer couponFee;
	private Integer couponCount;
	private String couponIdN;
	private Integer couponFeeN;
	private String transactionId;
	private String attach;
	private String timeEnd;
	private Date createDate;
	private Date updateDate;

	public PayTradeWeixinResult() {
	}

	public PayTradeWeixinResult(String outTradeNo, String appid, String mchId, String deviceInfo, String nonceStr,
			String sign, String returnCode, String resultCode, String errCode, String errCodeDes, String openId,
			String isSubscribe, String tradeType, String bankType, Integer totalFee, String feeType, Integer cashFee,
			String cashFeeType, Integer couponFee, Integer couponCount, String couponIdN, Integer couponFeeN,
			String transactionId, String attach, String timeEnd) {
		this.outTradeNo = outTradeNo;
		this.appid = appid;
		this.mchId = mchId;
		this.deviceInfo = deviceInfo;
		this.nonceStr = nonceStr;
		this.sign = sign;
		this.returnCode = returnCode;
		this.resultCode = resultCode;
		this.errCode = errCode;
		this.errCodeDes = errCodeDes;
		this.openId = openId;
		this.isSubscribe = isSubscribe;
		this.tradeType = tradeType;
		this.bankType = bankType;
		this.totalFee = totalFee;
		this.feeType = feeType;
		this.cashFee = cashFee;
		this.cashFeeType = cashFeeType;
		this.couponFee = couponFee;
		this.couponCount = couponCount;
		this.couponIdN = couponIdN;
		this.couponFeeN = couponFeeN;
		this.transactionId = transactionId;
		this.attach = attach;
		this.timeEnd = timeEnd;
	}

	public PayTradeWeixinResult(Long id, String outTradeNo, String appid, String mchId, String deviceInfo,
			String nonceStr, String sign, String returnCode, String resultCode, String errCode, String errCodeDes,
			String openId, String isSubscribe, String tradeType, String bankType, Integer totalFee, String feeType,
			Integer cashFee, String cashFeeType, Integer couponFee, Integer couponCount, String couponIdN,
			Integer couponFeeN, String transactionId, String attach, String timeEnd) {
		this(outTradeNo, appid, mchId, deviceInfo, nonceStr, sign, returnCode, resultCode, errCode, errCodeDes, openId,
				isSubscribe, tradeType, bankType, totalFee, feeType, cashFee, cashFeeType, couponFee, couponCount,
				couponIdN, couponFeeN, transactionId, attach, timeEnd);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Integer getCashFee() {
		return cashFee;
	}

	public void setCashFee(Integer cashFee) {
		this.cashFee = cashFee;
	}

	public String getCashFeeType() {
		return cashFeeType;
	}

	public void setCashFeeType(String cashFeeType) {
		this.cashFeeType = cashFeeType;
	}

	public Integer getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(Integer couponFee) {
		this.couponFee = couponFee;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public String getCouponIdN() {
		return couponIdN;
	}

	public void setCouponIdN(String couponIdN) {
		this.couponIdN = couponIdN;
	}

	public Integer getCouponFeeN() {
		return couponFeeN;
	}

	public void setCouponFeeN(Integer couponFeeN) {
		this.couponFeeN = couponFeeN;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
