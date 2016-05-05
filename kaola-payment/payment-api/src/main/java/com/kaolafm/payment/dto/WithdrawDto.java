package com.kaolafm.payment.dto;

import java.util.Date;

/**
 * @author gongzf
 * @date 2016/3/29
 */
public class WithdrawDto extends BaseDto {

    private static final long serialVersionUID = 4639764730387530119L;

    private  String id;

    private  String nickName;

    private  Double accountNum = 0.00;

    private  Double withDrawNum;

    private  String aliAccount;

    private  Date   applyTime;

    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Double getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(Double accountNum) {
        this.accountNum = accountNum;
    }

    public Double getWithDrawNum() {
        return withDrawNum;
    }

    public void setWithDrawNum(Double withDrawNum) {
        this.withDrawNum = withDrawNum;
    }

    public String getAliAccount() {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
