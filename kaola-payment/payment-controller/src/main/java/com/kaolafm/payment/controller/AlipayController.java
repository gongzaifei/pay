package com.kaolafm.payment.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Signed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kaolafm.payment.annotation.NeedLogin;
import com.kaolafm.payment.annotation.NeedSign;
import com.kaolafm.payment.appservice.AliService;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.response.CommonResponse;

/**
 * @author gongzf
 * @date 2016/3/11
 */
@Controller()
@RequestMapping("alipay")
public class AlipayController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(AlipayController.class);
    @Autowired
    private AliService aliServiceImpl;

    @NeedSign
    @NeedLogin
    @RequestMapping("/sign")
    public Object aliSign(@RequestParam Integer planid,
                          HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        CommonParams commonParam = buildCommonParams(request);
        logger.info("uid:"+commonParam.getUid()+" udid:"+commonParam.getUdid() +" planId:"+planid);
        Map<String, String> resultMap = aliServiceImpl.alipaySignAndParams(planid,commonParam);
        if(resultMap == null){
            throw ServiceException.create(Code.SERVER_ERROR,"signFailed");
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("outTradeNo",resultMap.get("outTradeNo"));
        map.put("url",resultMap.get("url"));
        return new CommonResponse<Map<String,String>>(map);

    }

    @RequestMapping("/notify")
    @ResponseBody
    public void aliNotify(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=gbk");
        response.setCharacterEncoding("gbk");
        response.addHeader("cache-control", "no-cache");
        response.addHeader("expires", "thu,  01 jan   1970 00:00:01 gmt");
        Boolean flag = aliServiceImpl.verifyAlipayNotify(request);
        if(flag){
            PrintWriter out = response.getWriter();
            out.print("success");
            out.flush();
            out.close();
        }
    }
}
