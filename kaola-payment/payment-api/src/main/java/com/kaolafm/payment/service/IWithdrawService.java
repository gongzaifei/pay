package com.kaolafm.payment.service;

import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.dto.WithdrawDto;
import com.kaolafm.payment.entity.PayAccountWithdrawal;
import com.kaolafm.payment.entity.PayLog;
import com.kaolafm.user.exception.UserServiceException;

import java.util.Date;
import java.util.List;

/**
 * @author gongzf
 * @date 2016/3/29
 */
public interface IWithdrawService {

    /**
     *
     * @param pagenum 页码
     * @param pagesize 每页大小
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param nickName 用户昵称
     * @param aliAccount 支付宝账号
     * @param status 查询状态 (0:待审核 1:审核成功 2:审核失败 3:已转账 4:转账失败 5:删除)
     * @return
     */
    public PageDto<WithdrawDto> getWithdrawalRecordList(Integer pagenum, Integer pagesize, Date startDate, Date endDate, String nickName, String aliAccount, Integer status);

    /**
     * 批量审核
     * @param ids  id集合
     * @param status  1：审核成功 待转账  2:审核失败
     * @param reason  原因
     * @return
     */
    public Boolean batchAudit(List<String> ids,Integer status,String reason,Long uid,String userName) throws UserServiceException;

    /**
     * 获取提现详情
     * @param id
     * @return
     */
    public PayAccountWithdrawal getWithdrawDetail(String id);

    /**
     * 某条提现的操作记录
     * @param dataId
     * @return
     */
    public List<PayLog> withdrawLogList(String  dataId);

    /**
     * 导出全部提现申请
     * @return
     */
    public List<WithdrawDto> getAllWithdrawList();

    /**
     * 保存导出日志
     * @param userName
     * @param uid
     * @param filePath
     * @return
     */
    public Boolean saveExportLog(String userName,Long uid,String filePath);

    /**
     *
     * @param uid 操作人Id
     * @param id  提现记录Id
     * @param reason 失败原因
     * @param userName 操作人昵称
     * @return
     */
    public Boolean doTransferFailure(Long uid,String id,String reason,String userName);


    /**
     * 导出日志
     * @param pagenum
     * @param pagesize
     * @return
     */
    public PageDto<PayLog> getExportLog(Integer pagenum, Integer pagesize);


    public Boolean batchWithdraw(List<String> withdrawIds,Long uid,String userName);



}
