package com.kaolafm.payment.entity;

import java.util.Date;

/**
 * @author gongzf
 * @date 2016/3/16
 */
public class PayTrade extends BaseBean {
	private static final long serialVersionUID = -8909957695347556101L;
	private long id;
	private String tradeId;
	private Integer type;
	private long uid;
	private Double tradeSumFee;
	private Integer quantity;
	private Integer planId;
	private String planName;
	private Integer planLeafQuantity;
	private Integer planPresentLeaf;
	private Double planFee;
	private String planImg;
	private Integer status;
	private String createrName;
	private Date createDate;
	private String updateby;
	private String updaterName;
	private Date updateDate;
	private Integer version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public Double getTradeSumFee() {
		return tradeSumFee;
	}

	public void setTradeSumFee(Double tradeSumFee) {
		this.tradeSumFee = tradeSumFee;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public Integer getPlanLeafQuantity() {
		return planLeafQuantity;
	}

	public void setPlanLeafQuantity(Integer planLeafQuantity) {
		this.planLeafQuantity = planLeafQuantity;
	}

	public Integer getPlanPresentLeaf() {
		return planPresentLeaf;
	}

	public void setPlanPresentLeaf(Integer planPresentLeaf) {
		this.planPresentLeaf = planPresentLeaf;
	}

	public Double getPlanFee() {
		return planFee;
	}

	public void setPlanFee(Double planFee) {
		this.planFee = planFee;
	}

	public String getPlanImg() {
		return planImg;
	}

	public void setPlanImg(String planImg) {
		this.planImg = planImg;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
