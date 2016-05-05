package com.kaolafm.payment.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4639764730387530956L;

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
