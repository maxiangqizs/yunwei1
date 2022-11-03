package com.neusoft.yunwei.Utils;
/*
 功能:记录程序预警落地日志
 author:mxq
 */


import com.neusoft.yunwei.pojo.TLog;
import com.neusoft.yunwei.service.ITLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogUtil {
    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);
    @Autowired
    TLog tLog;
    @Autowired
    ITLogService itLogService;
    //插入数据库日志表
     public void toDb(String service,String status) {
         tLog.setService( service);
         tLog.setStatus(status);
         tLog.setUpdateTime(DateUtils.today());
         itLogService.save(tLog);
         logger.info("功能:" +service+"成功插入日志表");
     }
     public static void main(String[] args) {
         LogUtil ll = new LogUtil();
         ll.toDb("Test","success");
     }

}
