package com.kaolafm.payment.impl;

import com.kaolafm.cache.dto.LivePlayProgrameDto;
import com.kaolafm.payment.dao.*;
import com.kaolafm.payment.entity.*;
import com.kaolafm.payment.service.IAnchorService;
import com.kaolafm.user.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gongzf
 * @date 2016/3/25
 */
@Service
public class AnchorServiceImpl  implements IAnchorService{

    private Logger logger = LoggerFactory.getLogger(AnchorServiceImpl.class);

    @Autowired
    private UserAccountDao accountDao;

    @Autowired
    private UserGiftDao userGiftDao;

    @Autowired
    private AccountBillDao accountBillDao;

    @Autowired
    private LiveRewardRecordDao liveRewardRecordDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Boolean doAnchorAccount(UserInfo anchorInfo, PayGift gift, Integer num, LivePlayProgrameDto liveDto,UserInfo userInfo) {
         try{
             Double goldLeaf = Double.valueOf(gift.getGiftWorth()*num);
             logger.info("anchorId:" + anchorInfo.getUid() + " uid:"+userInfo.getUid() + " goldLeaf:"+goldLeaf);
             PayUserAccount userAccount  =  accountDao.selectLock(anchorInfo.getUid());
             Integer record = 0;
             if(userAccount == null){
                 userAccount = new PayUserAccount();
                 userAccount.setUid(anchorInfo.getUid());
                 userAccount.setGoldLeaf(goldLeaf.intValue());
                 userAccount.setGreenLeaf(0);
                 record = accountDao.save(userAccount);
                 if(record !=1){
                     throw new RuntimeException("主播账户写入失败");
                 }
             }else{
                 userAccount.setGoldLeaf(userAccount.getGoldLeaf()+goldLeaf.intValue());
                 record = accountDao.update(userAccount);
                 if(record !=1){
                     throw new RuntimeException("主播账户更新失败");
                 }
             }
             PayUserGift userGift = new PayUserGift();
             userGift.setGiftExchangeRate(gift.getExchangeRate());
             userGift.setGiftId(gift.getId());
             userGift.setGiftImg(gift.getImg());
             userGift.setGiftWorth(gift.getGiftWorth());
             userGift.setQuantity(num);
             userGift.setReceiverUid(anchorInfo.getUid());
             userGift.setSenderUid(userInfo.getUid());
             userGift.setSender(userInfo.getNickName());
             userGift.setReceiver(anchorInfo.getNickName());
             userGift.setGiftName(gift.getName());
             record = userGiftDao.save(userGift);
             if(record !=1){
                 throw new RuntimeException("主播礼物写入失败");
             }
             PayAccountBill accountBill = new PayAccountBill();
             accountBill.setUid(anchorInfo.getUid());
             accountBill.setRefId(gift.getId()+"");
             accountBill.setDescription("在直播"+liveDto.getName()+"中收到听众"+userInfo.getUid()+"的礼物"+gift.getName());
             accountBill.setLeafQuantity(goldLeaf.intValue());
             accountBill.setCashQuantity(0.0); //暂时设为0
             accountBill.setBillsType(2);
             accountBill.setGoldLeafBalanceQuantity(userAccount.getGoldLeaf());
             accountBill.setLeafType(1);
             record = accountBillDao.save(accountBill);
             if(record != 1){
                 throw new RuntimeException("主播账户流水写入失败");
             }
             Map<String,Object> map = new HashMap<String,Object>();
             map.put("senderId",userInfo.getUid());
             map.put("liveProgramId",liveDto.getId());
             PayLiveRewardRecord liveRewardRecord =  liveRewardRecordDao.getLiveReward(map);
             if(liveRewardRecord == null){
                 liveRewardRecord = new PayLiveRewardRecord();
                 liveRewardRecord.setLeafAmount(gift.getGiftWorth()*num);
                 liveRewardRecord.setLiveId(liveDto.getLiveId());
                 liveRewardRecord.setLiveProgramId(liveDto.getId());
                 liveRewardRecord.setSenderId(userInfo.getUid());
                 liveRewardRecord.setAnchorId(anchorInfo.getUid());
                 record = liveRewardRecordDao.save(liveRewardRecord);
                 if(record != 1){
                     throw new RuntimeException("直播榜单写入失败");
                 }
             }else{
                 liveRewardRecord.setLeafAmount(liveRewardRecord.getLeafAmount()+gift.getGiftWorth()*num);
                 record = liveRewardRecordDao.update(liveRewardRecord);
                 if(record != 1){
                     throw new RuntimeException("直播榜单更新失败");
                 }
             }
             return true;

         }catch (Exception e){
             logger.info("doAnchorAccountError",e);
             throw new RuntimeException(e.getMessage());
         }
    }
}
