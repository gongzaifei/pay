package com.kaolafm.payment.controller;

import com.kaolafm.payment.annotation.NeedLogin;
import com.kaolafm.payment.appservice.AliService;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.response.CommonResponse;
import com.kaolafm.payment.service.IUserTestService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @NeedLogin
    @RequestMapping("/sign")
    public Object aliSign(@RequestParam Double fee, HttpServletRequest request,
                          HttpServletResponse response) throws ServiceException {
        CommonParams commonParam = buildCommonParams(request);
        logger.info("uid:"+commonParam.getUid()+" udid:"+commonParam.getUdid()+" fee:"+fee);
        String paramStr = aliServiceImpl.alipaySignAndParams(fee,commonParam);
        if(StringUtils.isBlank(paramStr)){
            throw ServiceException.create(Code.SERVER_ERROR,"signFailed");
        }
        return new CommonResponse<String>(paramStr);

    }

    @RequestMapping("notify")
    public Object aliNotify(){
        return null;
    }
}
