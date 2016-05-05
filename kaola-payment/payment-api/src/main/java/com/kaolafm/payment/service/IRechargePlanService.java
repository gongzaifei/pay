package com.kaolafm.payment.service;

import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.entity.PayLog;
import com.kaolafm.payment.entity.PayRechargePlan;

import java.util.List;

/**
 * @author gongzf
 * @date 2016/3/22
 */
public interface IRechargePlanService {
    /**
     * 设备类型获取充值套餐列表
     * @param deviceType
     * @return
     */
    public List<PayRechargePlan> getRechargePlanList(Integer deviceType);

    /**
     * 保存充值套餐
     * @param rechargePlan
     * @return
     */
    public boolean saveRechargePlan(PayRechargePlan rechargePlan);

    /**
     * 修改套餐列表
     * @param rechargePlan
     * @return
     */
    public boolean updateRechargePlan(PayRechargePlan rechargePlan);

    /**
     * 批量修改套餐列表状态
     * @param planIds
     * @param status
     * @return
     */
    public boolean batchUpdatePlanStatus(List<Long> planIds, Integer status,String userName,Long uid);

    /**
     * 套餐列表
     * @param deviceType
     * @param status
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageDto<PayRechargePlan> getRechargePlanPage(Integer deviceType,Integer status,String name,Integer pageNum,Integer pageSize);



    /**
     * 套餐操作日志
     * @param dataId
     * @return
     */
    public List<PayLog> getLogList(String dataId);


    /**
     * 充值套餐详情
     * @param planId
     * @return
     */
    public PayRechargePlan getPlan(Integer planId);

}
