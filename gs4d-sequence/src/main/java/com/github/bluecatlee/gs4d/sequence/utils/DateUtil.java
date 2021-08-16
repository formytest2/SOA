package com.github.bluecatlee.gs4d.sequence.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static final long second = 1000L;

    public static final long minute = 60000L;

    public static final long hour = 3600000L;

    public static final long day = 86400000L;

    public static final FastDateFormat ymdFormatter = FastDateFormat.getInstance("yyyy-MM-dd");

    public static final FastDateFormat ymdhmsFormatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static final FastDateFormat ymdhmsSFormatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss:SSS");

    public static final String al = "yyyyMMdd";

    public static final String am = "yyyy-MM-dd";

    public static String an = "yyyy-MM-dd HH:mm:ss";

    public static final String ao = "yyyy-MM-dd HH:mm:ss:SSS";

    public static Date parse(String date) {
        return parse(date, "yyyy-MM-dd");
    }

    public static Date parse(String date, String format) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format); // SimpleDateFormat是线程不安全的
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException parseException) {
            throw new IllegalArgumentException("Can't parse " + date + " using " + format);
        }
    }

    public static String safeParseCurrent(String format) {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(format);
        return fastDateFormat.format(new Date());
    }

    public static String safeParse(Date date, String format) {
        if (date == null) {
            return null;
        }
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(format); // FastDateFormat是线程安全的
        return fastDateFormat.format(date);
    }

    public static String getDateAsString(Date date) {
        return (date == null) ? null : ymdFormatter.format(date);
    }

    public static String getCurrentDateAsString() {
        return ymdFormatter.format(new Date());
    }

    public static String getCurrentDateTimeAsString() {
        return ymdhmsFormatter.format(new Date());
    }

    public static Date getStartDateTimeOfCurrentMonth() {
        return getStartDateTimeOfCurrentMonth(new Date());
    }

    public static Date getStartDateTimeOfCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndDateTimeOfCurrentMonth() {
        return getEndDateTimeOfCurrentMonth(new Date());
    }

    public static Date getEndDateTimeOfCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date getStartTimeOfCurrentDate() {
        return getStartTimeOfCurrentDate(new Date());
    }

    public static Date getStartTimeOfCurrentDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndTimeOfCurrentDate() {
        return getEndTimeOfCurrentDate(new Date());
    }

    public static Date getEndTimeOfCurrentDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date addYear(Date date, int years) {
        return add(date, Calendar.YEAR, years);
    }

    public static Date addMonth(Date date, int months) {
        return add(date, Calendar.MONTH, months);
    }

    public static Date addDay(Date date, int days) {
        return add(date, Calendar.DATE, days);
    }

    public static Date addHour(Date date, int hours) {
        return add(date, Calendar.HOUR_OF_DAY, hours);
    }

    public static Date addMinute(Date date, int minutes) {
        return add(date, Calendar.MINUTE, minutes);
    }

    public static Date addSecond(Date date, int seconds) {
        return add(date, Calendar.SECOND, seconds);
    }

    private static Date add(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static final int getIntervalDays(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        long l1 = calendar1.getTimeInMillis();
        long l2 = calendar2.getTimeInMillis();
        return (int)((l2 - l1) / day);
    }

    public static final int getIntervalMinutes(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        long l1 = calendar1.getTimeInMillis();
        long l2 = calendar2.getTimeInMillis();
        return (int)((l2 - l1) / minute);
    }

    public static String getNow() {
        String str = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Calendar calendar = Calendar.getInstance();
            str = simpleDateFormat.format(calendar.getTime());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return str;
    }

    public static boolean isDateStr(String date) {
        if (date.length() > 10) {
            date = date.substring(1);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            simpleDateFormat.parse(date);
            return true;
        } catch (ParseException parseException) {
            return false;
        }
    }

//    public static boolean j(String date) {
//        boolean bool = false;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            if (date.equals(simpleDateFormat.format(simpleDateFormat.parse(date)))) {
//                bool = true;
//            }
//        } catch (ParseException parseException) {
//        }
//        return bool;
//    }

    public static boolean before(Date date1, Date date2) {
        return date1.before(date2);
    }

//    public static String[] a(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3) {
//        if (paramString1 == null || paramString1.trim().equals("") || paramString2 == null || paramString2.trim().equals("")) {
//            return new String[0];
//        }
//        Date date1 = parse(paramString1, "yyyy-MM-dd");
//        Date date2 = parse(paramString2, "yyyy-MM-dd");
//        if (date1.after(date2)) {
//            return new String[0];
//        }
//        ArrayList<String> arrayList = new ArrayList();
//        int i = Long.valueOf((date2.getTime() - date1.getTime()) / day).intValue();
//        for (byte b = 0; b <= i; b++) {
//            Date date = addDay(date1, b);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            int j = calendar.get(Calendar.DAY_OF_WEEK);
//            int k = calendar.get(Calendar.WEEK_OF_MONTH);
//            int m = calendar.get(Calendar.MONTH);
//            if (paramInt1 == -1)
//                if (paramInt2 == 0) {
//                    if (paramInt3 == j)
//                        arrayList.add(safeParse(date, "yyyy-MM-dd"));
//                } else if (paramInt2 == k) {
//                    if (paramInt3 == 0) {
//                        arrayList.add(safeParse(date, "yyyy-MM-dd"));
//                    } else if (paramInt3 == j) {
//                        arrayList.add(safeParse(date, "yyyy-MM-dd"));
//                    }
//                }
//            if (paramInt1 != -1 && paramInt1 == m)
//                if (paramInt2 == 0) {
//                    if (paramInt3 == j) {
//                        arrayList.add(safeParse(date, "yyyy-MM-dd"));
//                    } else if (paramInt3 == 0) {
//                        arrayList.add(safeParse(date, "yyyy-MM-dd"));
//                    }
//                } else if (paramInt2 == k) {
//                    if (paramInt3 == 0) {
//                        arrayList.add(safeParse(date, "yyyy-MM-dd"));
//                    } else if (paramInt3 == j) {
//                        arrayList.add(safeParse(date, "yyyy-MM-dd"));
//                    }
//                }
//        }
//        return arrayList.<String>toArray(new String[arrayList.size()]);
//    }

    public static String ago(int days) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -days);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Date getLastDatetTimeOfMonth(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(date.substring(0, 4)).intValue());
        calendar.set(Calendar.MONTH, Integer.valueOf(date.substring(5)).intValue());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

}

