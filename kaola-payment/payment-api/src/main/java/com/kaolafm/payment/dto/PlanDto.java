package com.kaolafm.payment.dto;

/**
 * @author gongzf
 * @date 2016/3/25
 */
public class PlanDto extends BaseDto{

    private Integer id;

    private Double fee;

    private Integer leaf;

    private Integer presentnum;

    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public Integer getPresentnum() {
        return presentnum;
    }

    public void setPresentnum(Integer presentnum) {
        this.presentnum = presentnum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
