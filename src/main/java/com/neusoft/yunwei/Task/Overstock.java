package com.neusoft.yunwei.Task;

import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TOverStockAlr;
import com.neusoft.yunwei.pojo.TProvinceServerConfig;
import com.neusoft.yunwei.service.ITOverStockAlrService;
import com.neusoft.yunwei.service.ITProvinceServerConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.math.BigDecimal;

public class Overstock extends TaskInfo {

    //记录日志工具
    @Autowired
    LogUtil logUtil;
    @Autowired
    ITOverStockAlrService itOverStockAlrService;
    @Autowired
    TProvinceServerConfig tProvinceServerConfig;

    @Autowired
    ITProvinceServerConfigService itProvinceServerConfigService;
    public  void Overstock(){
        try {
            File file=new File(ConfigDb.getInstance().getString("file.jy.path"));
            {
                int MAX_SIZE = 20;
                InputStream log = null;
                BufferedReader logBR = null;
                String provice = null;
                String overStockSize = null;
                String overStockDirectory = null;
                TOverStockAlr tOverStockAlr = new TOverStockAlr();
                try {
                    boolean _printError = false;

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

                        String curIp = null;
                        if (head.indexOf("rc=") > 0) {
                            curIp = head.substring(0, head.indexOf("|")).trim();
                            if (curIp != null && !curIp.equals(lastIp)) {
                                lastIp = curIp;
                                printFlag = true;
                            } else {
                                printFlag = false;
                            }
                        } else if (head.indexOf("data") > 0) {
                            String val = head.substring(0, head.indexOf("G")).trim();

                            try {
                                Double valDb = Double.parseDouble(val);
                                if (valDb > (double)MAX_SIZE) {
                                    if (printFlag) {
                                        System.out.println(lastIp);
                                  TProvinceServerConfig tProvinceServerConfig = itProvinceServerConfigService.selectByIp(lastIp);
                                  if (tProvinceServerConfig == null){
                                      String message = "未识别到该ip";
                                      System.out.println(message);
                                  }else {
                                      provice = tProvinceServerConfig.getProvince();
                                      overStockSize = valDb +"";
                                      int begin = head.indexOf("/");
                                      int end = head.length();
                                      overStockDirectory = head.substring(begin,end);
                                      tOverStockAlr.setProvince(provice);
                                      tOverStockAlr.setOverStockDirectory(overStockDirectory);
                                      tOverStockAlr.setIp(lastIp);
                                      tOverStockAlr.setCheckTime(DateUtils.checkTime());
                                      tOverStockAlr.setCollectStartTime(DateUtils.startDay());
                                      tOverStockAlr.setCollectEndTime(DateUtils.endDay());
                                      tOverStockAlr.setOverStockSize(new BigDecimal(String.valueOf(overStockSize)));
                                      System.out.println("准备插入" );
                                      itOverStockAlrService.save(tOverStockAlr);
                                      System.out.println("插入成功");
                                  }
                                        printFlag = false;
                                    }
                                    System.out.println(overStockDirectory);
                                    System.out.println(head);

                                }
                            } catch (NumberFormatException var21) {
                                if (_printError) {
                                    System.err.println(lastIp + ",Parse Error:" + head);
                                    var21.printStackTrace();
                                }
                            }
                        }

                        try {
                            Thread.sleep(5L);
                        } catch (InterruptedException var22) {
                            if (_printError) {
                                var22.printStackTrace();
                            }
                        }

                        head = logBR.readLine();
                    }
                    logUtil.toDb("Overstock","success");
                } catch (FileNotFoundException var23) {
                    var23.printStackTrace();
                } catch (IOException var24) {
                    var24.printStackTrace();
                } finally {
                    if (logBR != null) {
                        logBR.close();
                    }

                    if (log != null) {
                        log.close();
                    }

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void excuteTask() {
        Overstock();
    }
}
