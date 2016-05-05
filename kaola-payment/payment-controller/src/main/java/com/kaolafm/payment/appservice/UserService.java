package com.kaolafm.payment.appservice;

import java.util.List;
import java.util.Map;

import com.kaolafm.payment.dto.AccountInfoDto;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.dto.PlanDto;
import com.kaolafm.payment.dto.RewardDto;
import com.kaolafm.payment.dto.TradeRecordDto;
import com.kaolafm.payment.dto.UserGiftDto;
import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.user.exception.UserServiceException;

public interface UserService {
	
	/**
	 * 保存用户体现申请
	 * @param accountname 
	 * @param account 
	 * @param money 
	 * @param goldleafcount 
	 * @return
	 * @throws ServiceException 
	 */
	public boolean saveApplyLeafToCash(Integer goldleafcount, Double money, String account, String accountname) throws ServiceException;


	/**
	 * 用户获取充值套餐表
	 * @param commonParams
	 * @return
     */
	public List<PlanDto> getRechargeList(CommonParams commonParams);

	/**
	 * 获取用户可体现金额
	 * @param uid
	 * @return
	 * @throws ServiceException 
	 */
	public Integer getWithdrawalCash(String uid) throws ServiceException;


	/**
	 * 查询用户礼物列表
	 * @param type
	 * @param pagesize 
	 * @param pagenum 
	 * @return
	 */
	public PageDto<UserGiftDto> getUserGiftList(Integer type, Integer pagenum, Integer pagesize);


	/**
	 * 获取支付结果
	 * @param outtradeno
	 * @return
	 * @throws ServiceException
	 */
	public PayTrade getPayState(String outtradeno) throws ServiceException;


	/**
	 * 获取用户账户信息
	 * @param uid
	 * @param anchoruid 
	 * @return
	 * @throws ServiceException 
	 */
	public AccountInfoDto getUserAccountInfo(Long uid,Integer type,Long programid, Long anchoruid) throws ServiceException;


	/**
	 * 获取用户交易记录列表
	 * 
	 * @param uid
	 * @param type
	 * @param pagenum
	 * @param pagesize
	 * @return
	 * @throws ServiceException
	 */
	public PageDto<TradeRecordDto> getTradeList(Long uid, Integer type, Integer pagenum, Integer pagesize) throws ServiceException;



	public void rewardAnchor(long anchorId,long giftId,int num,long programId,CommonParams commonParams) throws ServiceException, UserServiceException;


	/**
	 * 获取用户直播计划打赏列表
	 * @param uid
	 * @param programid
	 * @param pagenum
	 * @param pagesize
	 * @return
	 * @throws ServiceException 
	 */
	public PageDto<RewardDto> getRewardList(Long uid, Long anchorid, Long programid, Integer pagenum, Integer pagesize) throws ServiceException;


	/**
	 * 获取用户当日剩余可提现信息
	 * @param uid
	 * @return
	 */
	public Integer getUserRemainWithdrawalTimes(Long uid);

}
