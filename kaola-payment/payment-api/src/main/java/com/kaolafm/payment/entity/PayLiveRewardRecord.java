package com.kaolafm.payment.entity;

import java.util.Date;

public class PayLiveRewardRecord extends BaseBean {

	private Long id;
	private Long liveId;
	private Long liveProgramId;
	private Long senderId;
	private Integer leafAmount;
	private Date createDate;
	private Date updateDate;
	private Long anchorId;

	public PayLiveRewardRecord() {
	}

	public PayLiveRewardRecord(Long id, Long liveId, Long liveProgramId, Long senderId, Integer leafAmount,
			Date createDate, Date updateDate) {
		this.id = id;
		this.liveId = liveId;
		this.liveProgramId = liveProgramId;
		this.senderId = senderId;
		this.leafAmount = leafAmount;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLiveId() {
		return liveId;
	}

	public void setLiveId(Long liveId) {
		this.liveId = liveId;
	}

	public Long getLiveProgramId() {
		return liveProgramId;
	}

	public void setLiveProgramId(Long liveProgramId) {
		this.liveProgramId = liveProgramId;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Integer getLeafAmount() {
		return leafAmount;
	}

	public void setLeafAmount(Integer leafAmount) {
		this.leafAmount = leafAmount;
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

	public Long getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(Long anchorId) {
		this.anchorId = anchorId;
	}
}
