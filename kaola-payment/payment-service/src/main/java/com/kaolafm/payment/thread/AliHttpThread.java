package com.kaolafm.payment.thread;

import com.kaolafm.payment.constants.AlipayConstant;
import com.kaolafm.payment.entity.PayBatchWithdraw;
import com.kaolafm.payment.http.HttpProtocolHandler;
import com.kaolafm.payment.http.HttpRequest;
import com.kaolafm.payment.http.HttpResponse;
import com.kaolafm.payment.http.HttpResultType;
import com.kaolafm.payment.aliutils.AlipayCore;
import com.kaolafm.payment.aliutils.AlipayUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gongzf
 * @date 2016/4/21
 */
public class AliHttpThread implements Runnable {

    private String batchId;

    private List<PayBatchWithdraw> withdrawList;

    private Logger logger = LoggerFactory.getLogger(AliHttpThread.class);

    private SimpleDateFormat df = new SimpleDateFormat("YYYYMMDD");

    public AliHttpThread (String batchId,List<PayBatchWithdraw> withdrawList){
        this.withdrawList=withdrawList;
        this.batchId = batchId;
    }
    @Override
    public void run() {
        logger.info("batchId" + batchId);
        try{
            StringBuffer allData = new StringBuffer();
            Integer batchNum = 0;
            Double  batchFee = 0.00;
            for(PayBatchWithdraw withdraw : withdrawList){
                StringBuffer data = new StringBuffer();
                data.append(withdraw.getWithdrawId())
                        .append("^")
                        .append(withdraw.getAlipayAccount())
                        .append("^")
                        .append(withdraw.getAlipayName())
                        .append("^")
                        .append(withdraw.getCashQuantity())
                        .append("^")
                        .append("考拉FM提现"+withdraw.getCashQuantity());

                allData.append(data).append("|");
                batchNum++;
                batchFee +=withdraw.getCashQuantity();
            }
            String detailData = allData.substring(0,allData.length()-1);
            String payDate = df.format(new Date());
            logger.info("batchDetailInfo:"+detailData+ " payDate:"+payDate + " batchNum:" + batchNum + " batchFee:"+batchFee );
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("service", AlipayConstant.withdraw_service);
            map.put("partner", AlipayConstant.partener);
            map.put("_input_charset", AlipayConstant.input_charset);
            map.put("notify_url", AlipayConstant.withdraw_notify_url);
            map.put("account_name", AlipayConstant.seller_account);
            map.put("detail_data", detailData );
            map.put("batch_no", batchId );
            map.put("batch_num", batchNum+"");
            map.put("batch_fee",  batchFee+"");
            map.put("Email", AlipayConstant.seller_id);
            map.put("pay_date", payDate);
            String content = AlipayCore.createString(map);
            logger.info("content" + content + " batchId:" + batchId);
            String signContent = AlipayUtils.sign(content, AlipayConstant.PRIVATE_KEY);
                   signContent = URLEncoder.encode(signContent, "UTF-8");
            map.put("sign", signContent);
            map.put("sign_type", AlipayConstant.sign_type);
            //http发送post请求支付宝
            //获取请求实例
            HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
            //请求流字符串
            HttpRequest request = new HttpRequest(HttpResultType.STRING);
            //设置编码集
            request.setCharset(AlipayConstant.input_charset);
            //设置参数请求体
            request.setParameters(AlipayUtils.generateNameValuePair(map));
            //设置请求url
            request.setUrl(AlipayUtils.ALIPAY_GATEWAY_NEW);
            HttpResponse response = httpProtocolHandler.execute(request,"","");
            if (response == null) {
                logger.info("AlihttpError  "+batchId);
            }
            String strResult = response.getStringResult();
            System.out.println(strResult);
        }catch (Exception e){
            logger.info(batchId+"  AliHttpThreadError",e);
        }



    }

}
