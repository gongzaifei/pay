package com.kaolafm.payment.entity;


import java.util.Date;

/**
 * @author gongzf
 * @date 2016/3/24
 */
public class PayIosResult extends BaseBean {

    private static final long serialVersionUID = -8909957695347556111L;

    private int id;

    private int status;

    private String receipt;

    private String productId;

    private int quantity;

    private String outTradeNo;

    private double totalFee;

    private Date  createDate;

    private Date updateDate;

    private String receiptMd5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getReceiptMd5() {
        return receiptMd5;
    }

    public void setReceiptMd5(String receiptMd5) {
        this.receiptMd5 = receiptMd5;
    }
}
