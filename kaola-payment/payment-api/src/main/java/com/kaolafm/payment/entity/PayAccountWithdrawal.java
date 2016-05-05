package com.kaolafm.payment.entity;

import java.util.Date;

public class PayAccountWithdrawal extends BaseBean {

	private String id;
	private Long uid;
	private Integer leafQuantity;
	private Double cashQuantity;
	private String alipayAccount;
	private String aliplayAccountName;
	private Integer status;
	private String createrName;
	private Date createDate;
	private Long updateBy;
	private String updaterName;
	private Date updateDate;
	private String reason;
	private Integer remainLeafQuantity;
	private Double remainCash;
	private Integer version;

	public PayAccountWithdrawal() {
	}

	public PayAccountWithdrawal(String id, Long uid, Integer leafQuantity, Double cashQuantity, String alipayAccount,
			String aliplayAccountName, Integer status, String createrName, Date createDate, Long updateBy,
			String updaterName, Date updateDate, String reason) {
		this.id = id;
		this.uid = uid;
		this.leafQuantity = leafQuantity;
		this.cashQuantity = cashQuantity;
		this.alipayAccount = alipayAccount;
		this.aliplayAccountName = aliplayAccountName;
		this.status = status;
		this.createrName = createrName;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updaterName = updaterName;
		this.updateDate = updateDate;
		this.reason = reason;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getLeafQuantity() {
		return leafQuantity;
	}

	public void setLeafQuantity(Integer leafQuantity) {
		this.leafQuantity = leafQuantity;
	}

	public Double getCashQuantity() {
		return cashQuantity;
	}

	public void setCashQuantity(Double cashQuantity) {
		this.cashQuantity = cashQuantity;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getAliplayAccountName() {
		return aliplayAccountName;
	}

	public void setAliplayAccountName(String aliplayAccountName) {
		this.aliplayAccountName = aliplayAccountName;
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

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getRemainLeafQuantity() {
		return remainLeafQuantity;
	}

	public void setRemainLeafQuatity(Integer remainLeafQuantity) {
		this.remainLeafQuantity = remainLeafQuantity;
	}

	public Double getRemainCash() {
		return remainCash;
	}

	public void setRemainCash(Double remainCash) {
		this.remainCash = remainCash;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "AccountWithdrawal [id=" + id + ", uid=" + uid + ", leafQuantity=" + leafQuantity + ", cashQuantity="
				+ cashQuantity + ", alipayAccount=" + alipayAccount + ", aliplayAccountName=" + aliplayAccountName
				+ ", status=" + status + ", createrName=" + createrName + ", createDate=" + createDate + ", updateBy="
				+ updateBy + ", updaterName=" + updaterName + ", updateDate=" + updateDate + "]";
	}

}
