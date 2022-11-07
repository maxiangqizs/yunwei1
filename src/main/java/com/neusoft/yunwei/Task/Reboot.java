package com.neusoft.yunwei.Task;

import com.neusoft.yunwei.Config.DataCache;
import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TServerStatusInd;
import com.neusoft.yunwei.service.ITServerStatusIndService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
/*
功能:设备重启和负载均衡
 */
public class Reboot extends TaskInfo {
    //存储数据map
    Map<String, TServerStatusInd> alldata = new HashMap<>();

    //缓存省份数据
    @Autowired
    DataCache dataCache;

    @Autowired
    ITServerStatusIndService iTServerStatusIndService;

    //记录日志工具
    @Autowired
    LogUtil logUtil;

    //存储ip和对应的硬盘使用率
    Map<String, TreeSet<Integer>> diskdata = new HashMap<>();

    //排序用来取磁盘的最大值和最小值
    TreeSet<Integer> treeSet ;

    //把数据装载到alldata的tServerStatusInd当中
    //最后通过alldata统一插入
    //负载均衡数据输出到map
    public void loadMap(){
        //遍历diskdata放入alldata
        for (Map.Entry<String, TreeSet<Integer>> entry : diskdata.entrySet()) {
            //服务器状态表
            TServerStatusInd tServerStatusInd = new TServerStatusInd();

            Integer intdisk;
            String diskUsage = new String();
            intdisk = entry.getValue().last()- entry.getValue().first();
            diskUsage = intdisk.toString() + "%";
            String ip = entry.getKey().toString();

            if (alldata.containsKey(ip)) {
                alldata.get(ip).setLoadBalance(diskUsage);
            } else {
                tServerStatusInd.setLoadBalance(diskUsage);
                alldata.put(ip,tServerStatusInd);
            }

        }
    }
    //把数据装载到alldata的tServerStatusInd当中
    //最后通过alldata统一插入
    //设备重启 数据输出到map
    public void loadRootMap(String ip, String tmpTime){
        //把数据装载到alldata的tServerStatusInd当中
        //最后通过alldata统一插入
        if (alldata.containsKey(ip)) {
            alldata.get(ip).setIfServerReset("1");
            alldata.get(ip).setResetTime(tmpTime);
        } else {
            TServerStatusInd tServerStatusInd = new TServerStatusInd();
            tServerStatusInd.setIfServerReset("1");
            tServerStatusInd.setResetTime(tmpTime);
            alldata.put(ip,tServerStatusInd);
        }
    }

    //负载均衡
    public void loadBalance(){
        try{
            File file=new File(ConfigDb.getInstance().getString("file.disk.path"));
            BufferedReader logBR = null;
            logBR = new BufferedReader(new InputStreamReader(new FileInputStream(file )));
            String lastIp = null;
            String head = null;
            boolean flag = true;
            while(flag) {
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
                    treeSet= new TreeSet();
                    if (curIp != null && !curIp.equals(lastIp)) {
                        lastIp = curIp;
                    }

                } else {
                    int tmp2 = head.lastIndexOf("%");
                    if (head.indexOf("/") != -1 && head.indexOf("mnt") == -1) {
                        tmpstr = head.substring(tmp2 - 3, tmp2).trim();
                        //key:每个服务器地址 value:是通过list存储每一个服务器的IP的所有磁盘使用率
                        if (tmpstr != null) {
                            if (diskdata.containsKey(lastIp)) {
                                diskdata.get(lastIp).add(Integer.parseInt(tmpstr));
                            } else {
                                treeSet.add(Integer.parseInt(tmpstr));
                                diskdata.put(lastIp,treeSet);
                            }
                        }
                    }

                }
                head = logBR.readLine();
            }
            loadMap();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //设备重启
    public void Reboot(){
        try{
            File file=new File(ConfigDb.getInstance().getString("file.root.path"));
            Calendar calendar = Calendar.getInstance();
            calendar.set(5, calendar.get(5) - 1);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            Long dBefor = calendar.getTimeInMillis();
            new Date(dBefor);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            new HashMap();
            String[] args = new String[]{"0"};
            BufferedReader logBR = new BufferedReader(new InputStreamReader(new FileInputStream(file )));
            String ip = "";
            String head = null;
            boolean isIpPrint = false;
            Long tmp = 0L;
            System.out.println("准备开始");
            boolean flag = true;
            while(flag) {
                if (head == null) {
                    head = logBR.readLine();
                    if (head == null) {
                        logBR.close();
                        break;
                    }
                }
                if (head.indexOf("rc=") > 0) {
                    ip = head.substring(0, head.indexOf("|")).trim();
                    isIpPrint = false;
                } else {

                    if (!isIpPrint) {
                        if (head != null && !"".equals(head)) {
                            //把文件中时间转换成Long类型的变量tmp
                            //tmp = sdf.parse(head).getTime();
                            //if (tmp - dBefor >= 0L) {
                                //装载数据
                                loadRootMap(ip,head);
                                isIpPrint = true;
                            //}

                        }
                    } else if (head != null && !"".equals(head)) {
                        //把文件中时间转换成Long类型的变量tmp
                        //tmp = sdf.parse(head).getTime();
                        //if (tmp - dBefor >= 0L) {
                            //装载数据
                            loadRootMap(ip,head);
                        //}
                    }
                }
                //开始下一次循环
                head = logBR.readLine();
            }
            String head1 = null;

        }catch (Exception e){

        }
    }

    //更新alldata数据,并且插入数据库
    public void loadTODataBase(){
        List<TServerStatusInd> list = new ArrayList<>();
        String nowtimstr= DateUtils.today();
        for (Map.Entry<String, TServerStatusInd> entry : alldata.entrySet()) {
            TServerStatusInd tServerStatusInd = entry.getValue();
                String ip = entry.getKey();
                String province;
                String cluster;
                if (dataCache.dataMaper.containsKey(ip)){
                    province = dataCache.dataMaper.get(ip).getProvince();
                    cluster = dataCache.dataMaper.get(ip).getCluster();
                } else {
                    province ="未知省份";
                    cluster = "";
                }
            tServerStatusInd.setIp(ip);
            tServerStatusInd.setProvince(province);
            tServerStatusInd.setCluster(cluster);
            tServerStatusInd.setCheckTime(nowtimstr);
            list.add(tServerStatusInd);
        }
        iTServerStatusIndService.saveBatch(list);
        logUtil.toDb("Reboot","success");
    }
    @Override
    public void excuteTask(){
        //负载均衡数据
        loadBalance();
        //重启数据
        Reboot();
        //写入数据库
        loadTODataBase();
    }
}
