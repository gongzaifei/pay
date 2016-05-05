package com.kaolafm.payment;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kaolafm.payment.entity.PayGift;
import com.kaolafm.payment.entity.PayRechargePlan;
import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.payment.service.IGiftService;
import com.kaolafm.payment.service.IOrderService;
import com.kaolafm.payment.service.IRechargePlanService;
import com.kaolafm.payment.service.IUserCenterService;
import com.kaolafm.payment.service.IWithdrawService;
import com.kaolafm.user.dto.PageDto;
import com.kaolafm.user.entity.UserInfo;
import com.kaolafm.user.exception.UserServiceException;

public class GiftServiceTest{

	private IGiftService iGiftService;
	
	private IRechargePlanService iRechargePlanService;
	
	private IOrderService iOrderService;
	
	private IUserCenterService iUserCenterService;

	@Before
	public void setUp(){
		ApplicationContext context = new ClassPathXmlApplicationContext("application-service.xml");

		iGiftService = context.getBean(IGiftService.class);
		iRechargePlanService = context.getBean(IRechargePlanService.class);
		iOrderService = context.getBean(IOrderService.class);
		iUserCenterService=context.getBean(IUserCenterService.class);
	}
	
	/*@Test
	public void testSaveGift(){
		PayGift gift = new PayGift();
		gift.setCreateby(2107812);
		gift.setCreaterName("易水寒");
		gift.setExchangeRate(0.5);
		gift.setGiftWorth(800);
		gift.setImg("http://image.kaolafm.net/mz/images/201601/f5e957b3-a350-4ee9-bd89-7176aa1a0bca/100_100.jpg");
		gift.setName("20元代金券");
		gift.setSort(4);
		iGiftService.savePayGift(gift);
	}*/
	
	/*@Test
	public void testUpdateGift(){
		PayGift gift = iGiftService.getGift(1l);
		gift.setCreaterName("风萧萧");
		iGiftService.updatePayGift(gift);
	}
	
	@Test
	public void testBatchDeleteGift(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(1l);
		ids.add(2l);
		iGiftService.batchDeleteGift(ids);
	}*/
	
	/*@Test
	public void testSaveRechargePlan(){
		PayRechargePlan payRechargePlan = new PayRechargePlan();
		payRechargePlan.setApplyType(0);
		payRechargePlan.setCashFee(0.01);
		payRechargePlan.setLeafQuantity(10);
		payRechargePlan.setName("套餐C");
		payRechargePlan.setDescription("套餐C兑换");
		payRechargePlan.setImg("http://baidu.com");
		payRechargePlan.setPresentLeafQuantity(0);
		payRechargePlan.setPlanLimitType(0);
		
		iRechargePlanService.saveRechargePlan(payRechargePlan);
	}*/
	
	/*@Test
	public void testSaveRechargePlan(){
		PayRechargePlan payRechargePlan = iOrderService.getPlan(3);
		payRechargePlan.setApplyType(0);
		payRechargePlan.setCashFee(0.01);
		payRechargePlan.setLeafQuantity(10);
		payRechargePlan.setName("套餐CC");
		payRechargePlan.setDescription("套餐CC兑换");
		payRechargePlan.setImg("http://baidu.com");
		payRechargePlan.setPresentLeafQuantity(0);
		payRechargePlan.setPlanLimitType(0);
		
		iRechargePlanService.updateRechargePlan(payRechargePlan);
	}*/
	
	@Test
	public void testBatchUpdatePlan(){
		/*List<Long> ids = new ArrayList<Long>();
		ids.add(1l);
		ids.add(2l);
		
		iRechargePlanService.batchUpdatePlanStatus(ids,1);*/
		/*Long count = iUserCenterService.getUserTradeRecordCount(2016032401l);
		System.out.println(count);
		
		List<PayTrade> payTrades = iUserCenterService.getUserTradeRecordList(2016032401l, 1, 2);
		System.out.println("list====="+payTrades);*/
		
		Long count = iUserCenterService.getAccountBillLeafCount(2107812l, 0);
		System.out.println("count=========="+count);
	}

}
