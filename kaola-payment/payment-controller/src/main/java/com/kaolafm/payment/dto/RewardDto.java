package com.kaolafm.payment.dto;

public class RewardDto extends BaseDto {

	private Long otherUid;
	private String otherNickName;
	private String otherAvatar;
	private Integer otherGender;
	private Integer leafCount;
	private String leafImg;

	public RewardDto() {
	}

	public RewardDto(Long otherUid, String otherNickName, String otherAvatar, Integer otherGender, Integer leafCount,
			String leafImg) {
		this.otherUid = otherUid;
		this.otherNickName = otherNickName;
		this.otherAvatar = otherAvatar;
		this.otherGender = otherGender;
		this.leafCount = leafCount;
		this.leafImg = leafImg;
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

	public Integer getOtherGender() {
		return otherGender;
	}

	public void setOtherGender(Integer otherGender) {
		this.otherGender = otherGender;
	}

	public Integer getLeafCount() {
		return leafCount;
	}

	public void setLeafCount(Integer leafCount) {
		this.leafCount = leafCount;
	}

	public String getLeafImg() {
		return leafImg;
	}

	public void setLeafImg(String leafImg) {
		this.leafImg = leafImg;
	}

}
