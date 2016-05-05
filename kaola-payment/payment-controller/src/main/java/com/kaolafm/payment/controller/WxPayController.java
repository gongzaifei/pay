package com.kaolafm.payment.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kaolafm.payment.annotation.NeedLogin;
import com.kaolafm.payment.annotation.NeedSign;
import com.kaolafm.payment.appservice.WxPayService;
import com.kaolafm.payment.dto.WxAppPayDto;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.response.CommonResponse;

@Controller
@RequestMapping("/wx")
public class WxPayController extends BaseController {

	Logger log = LoggerFactory.getLogger(WxPayController.class);

	@Autowired
	private WxPayService wxPayService;

	@NeedSign
	@NeedLogin
	@RequestMapping("/getorderinfo")
	public Object getOrderId(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer planid)
			throws Exception {
		CommonResponse<Map<String, Object>> cp = new CommonResponse<Map<String, Object>>();
		WxAppPayDto wxAppPayDto = wxPayService.getOrderId(planid);

		Map<String, Object> resultMap = new HashMap<String, Object>();
//		StringBuffer sb = new StringBuffer();
//		sb.append("{");
//		sb.append("\"appid\":\"" + wxAppPayDto.getAppid() + "\",");
//		sb.append("\"noncestr\":\"" + wxAppPayDto.getNonceStr() + "\",");
//		sb.append("\"package\":\"" + wxAppPayDto.getAppPackage() + "\",");
//		sb.append("\"partnerid\":\"" + wxAppPayDto.getPartnerId() + "\",");
//		sb.append("\"prepayid\":\"" + wxAppPayDto.getPrepayid() + "\",");
//		sb.append("\"sign\":\"" + wxAppPayDto.getSign() + "\",");
//		sb.append("\"timestamp\":\"" + wxAppPayDto.getTimestamp() + "\"");
//		sb.append("}");
		resultMap.put("appid",wxAppPayDto.getAppid());
		resultMap.put("noncestr",wxAppPayDto.getNonceStr());
		resultMap.put("appPackage",wxAppPayDto.getAppPackage());
		resultMap.put("partnerid",wxAppPayDto.getPartnerId());
		resultMap.put("prepayid",wxAppPayDto.getPrepayid());
		resultMap.put("sign",wxAppPayDto.getSign());
		resultMap.put("timestamp",wxAppPayDto.getTimestamp());
		resultMap.put("outTradeNo", wxAppPayDto.getOutTradeNo());
		cp.setResult(resultMap);
		return cp;
	}

	@RequestMapping(value = "/order/notify", method = RequestMethod.POST)
	public Object receiveNotify(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer result = new StringBuffer();
		OutputStream out = null;
		try {
			ServletInputStream in = request.getInputStream();
			StringBuffer info = new StringBuffer();
			BufferedInputStream buf = new BufferedInputStream(in);
			byte[] buffer = new byte[1024];
			int iRead;
			while ((iRead = buf.read(buffer)) != -1) {
				info.append(new String(buffer, 0, iRead, "utf-8"));
			}
			log.info("wx pay result notify resultInfo:{}", info.toString());
			try {
				wxPayService.NotifyHandleResult(info.toString());
				result.append("<xml>").append("<return_code><![CDATA[SUCCESS]]></return_code>").append("</xml>");
			} catch (ServiceException e) {
				String msg = e.getExceptionEnums().getMessage();
				result.append("<xml>").append("<return_code><![CDATA[FAIL]]></return_code>")
						.append("<return_msg><![CDATA[" + msg + "]]></return_msg>").append("</xml>");
			}
			out = response.getOutputStream();
			out.write(result.toString().getBytes());
			out.flush();
		} catch (IOException e) {
			log.info("wx pay notify exception");
			e.printStackTrace();
		}
		return null;
	}

}
