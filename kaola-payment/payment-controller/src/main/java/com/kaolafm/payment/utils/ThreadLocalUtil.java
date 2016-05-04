package com.kaolafm.payment.utils;


import com.kaolafm.payment.request.CommonParams;

public class ThreadLocalUtil {
	
    private static final ThreadLocal<CommonParams> threadSession = new ThreadLocal<CommonParams>();  
    
    public static CommonParams getCommonParams() {
    	CommonParams s = (CommonParams) threadSession.get();  
        try {  
            if (s == null) {
            	//
            }  
        } catch (Exception e) {  
        	e.printStackTrace();
        }  
        return s;  
    }  
    
    public static void setCommonParams(CommonParams commonParams) throws Exception {
    	try {
			threadSession.set(commonParams);
		} catch (Exception e) {
			throw new Exception(e);  
		}
    }  
    
    
    public static void closeCommonParams() {
    	try {
			CommonParams s = (CommonParams) threadSession.get();
			threadSession.set(null);
			if (s != null){
				s = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
}
