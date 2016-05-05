package com.kaolafm.payment.dto;

/**
 * @author gongzf
 * @date 2016/3/15
 */
public class FillOrderDto extends BaseDto{

    private static final long serialVersionUID = 4639764730387530111L;
    //充值单号
    private String orderId;
    //套餐ID
    private Integer planId;
    //支付类型  0:支付宝 1:微信 2:ios支付
    private Integer type;
    //用户id
    private Long uid;
    //充值金额
    private Double fee;
    //充值状态 0:充值中
    private Integer status;

    private String userName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
