package com.tttiger.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2019/12/30 15:14
 */
public class EncodeUtil {


    /**
     * 使用URL编码,编码字符串
     * @param  str 需要进行URL编码字符
     * @return 编码后字符串
     */
    public static String urlEncode(String str) {
        return urlEncode(str,"UTF-8");
    }

    /**
     *
     * 使用URL编码,编码字符串
     * @param  str 需要进行URL编码字符
     * @param encode 指定字符编码
     * @return 编码后字符串
     */
    public static String urlEncode(String str,String encode){
        try {
            return java.net.URLEncoder.encode(str, encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用URL编码,解码字符串
     * @param str 需要进行URL解码的字符串
     * @return 解码后字符串
     */
    public static String urlDecode(String str){
        return urlDecode(str,"UTF-8");
    }


    /**
     * 使用URL编码,解码字符串
     * @param str 需要进行URL解码的字符串
     * @param encode 指定字符编码
     * @return 解码后字符串
     */
    public static String urlDecode(String str,String encode){
        try {
            return  java.net.URLDecoder.decode(str, encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * md5 盐值加密字符
     *
     * @param param 加密字符串
     * @param salt  盐
     * @return 加密字符串
     */
    public static String md5Encode(String param, String salt) {
        return md5Encode(param + salt);
    }

    /**
     * md5 加密字符串
     *
     * @param s 字符串
     * @return 加密字符串
     */
    public static String md5Encode(String s) {
        char[] hexDigits =
                {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }


    private EncodeUtil() {
    }
}
