package com.kaolafm.payment.interceptor;

import com.kaolafm.payment.annotation.NeedLogin;
import com.kaolafm.payment.controller.BaseController;
import com.kaolafm.payment.exception.Code;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.request.CommonParams;
import com.kaolafm.payment.utils.ThreadLocalUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cy-liuchong
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {

    private Logger slowLogger = LoggerFactory.getLogger("slowLog");
    private Logger errorLogger = LoggerFactory.getLogger("errorLog");
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("requestTime", System.currentTimeMillis());

        CommonParams params = BaseController.buildCommonParams(request);
        ThreadLocalUtil.setCommonParams(params);

        String currentUid = null;
        ApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(request.getSession()
                        .getServletContext());

        NeedLogin authPassport = ((HandlerMethod) handler)
                .getMethodAnnotation(NeedLogin.class);

        // 需要认证
        if (authPassport != null) {
        	String requestUrl = request.getRequestURL().toString();
                String uid = request.getParameter("uid");
                if (StringUtils.isEmpty(uid)) {
                    throw ServiceException.create(Code.USER_NOT_LOGIN);
                }

            }
            
        return super.preHandle(request, response, handler);
    }
}
