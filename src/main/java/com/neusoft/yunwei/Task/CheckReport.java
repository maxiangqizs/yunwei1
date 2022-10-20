package com.neusoft.yunwei.Task;

import com.neusoft.yunwei.Config.AppConfiguration;
import com.neusoft.yunwei.pojo.CodeTable;
import com.neusoft.yunwei.pojo.DiskStatusAlarmTable;
import com.neusoft.yunwei.service.ICodeTableService;
import com.neusoft.yunwei.service.IDiskStatusAlarmTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
    @Autowired
    public AppConfiguration appConfiguration;

    @Autowired
    ICodeTableService codeTableService;
    @Autowired
    IDiskStatusAlarmTableService diskStatusAlarmTableService;
    @Autowired
    DiskStatusAlarmTable diskStatusAlarmTable;
  /*  @RequestMapping("/urlResut")
    @ResponseBody*/
    public void excuteTask(){
        try{
        File file=new File("E:/bigdata/data/distUse.txt");
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
                   return;
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
                       CodeTable codeTable = codeTableService.selectByIp(lastIp);
                       provice = codeTable.getProvice();
                       cluster = codeTable.getCluster();
                       diskName = head.substring(head.lastIndexOf("/"), head.length());
                       diskUsage = tmpstr + "%";
                       time = format.format(new Date());
                       diskStatusAlarmTable.setCluster(cluster);
                       diskStatusAlarmTable.setProvince(provice);
                       diskStatusAlarmTable.setIp(lastIp);
                       diskStatusAlarmTable.setDiskName(diskName);
                       diskStatusAlarmTable.setDiskUsage(diskUsage);
                       diskStatusAlarmTable.setPatrolTime(time);
                       int count = diskStatusAlarmTableService.insterByIp(diskStatusAlarmTable);
                       System.out.println("成功插入" + count + "条");
                   }
               }

           }

           head = logBR.readLine();
       }}catch(Exception e){
            e.printStackTrace();
        }
   }


}



