package com.kaolafm.payment.dto;

public class WxAppPayDto extends BaseDto {
	private String appid;
	private String partnerId;
	private String nonceStr;
	private String appPackage;
	private String timestamp;
	private String prepayid;
	private String sign;

	private String outTradeNo;

	public WxAppPayDto() {
	}

	public WxAppPayDto(String appid, String partnerId, String nonceStr, String appPackage, String timestamp,
			String prepayid, String sign) {
		this.appid = appid;
		this.partnerId = partnerId;
		this.nonceStr = nonceStr;
		this.appPackage = appPackage;
		this.timestamp = timestamp;
		this.prepayid = prepayid;
		this.sign = sign;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Override
	public String toString() {
		return "WxAppPayDto [appid=" + appid + ", partnerId=" + partnerId + ", nonceStr=" + nonceStr + ", appPackage="
				+ appPackage + ", timestamp=" + timestamp + ", prepayid=" + prepayid + ", sign=" + sign + "]";
	}

}
