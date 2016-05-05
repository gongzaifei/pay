package com.kaolafm.payment.entity;

import java.util.Date;

public class PayUserGift extends BaseBean {

	private Long id;
	private Long senderUid;
	private Long receiverUid;
	private Long giftId;
	private String giftName;
	private String giftImg;
	private Integer giftWorth;
	private Double giftExchangeRate;
	private Integer quantity;
	private Date createDate;
	private String sender;
	private String receiver;

	public PayUserGift() {
	}

	public PayUserGift(Long senderUid, Long receiverUid, Long giftId, String giftName, String giftImg,
			Integer giftWorth, Double giftExchangeRate, Integer quantity, Date createDate) {
		this.senderUid = senderUid;
		this.receiverUid = receiverUid;
		this.giftId = giftId;
		this.giftName = giftName;
		this.giftImg = giftImg;
		this.giftWorth = giftWorth;
		this.giftExchangeRate = giftExchangeRate;
		this.quantity = quantity;
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(Long senderUid) {
		this.senderUid = senderUid;
	}

	public Long getReceiverUid() {
		return receiverUid;
	}

	public void setReceiverUid(Long receiverUid) {
		this.receiverUid = receiverUid;
	}

	public Long getGiftId() {
		return giftId;
	}

	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getGiftImg() {
		return giftImg;
	}

	public void setGiftImg(String giftImg) {
		this.giftImg = giftImg;
	}

	public Integer getGiftWorth() {
		return giftWorth;
	}

	public void setGiftWorth(Integer giftWorth) {
		this.giftWorth = giftWorth;
	}

	public Double getGiftExchangeRate() {
		return giftExchangeRate;
	}

	public void setGiftExchangeRate(Double giftExchangeRate) {
		this.giftExchangeRate = giftExchangeRate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
