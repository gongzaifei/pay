package com.kaolafm.payment.request;

import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaolafm.payment.utils.IdBuildUtils;
import com.kaolafm.payment.utils.MD5Utils;

public class OrderHandler {
	
	Logger logger = LoggerFactory.getLogger(OrderHandler.class);

	/** 请求的参数 */
	private TreeMap<String, String> parameters = new TreeMap<String, String>();
	
	public static final String allChar = "123456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";

	public OrderHandler() {
	}

	/**
	 * 设置参数值
	 * 
	 * @param parameter
	 *            参数名称
	 * @param parameterValue
	 *            参数值
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if (StringUtils.isNotBlank(parameterValue)) {
			v = parameterValue.trim();
			this.parameters.put(parameter, v);
		}
	}

	public String getWXPayXml() {
		Document document = DocumentHelper.createDocument();
		Element element = document.addElement("xml");
		for (String key : parameters.keySet()) {
			if(!key.equals("wxKey") && StringUtils.isNotBlank(parameters.get(key))){
				element.addElement(key).setText(parameters.get(key));
			}
		}
		String sign = this.getWxSign();
		element.addElement("sign").setText(sign);

		return document.asXML();
	}
	
	public String getWxSign(){
		StringBuffer sb = new StringBuffer();
		for (String key : parameters.keySet()) {
			if(!key.equals("wxKey") && StringUtils.isNotBlank(parameters.get(key))){
				sb.append(key).append("=").append(parameters.get(key) + "&");
			}
		}
		sb.append("key=").append(parameters.get("wxKey"));
		String sign = MD5Utils.getMD5Str(sb.toString().substring(0, sb.toString().length())).toUpperCase();
		return sign;
	}

	public static String getKaolaOrderId(String uid) {
		return IdBuildUtils.buildFillId(uid);
	}
	
	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}


}
