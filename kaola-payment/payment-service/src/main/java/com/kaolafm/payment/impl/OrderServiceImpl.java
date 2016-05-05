package com.kaolafm.payment.impl;

import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.dao.*;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kaolafm.payment.dto.AlipayDto;
import com.kaolafm.payment.dto.FillOrderDto;
import com.kaolafm.payment.service.IOrderService;

import java.util.List;

/**
 * @author gongzf
 * @date 2016/3/16
 */
@Service
public class OrderServiceImpl implements IOrderService {

	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	@Autowired
	private RechargePlanDao rechargePlanDao;
	@Autowired
	private TradeDao tradeDao;
	@Autowired
	private AlipayDao alipayDao;
	@Autowired
	private UserAccountDao userAccountDao;
	@Autowired
	private AccountBillDao accountBillDao;
	@Autowired
	private UserGiftDao userGiftDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean saveTrade(FillOrderDto fillOrderDto) throws Exception {
		try {
			PayRechargePlan plan = rechargePlanDao.getPlanById(fillOrderDto.getPlanId());
			if (plan == null) {
				throw new RuntimeException();
			}
			PayTrade trade = new PayTrade();
			trade.setType(fillOrderDto.getType());
			trade.setStatus(fillOrderDto.getStatus());
			trade.setPlanFee(plan.getCashFee());
			trade.setPlanId(plan.getId());
			trade.setPlanImg(plan.getImg());
			trade.setPlanLeafQuantity(plan.getLeafQuantity());
			trade.setPlanPresentLeaf(plan.getPresentLeafQuantity());
			trade.setPlanName(plan.getName());
			trade.setQuantity(1);
			trade.setTradeId(fillOrderDto.getOrderId());
			trade.setTradeSumFee(plan.getCashFee());
			trade.setUid(fillOrderDto.getUid());
			trade.setCreaterName(fillOrderDto.getUserName());
			Integer record = tradeDao.save(trade);
			if (record == 1) {
				return true;
			}
		} catch (Exception e) {
			logger.info("saveTradeError", e);
			throw new RuntimeException();
		}
		return false;
	}

	@Override
	public Double getPlanFee(Integer planId) {
		PayRechargePlan plan = rechargePlanDao.getPlanById(planId);
		if (plan == null) {
			return null;
		}
		return plan.getCashFee();
	}

	@Override
	public AlipayDto getAlipay(String outTradeNo) {
		PayAlipayResult alipayResult = alipayDao.getByOutTradeNo(outTradeNo);
		if (alipayResult != null) {
			AlipayDto alipayDto = new AlipayDto();
			alipayDto.setOutTradeNo(alipayResult.getOutTradeNo());
			alipayDto.setTradeStatus(alipayResult.getTradeStatus());
			alipayDto.setTotalFee(alipayResult.getTotalFee());
			return alipayDto;
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean doUserAccount(String outTradeNo, Integer payType) {
		PayTrade trade = tradeDao.getTradeByTradeId(outTradeNo);
		if (trade == null) {
			logger.info("TradeIsNull:" + outTradeNo);
			throw new RuntimeException();
		}
		PayUserAccount userAccount = userAccountDao.selectLock(trade.getUid());
		Integer record = 0;
		if (userAccount == null) {
			userAccount = new PayUserAccount();
			userAccount.setUid(trade.getUid());
			userAccount.setGreenLeaf(trade.getPlanLeafQuantity());
			userAccount.setGoldLeaf(0);
			record = userAccountDao.save(userAccount);
			if (record != 1) {
				logger.info("doUserAccountSaveError:" + trade.getUid() + " outTradeNo" + outTradeNo);
				throw new RuntimeException();
			}
		} else {
			userAccount.setGreenLeaf(userAccount.getGreenLeaf() + trade.getPlanLeafQuantity());
			record = userAccountDao.update(userAccount);
			if (record != 1) {
				logger.info("doUserAccountUpdateError:" + trade.getUid() + " outTradeNo" + outTradeNo);
				throw new RuntimeException();
			}
		}
		if (trade.getStatus() != 0) {
			logger.info("TradeFinish:" + outTradeNo + "TradeStatus:" + trade.getStatus());
			throw new RuntimeException();
		}
		trade.setStatus(1);
		record = tradeDao.update(trade);
		if (record != 1) {
			logger.info("TradeUpdateError:" + outTradeNo + "TradeStatus:" + trade.getStatus());
			throw new RuntimeException();
		}
		PayAccountBill bill = new PayAccountBill();
		bill.setUid(trade.getUid());
		bill.setBillsType(0);
		bill.setCashQuantity(trade.getTradeSumFee());
		bill.setLeafType(0);
		bill.setLeafQuantity(trade.getPlanLeafQuantity());
		bill.setRefId(trade.getTradeId());
		bill.setRechargeType(payType);
		String desc = "";
		if(payType == Constants.PAY_TYPE.ALIPAY.code()){
			desc = Constants.PAY_TYPE.ALIPAY.name();
		}else if(payType == Constants.PAY_TYPE.WEIXIN.code()){
			desc = Constants.PAY_TYPE.WEIXIN.name();
		}else{
			desc = Constants.PAY_TYPE.IOSPAY.name();
		}
		bill.setDescription("通过"+ desc +"购买"+trade.getPlanName()+"充值");
		record = accountBillDao.save(bill);
		if (record != 1) {
			logger.info("doUserAccountBillsSaveError:" + trade.getUid() + " outTradeNo" + outTradeNo);
			throw new RuntimeException();
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean closeTrade(String outTradeNo) {
		PayTrade trade = tradeDao.getTradeByTradeId(outTradeNo);
		if (trade == null) {
			logger.info("TradeIsNull:" + outTradeNo);
			throw new RuntimeException();
		}
		trade.setStatus(2);
		Integer record = tradeDao.update(trade);
		if (record != 1) {
			logger.info("TradeUpdateError:" + outTradeNo + "TradeStatus:" + trade.getStatus());
			throw new RuntimeException();
		}

		return true;
	}

	@Override
	public PayRechargePlan getPlan(Integer planId) {
		PayRechargePlan plan = rechargePlanDao.getPlanById(planId);
		if (plan == null) {
			return null;
		}
		return plan;
	}

	@Override
	public PayTrade getTrade(String outTradeNo) {
		PayTrade payTrade = tradeDao.getTradeByTradeId(outTradeNo);
		return payTrade;
	}

	@Override
	public PayUserGift getUserGift(Integer id) {
		return userGiftDao.getUserGift(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public void doPresentLeaf(String outTradeNo) {
		logger.info("PresentOutTradeNo:"+outTradeNo);
		try{
			PayTrade payTrade = tradeDao.getTradeByTradeId(outTradeNo);
			if(payTrade == null){
				return;
			}
			Integer planId =payTrade.getPlanId();
			PayRechargePlan plan =  rechargePlanDao.getPlanById(planId);
			if(plan == null){
				return;
			}
			if(plan.getPresentLeafQuantity() == null || plan.getPresentLeafQuantity() == 0){
				return;
			}
			PayUserAccount userAccount =  userAccountDao.selectLock(payTrade.getUid());
			userAccount.setGreenLeaf(userAccount.getGreenLeaf()+plan.getPresentLeafQuantity());
			Integer record = userAccountDao.update(userAccount);
			if(record != 1){
				throw new RuntimeException();
			}
			PayAccountBill bill = new PayAccountBill();
			bill.setUid(payTrade.getUid());
			bill.setBillsType(4);
			bill.setCashQuantity(0.00);
			bill.setLeafType(0);
			bill.setLeafQuantity(plan.getPresentLeafQuantity());
			bill.setRefId(payTrade.getTradeId());
			bill.setDescription("套餐"+plan.getName()+"赠送叶子"+plan.getPresentLeafQuantity());
			record = accountBillDao.save(bill);
			if (record != 1) {
				logger.info("doUserAccountBillsPresentError:" + payTrade.getUid() + " outTradeNo" + outTradeNo);
				throw new RuntimeException();
			}
		}catch (Exception e){
			logger.info("doPresentLeafError",e);
			throw new RuntimeException();
		}

	}
	@Override
	public PageDto<PayTrade> getTradeRecordList(Integer payType, Integer payStatus, String orderid,
												String nickName,String planName,Integer pageNum, Integer pageSize){

		if(pageNum <=0){
			pageNum =1;
		}
		if(pageSize <=0 || pageSize >50){
			pageSize = 50;
		}
		if("".equals(nickName)){
			nickName = null;
		}
		if("".equals(planName)){
			planName = null;
		}
		if("".equals(orderid)){
			orderid = null;
		}
		Integer  count = tradeDao.getTradeRecordCount(payType,payStatus,orderid,nickName,planName);
		if(count == null || count == 0){
			return null;
		}
		PageDto<PayTrade> page = new PageDto<PayTrade>(count,pageNum,pageSize);
		List<PayTrade> list = tradeDao.getTradeRecordList(payType,payStatus,orderid,nickName,planName,page.getStartNum(),page.getEndNum());
		page.setDataList(list);
		return page;
	}

	@Override
	public PageDto<PayUserGift> getUserGiftList(Integer orderId, String giftName, String nickName, String anchorName, Integer pageNum, Integer pageSize) {
		if(pageNum <=0){
			pageNum =1;
		}
		if(pageSize <=0 || pageSize >50){
			pageSize = 50;
		}
		if("".equals(nickName)){
			nickName = null;
		}
		if("".equals(giftName)){
			giftName = null;
		}
		if("".equals(anchorName)){
			anchorName =null;
		}
		Integer  count = userGiftDao.count(orderId,giftName,nickName,anchorName);
		if(count == null || count == 0){
			return null;
		}
		PageDto<PayUserGift> page = new PageDto<PayUserGift>(count,pageNum,pageSize);
		List<PayUserGift> list = userGiftDao.list(orderId,giftName,nickName,anchorName,page.getStartNum(),page.getEndNum());
		page.setDataList(list);
		return page;
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:application-service.xml" });
		OrderServiceImpl orderService = context.getBean(OrderServiceImpl.class);
		PageDto<PayTrade> page =  orderService.getTradeRecordList(null,null,"","","",1,10);
		for(PayTrade trade : page.getDataList()){
			System.out.println(trade.getCreaterName());
		}
	}
}
