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
    USER_NOT_LOGIN(50327, "用户未登录"),
    SIGN_ERROR(50504, "签名错误"),
    REQUEST_EXPIRE(50506, "请求无效 时间已过期"),
    USER_TOKEN_UNVALID(50313, "该设备已在其他地方登陆"),
    USER_NOT_EXIST(50322,"用户不存在"),
    USER_APPLY_TIMES_LIMIT(50443, "今日提现次数达到上限"),
    USER_WITHDRAWAL_CASH_LIMIT(50444, "提现金额达到上限"),
    USER_CASH_NOT_ENOUGH(50445, "账户余额不足"),
    GIFT_NULL(50446, "礼物不存在"),
    REWARD_NUM(50448, "错误的打赏次数"),
    LIVE_PROGRAM_NULL(50449, "直播不存在"),
    ANCHOR_LIVE_NOT(50450, "主播不在直播中"),
    LIVE_REWARD_FAIL(50451, "打赏失败"),
    FIRST_RECHARGE_GIFT_REPEAT_USE(50452, "新人礼包已被使用"),
    LEAFCOUNT_NOT_TEN_MULTIPLE(50453, "提现叶子数需为10的整数倍"),
    ORDER_NOT_EXIST(50454, "订单不存在"),
    ANCHOR_LIVE_UID(50455, "主播不能为自己打赏"),
    ANCHOR_NOT_VIP(50456, "主播不是V主播，不能打赏"),
    ANCHOR_IS_FREEZE(50457, "主播账号被冻结"),
    UID_IS_FREEZE(50458, "您的账号被冻结"),
    WITHDRAWAL_MONEY_ERROR(50814, "提现金额与限定金额范围不符"),
    PLAN_NULL(50459, "套餐不存在");

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
