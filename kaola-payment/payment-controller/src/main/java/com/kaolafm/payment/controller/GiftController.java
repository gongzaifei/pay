package com.kaolafm.payment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kaolafm.payment.annotation.NeedSign;
import com.kaolafm.payment.appservice.GiftService;
import com.kaolafm.payment.dto.GiftDto;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.response.CommonResponse;

@Controller
public class GiftController extends BaseController {

	@Autowired
	private GiftService giftService;

	@NeedSign
	@RequestMapping("/gift/list")
	public Object getGiftList(HttpServletRequest request, @RequestParam Integer pagenum, @RequestParam Integer pagesize)
			throws ServiceException {
		CommonResponse<Object> cp = new CommonResponse<Object>();
		PageDto<GiftDto> pageDto = new PageDto<GiftDto>();
		if (pagenum > 0 && pagesize >= 0) {
			pageDto = giftService.getGiftList(pagenum, pagesize);
		}
		cp.setResult(pageDto);
		return cp;
	}

	/**
	 * 充值方式列表就写这吧 不想单独写一个controller了
	 * 
	 * @param request
	 * @return
	 */
	@NeedSign
	@RequestMapping("/type/list")
	public Object getRechargeTypeList(HttpServletRequest request) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		Map<String, Object> wxMap = new HashMap<String, Object>();
		Map<String, Object> alipayMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		wxMap.put("type", 0);
		alipayMap.put("type", 1);
		maps.add(wxMap);
		maps.add(alipayMap);
		resultMap.put("dataList", maps);
		return new CommonResponse<Map<String, Object>>(resultMap);
	}

}
