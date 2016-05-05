package com.kaolafm.payment.impl;

import java.util.*;

import com.kaolafm.payment.dao.*;
import com.kaolafm.payment.entity.*;
import com.kaolafm.payment.service.IWithdrawService;
import com.kaolafm.payment.utils.IdBuildUtils;
import com.kaolafm.user.exception.UserServiceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kaolafm.cache.dto.LivePlayProgrameDto;
import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.dto.UserAccountDto;
import com.kaolafm.payment.service.IAnchorService;
import com.kaolafm.payment.service.IUserCenterService;
import com.kaolafm.user.entity.UserInfo;

/**
 * @author gongzf
 * @date 2016/3/11
 */
@Service("userCenterService")
public class UserCenterServiceImpl implements IUserCenterService {

	private Logger errorLogger = LoggerFactory.getLogger("errorLog");
	private Logger logger = LoggerFactory.getLogger(UserCenterServiceImpl.class);

	@Autowired
	private AccountWithdrawalDao accountWithdrawalDao;

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private UserGiftDao userGiftDao;

	@Autowired
	private TradeDao tradeDao;

	@Autowired
	private LiveRewardRecordDao liveRewardRecordDao;

	@Autowired
	private GiftDao giftDao;

	@Autowired
	private AccountBillDao accountBillDao;

	@Autowired
	private IAnchorService anchorService;

	@Autowired
	private IWithdrawService withdrawService;

	@Autowired
	private BatchWithdrawDao batchWithdrawDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean saveCashApplay(String id, Long uid, String nickName, Integer goldleafCount, Double cash, String alipayAccount,
			String alipayAccountName, Integer userGoldLeaf, double userCash) {
		PayAccountWithdrawal accountWithDrawal = new PayAccountWithdrawal();
		try {
			accountWithDrawal.setId(id);
			accountWithDrawal.setAlipayAccount(alipayAccount);
			accountWithDrawal.setAliplayAccountName(alipayAccountName);
			accountWithDrawal.setCashQuantity(cash);
			accountWithDrawal.setCreateDate(new Date());
			accountWithDrawal.setCreaterName(nickName);
			accountWithDrawal.setLeafQuantity(goldleafCount);
			accountWithDrawal.setStatus(Constants.WITHDRAWAL_STATUS.WAIT_CHECK.code());
			accountWithDrawal.setUid(uid);
			accountWithDrawal.setUpdateDate(new Date());
			accountWithDrawal.setRemainLeafQuatity(userGoldLeaf);
			accountWithDrawal.setRemainCash(userCash);
			accountWithdrawalDao.saveApplyCash(accountWithDrawal);

			// 提现保存成功后 扣减用户账户余额
			PayUserAccount userAccount = userAccountDao.selectLock(uid);
			if(userAccount.getGoldLeaf() >= goldleafCount){
				userAccount.setGoldLeaf(userAccount.getGoldLeaf() - goldleafCount);
				userAccountDao.update(userAccount);
				
				//写流水
				PayAccountBill accountBill = new PayAccountBill();
				accountBill.setUid(uid);
				accountBill.setRefId(id);
				accountBill.setBillsType(3);
				accountBill.setCashQuantity(cash);
				accountBill.setDescription("用户'"+nickName+"'提现操作,金叶子数:"+goldleafCount+"");
//				accountBill.setGoldLeafBalanceQuantity(userAccount.getGoldLeaf());
//				accountBill.setGreenLeafBalanceQuantity(userAccount.getGreenLeaf());
				accountBill.setLeafQuantity(goldleafCount);
				accountBill.setLeafType(1);
//				accountBill.setRechargeType(rechargeType);
				accountBillDao.save(accountBill);
			}else{
				throw new RuntimeException();
			}
		} catch (Exception e) {
			errorLogger.info("save cash apply exception,Object info:{}", accountWithDrawal.toString());
			throw new RuntimeException();
		}
		return true;
	}

	@Override
	public PayUserAccount getUserAccountByUid(Long uid) {
		return userAccountDao.selectUserAccountByUid(uid);
	}

	@Override
	public List<PayAccountWithdrawal> getUserWithdrawalInfoDayByUid(Long uid) {
		List<PayAccountWithdrawal> withdrawals = null;
		try {
			withdrawals = accountWithdrawalDao.getUserWithdrawalInfoDayByUid(uid);
		} catch (Exception e) {
			errorLogger.info("get user apply times exception,uid:{}", uid);
			throw new RuntimeException();
		}
		return withdrawals;
	}
	
	@Override
	public List<PayAccountWithdrawal> getUserWithdrawalInfoDayByUidLock(Long uid) {
		List<PayAccountWithdrawal> withdrawals = null;
		try {
			withdrawals = accountWithdrawalDao.getUserWithdrawalInfoDayByUidLock(uid);
		} catch (Exception e) {
			errorLogger.info("get user apply times exception,uid:{}", uid);
			throw new RuntimeException();
		}
		return withdrawals;
	}

	@Override
	public Long getUserGiftListCount(Long uid, Integer type) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(uid != null){
			paramsMap.put("uid", uid);
		}
		if(type != null){
			paramsMap.put("type", type);
		}
		return userGiftDao.getUserGiftListCount(paramsMap);
	}
	

	@Override
	public List<PayUserGift> getUserGiftPage(Long uid, Integer type, Integer pagenum, Integer pagesize) {
		Integer limitStart = (pagenum - 1) * pagesize;
		Integer limitEnd = pagesize;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid", uid);
		paramMap.put("type", type);
		paramMap.put("limitStart", limitStart);
		paramMap.put("limitEnd", limitEnd);
		List<PayUserGift> userGifts = userGiftDao.getUserGiftPage(paramMap);
		return userGifts;
	}
	
	@Override
	public Long getUserGiftSum(Long uid, Integer type) {
		return userGiftDao.getUserGiftSum(uid, type);
	}
	
	@Override
	public Long getAnchorLiveRewardSumLeaf(Long programid,Long anchorid) {
		return liveRewardRecordDao.getAnchorLiveRewardSumLeaf(programid,anchorid);
	}

	@Override
	public List<PayTrade> getUserTradeRecordList(Long uid, Integer pagenum, Integer pagesize) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Integer limitStart = (pagenum - 1) * pagesize;
		Integer limitEnd = pagesize;
		paramsMap.put("uid", uid);
		paramsMap.put("limitStart", limitStart);
		paramsMap.put("limitEnd", limitEnd);
		return tradeDao.getUserTradeRecordList(paramsMap);
	}

	@Override
	public Long getUserTradeRecordCount(Long uid) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("uid", uid);
		return tradeDao.getUserTradeRecordCount(paramsMap);
	}

	@Override
	public Long getUserWithdrawalRecord(Long uid) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("uid", uid);
		return accountWithdrawalDao.getUserWithdrawalRecordCount(paramsMap);
	}

	@Override
	public List<PayAccountWithdrawal> getUserWithdrawalRecordList(Long uid, Integer pagenum, Integer pagesize) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Integer limitStart = (pagenum - 1) * pagesize;
		Integer limitEnd = pagesize;
		paramsMap.put("uid", uid);
		paramsMap.put("limitStart", limitStart);
		paramsMap.put("limitEnd", limitEnd);
		return accountWithdrawalDao.getUserWithdrawalRecordList(paramsMap);
	}

	@Override
	public Long getUserLiveRewardRecordCount(Long programid,Long anchorid) {
		return liveRewardRecordDao.getUserLiveRewardRecordCount(programid,anchorid);
	}

	@Override
	public List<PayLiveRewardRecord> getUserLiveRewardList(Long programid, Long anchorid, Integer pagenum, Integer pagesize) {
		Integer limitStart = (pagenum - 1) * pagesize;
		Integer limitEnd = pagesize;
		return liveRewardRecordDao.getUserLiveRewardList(programid,anchorid, limitStart, limitEnd);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public Boolean doRewardAnchor(UserInfo userInfo, UserInfo anchorInfo, Integer num, LivePlayProgrameDto liveDto, Long giftId) {
		try {
			PayGift gift = giftDao.getGift(giftId);
			Integer leafNum = gift.getGiftWorth() * num;
			PayUserAccount userAccount = userAccountDao.selectLock(userInfo.getUid());
			if (userAccount.getGreenLeaf() < leafNum) {
				throw new RuntimeException("账户余额不足");
			}
			userAccount.setGreenLeaf(userAccount.getGreenLeaf() - leafNum);
			Integer record = userAccountDao.update(userAccount);
			if (record != 1) {
				throw new RuntimeException("账户扣款失败");
			}
			PayAccountBill accountBill = new PayAccountBill();
			accountBill.setUid(userInfo.getUid());
			accountBill.setBillsType(1);
			accountBill.setLeafType(0);
			accountBill.setDescription("在直播" + liveDto.getName() + "中赠送礼物" + gift.getName() + "给主播" + anchorInfo.getUid());
			accountBill.setLeafQuantity(leafNum);
			accountBill.setCashQuantity(0.0);//暂时设为0
			accountBill.setGreenLeafBalanceQuantity(userAccount.getGreenLeaf());
			accountBill.setRefId(giftId + "");
			record = accountBillDao.save(accountBill);
			if (record != 1) {
				throw new RuntimeException("账户写流水失败");
			}
			return anchorService.doAnchorAccount(anchorInfo, gift, num, liveDto, userInfo);
		} catch (Exception e) {
			logger.info("doRewardAnchorError", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public PayTrade getTradeByUidAndPlanId(Long uid, Integer planId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("planId", planId);
			return tradeDao.getByUidAndPlanId(map);
		} catch (Exception e) {
			logger.info("getBillByUidAndPlanIdError", e);
			return new PayTrade();
		}
	}

	@Override
	public UserAccountDto getUserCash(Long uid) {
		UserAccountDto userAccountDto = null;
		PayUserAccount payUserAccount = userAccountDao.selectUserAccountByUid(uid);
		if (payUserAccount != null) {
			userAccountDto = new UserAccountDto();
			userAccountDto.setGreenLeafCount(payUserAccount.getGreenLeaf());
			userAccountDto.setGreenLeafCount(payUserAccount.getGoldLeaf());
			userAccountDto.setCash(payUserAccount.getGoldLeaf() * Constants.goldleafToCashRatio);
		}

		return userAccountDto;
	}
	
	@Override
	public Double goldLeafConvertToCash(Integer goldLeafCount){
		Double cash = 0.0;
		if(goldLeafCount != null){
			cash = goldLeafCount * Constants.goldleafToCashRatio;
		}
		return cash;
	}

	
	@Override
	public List<PayUserAccount> getUserAccountPage(Integer pagenum,Integer pagesize){
		Integer limitStart = (pagenum - 1) * pagesize;
		Integer limitEnd = pagesize;
		return userAccountDao.getUsreAccountPage(limitStart,limitEnd);
	}
	
	
	@Override
	public Long getAccountBillLeafCount(Long uid,Integer type){
		return accountBillDao.getAccountBillLeafCount(uid,type);
	}

	@Override
	public PageDto<PayUserGift> getUserGiftPageByParams(Long uid, Integer type, String giftName, String sendNickName, Integer pagenum, Integer pagesize) {
		PageDto<PayUserGift> userGiftPage = new PageDto<PayUserGift>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(uid != null){
			paramsMap.put("uid", uid);
		}
		if(type != null){
			paramsMap.put("type", type);
		}
		if(StringUtils.isNotBlank(giftName)){
			paramsMap.put("giftName", giftName);
		}
		if(StringUtils.isNotBlank(sendNickName)){
			paramsMap.put("sendNickName", sendNickName);
		}
		if(pagenum != null && pagesize != null){
			Integer limitStart = (pagenum - 1) * pagesize;
			Integer limitEnd = pagesize;
			paramsMap.put("limitStart", limitStart);
			paramsMap.put("limitEnd", limitEnd);
		}
		Long count = userGiftDao.getUserGiftListCount(paramsMap);
		if(count != null && count > 0){
			userGiftPage = new PageDto<PayUserGift>(count.intValue(), pagenum, pagesize);
			List<PayUserGift> payUserGifts = userGiftDao.getUserGiftPage(paramsMap);
			userGiftPage.setDataList(payUserGifts);
		}
		return userGiftPage;
	}
	
	
	@Override
	public PageDto<PayTrade> getUserTradePageByParams(Long uid, String orderid, Integer type, String planName, Integer pagenum,
			Integer pagesize) {
		PageDto<PayTrade> payTradePage = new PageDto<PayTrade>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(orderid != null){
			paramsMap.put("orderid", orderid);
		}
		if(uid != null){
			paramsMap.put("uid", uid);
		}
		if(type != null){
			paramsMap.put("type", type);
		}
		if(StringUtils.isNotBlank(planName)){
			paramsMap.put("planName", planName);
		}
		if(pagenum != null && pagesize != null){
			Integer limitStart = (pagenum - 1) * pagesize;
			Integer limitEnd = pagesize;
			paramsMap.put("limitStart", limitStart);
			paramsMap.put("limitEnd", limitEnd);
		}
		Long count = tradeDao.getUserTradeRecordCount(paramsMap);
		if(count != null && count > 0){
			payTradePage = new PageDto<PayTrade>(count.intValue(), pagenum, pagesize);
			List<PayTrade> payTrades = tradeDao.getUserTradeRecordList(paramsMap);
			payTradePage.setDataList(payTrades);
		}
		return payTradePage;
	}
	
	

	@Override
	public PageDto<PayAccountWithdrawal> getUserWithdrawalPageByParams(Long uid, Integer status, String id,
			Integer pagenum, Integer pagesize) {
		PageDto<PayAccountWithdrawal> withdrawalPage = new PageDto<PayAccountWithdrawal>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(uid != null){
			paramsMap.put("uid", uid);
		}
		if(status != null){
			paramsMap.put("status", status);
		}
		if(StringUtils.isNotBlank(id)){
			paramsMap.put("id", id);
		}
		if(pagenum != null && pagesize != null){
			Integer limitStart = (pagenum - 1) * pagesize;
			Integer limitEnd = pagesize;
			paramsMap.put("limitStart", limitStart);
			paramsMap.put("limitEnd", limitEnd);
		}
		Long count = accountWithdrawalDao.getUserWithdrawalRecordCount(paramsMap);
		if(count != null && count > 0){
			withdrawalPage = new PageDto<PayAccountWithdrawal>(count.intValue(), pagenum, pagesize);
			List<PayAccountWithdrawal> accountWithdrawals = accountWithdrawalDao.getUserWithdrawalRecordList(paramsMap);
			withdrawalPage.setDataList(accountWithdrawals);
		}
		return withdrawalPage;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public String batchTranMoney(List<String> withdrawIds,Long uid,String userName) throws UserServiceException {
		List<PayAccountWithdrawal> list =  accountWithdrawalDao.getWithdrawByIds(withdrawIds);
		List<PayBatchWithdraw> withdrawList = new ArrayList<PayBatchWithdraw>();
		String batchId = IdBuildUtils.buildBatchWithdrawId();
		for(PayAccountWithdrawal withdraw : list){
			if(0!=withdraw.getStatus()){
				list.remove(withdraw);
				continue;
			}
			PayBatchWithdraw batchWithdraw = new PayBatchWithdraw();
			batchWithdraw.setStatus(0);
			batchWithdraw.setBatchNo(batchId);
			batchWithdraw.setWithdrawId(withdraw.getId());
			batchWithdraw.setCreateDate(new Date());
			batchWithdraw.setAlipayAccount(withdraw.getAlipayAccount());
			batchWithdraw.setAlipayName(withdraw.getAliplayAccountName());
			batchWithdraw.setCashQuantity(withdraw.getCashQuantity());
			withdrawList.add(batchWithdraw);
		}
		Boolean flag = withdrawService.batchAudit(withdrawIds,1,"审核通过",uid,userName);
		if(!flag){
			throw new RuntimeException();
		}
		Integer record = batchWithdrawDao.batchInsert(withdrawList);
		if(record != withdrawList.size()){
			throw new RuntimeException();
		}
		return batchId;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public Boolean withdrawFail(List<PayAccountWithdrawal> list) {
		if(list == null || list.isEmpty()){
			return false;
		}
		for(PayAccountWithdrawal pay : list){
			if(pay.getStatus() !=0 && pay.getStatus() != 1){
				logger.info("withdrawId"+ pay.getId() + " withdrawuid"+pay.getUid());
				continue;
			}
			PayAccountBill accountBill = new PayAccountBill();
			accountBill.setUid(pay.getUid());
			accountBill.setBillsType(4);
			accountBill.setLeafType(1);
			accountBill.setDescription("提现失败"+pay.getId());
			accountBill.setLeafQuantity(pay.getLeafQuantity());
			accountBill.setCashQuantity(pay.getCashQuantity());
			accountBill.setGreenLeafBalanceQuantity(0);
			accountBill.setRefId(pay.getId() + "");
			Integer record = accountBillDao.save(accountBill);
			if(record != 1){
				logger.info("withdrawBillFail"+pay.getId() + " withdrawUid"+pay.getUid());
				throw new RuntimeException();
			}
			PayUserAccount userAccount = userAccountDao.selectLock(pay.getUid());
			if(userAccount== null){
				logger.info("withdrawAccountFail" + pay.getId() + " withdrawUid"+pay.getUid());
				throw new RuntimeException();
			}
			userAccount.setGoldLeaf(userAccount.getGoldLeaf()+pay.getLeafQuantity());
			record = userAccountDao.update(userAccount);
			if(record != 1){
				logger.info("withdrawAccountUpdateFail"+pay.getId() + " withdrawUid"+pay.getUid());
				throw new RuntimeException();
			}
		}
		return true;
	}

	public static void main(String [] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:application-service.xml" });
		UserCenterServiceImpl userCenterService = context.getBean(UserCenterServiceImpl.class);
		PayTrade bill =  userCenterService.getTradeByUidAndPlanId(2158714l,1);
		if(bill == null){
			System.out.println("aaaaaaaaaaaa");
		}
	}
}
