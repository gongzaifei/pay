package com.kaolafm.payment.dto;

public class GiftDto extends BaseDto {

	private Long id;
	private String name;
	private Integer giftWorth;
	private String giftImg;

	public GiftDto() {
	}

	public GiftDto(Long id, String name, Integer giftWorth, String giftImg) {
		this.id = id;
		this.name = name;
		this.giftWorth = giftWorth;
		this.giftImg = giftImg;
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

	public String getGiftImg() {
		return giftImg;
	}

	public void setGiftImg(String giftImg) {
		this.giftImg = giftImg;
	}

}
