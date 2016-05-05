package com.kaolafm.payment.constants;

/**
 * @author gongzf
 * @date 2016/3/15
 */
public class AlipayConstant {

    //支付宝公钥
    public static final String PUBLIC_KEY="支付宝公钥";

    //应用私钥
    public static final  String PRIVATE_KEY="应用私钥";

    //接口名称
    public static final  String SERVICE="\"mobile.securitypay.pay\"";

    //合作者身份ID
    public static final  String PARTERNER="\"支付宝合作者id\"";
    //参数编码字符集
    public static final  String _INPUT_CHARSET="\"utf-8\"";
    //签名方式
    public static final  String SIGN_TYPE="\"RSA\"";
    //服务器异步通知url
    public static final  String NOTIFY_URL="\"notifyUrl\"";
    //商品名称
    public static final  String SUBJECT="\"考拉虚拟商品\"";
    //支付类型 默认为1 商品购买
    public static final  String PAYMENT_TYPE="\"1\"";
    //卖家支付宝账号
    public static final  String SELLER_ID="\"支付宝账号\"";

    //商品详情
    public static final  String BODY="\"考拉虚拟商品\"";
    //是否发起实名校验 T 校验  F 不校验
    public static final  String RN_CHECK="T";

    public static final  String IT_B_PAY="\"30m\"";

    //支付成功状态
    public static final String TRADE_STATUS_SUCCESS="TRADE_SUCCESS";
    //支付完成状态
    public static final String TRADE_STATUS_FINISHED="TRADE_FINISHED";
    //指定时间段内未支付关闭的交易
    public static final String TRADE_STATUS_CLOSED = "TRADE_CLOSED";


    public static final  String input_charset="utf-8";

    //批量转账接口名称
    public static final String withdraw_service = "batch_trans_notify";

    public static final  String partener ="2088021786280266";

    //批量转账通知URL
    public static final  String withdraw_notify_url ="http://repay.kaolafm.com/pay/alipay/withdraw/notify";

    //付款方的账户名
    public static final  String seller_account="网乐互联（北京）科技有限公司";

    //卖家支付宝账号
    public static final  String seller_id="gaol@autoradio.cn";

    //签名方式
    public static final  String sign_type="RSA";




}
