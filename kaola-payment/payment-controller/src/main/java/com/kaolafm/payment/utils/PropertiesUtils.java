package com.kaolafm.payment.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {

	private static Properties p = new Properties();
	
	private static Map<String, String> propertieMap = new HashMap<String, String>();
	
	
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
	
}
