package com.kaolafm.payment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kaolafm.payment.annotation.NeedLogin;
import com.kaolafm.payment.annotation.NeedSign;
import com.kaolafm.payment.appservice.UserService;
import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.dto.AccountInfoDto;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.dto.PlanDto;
import com.kaolafm.payment.dto.RewardDto;
import com.kaolafm.payment.dto.TradeRecordDto;
import com.kaolafm.payment.dto.UserGiftDto;
import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.response.CommonResponse;
import com.kaolafm.user.exception.UserServiceException;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@NeedSign
	@NeedLogin
	@RequestMapping("/gift/list")
	public Object list(HttpServletRequest request, @RequestParam Integer type, @RequestParam Integer pagenum,
			@RequestParam Integer pagesize) throws ServiceException {
		CommonResponse<Object> cp = new CommonResponse<Object>();
		if (pagesize > 100) {
			throw new ServiceException(Code.BAD_REQUEST);
		}
		PageDto<UserGiftDto> pageDto = new PageDto<UserGiftDto>();
		if (pagenum > 0 && pagesize >= 0
				&& (type == Constants.USER_GIFT_TYPE.RECEIVE.code() || type == Constants.USER_GIFT_TYPE.SEND.code())) {
			pageDto = userService.getUserGiftList(type, pagenum, pagesize);
		}
		cp.setResult(pageDto);
		return cp;
	}

	@NeedSign
	@RequestMapping("/reward/list")
	public Object rewardTop(HttpServletRequest request, @RequestParam Long anchorid, @RequestParam Long programid, @RequestParam Integer pagenum,
			@RequestParam Integer pagesize) throws ServiceException {
		if (pagesize > 100) {
			throw new ServiceException(Code.BAD_REQUEST);
		}
		CommonResponse<Object> cp = new CommonResponse<Object>();
		CommonParams params = buildCommonParams(request);
		Long uid = StringUtils.isBlank(params.getUid()) ? null : Long.valueOf(params.getUid());
		PageDto<RewardDto> pageDto = new PageDto<RewardDto>();
		if (pagenum > 0 && pagesize >= 0) {
			pageDto = userService.getRewardList(uid, anchorid, programid, pagenum, pagesize);
		}
		cp.setResult(pageDto);
		return cp;
	}

	@NeedSign
	@NeedLogin
	@RequestMapping(value = "/cash/apply", method = RequestMethod.POST)
	public Object saveApplyCash(HttpServletRequest request, @RequestParam Integer goldleafcount, @RequestParam(required=false) Double money,
			@RequestParam String account, @RequestParam String accountname) throws ServiceException {
		// 1验证金叶子数 2 验证兑换是否正确 3 验证体现申请次数 4扣减用户账户
		CommonResponse<Map<String, Object>> cp = new CommonResponse<Map<String, Object>>();
		money = money == null ? 0.0 : money;
		boolean flag = userService.saveApplyLeafToCash(goldleafcount, money, account, accountname);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", flag ? 1 : 0);
		cp.setResult(map);
		return cp;
	}

	@NeedSign
	@NeedLogin
	@RequestMapping("/trade/list")
	public Object tradelist(HttpServletRequest request,@RequestParam("type") Integer type, @RequestParam Integer pagenum,
			@RequestParam Integer pagesize) throws ServiceException {
		if (pagesize > 100) {
			throw new ServiceException(Code.BAD_REQUEST);
		}
		CommonResponse<Object> cp = new CommonResponse<Object>();
		CommonParams params = buildCommonParams(request);
		PageDto<TradeRecordDto> pageDto = new PageDto<TradeRecordDto>();
		if (pagenum > 0 && pagesize >= 0) {
			pageDto = userService.getTradeList(Long.valueOf(params.getUid()), type, pagenum, pagesize);
		}
		cp.setResult(pageDto);
		return cp;
	}

	@NeedSign
	@NeedLogin
	@RequestMapping(value = "/withdrawal/cash", method = RequestMethod.POST)
	public Object remainApplyCash(HttpServletRequest request) throws ServiceException {
		CommonResponse<Map<String, Object>> cp = new CommonResponse<Map<String, Object>>();
		CommonParams commonParams = buildCommonParams(request);
		Integer cash = userService.getWithdrawalCash(commonParams.getUid());
		Integer times = userService.getUserRemainWithdrawalTimes(Long.valueOf(commonParams.getUid()));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cash", cash);
		map.put("minCash", Constants.MIN_WITHDRAWAL_TIMES_CASH);
		map.put("ratio", Constants.goldleafToCashRatio);
		map.put("times", times);
		map.put("minGoldLeaf", Constants.MIN_WITHDRAWAL_GOLDLEAF_EVERY_TIMES);
		map.put("maxGoldLeaf", Constants.MAX_WITHDRAWAL_GOLDLEAF_EVERY_TIMES);
		cp.setResult(map);
		return cp;
	}

	/**
	 * 获取充值套餐列表
	 * 
	 * @param request
	 * @return
	 */

	@RequestMapping("/recharge/list")
	public Object getRechargeList(HttpServletRequest request) {
		CommonResponse<Map<String, Object>> cp = new CommonResponse<Map<String, Object>>();
		List<PlanDto> list = userService.getRechargeList(buildCommonParams(request));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataList", list);
		cp.setResult(map);
		return cp;
	}

	/**
	 * 主播打赏
	 * 
	 * @param anchorid
	 * @param giftid
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@NeedSign
	@NeedLogin
	@RequestMapping(value = "/reward/anchor", method = RequestMethod.POST)
	public Object rewardAnchor(@RequestParam long anchorid, @RequestParam long giftid, @RequestParam int num,
			@RequestParam long programid, HttpServletRequest request, HttpServletResponse response)
			throws ServiceException, UserServiceException {

		CommonParams param = buildCommonParams(request);
		Map<String, Object> map = new HashMap<String, Object>();
		userService.rewardAnchor(anchorid, giftid, num, programid, buildCommonParams(request));
		AccountInfoDto accountInfo = userService.getUserAccountInfo(Long.valueOf(param.getUid()), Constants.USERACCOUNT_INFO_TYPE.GREENLEAF_COUNT.code(), null, null);
		map.put("count", accountInfo.getCount());
		return new CommonResponse<Map<String, Object>>(map);
	}

	@NeedSign
	@RequestMapping(value = "/accountinfo/get", method = RequestMethod.POST)
	public Object accountInfo(HttpServletRequest request, @RequestParam Integer type,
			@RequestParam(required = false) Long programid,@RequestParam(required = false) Long anchoruid) throws ServiceException {
		CommonResponse<Map<String, Object>> cp = new CommonResponse<Map<String, Object>>();
		CommonParams params = buildCommonParams(request);
		Long uid = StringUtils.isBlank(params.getUid()) ? null : Long.valueOf(params.getUid());
		AccountInfoDto accountInfoDto = userService.getUserAccountInfo(uid, type, programid, anchoruid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", accountInfoDto.getCount());
		if(type == Constants.USERACCOUNT_INFO_TYPE.LIVE_PROGRAM_LEAF_COUNT.code()){
			map.put("uid", accountInfoDto.getUid());
			map.put("gender", accountInfoDto.getGender());
			map.put("nickName", accountInfoDto.getNickName());
			map.put("avatar", accountInfoDto.getAvatar());
			map.put("isVanchor", accountInfoDto.getIsVanchor());
		}
		cp.setResult(map);
		return cp;
	}

	@NeedSign
	@RequestMapping("/pay/result")
	public Object getPayState(HttpServletRequest request, @RequestParam String outtradeno) throws ServiceException {
		CommonResponse<Map<String, Object>> cp = new CommonResponse<Map<String, Object>>();
		PayTrade payTrade = userService.getPayState(outtradeno);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", payTrade.getStatus());
		cp.setResult(map);
		return cp;
	}
}
