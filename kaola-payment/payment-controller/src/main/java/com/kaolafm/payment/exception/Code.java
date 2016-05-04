package com.kaolafm.payment.exception;

/**
 * 505xx 通用异常状态码
 * <p>
 * 50311 - 50399 业务类异常状态码
 *
 * @author Zhenbo Li
 */
public enum Code implements ExceptionEnums {

    SUCCESS(10000, "success"),
    BAD_REQUEST(50501, "参数错误"),
    SERVER_ERROR(50502, "服务器异常"),
    USER_NOT_LOGIN(50327, "用户未登录");

    public int code;

    public String message;

    private Code(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
