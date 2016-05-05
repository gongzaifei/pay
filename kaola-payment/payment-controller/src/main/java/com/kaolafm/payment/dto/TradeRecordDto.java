package com.kaolafm.payment.dto;

public class TradeRecordDto extends BaseDto {

	private String outTradeNo; // 商户订单号
	private String transactionId; // 第三方交易号
	private String productName; // 商品名称
	private Integer leafCount; // 叶子数
	private Integer presentLeafCount = 0; // 赠送的叶子数
	private Integer tradeType; // 0支付宝 1微信 2苹果支付
	private Integer operationType;// 1 充值 2提现
	private Double money; // 人民币
	private String tradeTime; // 交易时间
	private String statusDesc; // 交易状态 0 失败 1成功 2审核中或充值中
	private String reason; // 失败原因

	public TradeRecordDto(String outTradeNo, String transactionId, String productName, Integer leafCount,
			Integer presentLeafCount, Integer tradeType, Integer operationType, Double money, String tradeTime,
			String statusDesc, String reason) {
		this.outTradeNo = outTradeNo;
		this.transactionId = transactionId;
		this.productName = productName;
		this.leafCount = leafCount;
		this.presentLeafCount = presentLeafCount;
		this.tradeType = tradeType;
		this.operationType = operationType;
		this.money = money;
		this.tradeTime = tradeTime;
		this.statusDesc = statusDesc;
		this.reason = reason;
	}

	public TradeRecordDto() {
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getLeafCount() {
		return leafCount;
	}

	public void setLeafCount(Integer leafCount) {
		this.leafCount = leafCount;
	}

	public Integer getPresentLeafCount() {
		return presentLeafCount;
	}

	public void setPresentLeafCount(Integer presentLeafCount) {
		this.presentLeafCount = presentLeafCount;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
