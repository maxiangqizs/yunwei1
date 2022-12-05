package com.neusoft.yunwei.Task;

import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TNorthLogProcessAlr;
import com.neusoft.yunwei.service.ITNorthLogProcessAlrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
/*
 北向错误日志和进程落地
 */
public class NorthLogProcessAlr extends TaskInfo{
    //记录日志工具
    @Autowired
    LogUtil logUtil;

    @Autowired
    ITNorthLogProcessAlrService itNorthLogProcessAlrService;
    @Autowired
    TNorthLogProcessAlr tNorthLogProcessAlr;
    //存储数据map
    Map<String, TNorthLogProcessAlr> alldata = new HashMap<>();
    String nowtimstr = DateUtils.checkTime();
    //当前时间的前一天
    String startTime = DateUtils.startDay();
    String endTime = DateUtils.endDay();
    //加载数据,并且插入数据库
    public void loadTODataBase(){
        for (Map.Entry<String, TNorthLogProcessAlr> entry : alldata.entrySet()) {
            TNorthLogProcessAlr tmp = entry.getValue();
            tmp.setCollectEndTime(endTime);
            tmp.setCollectStartTime(startTime);
            itNorthLogProcessAlrService.save(tmp);
        }
        //记录日志
        logUtil.toDb("OrderFailAlr","success");

    }
    //更新map
    public void loadMap( String lastIp) {
        if (alldata.containsKey(lastIp)){
            alldata.get(lastIp).setSchedulerErrorProc(tNorthLogProcessAlr.getSchedulerErrorProc());
            alldata.get(lastIp).setFerryBuilderErrorProc(tNorthLogProcessAlr.getFerryBuilderErrorProc());
            alldata.get(lastIp).setNorthFaceProc(tNorthLogProcessAlr.getNorthFaceProc());
        } else {
            TNorthLogProcessAlr tmpObject = new TNorthLogProcessAlr();
            tmpObject.setIp(tNorthLogProcessAlr.getIp());
            tmpObject.setProvince(tNorthLogProcessAlr.getProvince());
            tmpObject.setCheckTime(tNorthLogProcessAlr.getCheckTime());
            tmpObject.setSchedulerErrorProc(tNorthLogProcessAlr.getSchedulerErrorProc());
            tmpObject.setFerryBuilderErrorProc(tNorthLogProcessAlr.getFerryBuilderErrorProc());
            tmpObject.setNorthFaceProc(tNorthLogProcessAlr.getNorthFaceProc());
            alldata.put(lastIp,tmpObject);
        }
    }
    //清除上一次循环遗留内容
    public  void clear(){
        tNorthLogProcessAlr.setIp(null);
        tNorthLogProcessAlr.setProvince(null);
        tNorthLogProcessAlr.setSchedulerErrorLog(null);
        tNorthLogProcessAlr.setFerryBuilderErrorLog(null);
        tNorthLogProcessAlr.setNorthFaceLog(null);
    }
    //清除上一次循环遗留内容
    public  void clearProcess(){
        tNorthLogProcessAlr.setIp(null);
        tNorthLogProcessAlr.setProvince(null);
        tNorthLogProcessAlr.setCheckTime(null);
        tNorthLogProcessAlr.setSchedulerErrorLog(null);
        tNorthLogProcessAlr.setFerryBuilderErrorLog(null);
        tNorthLogProcessAlr.setNorthFaceLog(null);
        tNorthLogProcessAlr.setSchedulerErrorProc(null);
        tNorthLogProcessAlr.setFerryBuilderErrorProc(null);
        tNorthLogProcessAlr.setNorthFaceProc(null);
    }
    //北向日志数据
    public void loadNorthLogCheck(){
        try{
            File file=new File(ConfigDb.getInstance().getString("north.log.path"));
            BufferedReader logBR = null;
            logBR = new BufferedReader(new InputStreamReader(new FileInputStream(file )));
            String lastIp = null;
            String head = null;
            boolean flag = true;
            //第一次循环flag
            boolean firstFlag = false;
            //通过标志来判断读取哪一行
            //是否读取省份
            Boolean ifProvince = false;
            //是否读scheduler
            Boolean ifschedulerFlag = false;
            //是否读取ferry-builder
            Boolean ifFerryBuilderFlag = false;
            //是否读取north-face
            Boolean ifNorthFaceFlag = false;
            while(flag) {
                if (head == null) {
                    head = logBR.readLine();
                    if (head == null) {
                        logBR.close();
                        break;
                    }
                }
                String curIp = null;


                //是否读取到scheduler行
                Integer ifscheduler = head.indexOf("scheduler");
                //是否读取到ifFerryBuilder行
                Integer ifFerryBuilder = head.indexOf("ferry-builder");
                //是否读取到northFace行
                Integer ifNorthFace = head.indexOf("north-face");
                if (head.indexOf("rc=") > 0) {
                    if (firstFlag) {
                        TNorthLogProcessAlr tmp =new TNorthLogProcessAlr();
                        tmp.setIp(tNorthLogProcessAlr.getIp());
                        tmp.setProvince(tNorthLogProcessAlr.getProvince());
                        tmp.setCheckTime(tNorthLogProcessAlr.getCheckTime());
                        tmp.setSchedulerErrorLog(tNorthLogProcessAlr.getSchedulerErrorLog());
                        tmp.setFerryBuilderErrorLog(tNorthLogProcessAlr.getFerryBuilderErrorLog());
                        tmp.setNorthFaceLog(tNorthLogProcessAlr.getNorthFaceLog());
                        alldata.put(lastIp,tmp);
//                        itNorthLogProcessAlrService.save(tNorthLogProcessAlr);
//                        System.out.println("插入");
                    }
                    clear();
                    curIp = head.substring(0, head.indexOf("|")).trim();
                    if (curIp != null && !curIp.equals(lastIp)) {
                        lastIp = curIp;
                    }
                    tNorthLogProcessAlr.setIp(lastIp);
                    tNorthLogProcessAlr.setCheckTime(nowtimstr);
                    firstFlag = true;
                    //是否读取到省份
                    ifProvince = true;
                    //是否读scheduler
                    ifschedulerFlag = false;
                    //是否读取ferry-builder
                    ifFerryBuilderFlag = false;
                    //是否读取north-face
                    ifNorthFaceFlag = false;
                } else if (ifProvince) {
//                    head.indexOf(":");
                    tNorthLogProcessAlr.setProvince(head);
                    //读取之后改成false防止下次循环进入
                    ifProvince = false;
                } else if (ifscheduler != -1 || (ifscheduler == -1 && ifFerryBuilder == -1 && !ifFerryBuilderFlag)) {
                    String tmp = head;
                    String schduler= tNorthLogProcessAlr.getSchedulerErrorLog();
                    tNorthLogProcessAlr.setSchedulerErrorLog(tmp+schduler);
                    ifschedulerFlag = true;
                } else if (ifFerryBuilder != -1|| (ifFerryBuilder == -1 && ifNorthFace == -1 && !ifNorthFaceFlag)) {
                    String tmp1 = head;
                    String ferryBuilder= tNorthLogProcessAlr.getFerryBuilderErrorLog();
                    tNorthLogProcessAlr.setFerryBuilderErrorLog(tmp1+ferryBuilder);
                    ifFerryBuilderFlag = true;
                } else if (ifNorthFace != -1 || (ifschedulerFlag  && ifFerryBuilderFlag && ifNorthFaceFlag)) {
                    String tmp2 = head;
                    String northFace= tNorthLogProcessAlr.getNorthFaceLog();
                    tNorthLogProcessAlr.setNorthFaceLog(northFace+tmp2);
                    ifNorthFaceFlag = true;
                }
                head = logBR.readLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    //北向进程数据
    public void loadNorthProcessCheck(){
        try{
            File file=new File(ConfigDb.getInstance().getString("north.process.path"));
            BufferedReader logBR = null;
            logBR = new BufferedReader(new InputStreamReader(new FileInputStream(file )));
            String lastIp = null;
            String head = null;
            boolean flag = true;
            //第一次循环flag
            boolean firstFlag = false;
            //通过标志来判断读取哪一行
            //是否读取省份
            Boolean ifProvince = false;
            //是否读scheduler
            Boolean ifschedulerFlag = false;
            //是否读取ferry-builder
            Boolean ifFerryBuilderFlag = false;
            //是否读取north-face
            Boolean ifNorthFaceFlag = false;
            while(flag) {
                if (head == null) {
                    head = logBR.readLine();
                    if (head == null) {
                        //插入数据到map
                        loadMap(lastIp);
                        logBR.close();
                        break;
                    }
                }
                String curIp = null;

                //是否读取到scheduler行
                Integer ifScheduler = head.indexOf("scheduler");
                //是否读取到ifFerryBuilder行
                Integer ifFerryBuilder = head.indexOf("ferry-builder");
                //是否读取到northFace行
                Integer ifNorthFace = head.indexOf("north-face");
                if (head.indexOf("rc=") > 0) {
                    if (firstFlag) {
                        //插入数据到map
                        loadMap(lastIp);
//                        itNorthLogProcessAlrService.save(tNorthLogProcessAlr);
//                        System.out.println("插入");
                    }
                    clearProcess();
                    curIp = head.substring(0, head.indexOf("|")).trim();
                    if (curIp != null && !curIp.equals(lastIp)) {
                        lastIp = curIp;
                    }
                    tNorthLogProcessAlr.setIp(lastIp);
                    tNorthLogProcessAlr.setCheckTime(nowtimstr);
                    firstFlag = true;
                    //是否读取到省份
                    ifProvince = true;
                    //是否读scheduler
                    ifschedulerFlag = false;
                    //是否读取ferry-builder
                    ifFerryBuilderFlag = false;
                    //是否读取north-face
                    ifNorthFaceFlag = false;
                } else if (ifProvince) {
                    System.out.println(tNorthLogProcessAlr);
                    tNorthLogProcessAlr.setProvince(head);
                    //读取之后改成false防止下次循环进入
                    ifProvince = false;
                } else if (ifScheduler != -1 || (ifScheduler == -1 && ifFerryBuilder == -1 && !ifFerryBuilderFlag)) {
                    String tmp = head;
                    String schduler= tNorthLogProcessAlr.getSchedulerErrorProc();
                    tNorthLogProcessAlr.setSchedulerErrorProc(tmp+schduler);
                    ifschedulerFlag = true;
                } else if (ifFerryBuilder != -1|| (ifFerryBuilder == -1 && ifNorthFace == -1 && !ifNorthFaceFlag)) {
                    String tmp1 = head;
                    String ferryBuilder= tNorthLogProcessAlr.getFerryBuilderErrorProc();
                    tNorthLogProcessAlr.setFerryBuilderErrorProc(tmp1+ferryBuilder);
                    ifFerryBuilderFlag = true;
                } else if (ifNorthFace != -1 || (ifschedulerFlag  && ifFerryBuilderFlag && ifNorthFaceFlag)) {
                    String tmp2 = head;
                    String northFace= tNorthLogProcessAlr.getNorthFaceProc();
                    tNorthLogProcessAlr.setNorthFaceProc(northFace+tmp2);
                    ifNorthFaceFlag = true;
                }
                head = logBR.readLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void excuteTask(){
        //北向日志数据
        loadNorthLogCheck();
        //北向进程数据
        loadNorthProcessCheck();
        //写入数据库
        loadTODataBase();
    }

}
