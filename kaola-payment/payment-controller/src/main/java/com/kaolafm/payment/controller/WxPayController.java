package com.kaolafm.payment.controller;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kaolafm.payment.annotation.NeedLogin;
import com.kaolafm.payment.appservice.WxPayService;

@Controller
@RequestMapping("/v4/wxpay")
public class WxPayController extends BaseController {

	@Autowired
	private WxPayService wxPayService;

	@RequestMapping("/getorderid")
	@NeedLogin
	public Object getOrderId(HttpServletRequest request) {

		String orderid = wxPayService.getOrderId();

		return null;
	}

	@RequestMapping(value = "/order/notify", method = RequestMethod.POST)
	public Object receiveNotify(HttpServletRequest request) {
		System.out.println("-------------------------------------------");
		try {
			ServletInputStream in = request.getInputStream();
			StringBuffer info = new StringBuffer();
			BufferedInputStream buf = new BufferedInputStream(in);
			byte[] buffer = new byte[1024];
			int iRead;
			while ((iRead = buf.read(buffer)) != -1) {
				info.append(new String(buffer, 0, iRead, "utf-8"));
			}
			boolean flag = wxPayService.updateWxOrder(info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
