package com.kaolafm.payment.dto;

public class UserAccountDto extends BaseDto{

	private Integer greenLeafCount;
	private Integer goldLeafCount;
	private Double cash;

	public Integer getGreenLeafCount() {
		return greenLeafCount;
	}

	public void setGreenLeafCount(Integer greenLeafCount) {
		this.greenLeafCount = greenLeafCount;
	}

	public Integer getGoldLeafCount() {
		return goldLeafCount;
	}

	public void setGoldLeafCount(Integer goldLeafCount) {
		this.goldLeafCount = goldLeafCount;
	}

	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		this.cash = cash;
	}

}
