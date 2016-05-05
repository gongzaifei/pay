package com.kaolafm.payment.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    
    public static int NOTFUND = -1; // 不在解析范围内
    
    public static int TODAY = 0; // 今天
    
    public static int TOMORROW = 1; // 明天
    
    public static int ACQUIRED = 2; // 后天
    
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static synchronized Date parseDefaultDateForStr(String dateStr)
        throws ParseException {
        return format.parse(dateStr);
    }
    
    public static String getDetaultDateStr(Date date) {
        return format.format(date);
    }
    
    public static int getDayStr(Date date) {
        if ((date == null) || getMonth(date) != getMonth(new Date()))
            return NOTFUND;
        
        int day = getDay(date);
        int today = getDay(new Date());
        if (day == today) {
            return TODAY;
        }
        else if ((today + 1) == day) {
            return TOMORROW;
        }
        else if ((today + 2) == day) {
            return ACQUIRED;
        }
        else {
            return NOTFUND;
        }
    }
    
    public static int getDay(Date date) {
        Calendar cal = getCalendar(date);
        return cal.get(cal.DATE);
    }
    
    public static int getMonth(Date date) {
        Calendar cal = getCalendar(date);
        return cal.get(cal.MONTH) + 1;
    }
    
    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    
    public static Date getDateByMinute(int minute,Date nowDate,String pattern){
    	Date result = null;
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(nowDate);
    	cal.add(Calendar.MINUTE, minute);
    	Date d2 = cal.getTime();
    	String afterDate = sdf.format(d2);
    	try {
			result = sdf.parse(afterDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    public static String getStrDateByMinute(int minute,Date nowDate,String pattern){
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(nowDate);
    	cal.add(Calendar.MINUTE, minute);
    	Date d2 = cal.getTime();
    	String afterDate = sdf.format(d2);
    	return afterDate;
    }
    
    public static void main(String[] args) {
		Date date = new Date();
		System.out.println(getStrDateByMinute(10, date, "yyyyMMddHHmmss"));
	}
}
