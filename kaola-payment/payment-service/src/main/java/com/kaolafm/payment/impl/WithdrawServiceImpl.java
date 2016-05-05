package com.kaolafm.payment.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.dao.AccountWithdrawalDao;
import com.kaolafm.payment.dao.BatchWithdrawDao;
import com.kaolafm.payment.dao.UserAccountDao;
import com.kaolafm.payment.dao.WithdrawLogDao;
import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.dto.WithdrawDto;
import com.kaolafm.payment.entity.PayAccountWithdrawal;
import com.kaolafm.payment.entity.PayBatchWithdraw;
import com.kaolafm.payment.entity.PayUserAccount;
import com.kaolafm.payment.entity.PayLog;
import com.kaolafm.payment.service.IUserCenterService;
import com.kaolafm.payment.service.IWithdrawService;
import com.kaolafm.payment.thread.AliHttpThread;
import com.kaolafm.user.dto.SystemLetterDto;
import com.kaolafm.user.entity.UserInfo;
import com.kaolafm.user.exception.UserServiceException;
import com.kaolafm.user.service.SystemLetterService;
import com.kaolafm.user.service.UserService;
import com.kaolafm.user.service.VerifyCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author gongzf
 * @date 2016/3/29
 */
@Service
public class WithdrawServiceImpl implements IWithdrawService {
    private Logger logger = LoggerFactory.getLogger(UserCenterServiceImpl.class);

    @Autowired
    private AccountWithdrawalDao accountWithdrawalDao;

    @Autowired
    private WithdrawLogDao withdrawLogDao;

    @Autowired
    private UserAccountDao userAccountDao;

    @Autowired
    private BatchWithdrawDao batchWithdrawDao;

    @Autowired
    private IUserCenterService userCenterService;

    @Autowired
    private SystemLetterService rpcSystemLetterService;

    @Autowired
    private VerifyCodeService rpcVerifyCodeService;

    @Autowired
    private UserService rpcUserService;

    @Override
    public PageDto<WithdrawDto> getWithdrawalRecordList(Integer pagenum, Integer pagesize, Date startDate, Date endDate, String nickName, String aliAccount, Integer status) {
        try{
            if(pagenum == null || pagenum <= 0){
                pagenum = 1;
            }
            if(pagesize <=0 || pagesize >=100 ){
                pagesize = 100;
            }
            //产品要求 默认查询全部
            /*if(startDate == null && endDate == null){
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-1);
                startDate = calendar.getTime();
            }*/
            Integer count = accountWithdrawalDao.getWithdrawalListCount(status,nickName,aliAccount,startDate,endDate);
            PageDto pageDto = new PageDto<WithdrawDto>(count,pagenum,pagesize);
            List<PayAccountWithdrawal> list =  accountWithdrawalDao.getWithdrawalList(pageDto.getStartNum(),pageDto.getEndNum(),status,nickName,aliAccount,startDate,endDate);
            List<WithdrawDto> reList =  convertWithdraw(list);
            pageDto.setDataList(reList);
            return pageDto;
        }catch (Exception e){
            logger.info("getWithdrawalRecordListError",e);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Boolean batchAudit(List<String> ids, Integer status, String reason,Long uid,String userName) throws UserServiceException {
        List<PayAccountWithdrawal> withDrawlist =  accountWithdrawalDao.getWithdrawByIds(ids);
           if(status == 2 || status == 4){
               if(withDrawlist == null || withDrawlist.isEmpty()){
                   return false;
               }
               Boolean flag =  userCenterService.withdrawFail(withDrawlist);
               if(!flag){
                   throw new RuntimeException();
               }
               for(PayAccountWithdrawal account : withDrawlist){
                   SystemLetterDto systemLetterDto = new SystemLetterDto();
                   systemLetterDto.setToUid(account.getUid());
                   systemLetterDto.setContent("抱歉，您的提现请求未通过审核，原因如下:"+reason+" 感谢您的理解。如有疑问 请邮件联系kaolayy@kaolafm.com");
                   rpcSystemLetterService.sendSystemLetter(systemLetterDto);
               }

           }else{
               for(PayAccountWithdrawal account : withDrawlist){
                   SystemLetterDto systemLetterDto = new SystemLetterDto();
                   systemLetterDto.setToUid(account.getUid());
                   systemLetterDto.setContent("您好，您的提现请求审核通过审核，即可为您提交支付宝，感谢您的期待！");
                   if(status == 3){
                       systemLetterDto.setContent("您好，您的提现申请已通过审核并已提现到账，感谢您的支持！");
                       UserInfo userInfo =  rpcUserService.getUserInfoByUid(uid);
                       if(userInfo != null){
                           logger.info("userId"+uid + " mobile"+userInfo.getMobile());
                           rpcVerifyCodeService.sendSMS(userInfo.getMobile(), "您的提现申请已通过审核并转账，请查收。感谢您对于考拉FM的支持！");
                       }
                   }
                   rpcSystemLetterService.sendSystemLetter(systemLetterDto);
               }
           }
           Integer record =  accountWithdrawalDao.batchUpdate(ids,reason,status);
           List<PayLog> list = new ArrayList<PayLog>();
           for(String id : ids){
               PayLog log = new PayLog();
               log.setDataId(id);
               log.setDataType(0);
               log.setOperateType(status);
               log.setUserId(uid);
               log.setReason(reason);
               log.setOperateDate(new Date());
               log.setStatus(status);
               log.setUserName(userName);
               list.add(log);
           }
           withdrawLogDao.batchInsert(list);

        return true;
    }

    @Override
    public PayAccountWithdrawal getWithdrawDetail(String id) {

        return accountWithdrawalDao.getWithdrawDetail(id);
    }

    @Override
    public List<PayLog> withdrawLogList(String dataId) {

        return withdrawLogDao.withdrawLogList(dataId,0);
    }

    @Override
    public List<WithdrawDto> getAllWithdrawList() {
        List<PayAccountWithdrawal> list = accountWithdrawalDao.getAllWithdraw();
        return convertWithdraw(list);
    }

    @Override
    public Boolean saveExportLog(String userName, Long uid, String filePath) {
        PayLog log = new PayLog();
        log.setOperateDate(new Date());
        log.setUserName(userName);
        log.setUserId(uid);
        log.setOperateType(2);
        log.setStatus(0);
        log.setDataId("");
        log.setDataType(1);
        log.setReason("");
        log.setFilePath(filePath);
        Integer record =  withdrawLogDao.save(log);
        if(record == 1){
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Boolean doTransferFailure(Long uid, String id, String reason, String userName) {
        PayAccountWithdrawal pay =  accountWithdrawalDao.getWithdrawDetail(id);
        if(pay == null){
            logger.info("withdrawIsNull"+id + " operateUid"+uid);
            throw new RuntimeException();
        }
        PayUserAccount userAccount =  userAccountDao.selectLock(pay.getUid());

        if(pay.getStatus() == 3 || pay.getStatus() == 4 || pay.getStatus() == 5){
            logger.info("withdrawStatusIsError"+id + "operateUid"+uid + " uid" + pay.getUid());
            throw new RuntimeException();
        }
        pay.setStatus(4);
        pay.setReason(reason);
        Integer record =  accountWithdrawalDao.update(pay);
        if(record != 1){
            logger.info("doUpdateWithdrawError" + id + " operateUid" + uid);
            throw  new RuntimeException();
        }
        PayLog log = new PayLog();
        log.setReason(reason);
        log.setStatus(1);
        log.setDataType(0);
        log.setOperateType(4);
        log.setUserId(uid);
        log.setUserName(userName);
        log.setDataId(id);
        log.setOperateDate(new Date());
        record = withdrawLogDao.save(log);
        if(record != 1){
            logger.info("doSaveLogError" + id + " operateUid" + uid);
            throw  new RuntimeException();
        }
        userAccount.setGoldLeaf(userAccount.getGoldLeaf()+pay.getLeafQuantity());
        record = userAccountDao.update(userAccount);
        if(record !=1){
            logger.info("doUpdateAccountError" + id + " uid" + pay.getUid() + " operateUid"+uid);
            throw  new RuntimeException();
        }
        SystemLetterDto systemLetterDto = new SystemLetterDto();
        systemLetterDto.setToUid(pay.getUid());
        systemLetterDto.setContent("抱歉，您的转账请求失败，原因如下:"+reason+" 感谢您的理解。如有疑问 请邮件联系kaolayy@kaolafm.com");
        try {
            rpcSystemLetterService.sendSystemLetter(systemLetterDto);
        } catch (UserServiceException e) {
            logger.info("doTransferFailureError",e + " uid"+pay.getUid() + "withdrawId" + id);
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public PageDto<PayLog> getExportLog(Integer pagenum, Integer pagesize) {
        try{
            if(pagenum == null || pagenum <= 0){
                pagenum = 1;
            }
            if(pagesize <=0 || pagesize >=30 ){
                pagesize = 20;
            }
            Integer count = withdrawLogDao.getWithdrawLogCount();
            PageDto pageDto = new PageDto<WithdrawDto>(count,pagenum,pagesize);
            List<PayLog> list = withdrawLogDao.getWithdrawLogList(pageDto.getStartNum(),pageDto.getEndNum());
            pageDto.setDataList(list);
            return pageDto;
        }catch (Exception e){
            logger.info("getExportLogError",e);
        }
        return null;
    }

    @Override
    public Boolean batchWithdraw(List<String> withdrawIds,Long uid,String userName) {
        Boolean flag = true;
        String batchId = null;
        try{
            batchId = userCenterService.batchTranMoney(withdrawIds,uid,userName);
        }catch (Exception e){
            logger.info("batchWithdrawError",e);
            flag = false;
        }
        if(flag){
            List<PayBatchWithdraw> list =  batchWithdrawDao.getWithdrawInfo(batchId);
            AliHttpThread thread = new AliHttpThread(batchId,list);
            thread.run();
        }
        return flag;
    }


    private List<WithdrawDto> convertWithdraw(List<PayAccountWithdrawal> list){
        List<WithdrawDto> reList = new ArrayList<WithdrawDto>();
        for(PayAccountWithdrawal pay : list){
            WithdrawDto dto = new WithdrawDto();
            dto.setId(pay.getId());
            dto.setAliAccount(pay.getAlipayAccount());
            dto.setApplyTime(pay.getCreateDate());
            dto.setNickName(pay.getCreaterName());
            dto.setWithDrawNum(pay.getCashQuantity());
            dto.setStatus(pay.getStatus());
            //TODO redis
            PayUserAccount  account =  userAccountDao.selectUserAccountByUid(pay.getUid());
            if(account != null && account.getGoldLeaf() !=null){
                Double cash = account.getGoldLeaf()* Constants.goldleafToCashRatio;
                dto.setAccountNum(cash);
            }
            reList.add(dto);
        }
        return reList;
    }

    public static  void main(String [] args) throws UserServiceException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "classpath*:application-service.xml" });
        IWithdrawService withdrawService = context.getBean(WithdrawServiceImpl.class);
        List<String> ids = new ArrayList<String>();
        ids.add("0201931000070");
        withdrawService.batchAudit(ids,1,"审核通过",11111l,"test");
    }
}
