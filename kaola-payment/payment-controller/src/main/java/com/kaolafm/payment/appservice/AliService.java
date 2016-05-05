package com.kaolafm.payment.appservice;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kaolafm.payment.request.CommonParams;

/**
 * @author gongzf
 * @date 2016/3/15
 */
public interface AliService {

    public Map<String, String> alipaySignAndParams(Integer planId,CommonParams commonParam);

    public boolean verifyAlipayNotify(HttpServletRequest request) throws UnsupportedEncodingException;


}
