package com.kaolafm.payment.exception;

import com.alibaba.dubbo.rpc.RpcException;

import java.util.concurrent.TimeoutException;

/**
 * 服务异常
 * 
 * @author zhenbo.li
 *
 */

public class ServiceException extends Exception{
    
    private static final long serialVersionUID = -1695036681341844113L;
    
    private ExceptionEnums exceptionEnums;
    
    private String addMsg;

    
    public ExceptionEnums getExceptionEnums() {
        return exceptionEnums;
    }

    public String getAddMsg() {
        return addMsg;
    }

    public void setAddMsg(String addMsg) {
        this.addMsg = addMsg;
    }

    public void setExceptionEnums(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }

    public ServiceException(ExceptionEnums exceptionEnums, String addMsg) {
        this.exceptionEnums = exceptionEnums;
        this.addMsg = addMsg;
    }

    public ServiceException(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }


    public static ServiceException create(ExceptionEnums exceptionEnums) {
        return new ServiceException(exceptionEnums);
    }


    public static ServiceException create(ExceptionEnums exceptionEnums, String addMsg) {
        return new ServiceException(exceptionEnums, addMsg);
    }


    

    
}
