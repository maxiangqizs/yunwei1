package com.neusoft.yunwei.Utils;
/*
 功能:记录程序预警落地日志
 author:mxq
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeSet;

public class logUtil {
    private static Logger logger = LoggerFactory.getLogger(logUtil.class);

    public static void main(String[] args) {
//        for(int i = 0 ; i < 3 ; i++){
//            logger.info("logback So"+i);
//            logger.error("logback Down");
//            logger.debug("debug");
//        }
//
//        logger.info("logback So44");
        TreeSet treeSet = new TreeSet();
        treeSet.add(250);

        treeSet.add(50);

        treeSet.add(100);

        treeSet.add(150);

        treeSet.add(200);

        treeSet.add(300);

        treeSet.add(4000);

        treeSet.add(500);

        treeSet.add(800);

        treeSet.add(1000);

        System.out.println("TreeSet Lowest value = " + treeSet.first());

        System.out.println("TreeSet Highest value = " + treeSet.last());


    }

}
