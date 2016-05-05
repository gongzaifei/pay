package com.kaolafm.payment.controller;

import com.kaolafm.payment.annotation.NeedLogin;
import com.kaolafm.payment.annotation.NeedSign;
import com.kaolafm.payment.appservice.IOSPayService;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.response.CommonResponse;

import com.kaolafm.user.exception.UserServiceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Signed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gongzf
 * @date 2016/3/22
 */
@Controller
@RequestMapping("/iospay")
public class IOSPayController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(IOSPayController.class);

    @Autowired
    private IOSPayService iosPayService;

    /**
     * 充值获取订单接口
     * @param planid
     * @param request
     * @param response
     * @return
     * @throws ServiceException
     */
    @NeedSign
    @NeedLogin
    @RequestMapping("/getorder")
    public Object getOrderId(@RequestParam Integer planid,
                          HttpServletRequest request, HttpServletResponse response) throws ServiceException, UserServiceException {
        CommonParams commonParam = buildCommonParams(request);
        logger.info("uid:"+commonParam.getUid()+" udid:"+commonParam.getUdid() +" planId:"+planid);
        Map<String,String> map  = iosPayService.getOrderId(planid,commonParam);
        if(map.isEmpty()){
            throw ServiceException.create(Code.SERVER_ERROR,"signFailed");
        }
        return new CommonResponse<Map<String,String>>(map);

    }

    /**
     * 苹果充值校验接口
     * @param receipt
     * @param orderid
     * @param request
     * @param response
     * @return
     */
    @NeedSign
    @NeedLogin
    @RequestMapping(value = "/verify",method = RequestMethod.POST)
    public Object verifyIosReceipt(@RequestParam String receipt,@RequestParam String orderid,
                                   HttpServletRequest request,HttpServletResponse response){
        CommonParams commonParams = buildCommonParams(request);
        logger.info("uid"+commonParams.getUid() + " receipt"+receipt + " orderid"+orderid);
        Map<String,Integer> map = new HashMap<String,Integer>();
        boolean flag = iosPayService.verifyIosReceipt(receipt,orderid,commonParams);
        Integer status = 1;
        if(flag){
            status = 0;
        }
        map.put("status",status);
        return new CommonResponse<Map<String,Integer>>(map);
    }


}
