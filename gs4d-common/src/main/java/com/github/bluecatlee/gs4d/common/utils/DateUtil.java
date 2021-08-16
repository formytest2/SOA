package com.github.bluecatlee.gs4d.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化日期字符串 默认yyyy-MM-dd格式
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr) throws ParseException {
        return parse(dateStr, DEFAULT_PATTERN);
    }

    /**
     * 格式化日期字符串
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr, String pattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.parse(dateStr);
    }

    /**
     * 格式化日期 默认yyyy-MM-dd格式
     * @param date
     * @return
     */
    public static String format(Date date) {
        DateFormat df = new SimpleDateFormat(DEFAULT_PATTERN);
        return df.format(date);
    }

    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 把数字日期转换成其他日期格式
     * @param dayInt 数字 yyyyMMdd
     * @param split  分割符
     * @return
     */
    public static String getDateFromInt(Integer dayInt, String split) {
        String day = dayInt.toString();
        String str = String.format("%s%s%s%s%s", day.substring(0, 4), split, day.substring(4, 6), split, day.substring(6, 8));
        return str;
    }

    /**
     * 把数字日期转换成其他日期格式(年月)
     * @param dayInt 数字 yyyyMMdd
     * @param split  分割符
     * @return
     */
    public static String getYearMonthFromInt(Integer dayInt, String split) {
        String day = dayInt.toString();
        String str = String.format("%s%s%s", day.substring(0, 4), split, day.substring(4, 6));
        return str;
    }

    /**
     * 获取今日的日期格式 默认yyyy-MM-dd格式
     * @return
     */
    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(today);
    }

    /**
     * 获取今日的日期格式
     * @param dateFormat
     * @return
     */
    public static String getToday(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        return fmt.format(today);
    }

    /**
     * 获取明日的日期格式 默认yyyy-MM-dd格式
     * @return
     */
    public static String getTomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        Date tomorrow = cal.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat(DEFAULT_PATTERN);
        return fmt.format(tomorrow);
    }

    /**
     * 获取明日的日期格式
     * @param pattern
     * @return
     */
    public static String getTomorrow(String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        Date tomorrow = cal.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.format(tomorrow);
    }

    /**
     * 获取指定日期下一日的日期
     * @param day
     * @param pattern 格式必须与指定日期的格式一致 否则结果将不正确甚至异常
     * @return
     * @throws ParseException
     */
    public static String getNextDayFormat(String day, String pattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(pattern);
        Date d = df.parse(day);
        Date next = getAddDayDate(d,1);
        return format(next, pattern);
    }

    /**
     * 把时间格式化成int(yyyyMMdd)
     * @param dt
     * @return
     */
    public static Integer formatDateToInt(Date dt) {
        if (dt == null) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String day = fmt.format(dt);
        return Integer.parseInt(day);
    }

    /**
     * 格式化成日期时间
     * @param dt
     * @return
     */
    public static String formatDateTime(Date dt) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(dt);
    }

//    public static String formatDateYMDHM(Date dt) {
//        SimpleDateFormat fmt = new SimpleDateFormat("yy年M月d日 HH:mm");
//        return fmt.format(dt);
//    }

    @SuppressWarnings("static-access")
    @Deprecated
    public static String getPeriodDate(int intervalDay) {
        Calendar cal = Calendar.getInstance();
        // 1-intervalDay表示包含今天的间隔
        cal.add(cal.DAY_OF_MONTH, 1 - intervalDay);
        Date date = cal.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat(DEFAULT_PATTERN);
        return fmt.format(date);
    }

    @Deprecated
    public static String getPeriodDateByMonth(int intervalMonth){
        Calendar cal = Calendar.getInstance();
        // 1-intervalMonth
        cal.add(Calendar.MONTH,  - intervalMonth);
        Date date = cal.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat(DEFAULT_PATTERN);
        return fmt.format(date);
    }

    /**
     * 获取n天后的日期 时间部分为0
     * @param dt
     * @param days
     * @return
     */
    public static Date getAddDayDate(Date dt, int days) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * java.util.Date装换为java.sql.Date   java.sql.Date只能精确到天
     * @param dt
     * @return java.sql.Date
     */
    public static java.sql.Date toSqlDate(Date dt) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * Date类型转为java.sql.Timestamp
     * @param dt
     * @return Date
     */
    public static java.sql.Timestamp dateToTimestamp(Date dt) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return new java.sql.Timestamp(cal.getTimeInMillis());
    }

    /**
     * 获取传入日期所在月的第一天的日期
     * @param dt
     * @return Date
     */
    public static Date getMonthBeginTime(Date dt) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取传入日期所在月的最后一天的日期 时间是23:59:59
     * @param dt
     * @return Date
     */
    public static Date getMonthEndTime(Date dt) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    /**
     * 获得传入日期的年\月\日,以整型数组方式返回
     * @param dt
     * @return int[]
     */
    public static int[] getYMDArray(Date dt) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        int[] timeArray = new int[3];
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        timeArray[0] = cal.get(Calendar.YEAR);
        timeArray[1] = cal.get(Calendar.MONTH) + 1;
        timeArray[2] = cal.get(Calendar.DAY_OF_MONTH);
        return timeArray;
    }

    /**
     * 根据年月日得到Date类型时间
     * @param year
     * @param month
     * @param day
     * @return Date
     */
    public static Date getDate(Integer year, Integer month, Integer day) {
        Calendar cal = Calendar.getInstance();
        if (year != null) {
            cal.set(Calendar.YEAR, year);
        }
        if (month != null) {
            cal.set(Calendar.MONTH, month - 1);
        }
        if (day != null) {
            cal.set(Calendar.DAY_OF_MONTH, day);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到两个时间相差多少天，不足一天算一天
     * @param beginDate
     * @param endDate
     * @return
     */
    @Deprecated
    public static int getDateMargin(Date beginDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        final int mOfDay = 1000 * 60 * 60 * 24;
        final long divtime = (endDate.getTime() - beginDate.getTime());
        final long lday = divtime % mOfDay > 0 ? divtime / mOfDay + 1 : divtime / mOfDay;
        return Long.valueOf(lday).intValue();
    }

    /**
     * 得到当前时间和指定时间的小时差,不足一小时按一小时算
     * @param beginDate
     * @param endDate
     * @return
     */
    @Deprecated
    public static long getHourMargin(Date beginDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        long hour = endDate.getTime() - beginDate.getTime();
        hour = hour % (60 * 60 * 1000) > 0 ? hour / (60 * 60 * 1000) + 1 : hour / (60 * 60 * 1000);
        return hour;
    }

    /**
     * 得到两个时间相差多少分钟,不足一分钟按一分钟算
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getMinuteMargin(Date beginDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        final int mOfMinute = 1000 * 60;
        final long divtime = (endDate.getTime() - beginDate.getTime());
        final long lminute = divtime % mOfMinute > 0 ? divtime / mOfMinute + 1 : divtime / mOfMinute;
        return Long.valueOf(lminute).intValue();
    }

    /**
     * 获取指定月数后的时间
     * @param dt
     * @param month
     * @return
     */
    public static Date getAddMonthDate(Date dt, int month) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 获取指定小时后的时间
     * @param dt
     * @param hours
     * @return
     */
    public static Date getAddHourDate(Date dt, int hours) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + hours);
        return cal.getTime();
    }

    /**
     * 获取指定分钟后的时间
     * @param dt
     * @param minute
     * @return
     */
    public static Date getAddMinuteDate(Date dt, int minute) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 日期加上几年
     * @param date 当前日期
     * @param i 加几年
     * @return
     */
    public static Date dateAddYear(Date date, int i) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.YEAR, i);
        Date dt1 = rightNow.getTime();
        return dt1;
    }

    /**
     * 获取当前年份的周数
     * @param year
     * @return
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekOfYear(c.getTime());
    }

    /**
     * 获取当前时间属于所在年的第几周
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取某年的第几周的开始日期
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);
        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 获取某年的第几周的结束日期
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);
        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 获取当前时间所在周的开始日期
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 获取当前时间所在周的结束日期
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 通过生日得到当前年龄
     * @param birthDay
     * @return
     */
    public static String getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (birthDay == null) {
            return "未知";
        }
        Date birthDate = birthDay;
        if (cal.before(birthDate)) {
            throw new IllegalArgumentException("The birthDay is before Now. It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDate);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                }
            } else {
                age--;
            }
        } else {
        }
        return age + "岁";
    }

    /**
     * 通过生日得到当前年龄
     * @param birthDay
     * @return
     */
    public static int getAgeForInt(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (birthDay == null) {
            return 0;
        }
        Date birthDate = birthDay;
        if (cal.before(birthDate)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDate);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                }
            } else {
                age--;
            }
        } else {
        }
        return age;
    }

}
