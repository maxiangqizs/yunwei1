package com.neusoft.yunwei.Task;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Reboot extends TaskInfo {


    public void Reboot(){
        try{
            File file=new File("E:/bigdata/data/reboot.txt");
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
            System.out.println("准备开始");
            while(true) {
                if (head == null) {
                    head = logBR.readLine();
                    if (head == null) {
                        logBR.close();
                        return;
                    }
                }

                if (head.indexOf("rc=") > 0) {
                    ip = head.substring(0, head.indexOf("|")).trim();
                    isIpPrint = false;
                } else {
                    Long tmp;
                    if (!isIpPrint) {
                        if (head != null && !"".equals(head)) {
                            try {
                                tmp = sdf.parse(head).getTime();
                                if (tmp - dBefor >= 0L) {
                                    System.out.println(ip + ":");
                                    System.out.println(head);
                                    isIpPrint = true;
                                }
                            } catch (ParseException var13) {
                                var13.printStackTrace();
                            }
                        }
                    } else if (head != null && !"".equals(head)) {
                        try {
                            tmp = sdf.parse(head).getTime();
                            if (tmp - dBefor >= 0L) {
                                System.out.println(head);
                            }
                        } catch (ParseException var12) {
                            var12.printStackTrace();
                        }
                    }
                }

                head = logBR.readLine();
            }

        }catch (Exception e){

        }
    }
    @Override
    public void excuteTask() {
        Reboot();
    }
}
