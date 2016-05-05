package com.kaolafm.payment.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gongzf
 * @date 2016/3/17
 */
public class PayUserAccount  extends BaseBean{

    private static final long serialVersionUID = -8909957695347556104L;

    private long uid;
    private Integer greenLeaf;
    private Integer goldLeaf;
    private Date createDate;
    private Date updateDate;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


    public Integer getGreenLeaf() {
        return greenLeaf;
    }

    public void setGreenLeaf(Integer greenLeaf) {
        this.greenLeaf = greenLeaf;
    }

    public Integer getGoldLeaf() {
        return goldLeaf;
    }

    public void setGoldLeaf(Integer goldLeaf) {
        this.goldLeaf = goldLeaf;
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
}
