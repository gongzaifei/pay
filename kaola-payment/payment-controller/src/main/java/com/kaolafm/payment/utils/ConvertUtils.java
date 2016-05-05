package com.kaolafm.payment.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jodd.datetime.JDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaolafm.payment.dto.PageDto;

public class ConvertUtils {
	private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);

	public static <T> PageDto<T> create(List<T> dataList, Integer count, Integer pageNum, Integer pageSize) {
		int totalPages, haveNext, nextPage, havePre, prePage;
		totalPages = (count % pageSize) == 0 ? (count / pageSize) : (count / pageSize + 1);
		haveNext = (pageNum < totalPages) ? 1 : 0;
		nextPage = haveNext == 1 ? pageNum + 1 : pageNum;
		havePre = pageNum > 1 ? 1 : 0;
		prePage = havePre == 1 ? pageNum - 1 : pageNum;
		PageDto<T> pageDto = new PageDto<T>(haveNext, nextPage, havePre, prePage);
		pageDto.setCount(count);
		pageDto.setCurrentPage(pageNum);
		pageDto.setPageSize(pageSize);
		pageDto.setSumPage(totalPages);
		pageDto.setDataList(dataList);
		return pageDto;
	}

	public static String compareDate(Date dateTime) {
		if (dateTime == null) {
			return "";
		}
		String updateStatus = "";
		JDateTime updateDateTime = new JDateTime(dateTime);
		JDateTime nowDateTime = new JDateTime();
		Long timeMills = nowDateTime.getTimeInMillis();
		Long updateMills = updateDateTime.getTimeInMillis();
		Long sub = (timeMills - updateMills) / 1000 / 60;
		if (sub < 5) {
			updateStatus = "刚刚";
		}
		if (5 <= sub && sub < 60) {
			updateStatus = sub + "分钟前";
		} else if (60 <= sub && sub < 60 * 24) {
			updateStatus = (sub / 60) + "小时前";
		} else if (60 * 24 <= sub) {
			String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
			updateStatus = formatDate;
		}
		return updateStatus;
	}

	public static <T> PageDto<T> create(Integer count, Integer pageNum, Integer pageSize) {
		int totalPages, haveNext, nextPage, havePre, prePage;
		totalPages = (count % pageSize) == 0 ? (count / pageSize) : (count / pageSize + 1);
		haveNext = (pageNum < totalPages) ? 1 : 0;
		nextPage = haveNext == 1 ? pageNum + 1 : pageNum;
		havePre = pageNum > 1 ? 1 : 0;
		prePage = havePre == 1 ? pageNum - 1 : pageNum;
		PageDto<T> pageDto = new PageDto<T>(haveNext, nextPage, havePre, prePage);
		pageDto.setCount(count);
		pageDto.setSumPage(totalPages);
		pageDto.setCurrentPage(pageNum);
		pageDto.setPageSize(pageSize);
		return pageDto;
	}

}
