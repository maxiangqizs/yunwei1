package com.neusoft.yunwei.Utils;
/*
 功能:记录程序预警落地日志
 author:mxq
 */


import com.neusoft.yunwei.pojo.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class logUtil {
    private static Logger logger = LoggerFactory.getLogger(logUtil.class);
    public Test test;
    Map<String, Test> alldata = new HashMap<>();
    public static void main(String[] args) {
//        for(int i = 0 ; i < 3 ; i++){
//            logger.info("logback So"+i);
//            logger.error("logback Down");
//            logger.debug("debug");
//        }
//
//        logger.info("logback So44");
        Map<Integer, Test> alldata = new HashMap<>();

        for(int i = 0 ; i < 2 ; i++){
            TreeSet treeSet = new TreeSet();
            treeSet.add(i);
            Test test = new Test();
            test.setTreeSet(treeSet);
            alldata.put(i,test);
            System.out.println("----------------"+alldata);
        }


    }

}
