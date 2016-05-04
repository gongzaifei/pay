package com.kaolafm.payment.request;

import java.util.TreeMap;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kaolafm.payment.utils.MD5Utils;
import com.kaolafm.payment.utils.PropertiesUtils;

public class OrderHandler {

	/** 请求的参数 */
	private TreeMap<String, String> parameters = new TreeMap<String, String>();

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
		if (null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}

	public String getWXPayXml() {
		StringBuffer sb = new StringBuffer();
		Document document = DocumentHelper.createDocument();
		Element element = document.addElement("xml");
		for (String key : parameters.keySet()) {
			sb.append(key).append("=").append(parameters.get(key) + "&");
			element.addElement(key).setText(parameters.get(key));
		}
		sb.append("key=").append(PropertiesUtils.getValue("wxKey"));
		System.out.println("sb====" + sb.toString().substring(0, sb.toString().length()));
		String sign = MD5Utils.getMD5Str(sb.toString().substring(0, sb.toString().length())).toUpperCase();
		System.out.println("sign====" + sign);
		element.addElement("sign").setText(sign);

		return document.asXML();
	}

	public static String getKaolaOrderId() {
		long time = System.currentTimeMillis();
		String s = UUID.randomUUID().toString();
		return "kf" + time + s.substring(0, 8) + s.substring(9, 10);
	}

}
