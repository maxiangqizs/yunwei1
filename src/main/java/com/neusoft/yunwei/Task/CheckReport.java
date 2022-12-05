package com.neusoft.yunwei.Task;

import com.neusoft.yunwei.Config.AppConfiguration;
import com.neusoft.yunwei.Config.DataCache;
import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TDiskUseRatioAlr;
import com.neusoft.yunwei.service.ITDiskUseRatioAlrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * 磁盘告警
 */


@RestController
@RequestMapping("/ChexkReport")
@Slf4j
@Component
@RequiredArgsConstructor
public class CheckReport extends TaskInfo {
    public String provice;
    public String cluster;
    public String diskName;
    public String diskUsage;
    public String time;

    //记录日志工具
    @Autowired
    LogUtil logUtil;
    @Autowired
    public AppConfiguration appConfiguration;

    @Autowired
    DataCache dataCache;
    @Autowired
    ITDiskUseRatioAlrService iTDiskUseRatioAlrService;
    @Autowired
    TDiskUseRatioAlr tDiskUseRatioAlr;

    @Override
    public void excuteTask(){
        try{
        File file=new File(ConfigDb.getInstance().getString("file.disk.path"));
       boolean _printError = false;
       InputStream log = null;
       BufferedReader logBR = null;
       String[] args = new String[]{"0"};
       /*log = new FileInputStream(args[0]);*/
       logBR = new BufferedReader(new InputStreamReader(new FileInputStream(file )));
       String lastIp = null;
       String head = null;
       boolean printFlag = false;

       while(true) {
           if (head == null) {
               head = logBR.readLine();
               if (head == null) {
                   logBR.close();
                   break;
               }
           }

           String tmpstr = null;
           String curIp = null;
           if (head.indexOf("rc=") > 0) {
               curIp = head.substring(0, head.indexOf("|")).trim();
               if (curIp != null && !curIp.equals(lastIp)) {
                   lastIp = curIp;
                   printFlag = true;
               } else {
                   printFlag = false;
               }
           } else {
               int tmp2 = head.lastIndexOf("%");
               if (head.indexOf("/") != -1 && head.indexOf("mnt") == -1) {
                   tmpstr = head.substring(tmp2 - 3, tmp2).trim();
                   if (tmpstr != null && Integer.parseInt(tmpstr) > 70) {
                       if (printFlag) {
                           System.out.println(lastIp);
                           printFlag = false;
                       }
                       System.out.println(tmpstr + "% " + head.substring(head.lastIndexOf("/"), head.length()));

                       if (dataCache.dataMaper.containsKey(lastIp)){
                           provice = dataCache.dataMaper.get(lastIp).getProvince();
                           cluster = dataCache.dataMaper.get(lastIp).getCluster();
                       } else {
                           provice ="未知省份";
                           cluster = "";
                       }
                       diskName = head.substring(head.lastIndexOf("/"), head.length());
                       diskUsage = tmpstr + "%";
                       time = DateUtils.checkTime();
                       tDiskUseRatioAlr.setCluster(cluster);
                       tDiskUseRatioAlr.setProvince(provice);
                       tDiskUseRatioAlr.setIp(lastIp);
                       tDiskUseRatioAlr.setDiskName(diskName);
                       tDiskUseRatioAlr.setDiskUse(diskUsage);
                       tDiskUseRatioAlr.setCheckTime(time);
                       tDiskUseRatioAlr.setCollectEndTime(DateUtils.endDay());
                       tDiskUseRatioAlr.setCollectStartTime(DateUtils.startDay());
                       iTDiskUseRatioAlrService.save(tDiskUseRatioAlr);
                   }
               }

           }

           head = logBR.readLine();
       }}catch(Exception e){
            e.printStackTrace();
        }
        logUtil.toDb("CheckReport","success");

   }



}



