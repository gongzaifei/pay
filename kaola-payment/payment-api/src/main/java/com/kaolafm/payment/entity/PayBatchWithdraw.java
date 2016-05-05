package com.kaolafm.payment.entity;

import java.util.Date;

/**
 * @author gongzf
 * @date 2016/4/20
 */
public class PayBatchWithdraw extends BaseBean {

    private static final long serialVersionUID = -8909957695347556113L;

    private Integer id;

    private String batchNo;

    private String withdrawId;

    private Integer status;

    private Date notifyTime;

    private String notifyType;

    private String notifyId;

    private Date createDate;

    private String alipayAccount;

    private String alipayName;

    private Double cashQuantity;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }

    public Double getCashQuantity() {
        return cashQuantity;
    }

    public void setCashQuantity(Double cashQuantity) {
        this.cashQuantity = cashQuantity;
    }
}
