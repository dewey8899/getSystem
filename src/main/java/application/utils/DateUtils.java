/**
 * Project Name:ezplatform
 * File Name:DateUtils.java
 * Package Name:com.enmore.utils
 * Date:2016年8月2日上午10:45:18
 * Copyright (c) 2016, 上海易贸供应链管理有限公司版权所有.
 */

package application.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ClassName:DateUtils <br/>
 * Function: 时间格式转换工具类 <br/>
 * Date: 2016年8月2日 上午10:45:18 <br/>
 *
 * @author markhe
 * @version
 * @since JDK 1.6
 * @see
 */
public class DateUtils {
    /** yyyyMMdd */
    public final static String SHORT_FORMAT = "yyyyMMdd";

    /** yyyyMMddHHmmss */
    public final static String LONG_FORMAT = "yyyyMMddHHmmss";

    /** yyyyMMddHHmm */
    public final static String SHORT_LONG_FORMAT = "yyyyMMddHHmm";

    /** yyyy-MM-dd */
    public final static String WEB_FORMAT = "yyyy-MM-dd";

    /** yyyy.MM.dd  照片水印的日期需要added by Dewey*/
    public final static String WEB_FORMAT2 = "yyyy.MM.dd";

    /** yyyy-MM */
    public final static String WEB_MONTH_FORMAT = "yyyy-MM";

    /** HHmmss */
    public final static String TIME_FORMAT = "HHmmss";

    /** yyyyMM */
    public final static String MONTH_FORMAT = "yyyyMM";

    /** yyyy年MM月dd日 */
    public final static String CHINA_FORMAT = "yyyy年MM月dd日";
    
    public final static String CHINA_FORMAT_LONG = "yyyy/MM/dd HH:mm:ss";

    public final static String CHINA_FORMAT_MEDIUM = "yyyy/MM/dd HH:mm";

    public final static String CHINA_FORMAT_SHORT = "yyyy/MM/dd";

    /** yyyy-MM-dd HH:mm:ss */
    public final static String LONG_WEB_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** yyyy-MM-dd HH:mm */
    public final static String LONG_WEB_FORMAT_NO_SEC = "yyyy-MM-dd HH:mm";

    /** MM-dd HH:mm */
    public final static String WEB_FORMAT_NO_SEC = "MM-dd HH:mm";

    /** HH:mm */
    public final static String WEB_TIME_FORMAT = "HH:mm";

    public final static String CRON_DATE_FORMAT = "0 mm HH * * ?";

    /**
     * 日期对象解析成日期字符串基础方法，可以据此封装出多种便捷的方法直接使用
     *
     * @param date
     *          待格式化的日期对象
     * @param format
     *          输出的格式
     * @return 格式化的字符串
     */
    public static String format(Date date, String format) {
        if (date == null || StringUtils.isBlank(format)) {
            return StringUtils.EMPTY_STRING;
        }

        return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).format(date);
    }

    /**
     * 格式化当前时间
     *
     * @param format
     *          输出的格式
     * @return
     */
    public static String formatCurrent(String format) {
        if (StringUtils.isBlank(format)) {
            return StringUtils.EMPTY_STRING;
        }

        return format(new Date(), format);
    }

    /**
     * 日期字符串解析成日期对象基础方法，可以在此封装出多种便捷的方法直接使用
     *
     * @param dateStr
     *          日期字符串
     * @param format
     *          输入的格式
     * @return 日期对象
     * @throws ParseException
     */
    public static Date parse(String dateStr, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("format can not be null.", 0);
        }

        if (dateStr == null || dateStr.length() < format.length()) {
            throw new ParseException("date string's length is too small.", 0);
        }

        return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).parse(dateStr);
    }

    /**
     * 日期字符串格式化基础方法，可以在此封装出多种便捷的方法直接使用
     *
     * @param dateStr
     *          日期字符串
     * @param formatIn
     *          输入的日期字符串的格式
     * @param formatOut
     *          输出日期字符串的格式
     * @return 已经格式化的字符串
     * @throws ParseException
     */
    public static String format(String dateStr, String formatIn, String formatOut) throws ParseException {

        Date date = parse(dateStr, formatIn);
        return format(date, formatOut);
    }

    /**
     * 把日期对象按照<code>yyyyMMdd</code>格式解析成字符串
     *
     * @param date
     *          待格式化的日期对象
     * @return 格式化的字符串
     */
    public static String formatShort(Date date) {
        return format(date, SHORT_FORMAT);
    }

    /**
     * 把日期字符串按照<code>yyyyMMdd</code>格式，进行格式化
     *
     * @param dateStr
     *          待格式化的日期字符串
     * @param formatIn
     *          输入的日期字符串的格式
     * @return 格式化的字符串
     */
    public static String formatShort(String dateStr, String formatIn) throws ParseException {
        return format(dateStr, formatIn, SHORT_FORMAT);
    }

    /**
     * 把日期对象按照<code>yyyy-MM-dd</code>格式解析成字符串
     *
     * @param date
     *          待格式化的日期对象
     * @return 格式化的字符串
     */
    public static String formatWeb(Date date) {
        return format(date, WEB_FORMAT);
    }
    public static String formatWeb2(Date date) {
        return format(date, WEB_FORMAT2);
    }
    public static String formatWeb3(Date date) {
//        return format(date, SHORT_LONG_FORMAT);
        return format(date, LONG_FORMAT);
    }

    /**
     * 把日期字符串按照<code>yyyy-MM-dd</code>格式，进行格式化
     *
     * @param dateStr
     *          待格式化的日期字符串
     * @param formatIn
     *          输入的日期字符串的格式
     * @return 格式化的字符串
     * @throws ParseException
     */
    public static String formatWeb(String dateStr, String formatIn) throws ParseException {
        return format(dateStr, formatIn, WEB_FORMAT);
    }

    /**
     * 把日期对象按照<code>yyyyMM</code>格式解析成字符串
     *
     * @param date
     *          待格式化的日期对象
     * @return 格式化的字符串
     */
    public static String formatMonth(Date date) {

        return format(date, MONTH_FORMAT);
    }

    /**
     * 把日期对象按照<code>HHmmss</code>格式解析成字符串
     *
     * @param date
     *          待格式化的日期对象
     * @return 格式化的字符串
     */
    public static String formatTime(Date date) {
        return format(date, TIME_FORMAT);
    }

    /**
     * getCurrentMonth:或取当前档期 <br/>
     *
     * @author Administrator
     * @return
     * @since JDK 1.6
     */
    public static String getCurrentPeriodDay() {
        Calendar newCalendar = Calendar.getInstance();

        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH) + 1;
        if (month < 10) {

            return year + "-0" + month;

        } else {

            return year + "-" + month;
        }

    }

    /**
     * 获取今天初始时间，格式为yyyy-MM-dd 00:00:00
     * @return
     */
    public static Date todayBeginDate() {
        try {
            return parse(format(new Date(), WEB_FORMAT), WEB_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取今天结束时间，格式为yyyy-MM-dd 23:59:59
     * @return
     */
    public static Date todayEndDate () {
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayBeginDate());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    /**
     * 相较于今天，获取某天的开始时间
     * @param diff 与今天相差的天数，正数表示往后
     */
    public static Date beginDateByToday (int diff) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayBeginDate());
        cal.add(Calendar.DAY_OF_MONTH, diff);
        return cal.getTime();
    }

    /**
     * 获取某天的开始时间
     * @param date
     */
    public static Date beginDateByDate (Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 相较于今天，获取某天的结束时间
     * @param diff 与今天相差的天数，正数表示往后
     */
    public static Date endDateByToday (int diff) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayBeginDate());
        cal.add(Calendar.DAY_OF_MONTH, diff + 1);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    /**
     * 获取某天的结束时间
     * @param date
     */
    public static Date endDateByDate (Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取某天的结束时间
     * @param date
     */
    public static Date endDateByDateForSql (Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * getOneWeekAgoDay:获取一周前的日期 <br/>
     *
     * @author john
     * @return
     * @since JDK 1.6
     */
    public static String getOneWeekAgoDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7); // 得到一周前的日期
        long date = cal.getTimeInMillis();
        String startDay = sdf.format(new Date(date));
        return startDay;
    }

    /**
     * 功能: 返回date1与date2相差的天数
     *
     * @param date1
     *          截止日期
     * @param date2
     *          起始日期 
     * @return int
     */
    public static int dateDiff(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        boolean isAfter;
        if (date1.after(date2)) {
            isAfter = true;
            c1.setTime(date1);
            c2.setTime(date2);
        } else {
            isAfter = false;
            c1.setTime(date2);
            c2.setTime(date1);
        }

        int years = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int days = c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR);

        for (int i = 0; i < years; i++) {
            days += c2.getActualMaximum(Calendar.DAY_OF_YEAR);
            c2.add(Calendar.YEAR, 1);
        }

        return isAfter ? days : - days;
    }

    /**
     * 功能: 返回date1与date2相差的天数
     *
     * @param date1
     *          截止日期
     * @param date2
     *          起始日期 
     * @return int
     */
    public static int diffDays(Date date1, Date date2) {
      long intervalMilli = date1.getTime() - date2.getTime();
      return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }

    /**
     * 功能: 返回date1与date2相差的分钟数
     * @param date1 截止日期
     * @param date2 起始日期
     * @return int
     */
    public static int minuteDiff(Date date1, Date date2) {
        return (int)((date1.getTime() - date2.getTime()) / (1000 * 60));
    }

    /**
     * 功能: 返回date1与date2相差的秒数
     * @param date1 截止日期
     * @param date2 起始日期
     * @return int
     */
    public static int secondDiff(Date date1, Date date2) {
        return (int)((date1.getTime() - date2.getTime()) / 1000);
    }

    /**
     * 获取当前时间到晚上零点的秒数
     *
     * @return
     */
    public static long getNightSecond() {
        Date date = new Date();
        long startTime = date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long endTime = calendar.getTimeInMillis();
        long second = (endTime - startTime) / 1000;
        return second;
    }

    /**
     *
     * @param baseDate
     * @param latestDate
     * @return true, 代表没有过期， false，代表过期了
     */
    public static boolean compareDate(Date baseDate, Date latestDate) {
        if (baseDate.before(latestDate))
            return true;
        return false;
    }

    /**
     * 功能: 返回date1与date2相差的分钟数
     *
     * @param date1
     * @param date2
     * @return int
     */
    public static int minDiff(Date date1, Date date2) {
        int i = (int) ((date1.getTime() - date2.getTime()) / 1000 / 60);
        return i;
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     * @param date 日期
     * @param amount 数值
     * @return 计算后日期
     */
    public static Date addInteger(Date date, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }

    /**
     * 增加分钟
     * @param date
     * @param amount
     * @return
     */
    public static Date addMinute (Date date, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, amount);
        return calendar.getTime();
    }

    /**
     * 获取日期的星期。失败返回null。
     * @param date 日期
     * @return 星期
     */
    public static int getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekNumber;
    }

    public static Date formatDateByWebFormat (Date date) {
        try {
            return parse(format(date, WEB_FORMAT), WEB_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 很据当前时间获取指定日期月份的第一天，当月的以今天开始
     * @param date
     * @return
     */
    public static Date firstDayOfTheMonthFromTargetDateByToday(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatDateByWebFormat(date));
        if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
            calendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTime();
    }

    /**
     * 获取指定日期月份的第一天
     * @param date
     * @return
     */
    public static Date firstDayOfTheMonthFromTargetDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatDateByWebFormat(date));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定日期月份的最后一天
     * @param date
     * @return
     */
    public static Date lastDayOfTheMonthFromTargetDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatDateByWebFormat(date));
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }
    
    /**
     * 
     * getDaysByDate:(获取当月的日期列表 ). <br/>
     *
     * @author Daniel.Wang
     * @param date
     * @return
     * @since JDK 1.6
     */
    public static List<Date> getDaysByDate(Date date){
        Date  dateFirst = firstDayOfTheMonthFromTargetDate(date);
        Date  dateLast = lastDayOfTheMonthFromTargetDate(date);
        return betweenDays(dateFirst, dateLast);
        
    }
    /**
     * 获取两个日期之间的日期列表
     * @param fromDate
     * @param toDate
     * @return
     */
    public static List<Date> betweenDays(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        List<Date> dates = new ArrayList<Date>();
        while (from.before(to)) {
            dates.add(from.getTime());
            from.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    public static List<String> betweenMonths (Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(firstDayOfTheMonthFromTargetDate(fromDate));
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        List<String> dates = new ArrayList<String>();
        while (from.compareTo(to) <= 0) {
            dates.add(format(from.getTime(), WEB_MONTH_FORMAT));
            from.add(Calendar.MONTH, 1);
        }
        return dates;
    }
    
    public static List<String> betweenMonths (String fromDateStr, String toDateStr)  {
        Date fromDate = null;
        Date toDate = null;
        List<String> dates = new ArrayList<String>();

        try {
            fromDate = parse(fromDateStr, WEB_MONTH_FORMAT);
            toDate = parse(toDateStr, WEB_MONTH_FORMAT);
        } catch (ParseException e) {
            
            e.printStackTrace();
            
        }
        
        if(fromDate!=null && toDate!=null){
            Calendar from = Calendar.getInstance();
            from.setTime(firstDayOfTheMonthFromTargetDate(fromDate));
            Calendar to = Calendar.getInstance();
            to.setTime(toDate);

           
            while (from.compareTo(to) <= 0) {
                dates.add(format(from.getTime(), WEB_MONTH_FORMAT));
                from.add(Calendar.MONTH, 1);
            }
        }
       
        return dates;
    }
    
    public static int getMonthsCount (String fromDateStr, String toDateStr)  {
        Date fromDate = null;
        Date toDate = null;
        int count = 0;

        try {
            fromDate = parse(fromDateStr, WEB_MONTH_FORMAT);
            toDate = parse(toDateStr, WEB_MONTH_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        if(fromDate!=null && toDate!=null){
            Calendar from = Calendar.getInstance();
            from.setTime(firstDayOfTheMonthFromTargetDate(fromDate));
            Calendar to = Calendar.getInstance();
            to.setTime(toDate);
           
            while (from.compareTo(to) <= 0) {
                count++;
                from.add(Calendar.MONTH, 1);
            }
        }
       
        return count;
    }

    /**
     * 比较两个时间是否为同一天
     * @param left
     * @param right
     * @return
     */
    public static boolean isSameDate (Date left, Date right) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(left);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(right);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    /**
     * 计算两个日期相差的月数
     * @param left
     * @param right
     * @return
     */
    public static int monthDiff (Date left, Date right) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(left);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(right);

        int years = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);

        return years * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
    }

    /**
     * 在同一天内判断left是否在right之后
     * @param left
     * @param right
     * @return
     */
    public static boolean afterInDay(Date left, Date right) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(left);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(right);

        if (c1.get(Calendar.HOUR_OF_DAY) > c2.get(Calendar.HOUR_OF_DAY)) {
            return true;
        }
        if (c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY) && c1.get(Calendar.MINUTE) > c2.get(Calendar.MINUTE)) {
            return true;
        }
        if (c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
                && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)
                && c1.get(Calendar.SECOND) >= c2.get(Calendar.SECOND)) {
            return true;
        }
        return false;
    }

    /**
     * 在同一天内判断两个时间的大小
     * @param left
     * @param right
     * @return
     */
    public static int compareInDay(Date left, Date right) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(left);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(right);

        if (c1.get(Calendar.HOUR_OF_DAY) > c2.get(Calendar.HOUR_OF_DAY)) {
            return 1;
        } else if (c1.get(Calendar.HOUR_OF_DAY) < c2.get(Calendar.HOUR_OF_DAY)) {
            return -1;
        }

        if (c1.get(Calendar.MINUTE) > c2.get(Calendar.MINUTE)) {
            return 1;
        } else if (c1.get(Calendar.MINUTE) < c2.get(Calendar.MINUTE)) {
            return -1;
        }


        if (c1.get(Calendar.SECOND) > c2.get(Calendar.SECOND)) {
            return 1;
        } else if (c1.get(Calendar.SECOND) < c2.get(Calendar.SECOND)) {
            return -1;
        }

        if (c1.get(Calendar.MILLISECOND) > c2.get(Calendar.MILLISECOND)) {
            return 1;
        } else if (c1.get(Calendar.MILLISECOND) < c2.get(Calendar.MILLISECOND)) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 在同一天内判断left是否在right之前
     * @param left
     * @param right
     * @return
     */
    public static boolean beforeInDay(Date left, Date right) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(left);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(right);

        if (c1.get(Calendar.HOUR_OF_DAY) < c2.get(Calendar.HOUR_OF_DAY)) {
            return true;
        }
        if (c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY) && c1.get(Calendar.MINUTE) < c2.get(Calendar.MINUTE)) {
            return true;
        }
        if (c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
                && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)
                && c1.get(Calendar.SECOND) <= c2.get(Calendar.SECOND)) {
            return true;
        }
        return false;
    }

    /**
     * 在同一天内赋值小时，分钟
     * @param left
     * @param right
     */
    public static Date setTimeInDay(Date left, Date right) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(left);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(right);

        c1.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
        c1.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
        c1.set(Calendar.SECOND, c2.get(Calendar.SECOND));
        c1.set(Calendar.MILLISECOND, c2.get(Calendar.MILLISECOND));

        return c1.getTime();
    }
    
    /**
     * 
     * getFirstDayOfWeek:得到某年某周的第一天. <br/>
     *
     * @author Nacy
     * @param year
     * @param week
     * @return
     * @since JDK 1.6
     */
 	public static String getFirstDayOfWeek(int year, int week) {
 		Calendar cal = Calendar.getInstance();  
         //设置年份  
         cal.set(Calendar.YEAR,year);  
         //设置周  
         cal.set(Calendar.WEEK_OF_YEAR, week);  
         //设置为星期一
         cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
         //格式化日期  
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");  
         String firstDayOfWeek = sdf.format(cal.getTime()); 
         return firstDayOfWeek;
 	}
    
	/**
	 * 
	 * getLastDayOfWeek:得到某年某周的最后一天. <br/>
	 *
	 * @author Nacy
	 * @param year
	 * @param week
	 * @return
	 * @since JDK 1.6
	 */
	public static String getLastDayOfWeek(int year, int week) {
	    Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置周
        cal.set(Calendar.WEEK_OF_YEAR, week);
        //设置为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //加6天
        cal.add(Calendar.DATE, 6);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String lastDayOfWeek = sdf.format(cal.getTime());
        return lastDayOfWeek;
	}


	/**
	 * 
	 * getFirstDayOfMonth:得到指定年月的月的第一天. <br/>
	 *
	 * @author Nacy
	 * @param year
	 * @param month
	 * @return
	 * @since JDK 1.6
	 */
	public static String getFirstDayOfMonth(int year, int month) {     
        Calendar cal = Calendar.getInstance();     
        cal.set(Calendar.YEAR, year);     
        cal.set(Calendar.MONTH, month-1);  
        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
       return   new   SimpleDateFormat( "yyyy-MM-dd 00:00:00").format(cal.getTime());  
    } 
	
	public static Date getFirstDayOfMonth1(int year, int month) {     
		String strDate = getFirstDayOfMonth(year, month);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(strDate); 
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return date;
    } 
	
	/**
	 * 
	 * getLastDayOfMonth:得到指定年月的月的最后一天. <br/>
	 *
	 * @author Nacy
	 * @param year
	 * @param month
	 * @return
	 * @since JDK 1.6
	 */
	public static String getLastDayOfMonth(int year, int month) {     
        Calendar cal = Calendar.getInstance();     
        cal.set(Calendar.YEAR, year);     
        cal.set(Calendar.MONTH, month-1);     
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
       return  new SimpleDateFormat( "yyyy-MM-dd 23:59:59").format(cal.getTime());  
    } 
	
	public static Date getLastDayOfMonth1(int year, int month) {  
		String strDate = getLastDayOfMonth(year, month);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(strDate); 
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return date;
    } 
	
	public static void main(String[] args) {
		System.out.println(betweenMonths("2017-02", "2017-07"));
        Date date1 = DateUtils.beginDateByToday(-8);
        System.out.println(date1);
	}

    public static Date copy(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTime();
    }

    public static Date getLastSunday() {
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, 1);
        }

        return beginDateByDate(calendar.getTime());
    }

    public static Date getLastMonday() {
        Calendar calendar = Calendar.getInstance();

        boolean isSunday = calendar.get(Calendar.DAY_OF_WEEK) == 1;

        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.add(Calendar.DAY_OF_MONTH, -7);

        if (isSunday) {
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        }

        return beginDateByDate(calendar.getTime());
    }
}
