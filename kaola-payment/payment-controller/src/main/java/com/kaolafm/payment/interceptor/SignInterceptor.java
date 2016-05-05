package com.kaolafm.payment.interceptor;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.kaolafm.payment.annotation.NeedSign;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.user.util.StringUtils;

/**
 *
 */
public class SignInterceptor implements HandlerInterceptor {

	public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

	private static final String key = "9476cf76d0a82143d0030385e04ab301";

	private static final String SIGNV2 = "signv2";

	private static final String TIMESTAMP = "timestamp";

	/**
	 * 获取签名
	 *
	 * @param sortMap
	 *            排序后的MAP
	 * @return 签名
	 */
	public static String getSign(Map<String, String> sortMap) throws Exception {
		if (sortMap != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(key);
			int i = 0;
			for (Map.Entry<String, String> entry : sortMap.entrySet()) {
				builder.append(i).append(entry.getKey()).append("=").append(entry.getValue());
				i++;
			}
			builder.append(key);
			logger.info("加密前：" + builder.toString());
			String signV2 = DigestUtils.md5Hex(builder.toString()).toLowerCase();
			logger.info("签名值:" + signV2);
			return signV2;
		}
		return "";
	}

	/**
	 * 获取所有请求参数
	 *
	 * @param request
	 * @return
	 */
	private static Map<String, String> getRequestParams(HttpServletRequest request) {

		Map<String, String> map = new TreeMap<String, String>();
		Enumeration paramNames = request.getParameterNames();
		if (paramNames != null) {
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				String[] paramValues = request.getParameterValues(paramName);
				if (paramValues.length == 1) {
					String paramValue = paramValues[0];
					if (paramValue.length() != 0) {
						map.put(paramName, paramValue);
					}
				}
			}
		}
		return map;
	}

	/**
	 * 验证时间戳是否过期
	 *
	 * @return
	 */
	private static boolean isExpire(String timestamp) {
		try {
			if (!StringUtils.isEmpty(timestamp)) {

				long current = System.currentTimeMillis() / 1000;
				long longTS = Long.valueOf(timestamp);
				double hour = (current - longTS) / (60 * 60); // 时间戳单位 秒
				if (hour < 2 && hour > -2) { // 1小时内有效
					return true;
				}
			}
		} catch (Exception ex) {
			logger.error("时间戳转换异常,{}", timestamp, ex);
		}
		return false;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		NeedSign needSign = ((HandlerMethod) handler).getMethodAnnotation(NeedSign.class);
		if(needSign != null){
			Map<String, String> sortMap = getRequestParams(request);
			// 获取sign 并在参数列表删除 sign值不参与签名
			String requestSignv2 = sortMap.get(SIGNV2);
			String param = sortMap.get("test");
			//TODO 方便测试 上线之前去掉
			if("kaolafm".equals(param)){
				logger.info("testKaola");
				return true;
			}
			if (StringUtils.isEmpty(requestSignv2)) {
				throw new ServiceException(Code.BAD_REQUEST);
			}else{
				/*if (!isExpire(sortMap.get(TIMESTAMP))) {
					logger.info("request exprie", sortMap.get(TIMESTAMP));
					throw new ServiceException(Code.REQUEST_EXPIRE);
				}*/
				sortMap.remove(SIGNV2);
				String paramSign = getSign(sortMap);
				if (!requestSignv2.equals(paramSign)) {
					logger.info("sign err {}, {}", paramSign, sortMap.toString());
					throw new ServiceException(Code.SIGN_ERROR);
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
