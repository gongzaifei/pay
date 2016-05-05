package com.kaolafm.payment.entity;

import java.util.Date;

public class PayGift extends BaseBean {

	private Long id;
	private String name;
	private Integer giftWorth;
	private Double exchangeRate;
	private String img;
	private Integer status;
	private Integer createby;
	private String createrName;
	private Date createDate;
	private Long updateby;
	private String updaterName;
	private Date updateDate;
	private Integer sort;

	public PayGift() {
	}

	public PayGift(String name, Integer giftWorth, Double exchangeRate, String img, Integer status, Integer createby,
			String createrName, Date createDate, Long updateby, String updaterName, Date updateDate, Integer sort) {
		this.name = name;
		this.giftWorth = giftWorth;
		this.exchangeRate = exchangeRate;
		this.img = img;
		this.status = status;
		this.createby = createby;
		this.createrName = createrName;
		this.createDate = createDate;
		this.updateby = updateby;
		this.updaterName = updaterName;
		this.updateDate = updateDate;
		this.sort = sort;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGiftWorth() {
		return giftWorth;
	}

	public void setGiftWorth(Integer giftWorth) {
		this.giftWorth = giftWorth;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreateby() {
		return createby;
	}

	public void setCreateby(Integer createby) {
		this.createby = createby;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateby() {
		return updateby;
	}

	public void setUpdateby(Long updateby) {
		this.updateby = updateby;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
