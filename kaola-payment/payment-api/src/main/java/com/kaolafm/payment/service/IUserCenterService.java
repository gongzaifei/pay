package com.kaolafm.payment.service;

import java.util.List;

import com.kaolafm.cache.dto.LivePlayProgrameDto;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.dto.UserAccountDto;
import com.kaolafm.payment.entity.PayAccountWithdrawal;
import com.kaolafm.payment.entity.PayLiveRewardRecord;
import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.payment.entity.PayUserAccount;
import com.kaolafm.payment.entity.PayUserGift;
import com.kaolafm.user.entity.UserInfo;
import com.kaolafm.user.exception.UserServiceException;

/**
 * @author gongzf
 * @date 2016/3/12
 */
public interface IUserCenterService {
	
	public PayUserAccount getUserAccountByUid(Long uid);
	
	/**
	 * 保存提现申请
	 * @param uid
	 * @param nickName
	 * @param goldleafCount
	 * @param cash
	 * @param alipayCount
	 * @param alipayCountName
	 * @param userCash 
	 * @param userGoldLeaf 
	 * @return
	 */
	public boolean saveCashApplay(String id, Long uid, String nickName, Integer goldleafCount, Double cash, String alipayCount,
			String alipayCountName, Integer userGoldLeaf, double userCash);

	/**
	 * 查询用户一天之内的申请提现信息
	 * @param uid
	 * @return
	 */
	public List<PayAccountWithdrawal> getUserWithdrawalInfoDayByUid(Long uid);

	/**
	 * 获取用户礼物列表总条目数
	 * @param uid
	 * @param type
	 * @return
	 */
	public Long getUserGiftListCount(Long uid, Integer type);
	
	/**
	 * 获取用户礼物列表
	 * @param uid
	 * @param type
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public List<PayUserGift> getUserGiftPage(Long uid, Integer type, Integer pagenum, Integer pagesize);

	/**
	 * 获取用户礼物总数
	 * @param uid
	 * @param type
	 * @return
	 */
	public Long getUserGiftSum(Long uid, Integer type);

	/**
	 * 根据programid获取用户收到的打赏叶子总数
	 * @param programid
	 * @param anchoruid 
	 * @return
	 */
	public Long getAnchorLiveRewardSumLeaf(Long programid, Long anchoruid);

	/**
	 * 获取用户交易记录列表
	 * @param uid
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public List<PayTrade> getUserTradeRecordList(Long uid, Integer pagenum, Integer pagesize);

	/**
	 * 获取用户充值记录数
	 * @param uid
	 * @return
	 */
	public Long getUserTradeRecordCount(Long uid);

	/**
	 * 获取用户提现记录数
	 * @param uid
	 * @return
	 */
	public Long getUserWithdrawalRecord(Long uid);

	/**
	 * 获取用用提现记录
	 * @param uid
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public List<PayAccountWithdrawal> getUserWithdrawalRecordList(Long uid, Integer pagenum, Integer pagesize);

	/**
	 * 根据计划id获取打赏记录数
	 * @param programid
	 * @param anchorid 
	 * @return
	 */
	public Long getUserLiveRewardRecordCount(Long programid, Long anchorid);

	/**
	 * 根据programid获取打赏列表
	 * @param programid
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public List<PayLiveRewardRecord> getUserLiveRewardList(Long programid, Long anchorid, Integer pagenum, Integer pagesize);
	 
	/** 主播打赏
	 * @param userInfo
	 * @param anchorInfo
	 * @param num
	 * @param liveDto
     */
	public Boolean doRewardAnchor(UserInfo userInfo,UserInfo anchorInfo,Integer num, LivePlayProgrameDto liveDto, Long giftId);

	/**
	 * 查看用户是否充值过这个套餐
	 * @param uid
	 * @param planId
     * @return
     */
	public PayTrade getTradeByUidAndPlanId(Long uid, Integer planId);
	
	/**
	 * 根据用户id获取用户绿叶子数 金叶子数  金叶子对应的人民币数
	 * @param uid
	 * @return
	 */
	public UserAccountDto getUserCash(Long uid);

	/**
	 * 根据金叶子数转换为rmb
	 * @param goldLeafCount
	 * @return
	 */
	public Double goldLeafConvertToCash(Integer goldLeafCount);

	/**
	 * 用户提现申请时 加锁判断提现次数
	 * @param uid
	 * @return
	 */
	public List<PayAccountWithdrawal> getUserWithdrawalInfoDayByUidLock(Long uid);


	/**
	 * 分页获取用户账户列表
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public List<PayUserAccount> getUserAccountPage(Integer pagenum, Integer pagesize);

	/**
	 * 获取用户账单流水绿叶子/金叶子数
	 * @param uid
	 * @param type
	 * @return
	 */
	public Long getAccountBillLeafCount(Long uid, Integer type);

	
	/**
	 * 后台调用  分页获取用户收到/送出礼物记录 3 收到 4送出
	 * @param type
	 * @param name
	 * @param nickName
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public PageDto<PayUserGift> getUserGiftPageByParams(Long uid, Integer type,String name,String nickName,Integer pagenum,Integer pagesize);

	/**
	 * 后台调用  获取充值记录
	 * @param uid
	 * @param type
	 * @param planName
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public PageDto<PayTrade> getUserTradePageByParams(Long uid, String orderid, Integer type, String planName, Integer pagenum, Integer pagesize);


	/**
	 * 后台调用 获取用户提现记录
	 * @param uid
	 * @param status
	 * @param id
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public PageDto<PayAccountWithdrawal> getUserWithdrawalPageByParams(Long uid, Integer status, String id, Integer pagenum, Integer pagesize);

	/**
	 * 批量审核提现成功并转账。支付宝接口暂停使用
	 * @param withdrawIds
	 * @return
     */
	public String batchTranMoney(List<String> withdrawIds,Long uid,String userName) throws UserServiceException;


	/**
	 * 批量审核失败 流水写入 账户更新
	 * @param list
	 * @return
     */
	public Boolean withdrawFail(List<PayAccountWithdrawal> list);

}
