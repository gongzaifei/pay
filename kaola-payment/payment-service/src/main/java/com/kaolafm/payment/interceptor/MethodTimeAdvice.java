package com.kaolafm.payment.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author gongzf
 * @date 2016/3/11
 */
public class MethodTimeAdvice implements MethodInterceptor {

    private static final Logger Log = LoggerFactory.getLogger("requestLog");

    private static final Logger slowLog = LoggerFactory.getLogger("slowLog");

    private static final Logger errorLog = LoggerFactory.getLogger("errorLog");

    public Object invoke(MethodInvocation invocation) throws Throwable{
        StopWatch clock = new StopWatch();
        clock.start(); // 计时开始
        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            errorLog.error("errorLog in MethodInterceptor:" + e.getMessage(), e);
            throw e;
        }
        clock.stop(); // 计时结束

        // 方法参数类型，转换成简单类型
        Class[] params = invocation.getMethod().getParameterTypes();
        String[] simpleParams = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            simpleParams[i] = params[i].getSimpleName();
        }
        Object[] args = invocation.getArguments();

        String logContent = "Takes:" + clock.getTime() + " ms [" + invocation.getThis().getClass().getName() + "."
                + invocation.getMethod().getName() + "(" + StringUtils.join(simpleParams, ",") + ")("
                + StringUtils.join(args, ",") + ")] ";


        Log.info(logContent);

        if (clock.getTime() > 200) {

                slowLog.info(logContent);
        }
        return result;
    }
}
