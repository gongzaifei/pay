package com.kaolafm.payment.appservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaolafm.payment.appservice.GiftService;
import com.kaolafm.payment.dto.GiftDto;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.entity.PayGift;
import com.kaolafm.payment.service.IGiftService;
import com.kaolafm.payment.utils.ConvertUtils;

@Service
public class GiftServiceImpl implements GiftService {
	
	Logger logger = LoggerFactory.getLogger(GiftServiceImpl.class);

	@Autowired
	private IGiftService rpcGiftService;

	@Override
	public PageDto<GiftDto> getGiftList(Integer pagenum, Integer pagesize) {
		PageDto<GiftDto> pageDto = new PageDto<GiftDto>();
		try {
			Long count = rpcGiftService.getGiftCount();
			if (count > 0) {
				List<PayGift> gifts = rpcGiftService.getGiftList(pagenum, pagesize);
				if(gifts != null && gifts.size() > 0){
					List<GiftDto> giftDtos = new ArrayList<GiftDto>();
					for(PayGift gift : gifts){
						giftDtos.add(convertGiftDto(gift));
					}
					pageDto = ConvertUtils.create(giftDtos, count.intValue(), pagenum, pagesize);
				}
			}
		} catch (Exception e) {
			logger.info("rpc get gift list exception");
			e.printStackTrace();
		}
		return pageDto;
	}

	private GiftDto convertGiftDto(PayGift gift) {
		GiftDto giftDto = new GiftDto();
		giftDto.setId(gift.getId());
		giftDto.setName(gift.getName());
		giftDto.setGiftImg(gift.getImg());
		giftDto.setGiftWorth(gift.getGiftWorth());
		return giftDto;
	}

}
