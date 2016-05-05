package com.kaolafm.payment.appservice.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.datetime.JDateTime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kaolafm.cache.dto.LivePlayProgrameDto;
import com.kaolafm.cache.service.v31.INewLivePlayService;
import com.kaolafm.counter.api.CounterService;
import com.kaolafm.imservice.service.ChatService;
import com.kaolafm.payment.appservice.UserService;
import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.constants.CounterConstants;
import com.kaolafm.payment.dto.AccountInfoDto;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.dto.PlanDto;
import com.kaolafm.payment.dto.RewardDto;
import com.kaolafm.payment.dto.TradeRecordDto;
import com.kaolafm.payment.dto.UserGiftDto;
import com.kaolafm.payment.entity.PayAccountWithdrawal;
import com.kaolafm.payment.entity.PayGift;
import com.kaolafm.payment.entity.PayLiveRewardRecord;
import com.kaolafm.payment.entity.PayRechargePlan;
import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.payment.entity.PayUserAccount;
import com.kaolafm.payment.entity.PayUserGift;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.service.IGiftService;
import com.kaolafm.payment.service.IOrderService;
import com.kaolafm.payment.service.IRechargePlanService;
import com.kaolafm.payment.service.IUserCenterService;
import com.kaolafm.payment.utils.ConvertUtils;
import com.kaolafm.payment.utils.DateUtils;
import com.kaolafm.payment.utils.IdBuildUtils;
import com.kaolafm.payment.utils.ThreadLocalUtil;
import com.kaolafm.user.dto.SystemLetterDto;
import com.kaolafm.user.entity.AnchorRefInfo;
import com.kaolafm.user.entity.UserInfo;
import com.kaolafm.user.exception.UserServiceException;
import com.kaolafm.user.service.AnchorUserService;
import com.kaolafm.user.service.SystemLetterService;

@Service
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private IUserCenterService rpcUserCenterService;

	@Autowired
	private com.kaolafm.user.service.UserService rpcUserSerivce;

	@Autowired
	private IRechargePlanService rpcRechargePlanService;

	@Autowired
	private IOrderService rpcOrderService;

	@Autowired
	private IGiftService rpcGiftService;

	@Autowired
	private INewLivePlayService rpcNewLiveService;
	
	@Autowired
	private CounterService rpcCounterService;

	@Autowired
	private AnchorUserService rpcAnchorService;
	
	@Autowired
	private ChatService rpcChatService;
	
	@Autowired
	private SystemLetterService rpcSystemLetterService;

	@Override
	public boolean saveApplyLeafToCash(Integer goldleafcount, Double money, String alipayAccount,
			String alipayAccountname) throws ServiceException {
		CommonParams params = ThreadLocalUtil.getCommonParams();
		Long uid = Long.valueOf(params.getUid());
		UserInfo userInfo = null;
		try {
			userInfo = rpcUserSerivce.getUserInfoByUid(uid);
			if (userInfo == null) {
				throw new ServiceException(Code.USER_NOT_EXIST);
			}
			
			//主播账号是否被冻结
			if(userInfo.getStatus() != 0){
				throw new ServiceException(Code.UID_IS_FREEZE);
			}
			
			// 是否V主播
			Boolean isVAnchor = rpcAnchorService.isVipAnchorUser(uid);
			if(!isVAnchor){
				throw new ServiceException(Code.ANCHOR_NOT_VIP,"主播未认证,不能提现");
			}

//			if (goldleafcount % 10 != 0) {
//				throw new ServiceException(Code.LEAFCOUNT_NOT_TEN_MULTIPLE);
//			}
//			
//			if(money > Constants.MAX_WITHDRAWAL_DAY_CASH){
//				throw new ServiceException(Code.USER_WITHDRAWAL_CASH_LIMIT);
//			}
			
			if(goldleafcount < Constants.MIN_WITHDRAWAL_GOLDLEAF_EVERY_TIMES || goldleafcount > Constants.MAX_WITHDRAWAL_GOLDLEAF_EVERY_TIMES){
				throw new ServiceException(Code.WITHDRAWAL_MONEY_ERROR);
			}
			
			Double timesMaxWithdrawalCash = Constants.MAX_WITHDRAWAL_GOLDLEAF_EVERY_TIMES * Constants.goldleafToCashRatio;
			Double timesMinWithdrawalCash = Constants.MIN_WITHDRAWAL_GOLDLEAF_EVERY_TIMES * Constants.goldleafToCashRatio;
			Double withdrawalCash = goldleafcount * Constants.goldleafToCashRatio;
			if(withdrawalCash < timesMinWithdrawalCash){
				throw new ServiceException(Code.WITHDRAWAL_MONEY_ERROR);
			}
			if(withdrawalCash > timesMaxWithdrawalCash){
				throw new ServiceException(Code.WITHDRAWAL_MONEY_ERROR);
			}

			// 查询提现申请次数,金额判断
			List<PayAccountWithdrawal> withdrawals = rpcUserCenterService.getUserWithdrawalInfoDayByUidLock(uid);
			if (withdrawals != null) {
				//提现次数
				if (withdrawals.size() >= Constants.MAX_TIMES_WITHDRAWAL_DAY) {
					throw new ServiceException(Code.USER_APPLY_TIMES_LIMIT);
				}
//				Double withdrawalCashed = 0.0; //已提现的rmb
//				for (PayAccountWithdrawal withdrawal : withdrawals) {
//					withdrawalCashed += withdrawal.getCashQuantity();
//				}
//				//判断用户已提现金额
//				if ((int) Math.ceil(withdrawalCashed) >= 1000) {
//					throw new ServiceException(Code.USER_WITHDRAWAL_CASH_LIMIT);
//				}
//				
//				//判断已提现的rmb加上用户输入的rmb 与每日提现上限比较
//				if((int) Math.ceil(withdrawalCashed) + money > 1000){
//					throw new ServiceException(Code.USER_WITHDRAWAL_CASH_LIMIT);
//				}
				
			}

			PayUserAccount payUserAccount = rpcUserCenterService.getUserAccountByUid(uid);
			if (payUserAccount == null || payUserAccount.getGoldLeaf() < goldleafcount) {
				// 你以为想体现多少就体现多少吗 你错了 赶紧赚钱吧
				throw new ServiceException(Code.USER_CASH_NOT_ENOUGH);
			}
			Double userCash = payUserAccount.getGoldLeaf() * Constants.goldleafToCashRatio;
			/*BigDecimal userCashDecimal = new BigDecimal(userCash);
			BigDecimal withdrawalDecimal = new BigDecimal(withdrawalCash);*/
			if (userCash < withdrawalCash) {
				// 你以为想体现多少就体现多少吗 你错了 赶紧赚钱吧
				throw new ServiceException(Code.USER_CASH_NOT_ENOUGH);
			}
			
			String id = IdBuildUtils.buildWithdrawlPrefix(uid.toString()) + CounterConstants.PAY_MODULE_START_ID
					+ String.valueOf(rpcCounterService.incr(CounterConstants.WITHDRAWAL_MODULE_UNI_KEY, 1));
			rpcUserCenterService.saveCashApplay(id, uid, userInfo.getNickName(), goldleafcount, withdrawalCash,
					alipayAccount, alipayAccountname,payUserAccount.getGoldLeaf(),userCash);

			//发送私信
			SystemLetterDto systemLetterDto = new SystemLetterDto();
			systemLetterDto.setContent("您好，您的提现请求已经提交系统审核，1个工作日内审核完毕，请您耐心等待。");
			systemLetterDto.setToUid(uid);
			rpcSystemLetterService.sendSystemLetter(systemLetterDto);
		} catch (UserServiceException e) {
			logger.info("saveApplyLeafToCash, uid:{}", uid);
			throw new ServiceException(Code.SERVER_ERROR);
		}
		return true;
	}

	@Override
	public List<PlanDto> getRechargeList(CommonParams commonParams) {
		List<PlanDto> reList = new ArrayList<PlanDto>();
		try {
			String uid = commonParams.getUid();
			String deviceType = commonParams.getDevicetype();
			List<PayRechargePlan> list = rpcRechargePlanService.getRechargePlanList(Integer.valueOf(deviceType));
			for (PayRechargePlan plan : list) {
				if (plan.getPlanLimitType() == 1) {
					PayTrade trade = rpcUserCenterService.getTradeByUidAndPlanId(
							Long.valueOf(commonParams.getUid()), plan.getId());
					if (trade == null) {
						reList.add(convertPlan(plan));
					}
				} else {
					reList.add(convertPlan(plan));
				}
			}
			return reList;
		} catch (Exception e) {
			logger.info("getRechargeListError", e);
		}
		return null;
	}

	private PlanDto convertPlan(PayRechargePlan plan) {
		PlanDto dto = new PlanDto();
		dto.setFee(plan.getCashFee());
		dto.setDesc(plan.getDescription());
		dto.setId(plan.getId());
		dto.setLeaf(plan.getLeafQuantity());
		dto.setPresentnum(plan.getPresentLeafQuantity());
		return dto;
	}

	@Override
	public Integer getWithdrawalCash(String uid) throws ServiceException {
		Integer cash = 0;
		try {
			PayUserAccount payUserAccount = rpcUserCenterService.getUserAccountByUid(Long.valueOf(uid));
			if (payUserAccount != null) {
				Integer userCash = (int) Math.floor(payUserAccount.getGoldLeaf() * Constants.goldleafToCashRatio);
				if (userCash > Constants.MIN_WITHDRAWAL_TIMES_CASH) {
					List<PayAccountWithdrawal> withdrawals = rpcUserCenterService.getUserWithdrawalInfoDayByUid(Long
							.valueOf(uid));
					Integer withdrawalLeaf = 0;
					Integer withdrawalCash = 0;
					if (withdrawals != null && withdrawals.size() > 0) {
						if (withdrawals.size() < Constants.MAX_TIMES_WITHDRAWAL_DAY) {
							// 如果单日体现次数小于限制 则可继续申请
							for (PayAccountWithdrawal withdrawal : withdrawals) {
								withdrawalLeaf += withdrawal.getLeafQuantity();
							}
							withdrawalCash = (int) Math.ceil(withdrawalLeaf * Constants.goldleafToCashRatio);
							if (withdrawalCash < Constants.MAX_WITHDRAWAL_DAY_CASH) {
								// 如果用户当日提现金额未达到上限 则可继续提现申请
								if (userCash + withdrawalCash > Constants.MAX_WITHDRAWAL_DAY_CASH) {
									cash = Constants.MAX_WITHDRAWAL_DAY_CASH - withdrawalCash;
								} else if (userCash + withdrawalCash < Constants.MAX_WITHDRAWAL_DAY_CASH) {
									cash = userCash;
								}

							}
						}
					} else {
						if (userCash > Constants.MAX_WITHDRAWAL_DAY_CASH) {
							cash = Constants.MAX_WITHDRAWAL_DAY_CASH;
						} else {
							cash = userCash;
						}
					}
				}

			}
		} catch (Exception e) {
			logger.info("rpc get remain withdrawal cash exception");
			throw new ServiceException(Code.SERVER_ERROR);
		}

		return cash;
	}

	@Override
	public PageDto<UserGiftDto> getUserGiftList(Integer type, Integer pagenum, Integer pagesize) {
		CommonParams params = ThreadLocalUtil.getCommonParams();
		Long uid = Long.valueOf(params.getUid());
		PageDto<UserGiftDto> pageDto = new PageDto<UserGiftDto>();
		List<UserGiftDto> userGiftDtos = null;
		Long count = rpcUserCenterService.getUserGiftListCount(uid, type);
		if (count > 0) {
			List<PayUserGift> gifts = rpcUserCenterService.getUserGiftPage(uid, type, pagenum, pagesize);
			if (gifts != null && gifts.size() > 0) {
				List<Long> uids = new ArrayList<Long>();
				userGiftDtos = new ArrayList<UserGiftDto>();
				for (PayUserGift userGift : gifts) {
					Long otherUid = type == Constants.USER_GIFT_TYPE.RECEIVE.code() ? userGift.getSenderUid()
							: userGift.getReceiverUid();
					uids.add(otherUid);
					userGiftDtos.add(convertUserGiftDto(userGift, type));
				}
				try {
					List<UserInfo> userInfos = rpcUserSerivce.getUserInfoByUids(uids);
					Map<Long, UserInfo> userMap = new HashMap<Long, UserInfo>();
					if (userInfos != null && userInfos.size() > 0) {
						for (UserInfo userInfo : userInfos) {
							userMap.put(userInfo.getUid(), userInfo);
						}
						for (UserGiftDto userGiftDto : userGiftDtos) {
							UserInfo userInfo = userMap.get(userGiftDto.getOtherUid());
							if (userInfo != null) {
								userGiftDto.setOtherAvatar(userInfo.getAvatar());
								userGiftDto.setOtherNickName(userInfo.getNickName());
							}
						}
					}
				} catch (UserServiceException e) {
					logger.info("rpc get userinfo fail,method:getUserGiftList,uids:{}", uids);
					e.printStackTrace();
				}
				pageDto = ConvertUtils.create(userGiftDtos, count.intValue(), pagenum, pagesize);
			}

		}
		return pageDto;
	}

	private UserGiftDto convertUserGiftDto(PayUserGift userGift, Integer type) {
		UserGiftDto userGiftDto = new UserGiftDto();
		userGiftDto.setGiftCount(userGift.getQuantity());
		userGiftDto.setGiftImg(userGift.getGiftImg());
		userGiftDto.setGiftName(userGift.getGiftName());
		userGiftDto.setOtherUid(type == Constants.USER_GIFT_TYPE.RECEIVE.code() ? userGift.getSenderUid() : userGift
				.getReceiverUid());
		userGiftDto.setUpdateTime(ConvertUtils.compareDate(userGift.getCreateDate()));
		return userGiftDto;
	}

	@Override
	public PayTrade getPayState(String outtradeno) throws ServiceException {
		PayTrade payTrade = rpcOrderService.getTrade(outtradeno);
		if (payTrade == null) {
			throw new ServiceException(Code.ORDER_NOT_EXIST);
		}

		return payTrade;
	}

	@Override
	public AccountInfoDto getUserAccountInfo(Long uid, Integer type, Long programid, Long anchoruid) throws ServiceException {
		AccountInfoDto accountInfoDto = new AccountInfoDto();
//		UserInfo userInfo = null;
		try {
//			userInfo = rpcUserSerivce.getUserInfoByUid(uid);
//			if (userInfo == null) {
//				throw new ServiceException(Code.USER_NOT_EXIST);
//			}
			if (type != null && type == Constants.USERACCOUNT_INFO_TYPE.GREENLEAF_COUNT.code()
					|| type == Constants.USERACCOUNT_INFO_TYPE.GOLDLEAF_COUNT.code()) {
				PayUserAccount payUserAccount = rpcUserCenterService.getUserAccountByUid(uid);
				if (payUserAccount == null) {
					accountInfoDto.setCount(0l);
				} else {
					Integer count = type == Constants.USERACCOUNT_INFO_TYPE.GREENLEAF_COUNT.code() ? payUserAccount
							.getGreenLeaf() : payUserAccount.getGoldLeaf();
					accountInfoDto.setCount(Long.valueOf(count));
				}
			} else if (type == Constants.USERACCOUNT_INFO_TYPE.RECEIVE_GIFT_COUNT.code()
					|| type == Constants.USERACCOUNT_INFO_TYPE.SEND_GIFT_COUNT.code()) {
				Integer giftType = type == Constants.USERACCOUNT_INFO_TYPE.RECEIVE_GIFT_COUNT.code() ? Constants.USER_GIFT_TYPE.RECEIVE
						.code() : Constants.USER_GIFT_TYPE.SEND.code();
				Long count = rpcUserCenterService.getUserGiftSum(uid, giftType);
				accountInfoDto.setCount(count == null ? 0 : count);
			} else if (type == Constants.USERACCOUNT_INFO_TYPE.LIVE_PROGRAM_LEAF_COUNT.code()){
				if (programid == null || anchoruid == null) {
					throw new ServiceException(Code.BAD_REQUEST);
				}
				UserInfo anchorUserInfo = rpcUserSerivce.getUserInfoByUid(anchoruid);
				if(anchorUserInfo == null){
					throw new ServiceException(Code.USER_NOT_EXIST);
				}
				accountInfoDto.setUid(anchoruid);
				accountInfoDto.setAvatar(anchorUserInfo.getAvatar());
				accountInfoDto.setGender(anchorUserInfo.getGender());
				accountInfoDto.setNickName(anchorUserInfo.getNickName());
				Long count = rpcUserCenterService.getAnchorLiveRewardSumLeaf(programid,anchoruid);
				accountInfoDto.setCount(count == null ? 0 : count);
				
				AnchorRefInfo anchorRefInfo = rpcUserSerivce.getAnchorRefInfo(anchoruid);
				if(anchorRefInfo != null){
					accountInfoDto.setIsVanchor(anchorRefInfo.getIsvAnchor()==null||anchorRefInfo.getIsvAnchor()==2||anchorRefInfo.getIsvAnchor()==0?0:1);
				}
			}
		} catch (UserServiceException e) {
			logger.info("rpc get userInfo exception,uid:{}", uid);
			e.printStackTrace();
		}
		return accountInfoDto;
	}

	@Override
	public PageDto<TradeRecordDto> getTradeList(Long uid, Integer type, Integer pagenum, Integer pagesize)
			throws ServiceException {
		PageDto<TradeRecordDto> pageDto = new PageDto<TradeRecordDto>();
		UserInfo userInfo = null;
		try {
			userInfo = rpcUserSerivce.getUserInfoByUid(uid);
			if (userInfo == null) {
				throw new ServiceException(Code.USER_NOT_EXIST);
			}
			List<TradeRecordDto> recordDtos = new ArrayList<TradeRecordDto>();
			if (type == Constants.USER_OPERATE_TYPE.RECHARGE.code()) {
				Long count = rpcUserCenterService.getUserTradeRecordCount(uid);
				if (count > 0) {
					List<PayTrade> payTrades = rpcUserCenterService.getUserTradeRecordList(uid, pagenum, pagesize);
					if (payTrades != null && payTrades.size() > 0) {
						for (PayTrade payTrade : payTrades) {
							recordDtos.add(tradeConvertToTradeRecordDto(payTrade));
						}
						pageDto = ConvertUtils.create(recordDtos, count.intValue(), pagenum, pagesize);
					}
				}
			} else if (type == Constants.USER_OPERATE_TYPE.WITHDRAWAL.code()) {
				Long count = rpcUserCenterService.getUserWithdrawalRecord(uid);
				if (count > 0) {
					List<PayAccountWithdrawal> withdrawals = rpcUserCenterService.getUserWithdrawalRecordList(uid,
							pagenum, pagesize);
					if (withdrawals != null && withdrawals.size() > 0) {
						for (PayAccountWithdrawal withdrawal : withdrawals) {
							recordDtos.add(withdrawalConvertToTradeRecordDto(withdrawal));
						}
						pageDto = ConvertUtils.create(recordDtos, count.intValue(), pagenum, pagesize);
					}
				}
			} else if(type == 0){
				List<PayTrade> payTrades = rpcUserCenterService.getUserTradeRecordList(uid, 1, 5);
				List<PayAccountWithdrawal> withdrawals = rpcUserCenterService.getUserWithdrawalRecordList(uid, 1, 5);
				if (payTrades != null && payTrades.size() > 0) {
					for (PayTrade payTrade : payTrades) {
						recordDtos.add(tradeConvertToTradeRecordDto(payTrade));
					}
				}
				if (withdrawals != null && withdrawals.size() > 0) {
					for (PayAccountWithdrawal withdrawal : withdrawals) {
						recordDtos.add(withdrawalConvertToTradeRecordDto(withdrawal));
					}
				}
				if (recordDtos != null && recordDtos.size() > 0) {
					Collections.sort(recordDtos, new Comparator<TradeRecordDto>() {
						@Override
						public int compare(TradeRecordDto o1, TradeRecordDto o2) {
							try {
								Date date1 = DateUtils.parseDefaultDateForStr(o1.getTradeTime());
								Date date2 = DateUtils.parseDefaultDateForStr(o2.getTradeTime());
								return date2.compareTo(date1);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							return 0;
						}
					});
					if(recordDtos.size() > 5){
						recordDtos = recordDtos.subList(0, 5);
					}
					pageDto = ConvertUtils.create(recordDtos, recordDtos.size(), 1, 5);
				}
			}
		} catch (UserServiceException e) {
			logger.info("rpc get userInfo exception,uid:{}", uid);
			e.printStackTrace();
		}

		pageDto.setCurrentPage(pagenum);
		pageDto.setPageSize(pagesize);
		return pageDto;
	}

	private TradeRecordDto withdrawalConvertToTradeRecordDto(PayAccountWithdrawal withdrawal) {
		TradeRecordDto tradeRecordDto = new TradeRecordDto();
		tradeRecordDto.setOutTradeNo(String.valueOf(withdrawal.getId()));
		tradeRecordDto.setLeafCount(withdrawal.getLeafQuantity());
		tradeRecordDto.setMoney(withdrawal.getCashQuantity());
		tradeRecordDto.setTradeType(Constants.PAY_TYPE.ALIPAY.code()); // 提现都是支付宝
		tradeRecordDto.setOperationType(Constants.USER_OPERATE_TYPE.WITHDRAWAL.code()); // 提现操作
		String statusDesc = "";
		if (withdrawal.getStatus() == Constants.WITHDRAWAL_STATUS.WAIT_CHECK.code()) {
			statusDesc = "审核中";
		} else if (withdrawal.getStatus() == Constants.WITHDRAWAL_STATUS.CHECK_SUCCESS.code()) {
			statusDesc = "审核通过,待转账";
		} else if (withdrawal.getStatus() == Constants.WITHDRAWAL_STATUS.CHECK_FAIL.code()) {
			statusDesc = "审核不通过";
		} else if (withdrawal.getStatus() == Constants.WITHDRAWAL_STATUS.PAYMENT_SUCCESS.code()) {
			statusDesc = "转账成功";
		} else if (withdrawal.getStatus() == Constants.WITHDRAWAL_STATUS.PAYMENT_FAIL.code()) {
			statusDesc = "转账失败";
		}
		tradeRecordDto.setStatusDesc(statusDesc);
		tradeRecordDto.setReason(withdrawal.getReason() == null ? "" : withdrawal.getReason());
		tradeRecordDto.setTradeTime(DateUtils.getDetaultDateStr(withdrawal.getCreateDate()));
		tradeRecordDto.setTransactionId("");
		tradeRecordDto.setProductName("");
		return tradeRecordDto;
	}

	private TradeRecordDto tradeConvertToTradeRecordDto(PayTrade payTrade) {
		TradeRecordDto tradeRecordDto = new TradeRecordDto();
		tradeRecordDto.setOutTradeNo(payTrade.getTradeId());
		tradeRecordDto.setMoney(payTrade.getTradeSumFee());
		tradeRecordDto.setLeafCount(payTrade.getPlanLeafQuantity());
		tradeRecordDto.setPresentLeafCount(payTrade.getPlanPresentLeaf());
		tradeRecordDto.setTradeType(payTrade.getType());
		tradeRecordDto.setOperationType(Constants.USER_OPERATE_TYPE.RECHARGE.code()); // 充值操作
		tradeRecordDto.setReason("");
		String statusDesc = "";
		if (payTrade.getStatus() == Constants.RECHARGE_STATUS.FAIL.code()) {
			statusDesc = Constants.RECHARGE_STATUS.FAIL.content();
		} else if (payTrade.getStatus() == Constants.RECHARGE_STATUS.SUCCESS.code()) {
			statusDesc = Constants.RECHARGE_STATUS.SUCCESS.content();
		} else {
			statusDesc = Constants.RECHARGE_STATUS.WAIT.content();
		}
		tradeRecordDto.setStatusDesc(statusDesc);
		tradeRecordDto.setTradeTime(DateUtils.getDetaultDateStr(payTrade.getCreateDate()));
		tradeRecordDto.setTransactionId("");
		tradeRecordDto.setProductName("");
		return tradeRecordDto;
	}

	@Override
	public void rewardAnchor(long anchorId, long giftId, int num, long programId, CommonParams commonParams)
			throws ServiceException, UserServiceException {
		try {
			String uid = commonParams.getUid();
			logger.info("rewardUid:" + uid + " anchorId:" + anchorId + " giftId:" + giftId + "rewardNum:" + num);
			//打赏次数
			if (num <= 0) {
				throw new ServiceException(Code.REWARD_NUM);
			}
			//打赏人
			if (StringUtils.isBlank(uid)) {
				throw new ServiceException(Code.USER_NOT_EXIST);
			}
			// 是否V主播
			Boolean isVAnchor = rpcAnchorService.isVipAnchorUser(anchorId);
			if(!isVAnchor){
				throw new ServiceException(Code.ANCHOR_NOT_VIP);
			}
			//主播账号是否被冻结
			UserInfo anchorInfo = rpcUserSerivce.getUserInfoByUid(anchorId);
			if(anchorInfo == null ){
				throw new ServiceException(Code.USER_NOT_EXIST);
			}
			if(anchorInfo.getStatus() != 0){
				throw new ServiceException(Code.ANCHOR_IS_FREEZE);
			}
			//打赏人账号是否被冻结
			UserInfo userInfo = rpcUserSerivce.getUserInfoByUid(Long.valueOf(uid));
			if(userInfo == null ){
				throw new ServiceException(Code.USER_NOT_EXIST);
			}
			if(userInfo.getStatus() != 0){
				throw new ServiceException(Code.UID_IS_FREEZE);
			}
			//主播是否给自己打赏
			if(anchorId == Long.valueOf(uid)){
				throw new ServiceException(Code.ANCHOR_LIVE_UID);
			}
			//打赏人账户判断
			PayUserAccount userAccount = rpcUserCenterService.getUserAccountByUid(Long.valueOf(uid));
			if (userAccount == null) {
				logger.info("userAccountIsNull:" + uid);
				throw new ServiceException(Code.USER_CASH_NOT_ENOUGH);
			}
			//礼物判断
			PayGift gift = rpcGiftService.getGiftUse(giftId);
			if (gift == null) {
				logger.info("GiftIsNull:" + giftId + "uid" + uid);
				throw new ServiceException(Code.GIFT_NULL);
			}
//			LivePlayProgrameDto liveNowProgrameDto = rpcNewLiveService.getLivePlayNow(anchorId,
//					com.kaolafm.cache.constants.Constants.APP_SUPPERMODE_TYPE.SUPPER_TYPE.code(),
//					com.kaolafm.cache.constants.Constants.LIVE_LOCK_TYPE.LOCKED.code());

			LivePlayProgrameDto lppd = rpcNewLiveService.getLiveProgramDetai(programId);
			//判断计划是否存在
			if (lppd == null || lppd.getId() == null) {
				logger.info("liveIsNull" + programId + " uid" + uid);
				throw new ServiceException(Code.LIVE_PROGRAM_NULL);
			}
			Integer status = this.compareDate(lppd.getLiveStart(), lppd.getLiveEnd(), lppd.getRealLiveStart(), lppd.getRealLiveEnd(), lppd.getSource());

			//判断是否正在直播中
			if((status != null && status != 1) || lppd.getStatus() != 1){
				logger.info("live program not now" + programId + " anchorid" + anchorId + " uid" + uid);
				throw new ServiceException(Code.ANCHOR_LIVE_NOT);
			}
			//主播直播id是否与客户端传进来一致
			if(lppd.getUid().longValue() != anchorId){
				logger.info("liveIsNull" + programId + " anchorid" + anchorId + " uid" + uid);
				throw new ServiceException(Code.ANCHOR_LIVE_NOT);
			}
			int value = gift.getGiftWorth();
			int rewardNum = value * num;
			//打赏人账户余额判断
			if (userAccount.getGreenLeaf() < rewardNum) {
				if(userAccount.getGreenLeaf() < value){
					logger.info("rewardUid:" + uid + " rewardNum:" + rewardNum + " userAccount:"
							+ userAccount.getGreenLeaf());
					throw new ServiceException(Code.USER_CASH_NOT_ENOUGH);
				}
				for(int i=num;i>0;i--){
					rewardNum = value * i;
					if(userAccount.getGreenLeaf() >= rewardNum){
						num = i;
						break;
					}
				}
			}
			//打赏操作
			Boolean flag = rpcUserCenterService.doRewardAnchor(userInfo, anchorInfo, num, lppd, giftId);
			if (!flag) {
				throw new ServiceException(Code.LIVE_REWARD_FAIL);
			}
			String roomId = lppd.getRoomid();
			if(StringUtils.isNotBlank(roomId)){
				Long count = rpcUserCenterService.getAnchorLiveRewardSumLeaf(programId,anchorId);
				Map<String, Object> body = new HashMap<String, Object>();
		        Map<String, Object> result = new HashMap<String, Object>();
				result.put("action_type", 2);
				result.put("reward_uid", uid);
				result.put("reward_pic", gift.getImg());
				result.put("reward_name", gift.getName());
				result.put("reward_count", num);
				result.put("reward_giftid", gift.getId());
				result.put("anchor_account", count);
				
				body.put("body", result);
				rpcChatService.sendMsg("kaolafm", roomId, JSONObject.toJSONString(body));
			}

		} catch (ServiceException e) {
			logger.info("rewardAnchorError", e);
			throw e;
		}
	}

	@Override
	public PageDto<RewardDto> getRewardList(Long uid, Long anchorid, Long programid, Integer pagenum, Integer pagesize)
			throws ServiceException {
		PageDto<RewardDto> pageDto = new PageDto<RewardDto>();
		try {
			List<RewardDto> rewardDtos = new ArrayList<RewardDto>();
			Long count = rpcUserCenterService.getUserLiveRewardRecordCount(programid,anchorid);
			if (count > 0) {
				List<PayLiveRewardRecord> liveRewardRecords = rpcUserCenterService.getUserLiveRewardList(programid,anchorid,
						pagenum, pagesize);
				if (liveRewardRecords != null && liveRewardRecords.size() > 0) {
					List<Long> uids = new ArrayList<Long>();
					for (PayLiveRewardRecord liveRewardRecord : liveRewardRecords) {
						uids.add(liveRewardRecord.getSenderId());
						rewardDtos.add(convertRewardDto(liveRewardRecord));
					}
					if (uids != null && uids.size() > 0) {
						List<UserInfo> userInfos = rpcUserSerivce.getUserInfoByUids(uids);
						Map<Long, UserInfo> userMap = new HashMap<Long, UserInfo>();
						if (userInfos != null && userInfos.size() > 0) {
							for (UserInfo userInfo : userInfos) {
								userMap.put(userInfo.getUid(), userInfo);
							}
							for (RewardDto rewardDto : rewardDtos) {
								UserInfo userInfo = userMap.get(rewardDto.getOtherUid());
								if (userInfo != null) {
									rewardDto.setOtherAvatar(userInfo.getAvatar());
									rewardDto.setOtherNickName(userInfo.getNickName());
									rewardDto.setOtherGender(userInfo.getGender());
								}
							}
						}
					}

					if (rewardDtos != null && rewardDtos.size() > 0) {
						pageDto = ConvertUtils.create(rewardDtos, count.intValue(), pagenum, pagesize);
					}
				}
			}
		} catch (UserServiceException e) {
			logger.info("get RewardList rpc call exception,uid:{},programid:{}", uid, programid);
		}
		return pageDto;
	}
	

	@Override
	public Integer getUserRemainWithdrawalTimes(Long uid) {
		Integer times = Constants.MAX_TIMES_WITHDRAWAL_DAY;
		List<PayAccountWithdrawal> withdrawals = rpcUserCenterService.getUserWithdrawalInfoDayByUid(Long.valueOf(uid));
		if(withdrawals != null && withdrawals.size() > 0){
			if(withdrawals.size() >= 3){
				times = 0;
			}else{
				times -= withdrawals.size();
			}
		}
		return times;
	}

	private RewardDto convertRewardDto(PayLiveRewardRecord liveRewardRecord) {
		RewardDto rewardDto = new RewardDto();
		rewardDto.setLeafCount(liveRewardRecord.getLeafAmount());
		rewardDto.setOtherUid(liveRewardRecord.getSenderId());
		rewardDto.setLeafImg("");
		return rewardDto;
	}
	
	public Integer compareDate(String start, String end, String realStart, String realEnd,int source) {
        JDateTime startTime = new JDateTime(start, "YYYY-MM-DD hh:mm:ss");
        JDateTime endTime = new JDateTime(end, "YYYY-MM-DD hh:mm:ss");
        JDateTime nowTime = new JDateTime();
        
        JDateTime nowTimeAfter2Day = new JDateTime();
        nowTimeAfter2Day.addDay(2);
        nowTimeAfter2Day.addHour(23 - nowTimeAfter2Day.getHour());
        nowTimeAfter2Day.addMinute(59 - nowTimeAfter2Day.getMinute());
        nowTimeAfter2Day.addSecond(59 - nowTimeAfter2Day.getSecond());
        // 3天后开始的节目
        if (startTime.compareTo(nowTimeAfter2Day) >= 0) {
            return -1;
        }
        nowTimeAfter2Day.subDay(2);
        
        // 私人直播
        if(source == 1){
        	// 没有真正开始时间,并且当前时间在开始时间之后的代表已延期
        	if (StringUtils.isEmpty(realStart) && nowTime.isAfter(startTime)) {
        		return 7;
        	}
        	// 6预告
        	if (StringUtils.isEmpty(realStart) && nowTime.isBefore(startTime)) {
        		return 6;
        	}
        	// 直播中
        	if(StringUtils.isNotEmpty(realStart) && StringUtils.isEmpty(realEnd)){
        		JDateTime realStartTime = new JDateTime(realStart, "YYYY-MM-DD hh:mm:ss");
        		if(nowTime.isAfter(realStartTime)){
        			return 1;
        		}
        	}
        	
        	if(StringUtils.isNotEmpty(realStart) && StringUtils.isNotEmpty(realEnd)){
        		return 0;
        	}
        }else{
        	 // 直播中 1
            if (nowTime.isAfter(startTime) && nowTime.isBefore(endTime)) {
                return  1;
            }
            // 为开始 预告
            if(nowTime.isBefore(startTime)){
            	return 6;
            }
            
            if(nowTime.isAfter(endTime)){
            	return 0;
            }
        }
        return null;
    }




}
