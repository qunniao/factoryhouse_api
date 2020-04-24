package com.baoge.Util;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DateUtil {
    public static String current(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }

    public static Date parseDate(String date, String format) {
        if (StringUtils.isNotEmpty(date)) {
            try {
                return new SimpleDateFormat(format).parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Timestamp getDateToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Date getTimestampToDate(Timestamp tamp) {
        Date date = new Date();
        date = tamp;
        return date;
    }

    public static String current() {
        return current("yyyyMMddHHmmss");
    }

    public static String format(String date, String sourceFormat, String transFormat) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }

        DateFormat _formater1 = new SimpleDateFormat(sourceFormat);
        DateFormat _formater2 = new SimpleDateFormat(transFormat);
        try {
            return _formater2.format(_formater1.parse(date));
        } catch (ParseException pe) {
        }
        return date;
    }

    public static String formatMedium(String date, String transFormat) {
        return format(date, "yyyyMMddHHmmss", transFormat);
    }

    public static String formatShort(String date) {
        return formatMedium(date, "yyyy年MM月dd日");
    }

    public static String format(String date) {
        return formatMedium(date, "yyyy-MM-dd");
    }

    public static String getDateToDateString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);
    }

    public static boolean isAM(Date date) {
        boolean isTrue = true;

        DateFormat df = new SimpleDateFormat("HH");
        try {
            int integerHour = Integer.parseInt(df.format(date));

            if ((integerHour >= 0) && (integerHour <= 12)) {
                isTrue = true;
            } else {
                isTrue = false;
            }
        } catch (NumberFormatException nfe) {
            nfe.getStackTrace();
        }

        return isTrue;
    }

    public static boolean isAM() {
        return isAM(new Date());
    }

    public static Date getFirstDay(Date theDate) {
        String temStr = getDateToDateString(theDate, "yyyy-MM") + "-01";
        return parseDate(temStr, "yyyy-MM-dd");
    }

    public static Date getLastDay(Date theDate) {
        String temStr = getDateToDateString(theDate, "yyyy-MM-dd") + " 23:59:59";
        Date tem = parseDate(temStr, "yyyy-MM-dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.setTime(tem);
        cal.set(5, 1);
        cal.roll(5, -1);
        return cal.getTime();
    }

    public static List<Date> calStartAndEndTime(String kind, Date reportDate) {
        Date endDate = new Date();
        Date startDate = new Date();
        List<Date> res = new ArrayList();
        if (kind.equals("天")) {
            String temStr = getDateToDateString(reportDate, "yyyy-MM-dd");
            startDate = parseDate(temStr, "yyyy-MM-dd");

            temStr = getDateToDateString(reportDate, "yyyy-MM-dd") + " 23:59:59";
            endDate = parseDate(temStr, "yyyy-MM-dd HH:mm:ss");
        } else if (kind.equals("月")) {
            startDate = getFirstDay(reportDate);
            endDate = getLastDay(reportDate);
        } else if (kind.equals("季度")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reportDate);
            int monthIndex = calendar.get(2);
            int jd = monthIndex / 3;
            int startMonth = jd * 3;
            int endMonth = startMonth + 2;

            calendar.set(2, startMonth);
            Date start = calendar.getTime();

            calendar.set(2, endMonth);
            Date end = calendar.getTime();

            startDate = getFirstDay(start);
            endDate = getLastDay(end);
        } else if (kind.equals("半年")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reportDate);
            int monthIndex = calendar.get(2);
            int jd = monthIndex / 6;
            int startMonth = jd * 6;
            int endMonth = startMonth + 5;

            calendar.set(2, startMonth);
            Date start = calendar.getTime();

            calendar.set(2, endMonth);
            Date end = calendar.getTime();

            startDate = getFirstDay(start);
            endDate = getLastDay(end);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reportDate);
            int monthIndex = calendar.get(2);
            int jd = monthIndex / 12;
            int startMonth = jd * 12;
            int endMonth = startMonth + 11;

            calendar.set(2, startMonth);
            Date start = calendar.getTime();

            calendar.set(2, endMonth);
            Date end = calendar.getTime();

            startDate = getFirstDay(start);
            endDate = getLastDay(end);
        }
        res.add(startDate);
        res.add(endDate);

        return res;
    }

    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(current("yyMMddHHmmss"));
        isAM();

        Date theDate = parseDate("2014-6-3 12:56:07", "yyyy-MM-dd HH:mm:ss");

        List<Date> res1 = calStartAndEndTime("天", theDate);
        System.out.println(res1);
        List<Date> res2 = calStartAndEndTime("月", theDate);

        List<Date> res3 = calStartAndEndTime("季度", theDate);
        List<Date> res4 = calStartAndEndTime("半年", theDate);
        List<Date> res5 = calStartAndEndTime("一年", theDate);

        System.out.println(res2);
        System.out.println(res3);
        System.out.println(res4);
        System.out.println(res5);

        System.out.println(getDateToTimestamp(getFirstDay(new Date())));

        System.out.println(getDateToTimestamp(new Date()));

        System.out.println(getDateToDateString(new Date(), "yyyy-MM-dd"));
    }
}
