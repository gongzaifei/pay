package com.kaolafm.payment.dto;

public class UserGiftDto {

	private Long otherUid;
	private String otherNickName;
	private String otherAvatar;
	private String giftName;
	private String giftImg;
	private Integer giftCount;
	private String updateTime;

	public UserGiftDto() {
	}

	public UserGiftDto(Long otherUid, String otherNickName, String otherAvatar, String giftName, String giftImg,
			Integer giftCount, String updateTime) {
		this.otherUid = otherUid;
		this.otherNickName = otherNickName;
		this.otherAvatar = otherAvatar;
		this.giftName = giftName;
		this.giftImg = giftImg;
		this.giftCount = giftCount;
		this.updateTime = updateTime;
	}

	public Long getOtherUid() {
		return otherUid;
	}

	public void setOtherUid(Long otherUid) {
		this.otherUid = otherUid;
	}

	public String getOtherNickName() {
		return otherNickName;
	}

	public void setOtherNickName(String otherNickName) {
		this.otherNickName = otherNickName;
	}

	public String getOtherAvatar() {
		return otherAvatar;
	}

	public void setOtherAvatar(String otherAvatar) {
		this.otherAvatar = otherAvatar;
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

	public Integer getGiftCount() {
		return giftCount;
	}

	public void setGiftCount(Integer giftCount) {
		this.giftCount = giftCount;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
