package com.kaolafm.payment.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class PropertiesUtils {

	private static Properties p = new Properties();
	
	private static Map<String, String> propertieMap = new HashMap<String, String>();
	
	public static final String allChar = "123456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
	
	/**
	 * 读取applicationContext.properties配置文件信息
	 */
	static {
		try {
			p.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("pay.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据key得到value的值
	 */
	public static String getValue(String key) {
		String result = propertieMap.get(key);
        if (result == null) {
            result = p.getProperty(key);
            propertieMap.put(key, result);
        }
        return result;
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
