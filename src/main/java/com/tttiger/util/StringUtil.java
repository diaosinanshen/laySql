package com.tttiger.util;


/**
 * 字符串相关方法
 *
 * @author 秦浩桐
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }


    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }


    private final static String UNDERLINE = "_";

    /***
     * 下划线命名转为驼峰命名
     * @param para 下划线命名的字符串
     * @return 驼峰命名字符串
     */
    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String [] a = para.split(UNDERLINE);
        for (String s : a) {
            if (!para.contains(UNDERLINE)) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     * @param para 驼峰命名的字符串
     * @return 下划线命名字符串
     */
    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        para = StringUtil.toLowerCaseFirstOne(para);
        int temp = 0;
        if (!para.contains(UNDERLINE)) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }


    /**
     * 首字母转小写
     *
     * @param str 转换字符串
     * @return 首字母转为小写
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();

        }
    }

    /**
     * 首字母转大写
     *
     * @param str 转换字符串
     * @return 首字母转为大写
     */
    public static String toUpperCaseFirstOne(String str) {
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }

    /**
     * 给定一个url判断url是否是一级路径
     * 一级路径:/root
     * 二级路径:/root/path
     *
     * @param path url路径
     * @return 是否为一级路径
     */
    public static boolean isRootPath(String path) {
        return path.indexOf("/") == path.lastIndexOf("/");
    }


    private StringUtil() {
    }
}
