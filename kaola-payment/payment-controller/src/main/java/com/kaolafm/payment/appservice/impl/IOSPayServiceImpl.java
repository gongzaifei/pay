package com.kaolafm.payment.appservice.impl;

import java.util.HashMap;
import java.util.Map;

import com.kaolafm.payment.utils.Md5Utils;
import com.kaolafm.user.entity.UserInfo;
import com.kaolafm.user.exception.UserServiceException;
import com.kaolafm.user.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaolafm.counter.api.CounterService;
import com.kaolafm.payment.appservice.IOSPayService;
import com.kaolafm.payment.constants.CounterConstants;
import com.kaolafm.payment.dto.FillOrderDto;
import com.kaolafm.payment.entity.PayIosResult;
import com.kaolafm.payment.entity.PayRechargePlan;
import com.kaolafm.payment.entity.PayTrade;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.request.OrderHandler;
import com.kaolafm.payment.service.IOrderService;
import com.kaolafm.payment.service.IUserCenterService;
import com.kaolafm.payment.service.IosPayService;
import com.kaolafm.payment.utils.IdBuildUtils;
import com.kaolafm.payment.utils.IosPayUtils;

/**
 * @author gongzf
 * @date 2016/3/22
 */
@Service
public class IOSPayServiceImpl implements IOSPayService{

    private Logger logger = LoggerFactory.getLogger(AliServiceImpl.class);

    @Autowired
    private IOrderService rpcOrderService;

    @Autowired
    private IosPayService rpcIosPayService;
    
    @Autowired
    private IUserCenterService rpcUserCenterService;
    @Autowired
	private CounterService rpcCounterService;

    @Autowired
    private UserService rpcUserSerivce;

    @Override
    public Map<String,String> getOrderId(Integer planId, CommonParams commonParam) throws ServiceException, UserServiceException {
        try{
            PayRechargePlan plan  = rpcOrderService.getPlan(planId);
            if(plan == null){
                throw new ServiceException(Code.PLAN_NULL);
            }
            if(plan.getPlanLimitType() == 1){
 				PayTrade trade= rpcUserCenterService.getTradeByUidAndPlanId(Long.valueOf(commonParam.getUid()), planId);
 				if(trade != null){
 					throw new ServiceException(Code.FIRST_RECHARGE_GIFT_REPEAT_USE);
 				}
 			}
            String orderId = null;
			try {
				orderId = IdBuildUtils.buildFillIdPrefix(commonParam.getUid()) + CounterConstants.PAY_MODULE_START_ID
						+ String.valueOf(rpcCounterService.incr(CounterConstants.PAY_MODULE_UNI_KEY, 1));
			} catch (Exception e) {
				logger.info("计数器获取订单号失败");
				orderId = OrderHandler.getKaolaOrderId(commonParam.getUid());
				e.printStackTrace();
			}

			if (orderId == null) {
				throw new ServiceException(Code.SERVER_ERROR);
			}
            UserInfo userInfo =  rpcUserSerivce.getUserInfoByUid(Long.valueOf(commonParam.getUid()));
            if(userInfo == null){
                throw new ServiceException(Code.USER_NOT_EXIST);
            }
            FillOrderDto fillDto = new FillOrderDto();
            fillDto.setUid(userInfo.getUid());
            fillDto.setUserName(userInfo.getNickName());
            fillDto.setFee(plan.getCashFee());
            fillDto.setOrderId(orderId);
            fillDto.setPlanId(planId);
            fillDto.setType(2);
            fillDto.setStatus(0);
            Boolean flag  = rpcIosPayService.doSaveIosPay(fillDto);
            if(flag){
                Map<String,String> map = new HashMap<String,String>();
                map.put("orderid",orderId);
                map.put("productid",plan.getProductId());
                return map;
            }

        }catch (ServiceException e){
            logger.info("alipaySignAndParamsError",e);
            throw e;
        }

        return null;

    }

    @Override
    public boolean verifyIosReceipt(String receipt, String orderId, CommonParams commonParams) {
        String receiptMd5 = Md5Utils.getMD5(receipt.getBytes());
        PayIosResult resultMd5 =  rpcIosPayService.getIosResultByMd5(receiptMd5);

        if(resultMd5 != null){
            logger.info("uid:"+commonParams.getUid() + "receipt:" + receipt);
            return false;
        }
        PayIosResult result =  rpcIosPayService.getIosResult(orderId);
        if(result == null){
            logger.info("uid:"+commonParams.getUid() + "tradeId:" + orderId);
            return false;
        }
        if(result.getStatus()!= 0){
            logger.info("uid:"+commonParams.getUid() + "tradeId:" + orderId);
            return false;
        }
        PayTrade payTrade =  rpcOrderService.getTrade(orderId);

        if(payTrade == null){
            logger.info("uid:"+commonParams.getUid() + "tradeId:" + orderId);
            return false;
        }
        //TODO url 为测试地址 上线审核时改为正式地址
        String url = IosPayUtils.url_sandbox;
        JSONObject json = IosPayUtils.verifyReceipt(url,receipt);
        String status =  json.getString("status");
        //审核时加钱未加上，然后再次访问沙箱地址
        if("21007".equals(status)){
            json = IosPayUtils.verifyReceipt(IosPayUtils.url_sandbox,receipt);
        }
        if(!"0".equals(status)){
            logger.info("verifyStatus:" +status);
            return false;
        }
        JSONObject paramJson = json.getJSONObject("receipt");
        logger.info("paramJson:" + paramJson.toString());

        JSONArray intApp = paramJson.getJSONArray("in_app");
        if(intApp.isEmpty()){
            logger.info("intAppIsEmpty");
            return false;
        }
        JSONObject productInfo = (JSONObject) intApp.get(0);
        //产品ID
        String product_id=productInfo.getString("product_id");
        //数量
        String quantity=productInfo.getString("quantity");
        //交易日期
        String purchase_date=productInfo.getString("purchase_date");

        result.setStatus(1);
        result.setTotalFee(payTrade.getTradeSumFee());
        result.setProductId(product_id);
        result.setQuantity(Integer.valueOf(quantity));
        result.setReceipt(receipt);
        result.setReceiptMd5(receiptMd5);
        Boolean flag =   rpcIosPayService.doSaveIosPayResult(result);
        if(flag){
            rpcOrderService.doPresentLeaf(orderId);
        }
        return flag;
    }


}
