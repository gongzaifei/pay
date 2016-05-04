package com.kaolafm.payment.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Utils {

    private static Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    private MD5Utils() {

    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException caught!", e);
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error("编码错误", e);
            return null;
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (byte element : byteArray) {
            if (Integer.toHexString(0xFF & element).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & element));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & element));
            }
        }

        return md5StrBuff.toString();
    }

}
