/**
 * Project Name:ezplatform
 * File Name:StringUtils.java
 * Package Name:com.enmore.utils
 * Date:2016年8月2日上午10:45:28
 * Copyright (c) 2016, 上海易贸供应链管理有限公司版权所有.
 */

package application.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName:StringUtils <br/>
 * Function: String转换工具类. <br/>
 * Date: 2016年8月2日 上午10:45:28 <br/>
 *
 * @author markhe
 * @version
 * @since JDK 1.6
 * @see
 */
public class StringUtils {
    public static final String EMPTY_STRING = "";

    /**
     *
     * 判断字符串是否为空，为空返回true <br/>
     *
     * @author markhe
     * @param str
     * @return
     * @since JDK 1.6
     */
    public static boolean isBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0))
            return true;
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * 判断字符串是否不为空， 不为空是true <br/>
     *
     * @author markhe
     * @param str
     * @return
     * @since JDK 1.6
     */
    public static boolean isNotBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0))
            return false;
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * 判断字符串是否相同 <br/>
     *
     * @author markhe
     * @param str1
     * @param str2
     * @return
     * @since JDK 1.6
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    /**
     *
     * 判断是否为手机号码
     *
     * @param str
     * @return
     * @since JDK 1.6
     */
    public static boolean checkMobile(String str) {
        Pattern pattern = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        return pattern.matcher(str).matches();
    }

    /**
     *
     * @Title: doubleFormat
     * @Description: double格式化，保留两位小数
     * @param @param
     *          number
     * @param @return
     *          设定文件
     * @return Double 返回类型
     * @version 1.0
     */
    public static Double doubleFormat(Double number) {
        if (number != null) {
            DecimalFormat df = new DecimalFormat("#.0000");
            number = Double.parseDouble(df.format(number));
        }
        return number;
    }

    /**
     *
     * @Title: checkArryBlank
     * @Description: 判断字符串数组是否为空
     * @param @param
     *          strs
     * @param @return
     *          设定文件
     * @return boolean 返回类型
     * @version 1.0
     */
    public static boolean checkArryisNotBlank(String[] strs) {
        if (strs == null || (strs != null && strs.length == 0)) {
            return false;
        }
        return true;
    }

    /**
     *
     * getOriginalValueOrDefault:获取对应 <br/>
     *
     * @author jasperzuo
     * @param str
     * @return
     * @since JDK 1.6
     */
    public static String getOriginalValueOrDefault(String str) {
        return str != null ? str : StringUtils.EMPTY_STRING;
    }

    /**
     * 判断字符串是否为正整数
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
