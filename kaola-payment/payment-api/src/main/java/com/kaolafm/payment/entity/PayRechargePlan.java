package com.kaolafm.payment.entity;

import java.util.Date;

/**
 * @author gongzf
 * @date 2016/3/16
 */
public class PayRechargePlan extends BaseBean {
    private static final long serialVersionUID = -8909957695347556102L;
    private int id;
    private String name;
    private String description;
    private String img;
    private Double cashFee;
    private Integer leafQuantity;
    private Integer applyType;
    private Integer planLimitType;
    private Integer status;
    private Integer createby;
    private String createrName;
    private Date createDate;
    private Integer updateby;
    private String updaterName;
    private Date updateDate;
    private String productId;
    private Integer presentLeafQuantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Double getCashFee() {
        return cashFee;
    }

    public void setCashFee(Double cashFee) {
        this.cashFee = cashFee;
    }

    public Integer getLeafQuantity() {
        return leafQuantity;
    }

    public void setLeafQuantity(Integer leafQuantity) {
        this.leafQuantity = leafQuantity;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public Integer getPlanLimitType() {
        return planLimitType;
    }

    public void setPlanLimitType(Integer planLimitType) {
        this.planLimitType = planLimitType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Integer updateby) {
        this.updateby = updateby;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getPresentLeafQuantity() {
        return presentLeafQuantity;
    }

    public void setPresentLeafQuantity(Integer presentLeafQuantity) {
        this.presentLeafQuantity = presentLeafQuantity;
    }
}
