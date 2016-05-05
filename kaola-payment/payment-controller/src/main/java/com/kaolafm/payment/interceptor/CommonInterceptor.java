package com.kaolafm.payment.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.kaolafm.monitor.web.pojo.LogMsg;
import com.kaolafm.payment.annotation.NeedLogin;
import com.kaolafm.payment.constants.Constants;
import com.kaolafm.payment.controller.BaseController;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.response.CommonResponse;
import com.kaolafm.payment.utils.ThreadLocalUtil;
import com.kaolafm.payment.utils.TopicProducer;
import com.kaolafm.user.service.UserService;

/**
 @author cy-liuchong
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {

	private Logger requestLogger = LoggerFactory.getLogger("requestLog");
	private Logger slowLogger = LoggerFactory.getLogger("slowLog");
	private Logger errorLogger = LoggerFactory.getLogger("errorLog");
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ExecutorService pushExecutorService = Executors.newFixedThreadPool(20);

	@Override
	@SuppressWarnings("rawtypes")
	public void postHandle(HttpServletRequest request, HttpServletResponse arg1, Object handler, ModelAndView arg3)
			throws Exception {
		
		if (arg3 != null) {
			ModelMap mm = arg3.getModelMap();
			if (mm != null && mm.values() != null) {
				Iterator<Object> result = mm.values().iterator();// 遍历modelMap
				if (result != null) {
					while (result.hasNext()) {
						Object o = result.next();
						if (o instanceof CommonResponse) {
							CommonResponse cp = (CommonResponse) o;
							String code = cp.getCode();
							request.setAttribute("commonResponseCode", code);
						}
					}
				}
			}
		}

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, final Object handler, Exception ex)
			throws Exception {
		
		final ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		
		Long beforTime = (Long) request.getAttribute("requestTime");
		StringBuffer sb = new StringBuffer();
		sb.append(request.getRequestURL());
		sb.append("?");
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap != null && parameterMap.size() > 0) {
			for (String key : parameterMap.keySet()) {
				sb.append("&").append(key).append("=");
				String[] values = parameterMap.get(key);
				if (values != null && values.length > 0) {
					for (int i = 0; i < values.length; i++) {
						if (i != 0) {
							sb.append(",");
						}
						sb.append(values[i]);
					}
				}
			}
		}

		Object commonResponseCode = request.getAttribute("commonResponseCode");

		final long responseTime = System.currentTimeMillis() - beforTime;
		if (responseTime > 300) {
			slowLogger.info(sb.toString() + " 耗时 " + (System.currentTimeMillis() - beforTime));
			requestLogger.info(sb.toString() + " 耗时 " + (System.currentTimeMillis() - beforTime) + " 返回码："
					+ commonResponseCode);
		} else {
			requestLogger.info(sb.toString() + " 耗时 " + (System.currentTimeMillis() - beforTime) + " 返回码："
					+ commonResponseCode);
		}
		
		pushExecutorService.submit(new Runnable() {
			@Override
			public void run() {
				if(handler instanceof HandlerMethod){
					HandlerMethod handlerMethod = (HandlerMethod) handler;
					Method method = handlerMethod.getMethod();
					String methodName = method.getName();
					Object o = handlerMethod.getBean();
					
					LogMsg logMsg = new LogMsg();
					logMsg.setAppName("pay-controller");
					logMsg.setMethodName(methodName);
					logMsg.setRequestDate(new Date());
					logMsg.setResponseTime(responseTime);
					logMsg.setServiceName(o.getClass().getName());
					
					TopicProducer producer = context.getBean(TopicProducer.class);
					producer.sendMessage("pay-controller", JSONObject.toJSONString(logMsg));
				}
			}
		});
		
		
		ThreadLocalUtil.closeCommonParams();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("requestTime", System.currentTimeMillis());

		CommonParams params = BaseController.buildCommonParams(request);
		ThreadLocalUtil.setCommonParams(params);

		String currentUid = null;
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());

		UserService userService = context.getBean(UserService.class);
		NeedLogin authPassport = ((HandlerMethod) handler).getMethodAnnotation(NeedLogin.class);

		boolean requireLogin = false;
		String requestUrl = request.getRequestURL().toString();
		if(requestUrl.indexOf("accountinfo/get") > 0){
			String type = request.getParameter("type");
			if(type != null && !type.equals(Constants.USERACCOUNT_INFO_TYPE.LIVE_PROGRAM_LEAF_COUNT.code().toString())){
				requireLogin = true;
			}
		}
		// 需要认证
		String uid = request.getParameter("uid");
		String token = request.getParameter("token");
		if (authPassport != null || requireLogin) {
			// if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token)) {
			if (StringUtils.isEmpty(uid)) {
				throw ServiceException.create(Code.USER_NOT_LOGIN);
			}
			
			if (StringUtils.isEmpty(token)) {
				throw ServiceException.create(Code.BAD_REQUEST);
			}
			
			String tokenValue = userService.getTokenByUid(uid);
			if (StringUtils.isEmpty(tokenValue)) {
				errorLogger.error("real token value {}, parameter value {}", tokenValue, token);
				throw ServiceException.create(Code.USER_NOT_LOGIN);
			} else {
				if (!token.equals(tokenValue)) {
					errorLogger.error("real token value {}, parameter value {}", tokenValue, token);
					throw ServiceException.create(Code.USER_TOKEN_UNVALID);
				}
			}
			 

		}
		

		return super.preHandle(request, response, handler);
	}
}
