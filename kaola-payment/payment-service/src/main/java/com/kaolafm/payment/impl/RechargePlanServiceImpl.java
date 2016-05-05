package com.kaolafm.payment.impl;

import com.kaolafm.payment.dao.RechargePlanDao;
import com.kaolafm.payment.dao.WithdrawLogDao;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.entity.PayLog;
import com.kaolafm.payment.entity.PayRechargePlan;
import com.kaolafm.payment.service.IRechargePlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gongzf
 * @date 2016/3/22
 */
@Service
public class RechargePlanServiceImpl implements IRechargePlanService {
    @Autowired
    private RechargePlanDao rechargePlanDao;

	@Autowired
	private WithdrawLogDao logDao;
    
    @Override
    public List<PayRechargePlan> getRechargePlanList(Integer deviceType) {

        List<PayRechargePlan> reList =rechargePlanDao.getRechargePlanList(deviceType);

        return reList;
    }
    
    @Override
    public boolean saveRechargePlan(PayRechargePlan rechargePlan){
    	if(rechargePlan == null){
    		return false;
    	}
		PayRechargePlan temp = rechargePlanDao.getPlanByName(rechargePlan.getName());
		if(temp != null){
			return false;
		}
    	if(rechargePlan.getPresentLeafQuantity() == null){
    		rechargePlan.setPresentLeafQuantity(0);
    	}
		rechargePlan.setStatus(1);
    	Integer result = rechargePlanDao.save(rechargePlan);
    	if(result == 1){
			saveLog(rechargePlan,3);
    		return true;
    	}else{
    		throw new RuntimeException();
    	}
    }
    
    @Override
    public boolean updateRechargePlan(PayRechargePlan rechargePlan){
    	if(rechargePlan == null){
    		return false;
    	}
		PayRechargePlan idPlan =  rechargePlanDao.getPlanAll(rechargePlan.getId());
		if(idPlan == null){
			return false;
		}
		PayRechargePlan namePlan = rechargePlanDao.getPlanByName(rechargePlan.getName());
		if(namePlan!=null && namePlan.getId()!= rechargePlan.getId()){
			return false;
		}
    	Integer result = rechargePlanDao.update(rechargePlan);
    	if(result == 1){
			saveLog(rechargePlan,4);
    		return true;
    	}else{
    		throw new RuntimeException();
    	}
    }
    
    @Override
    public boolean batchUpdatePlanStatus(List<Long> planIds, Integer status,String userName,Long uid){
    	Integer result = 0;
    	if(planIds != null && planIds.size() > 0 ){
    		result = rechargePlanDao.batchUpdatePlanStatus(planIds,status);
    	}
    	if(result > 0){
			for(Long id : planIds){
				PayLog payLog = new PayLog();
				payLog.setStatus(0);
				payLog.setUserId(uid);
				payLog.setUserName(userName);
				payLog.setDataId(String.valueOf(id));
				payLog.setDataType(3);
				payLog.setOperateType(status);
				logDao.save(payLog);
			}
			return true;
		}else{
			throw new RuntimeException();
		}
    }

	@Override
	public PageDto<PayRechargePlan> getRechargePlanPage(Integer deviceType, Integer status, String name, Integer pageNum, Integer pageSize) {
		if(pageNum <=0){
			pageNum =1;
		}
		if(pageSize <=0 || pageSize >50){
			pageSize = 50;
		}
		Integer count = rechargePlanDao.count(deviceType,status,name);
		if(count == null || count == 0){
			return null;
		}
		PageDto<PayRechargePlan> page = new PageDto<PayRechargePlan>(count,pageNum,pageSize);
		List<PayRechargePlan> list = rechargePlanDao.pageRechargePlan(deviceType,status,name,page.getStartNum(),page.getEndNum());
		page.setDataList(list);
		return page;
	}

	@Override
	public List<PayLog> getLogList(String dataId) {
		return logDao.withdrawLogList(dataId,3);

	}

	@Override
	public PayRechargePlan getPlan(Integer planId) {
		return rechargePlanDao.getPlanAll(planId);
	}


	private void saveLog(PayRechargePlan plan,Integer operateType){
		try{
			Long uid = null;
			if(plan.getUpdateby() != null){
				uid =  Long.valueOf(plan.getUpdateby());
			}
			String name = plan.getUpdaterName();
			if(operateType == 3){
				if(plan.getCreateby() != null){
					uid =  Long.valueOf(plan.getCreateby());
				}
				name = plan.getCreaterName();
			}
			PayLog payLog = new PayLog();
			payLog.setStatus(0);
			payLog.setUserId(uid);
			payLog.setUserName(name);
			payLog.setDataId(plan.getId()+"");
			payLog.setDataType(3);
			payLog.setOperateType(operateType);
			logDao.save(payLog);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	public static void main(String [] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:application-service.xml" });
		RechargePlanServiceImpl rechargePlanService = context.getBean(RechargePlanServiceImpl.class);
		/*PageDto<PayRechargePlan> page =  rechargePlanService.getRechargePlanPage(null,null,"套",1,20);
		for(PayRechargePlan plan : page.getDataList()){
			System.out.println(plan.getName());
		}*/
		PayRechargePlan plan = new PayRechargePlan();
		plan.setId(16);
		plan.setStatus(0);
		plan.setName("2222222");
		plan.setCashFee(10.00);
		plan.setApplyType(1);
		plan.setDescription("test");
		plan.setImg("aaaaa");
		plan.setLeafQuantity(100);
		Boolean flag  =  rechargePlanService.updateRechargePlan(plan);
		if(flag){
			System.out.print("aaaaaaaaa");
		}


	}

}
