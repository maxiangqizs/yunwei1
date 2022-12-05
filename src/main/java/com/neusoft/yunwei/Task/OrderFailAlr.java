package com.neusoft.yunwei.Task;

import com.neusoft.yunwei.Config.DataCache;
import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TNorthOrderFailAlr;
import com.neusoft.yunwei.service.ITNorthOrderFailAlrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/*
 待定
 */
public class OrderFailAlr extends TaskInfo{
    //mysql 8.0以上版本
    static final String JDBC_DRIVER = ConfigDb.getInstance().getString("jdbc.driver.class");
    static final String DB_URL = ConfigDb.getInstance().getString("jdbc.connection.url");
    static final String USER = ConfigDb.getInstance().getString("jdbc.connection.username");
    static final String PASS = ConfigDb.getInstance().getString("jdbc.connection.password");
    //记录日志工具
    @Autowired
    LogUtil logUtil;
    @Autowired
    TNorthOrderFailAlr tNorthCheckAlr;

    @Autowired
    ITNorthOrderFailAlrService itNorthOrderFailAlrService;
    @Autowired
    DataCache dataCache;
    public void find() {
        Connection conn = null;
        //轮询各打通池下mysql库链接
        Connection conn1 = null;
        PreparedStatement prepare = null;
        PreparedStatement prepare0 = null;
        PreparedStatement prepare1 = null;
        //获取当前时间
        String nowtimstr = DateUtils.nowTime();
        //当前时间的前一天
        String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"000000";
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sqlconfig = "select ip,mysql_dbname,port,province from T_mysql_url_config  where cluster='北向'";
            prepare = conn.prepareStatement(sqlconfig);
            ResultSet rs = prepare.executeQuery();

            while (rs.next()) {
                String ip = rs.getString("ip");
                String my_sql_name = rs.getString("mysql_dbname");
                String port = rs.getString("port");
                String province = rs.getString("province");
                String DB_URL1 = "jdbc:mysql://"+ ip +":"+ port +"/"+my_sql_name+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                conn1 = DriverManager.getConnection(DB_URL1,USER,PASS);
                String sql1;
                sql1 = "SELECT count(1) fail_count FROM task_data_sort_record_tab where STATUS=3 or STATUS=6 or STATUS=9 or (STATUS=1 AND create_time< DATE_FORMAT(ADDTIME( ?,'-1:0:0'),'%Y%m%d%H%i%s') );\n";
                prepare1 = conn1.prepareStatement(sql1);
                prepare1.setString(1,startTime);
                ResultSet rs1 =prepare1.executeQuery();
                // 展开结果集数据库
                while(rs1.next()){
                    // 通过字段检索
                    System.out.println("=====");
                    BigDecimal failCount = rs1.getBigDecimal("fail_count");
                    String checkTime = nowtimstr;
                    //输出数据
                    tNorthCheckAlr.setProvince(province);
                    tNorthCheckAlr.setCheckTime(checkTime);
                    tNorthCheckAlr.setOrderFailCount(failCount);
                    //插入数据
                    itNorthOrderFailAlrService.save(tNorthCheckAlr);
                    //记录日志
                    logUtil.toDb("OrderFailAlr","success");
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
            logUtil.toDb("OrderFailAlr","success");
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
