package com.kaolafm.payment.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gongzf
 * @date 2016/3/17
 */
public class PayAccountBill extends BaseBean {

    private static final long serialVersionUID = -8909957695347556103L;
    private long id;
    private long uid;
    private String refId;
    private String description;
    private Integer leafType;
    private Integer billsType;
    private Integer leafQuantity;
    private Double cashQuantity;
    private Integer greenLeafBalanceQuantity =0;
    private Integer goldLeafBalanceQuantity =0;
    private Integer rechargeType;
    private Date createDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeafType() {
        return leafType;
    }

    public void setLeafType(Integer leafType) {
        this.leafType = leafType;
    }

    public Integer getBillsType() {
        return billsType;
    }

    public void setBillsType(Integer billsType) {
        this.billsType = billsType;
    }

    public Integer getLeafQuantity() {
        return leafQuantity;
    }

    public void setLeafQuantity(Integer leafQuantity) {
        this.leafQuantity = leafQuantity;
    }

    public Double getCashQuantity() {
        return cashQuantity;
    }

    public void setCashQuantity(Double cashQuantity) {
        this.cashQuantity = cashQuantity;
    }

    public Integer getGreenLeafBalanceQuantity() {
        return greenLeafBalanceQuantity;
    }

    public void setGreenLeafBalanceQuantity(Integer greenLeafBalanceQuantity) {
        this.greenLeafBalanceQuantity = greenLeafBalanceQuantity;
    }

    public Integer getGoldLeafBalanceQuantity() {
        return goldLeafBalanceQuantity;
    }

    public void setGoldLeafBalanceQuantity(Integer goldLeafBalanceQuantity) {
        this.goldLeafBalanceQuantity = goldLeafBalanceQuantity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }
}
