package com.neusoft.yunwei.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Component
public class Test {

    @Autowired
    public static void main(String[] args) {
        String startDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"000000";
        String endDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"240000";
        String endDay1 = LocalDateTime.now().plusDays(-2).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"240000";
        String aa = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        System.out.println(startDay + "::::" + endDay1+":::::"+aa);
        Test test = new Test();
    }
}
