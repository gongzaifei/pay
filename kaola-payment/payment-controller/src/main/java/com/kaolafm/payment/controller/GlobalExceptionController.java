package com.kaolafm.payment.controller;

import com.alibaba.dubbo.rpc.RpcException;
import com.kaolafm.payment.exception.ExceptionEnums;
import com.kaolafm.payment.exception.ServiceException;
import com.kaolafm.payment.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

@ControllerAdvice
public class GlobalExceptionController {

	private static Logger errorLog = LoggerFactory.getLogger("errorLog");

	@ExceptionHandler(ServiceException.class)
	public Object serviceException(ServiceException ex, HttpServletRequest request,
			HttpServletResponse response) {

		writeLog(ex, request);
		ExceptionEnums enums = ex.getExceptionEnums();
		CommonResponse<Object> cp = new CommonResponse<Object>();

		StringBuilder errBuilder = new StringBuilder();
		errBuilder.append(enums.getMessage());
		if (ex.getAddMsg() != null) {
			errBuilder.append(":").append(ex.getAddMsg());
		}
		cp.setCode(String.valueOf(enums.getCode()));
		cp.setMessage(errBuilder.toString());
		return cp;
	}
	/**
	 * 参数异常
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 * @return
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Object handleException(MissingServletRequestParameterException ex,
			HttpServletRequest request, HttpServletResponse response) {

		writeLog(ex, request);
		CommonResponse<Object> cp = new CommonResponse<Object>();
		cp.setCode(String.valueOf(50501));
		cp.setMessage("参数错误");
		return cp;
	}

	@ExceptionHandler(Exception.class)
	public Object handleException(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {

		writeLog(ex, request);
		CommonResponse<Object> cp = new CommonResponse<Object>();
		cp.setCode(String.valueOf(50502));
		cp.setMessage("服务器异常");
		return cp;
	}

	private void writeLog(Exception ex, HttpServletRequest request) {
		String url = MessageFormat.format("Exception :{0}?{1}", request.getRequestURL(),
				request.getQueryString());
		errorLog.info(url);
	}

}