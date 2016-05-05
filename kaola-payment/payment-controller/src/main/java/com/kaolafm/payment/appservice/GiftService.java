package com.kaolafm.payment.appservice;

import com.kaolafm.payment.dto.GiftDto;
import com.kaolafm.payment.dto.PageDto;

public interface GiftService {

	/**
	 * 获取礼物列表
	 * @return
	 */
	public PageDto<GiftDto> getGiftList(Integer pagenum, Integer pagesize);

}
