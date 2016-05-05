package com.kaolafm.payment.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kaolafm.payment.entity.PayLiveRewardRecord;

public interface LiveRewardRecordDao {

	public Long getAnchorLiveRewardSumLeaf(@Param("programid") Long liveProgramId,@Param("anchorid") Long anchorid);

	public Long getUserLiveRewardRecordCount(@Param("programid") Long liveProgramId,@Param("anchorid") Long anchorid);

	public List<PayLiveRewardRecord> getUserLiveRewardList(@Param("programid") Long programid,@Param("anchorid") Long anchorid,
			@Param("limitStart") Integer limitStart, @Param("limitEnd") Integer limitEnd);

	public PayLiveRewardRecord getLiveReward(Map<String, Object> map);

	public Integer save(PayLiveRewardRecord liveRewardRecord);

	public Integer update(PayLiveRewardRecord liveRewardRecord);

}
