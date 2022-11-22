package com.neusoft.yunwei.Task;


import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TSouthFileProcessAlr;
import com.neusoft.yunwei.service.ITSouthFileProcessAlrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 南向文件处理完整率预警
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class FileIntegrityAlert extends TaskInfo{
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = ConfigDb.getInstance().getString("jdbc.driver.class");
    static final String DB_URL = ConfigDb.getInstance().getString("jdbc.connection.url");
    static final String USER = ConfigDb.getInstance().getString("jdbc.connection.username");
    static final String PASS = ConfigDb.getInstance().getString("jdbc.connection.password");

    //记录日志工具
    @Autowired
    LogUtil logUtil;
    @Autowired
    ITSouthFileProcessAlrService itSouthFileProcessAlrService;
    public void find() {
        Connection conn = null;
        //轮询各打通池下mysql库链接
        Connection conn1 = null;
        PreparedStatement prepare = null;
        PreparedStatement prepare1 = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sqlconfig = "select ip,mysql_dbname,port,province from T_mysql_url_config where cluster='南向' ";
            prepare = conn.prepareStatement(sqlconfig);
            ResultSet rs = prepare.executeQuery();
            TSouthFileProcessAlr tSouthFileProcessAlr = new TSouthFileProcessAlr();
            while (rs.next()) {
                String ip = rs.getString("ip");
                String province = rs.getString("province");
                String mysql_dbname = rs.getString("mysql_dbname");
                String port = rs.getString("port");
                String DB_URL1 = "jdbc:mysql://"+ ip +":"+ port +"/"+mysql_dbname+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                conn1 = DriverManager.getConnection(DB_URL1,USER,PASS);
                String startDay = LocalDateTime.now().plusDays(-2).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"000000";
                String endDay = LocalDateTime.now().plusDays(-2).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"240000";
                BigDecimal failNum = new BigDecimal(0);
                BigDecimal allNum = new BigDecimal(0);
                BigDecimal value = new BigDecimal(0);
                String sql1;
                sql1 = "select count(distinct data_file_name) as countNum, '校验失败文件数' as fail_type from log_store_file_info where check_state=1 and file_time >=${startDay} and file_time <${endDay}\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '文件名称非法' as fail_type from log_store_file_info where check_state=1 and file_time >=${startDay} and file_time <${endDay} and report_cause like '%文件名称非法%'\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '文件生成时间不一致' as fail_type from log_store_file_info where check_state=1 and file_time >=${startDay} and file_time <${endDay} and report_cause like '%文件生成时间不一致%'\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '文件不完整' as fail_type from log_store_file_info where check_state=1 and file_time >=${startDay} and file_time <${endDay} and report_cause like '%文件不完整%'\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '读取压缩文件出错' as fail_type from log_store_file_info where check_state=1 and file_time >=${startDay} and file_time <${endDay} and report_cause like '%读取压缩文件出错%'\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '文件缺失' as fail_type from log_store_file_info where check_state=1 and file_time >=${startDay} and file_time <${endDay} and report_cause like '%文件缺失%'\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '重报后校验失败文件数' as fail_type from log_store_file_info where check_state=1 and file_time >=${startDay} and file_time <${endDay} and data_file_name not in (select data_file_name from log_store_file_info where file_time >=${startDay} and file_time <${endDay} and check_state=0)\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '重报后稽核缺失文件数' as fail_type from log_store_audit_file_info where file_time >=${startDay} and file_time <${endDay} and data_file_name not in (select data_file_name from log_store_file_info where file_time >=${startDay} and file_time <${endDay}) and data_file_name like '%gz'\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '稽核重报请求文件数' as fail_type from log_store_audit_file_info where file_time >=${startDay} and file_time <${endDay} and report_state is not null\n" +
                        "union all\n" +
                        "select count(distinct data_file_name) as countNum, '总文件数' as fail_type from log_store_file_info where file_time >=${startDay} and file_time <${endDay};\n";
                sql1=sql1.replace("${startDay}",startDay).replace("${endDay}",endDay);
                prepare1 = conn1.prepareStatement(sql1);
                ResultSet rs1 =prepare1.executeQuery();
                while(rs1.next()){
                    String countNum = rs1.getString("countNum");
                    String fail_type = rs1.getString("fail_type");
                    log.info("countNum:{},fail_type:{}",countNum,fail_type);
                    if ("校验失败文件数".equals(fail_type)){
                        tSouthFileProcessAlr.setCheckFailReupload(new BigDecimal(countNum));
                    }else if ("文件名称非法".equals(fail_type)){
                        tSouthFileProcessAlr.setAbnormalFileName(new BigDecimal(countNum));
                    }else if ("文件生成时间不一致".equals(fail_type)){
                        tSouthFileProcessAlr.setAbnormalCreateTime(new BigDecimal(countNum));
                    }else if ("文件不完整".equals(fail_type)){
                        tSouthFileProcessAlr.setAbnormalCompleteFile(new BigDecimal(countNum));
                    }else if ("读取压缩文件出错".equals(fail_type)){
                        tSouthFileProcessAlr.setAbnormalReadCompressFile(new BigDecimal(countNum));
                    }else if ("文件缺失".equals(fail_type)){
                        tSouthFileProcessAlr.setLostFile(new BigDecimal(countNum));
                    }else if ("重报后校验失败文件数".equals(fail_type)){
                        tSouthFileProcessAlr.setCheckReupload(new BigDecimal(countNum));
                    }else if ("重报后稽核缺失文件数".equals(fail_type)){
                        tSouthFileProcessAlr.setAuditReupload(new BigDecimal(countNum));
                    }else if ("稽核重报请求文件数".equals(fail_type)){
                        tSouthFileProcessAlr.setAuditFailReupload(new BigDecimal(countNum));
                    } else if ("总文件数".equals(fail_type)){
                        tSouthFileProcessAlr.setTotalFileCount(new BigDecimal(countNum));
                        allNum = new BigDecimal(countNum);
                    }
                    if (!"总文件数".equals(fail_type)){
                        failNum = failNum.add(new BigDecimal(countNum));
                    }
                }
                tSouthFileProcessAlr.setProvince(province);
                tSouthFileProcessAlr.setCheckTime( DateUtils.checkTime());
                tSouthFileProcessAlr.setCollectEndTime(endDay);
                tSouthFileProcessAlr.setCollectStartTime(startDay);
                if (failNum.toString().equals("0")||allNum.toString().equals("0")) {
                    tSouthFileProcessAlr.setCompleteRatio(value);
                } else {
                    tSouthFileProcessAlr.setCompleteRatio(failNum.divide(allNum).setScale(BigDecimal.ROUND_HALF_UP,2));
                }
                if (tSouthFileProcessAlr!=null) {
                    itSouthFileProcessAlrService.save(tSouthFileProcessAlr);
                }
                rs1.close();
                prepare1.close();
                conn1.close();
            }

            // 完成后关闭
            rs.close();
            prepare.close();
            conn.close();
            logUtil.toDb("FileIntegrityAlert","success");
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(prepare!=null) prepare.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

    }

    @Override
    public void excuteTask() {

        find();
    }
}
