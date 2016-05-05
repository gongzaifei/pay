package com.kaolafm.payment.service;

import com.kaolafm.cache.dto.LivePlayProgrameDto;
import com.kaolafm.payment.entity.PayGift;
import com.kaolafm.user.entity.UserInfo;

/**
 * @author gongzf
 * @date 2016/3/25
 */
public interface IAnchorService {
    /**
     * 主播打赏 主播账号操作以及系列流水写入
     * @param anchorInfo
     * @param gift
     * @param num
     * @param liveDto
     * @param userInfo
     * @return
     */
    public Boolean doAnchorAccount(UserInfo anchorInfo, PayGift gift, Integer num, LivePlayProgrameDto liveDto,UserInfo userInfo);
}
