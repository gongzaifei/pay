package com.kaolafm.payment.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * id生成
 */
public class IdBuildUtils {

    private static SimpleDateFormat day = new SimpleDateFormat("yyMMddHHmmss");
    // 充值订单号
    public static String buildFillId(String uid) {
        return buildId("00"+uid);
    }
    //账户流水Id
    public static String buildAccountBillsId(String uid){
        return buildId("01"+uid);
    }

	// 充值订单号前缀
	public static String buildFillIdPrefix(String uid) {
		return "00" + uid.substring(uid.length() - 4, uid.length());
	}
    
	// 提现申请订单号前缀
	public static String buildWithdrawlPrefix(String uid) {
		return "02" + uid.substring(uid.length() - 4, uid.length());
	}

    public static String buildBatchWithdrawId(){
        return buildId("10");
    }

    public static String buildId(String begin) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(day.format(new Date()));
        StringBuffer orderId = new StringBuffer();
        orderId.append(begin);
        orderId.append(stringBuffer);
        orderId.append(random(4));
        return orderId.toString();
    }
    

    public static String random(int num) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < num; i++) {
            buffer.append(intRandom());
        }
        return buffer.toString();
    }

    /**
     * 生成0-9的随机数
     *
     * @return
     */
    public static int intRandom() {
        Random random = new Random();
        return random.nextInt(9);
    }


    public static void main(String [] args){
        System.out.println(buildBatchWithdrawId());
    }



}
