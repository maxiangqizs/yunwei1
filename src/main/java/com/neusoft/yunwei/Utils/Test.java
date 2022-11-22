package com.neusoft.yunwei.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void main(String[] args) {
        String startDay = LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"000000";
        String endDay = LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"240000";
        String endDay1 = LocalDateTime.now().plusDays(-2).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"240000";
        String aa = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        System.out.println(startDay + "::::" + endDay1+":::::"+aa);
    }
}
