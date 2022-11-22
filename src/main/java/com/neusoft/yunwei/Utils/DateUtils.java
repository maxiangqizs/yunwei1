package com.neusoft.yunwei.Utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    //获取当前时间的前一天       
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    
    public static String lastday(){
        /*String back="";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        Date date2 = cal.getTime();
        String dateStringYYYYMMDD2 = DATE_FORMAT.format(date2);
        back = dateStringYYYYMMDD2;*/
        String startDay = LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"000000";
        return startDay;
    }

    public static String lasttwoday(){
        String back="";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -2);
        Date date2 = cal.getTime();
        String dateStringYYYYMMDD2 = DATE_FORMAT.format(date2);
        back = dateStringYYYYMMDD2;
        return back;
    }

    //获取当前时间的后一天
    public static String before(){
        String back="";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Date date2 = cal.getTime();
        String dateStringYYYYMMDD2 = DATE_FORMAT.format(date2);
        back = dateStringYYYYMMDD2;
        return back;
    }
    public static String lasthours(){
        String back="";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -4);
        Date date2 = cal.getTime();
        String dateStringYYYYMMDD2 = DATE_FORMAT.format(date2);
        back = dateStringYYYYMMDD2;
        return back;
    }
    public static String today(){
        String endDay = LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"240000";
        return endDay;
    }
    public static String startDay(){
        String startDay = LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"000000";
        return startDay;
    }
    public static String endDay(){
        String endDay = LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"240000";
        return endDay;
    }
    public static String nowTime(){
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        return nowTime;
    }
    public static String checkTime(){
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        return nowTime;
    }
}
