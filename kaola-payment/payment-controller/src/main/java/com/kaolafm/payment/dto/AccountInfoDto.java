package com.kaolafm.payment.dto;

public class AccountInfoDto {
	private Long uid;
	private String avatar;
	private String nickName;
	private Integer gender;
	private Long count = 0l;
	private Integer isVanchor;

	public AccountInfoDto() {
	}

	public AccountInfoDto(Long uid, String avatar, Long count) {
		this.uid = uid;
		this.avatar = avatar;
		this.count = count;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getIsVanchor() {
		return isVanchor;
	}

	public void setIsVanchor(Integer isVanchor) {
		this.isVanchor = isVanchor;
	}

}
