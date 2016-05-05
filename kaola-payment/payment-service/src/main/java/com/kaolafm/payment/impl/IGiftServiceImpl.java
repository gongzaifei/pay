package com.kaolafm.payment.impl;

import java.util.List;

import com.kaolafm.payment.dao.WithdrawLogDao;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.entity.PayLog;
import com.kaolafm.payment.entity.PayRechargePlan;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.kaolafm.payment.dao.GiftDao;
import com.kaolafm.payment.entity.PayGift;
import com.kaolafm.payment.service.IGiftService;

@Service("giftServiceImpl")
public class IGiftServiceImpl implements IGiftService {

	private Logger log = LoggerFactory.getLogger(IGiftServiceImpl.class);
	@Autowired
	private GiftDao giftDao;

	@Autowired
	private WithdrawLogDao logDao;

	@Override
	public Long getGiftCount() {
		return this.getGiftCount(0, null);
	}
	
	@Override
	public Long getGiftCount(Integer status,String name) {
		return giftDao.getGiftCount(status,name);
	}

	@Override
	public List<PayGift> getGiftList(Integer pagenum, Integer pagesize) {
		return this.getGiftList(0,null,pagenum,pagesize);
	}

	@Override
	public PayGift getGift(Long giftId) {
		return  giftDao.getGift(giftId);
	}

	@Override
	public List<PayGift> getGiftList(Integer status, String name, Integer pagenum, Integer pagesize) {
		Integer limitStart = (pagenum-1)*pagesize;
		Integer limitEnd = pagesize;
		return giftDao.getGiftList(status,name,limitStart,limitEnd);
	}

	@Override
	public PayGift getGiftUse(Long giftId) {

		return giftDao.getGiftUse(giftId);
	}

	@Override
	public boolean savePayGift(PayGift payGift,Long uid,String userName) {
		if(payGift == null){
			return false;
		}
		PayGift temp = giftDao.getGiftByName(payGift.getName());
		if(temp != null ){
			return false;
		}
		Integer order = giftDao.getGiftMaxOrder();
		payGift.setStatus(1);
		payGift.setCreateby(Integer.valueOf(uid+""));
		payGift.setExchangeRate(0.50);
		payGift.setCreaterName(userName);
		payGift.setSort(order+1);
		Integer record = giftDao.savePayGift(payGift);
		if(record == 1){
			saveLog(payGift,3);
			return true;
		}else{
			throw new RuntimeException();
		}
	}

	@Override
	public boolean updatePayGift(PayGift payGift,Long uid,String userName) {
		try{
			PayGift temp =  giftDao.getGift(payGift.getId());
			if(temp == null){
				return false;
			}
			PayGift nameGift = giftDao.getGiftByName(payGift.getName());
			if(nameGift != null && nameGift.getId()!= payGift.getId()){
				return false;
			}
			temp.setStatus(payGift.getStatus());
			temp.setUpdateby(uid);
			temp.setUpdaterName(userName);
			temp.setName(payGift.getName());
			temp.setImg(payGift.getImg());
			temp.setGiftWorth(payGift.getGiftWorth());
			Integer result = giftDao.updatePayGift(temp);
			if(result !=1){
				throw new RuntimeException();
			}
			saveLog(payGift,4);
			return true;
		}catch (Exception e){
			log.info("updatePayGiftError",e);
		}
		return false;
	}
	
	@Override
	public boolean batchUpdateGift(List<Long> giftIds,Integer status,Long uid,String userName){
		try{
			giftDao.batchUpdateGift(giftIds,status);
			for(Long giftId : giftIds){
				PayLog payLog = new PayLog();
				payLog.setStatus(0);
				payLog.setUserId(uid);
				payLog.setUserName(userName);
				payLog.setDataId(String.valueOf(giftId));
				payLog.setDataType(2);
				payLog.setOperateType(status);
				logDao.save(payLog);
			}
			return true;
		}catch (Exception e){
			log.info("batchDeleteGiftError",e);
			return false;
		}

	}

	@Override
	public PageDto<PayGift> getPageGift(String name, Integer status, Integer pageNum, Integer pageSize) {
		if(pageNum <=0){
			pageNum =1;
		}
		if(pageSize <=0 || pageSize >50){
			pageSize = 50;
		}
		if("".equals(name)){
			name = null;
		}
		Long count = getGiftCount(status,name);
		if(count == 0){
			return null;
		}
		PageDto<PayGift> page = new PageDto<PayGift>(Integer.valueOf(count+""),pageNum,pageSize);
		List<PayGift> list = giftDao.getGiftList(status,name,page.getStartNum(),page.getEndNum());
		page.setDataList(list);
		return page;
	}

	@Override
	public Boolean giftOrder(String orderJson,Long uid,String userName) {
		try{
			JSONArray params = JSONArray.fromObject(orderJson);
			for(Object param : params){
				JSONObject json = JSONObject.fromObject(param);
				Long id = json.getLong("id");
				Integer order = json.getInt("order");
				PayGift payGift =  giftDao.getGift(id);
				if(payGift !=null){
					payGift.setUpdateby(uid);
					payGift.setUpdaterName(userName);
					payGift.setSort(order);
				}
				Integer result = giftDao.updatePayGift(payGift);
				if(result !=1){
					throw new RuntimeException();
				}
				PayLog payLog = new PayLog();
				payLog.setStatus(0);
				payLog.setUserId(uid);
				payLog.setUserName(userName);
				payLog.setDataId(String.valueOf(payGift.getId()));
				payLog.setDataType(2);
				payLog.setOperateType(6);
				logDao.save(payLog);
			}
			return true;
		}catch (Exception e){
			log.info("giftOrderError",e);
			return false;
		}
	}

	@Override
	public List<PayLog> getLogList(String dataId) {

		return logDao.withdrawLogList(dataId,2);
	}

	@Override
	public Boolean verifyName(String giftName,Long id) {
		Boolean flag = false;
		PayGift nameGift = giftDao.getGiftByName(giftName);
		if(nameGift !=null){
			if(id == null){
				flag = true;
			}
			if(id != null && id == nameGift.getId()){
				flag = false;
			}
		}
		return flag;
	}

	private void saveLog(PayGift gift,Integer operateType){
		try{
			Long uid = null;
			if(gift.getUpdateby() != null){
				uid =  Long.valueOf(gift.getUpdateby());
			}
			String name = gift.getUpdaterName();
			if(operateType == 3){
				if(gift.getCreateby() != null){
					uid =  Long.valueOf(gift.getCreateby());
				}
				name = gift.getCreaterName();
			}
			PayLog payLog = new PayLog();
			payLog.setStatus(0);
			payLog.setUserId(uid);
			payLog.setUserName(name);
			payLog.setDataId(""+gift.getId());
			payLog.setDataType(2);
			payLog.setOperateType(operateType);
			logDao.save(payLog);
		}catch (Exception e){
			e.printStackTrace();
		}

	}


	public static void main(String [] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:application-service.xml" });
		IGiftServiceImpl giftService = context.getBean(IGiftServiceImpl.class);
		PayGift gift = new PayGift();
		gift.setId(5l);
		gift.setName("aaaaaasa");
		gift.setGiftWorth(100);
		gift.setImg("aaaa");
		gift.setCreateby(11);
		Boolean flag  =  giftService.savePayGift(gift,11111l,"test");
		System.out.println(flag);

	}

}
