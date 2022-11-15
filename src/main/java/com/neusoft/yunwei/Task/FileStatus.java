package com.neusoft.yunwei.Task;

import com.neusoft.yunwei.Config.DataCache;
import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TFileStatusInd;
import com.neusoft.yunwei.service.ITFileStatusIndService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileStatus extends TaskInfo {
    //mysql 8.0以上版本
    static final String JDBC_DRIVER = ConfigDb.getInstance().getString("jdbc.driver.class");
    static final String DB_URL = ConfigDb.getInstance().getString("jdbc.connection.url");
    static final String USER = ConfigDb.getInstance().getString("jdbc.connection.username");
    static final String PASS = ConfigDb.getInstance().getString("jdbc.connection.password");

    Map<String, String> data=new HashMap<>();
    //记录日志工具
    @Autowired
    LogUtil logUtil;
    @Autowired
    TFileStatusInd tFileStatusInd;

    @Autowired
    ITFileStatusIndService iTFileStatusIndService;
    @Autowired
    DataCache dataCache;
    public void find() {
        Connection conn = null;
        //轮询各打通池下mysql库链接
        Connection conn1 = null;
        PreparedStatement prepare = null;
        //分两个sql查询
        PreparedStatement prepare1 = null;
        PreparedStatement prepare2 = null;
        //获取当前时间
        String nowtimstr= DateUtils.today();
        //获取当前时间前一天
        String beforenowtim=DateUtils.lastday();
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sqlconfig = "select province,ip,mysql_dbname,port from T_mysql_url_config where cluster='南向' ";
            prepare = conn.prepareStatement(sqlconfig);
            ResultSet rs = prepare.executeQuery();

            while (rs.next()) {
                String province = rs.getString("province");
                String ip = rs.getString("ip");
                String my_sql_name = rs.getString("mysql_dbname");
                String port = rs.getString("port");
                String DB_URL1 = "jdbc:mysql://"+ ip +":"+ port +"/"+my_sql_name+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                conn1 = DriverManager.getConnection(DB_URL1,USER,PASS);
                String sql1;
                sql1 = "select businesstype,filecount,filesize,abnormalfilesize,datacount,abnormaldatacount,datasize from (select count(1) as filecount,sum(file_size)  as filesize ,\n" +
                        "               sum(case when lsfi.check_state!='0' then file_size else 0 end)abnormalfilesize,sub_code businesstype\n" +
                        "                        from log_store_file_info lsfi \n" +
                        "                        where file_time>? and file_time<? \n" +
                        "                        group by sub_code)lsfi \n" +
                        "inner join (select biz_code , sum(row_count) datacount,\n" +
                        "sum(error_row_count)abnormaldatacount,\n" +
                        "sum(size_mb)datasize\n" +
                        "from log_metrics  \n" +
                        " where log_time>? and log_time<?\n" +
                        "group by biz_code)lm on lsfi.businesstype=lm.biz_code";

                prepare1 = conn1.prepareStatement(sql1);
                prepare1.setString(1,beforenowtim);
                prepare1.setString(2,nowtimstr);
                prepare1.setString(3,beforenowtim);
                prepare1.setString(4,nowtimstr);
                ResultSet rs1 =prepare1.executeQuery();
                // 展开结果集数据库
                while(rs1.next()){
                    // 通过字段检索
                    System.out.println("=====");
                    String checkTime = nowtimstr;
                    String businessType = rs1.getString("businesstype");
                    BigDecimal totalFileCount =rs1.getBigDecimal("filecount");
                    BigDecimal totalFileSize = rs1.getBigDecimal("filesize");
                    BigDecimal abnormalFileSize = rs1.getBigDecimal("abnormalfilesize");
                    BigDecimal dataCount  = rs1.getBigDecimal("datacount");
                    BigDecimal abnormalDataCount = rs1.getBigDecimal("abnormaldatacount");
                    BigDecimal dataSize = rs1.getBigDecimal("datasize");

                    // 输出数据
                    tFileStatusInd.setProvince(province);
                    tFileStatusInd.setBusinessType(businessType);
                    tFileStatusInd.setCheckTime(checkTime);
                    tFileStatusInd.setTotalFileCount(totalFileCount);
                    tFileStatusInd.setTotalFileSize(totalFileSize);
                    tFileStatusInd.setAbnormalFileSize(abnormalFileSize);
                    tFileStatusInd.setDataCount(dataCount);
                    tFileStatusInd.setAbnormalDataCount(abnormalDataCount);
                    tFileStatusInd.setDataSize(dataSize);
                    iTFileStatusIndService.insert(tFileStatusInd);
                }

                // 完成后关闭
                rs1.close();
                prepare1.close();
                conn1.close();
            }
            // 完成后关闭
            rs.close();
            prepare.close();
            conn.close();
            logUtil.toDb("FileStatus","success");
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
