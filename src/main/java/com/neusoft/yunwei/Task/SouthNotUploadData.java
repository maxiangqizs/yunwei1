package com.neusoft.yunwei.Task;

import com.neusoft.yunwei.Config.DataCache;
import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TSouthUploadAlr;
import com.neusoft.yunwei.service.ITSouthUploadAlrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SouthNotUploadData extends TaskInfo {
    //mysql 8.0以上版本
    static final String JDBC_DRIVER = ConfigDb.getInstance().getString("jdbc.driver.class");
    static final String DB_URL = ConfigDb.getInstance().getString("jdbc.connection.url");
    static final String USER = ConfigDb.getInstance().getString("jdbc.connection.username");
    static final String PASS = ConfigDb.getInstance().getString("jdbc.connection.password");
    //记录日志工具
    @Autowired
    LogUtil logUtil;
    @Autowired
    TSouthUploadAlr tSouthUploadAlr;

    @Autowired
    ITSouthUploadAlrService iTSouthUploadAlrService;
    @Autowired
    DataCache dataCache;
    public void find() {
        Connection conn = null;
        //轮询各打通池下mysql库链接
        Connection conn1 = null;
        PreparedStatement prepare = null;
        PreparedStatement prepare1 = null;
        //获取当前时间
        String nowtimstr=DateUtils.today();
        //获取当前时间-4小时
        String beforenowtim4hour=DateUtils.lasthours();
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sqlconfig = "select ip,mysql_dbname,port from T_mysql_url_config ";
            prepare = conn.prepareStatement(sqlconfig);
            ResultSet rs = prepare.executeQuery();

            while (rs.next()) {
                String ip = rs.getString("ip");
                String my_sql_name = rs.getString("mysql_dbname");
                String port = rs.getString("port");
                String DB_URL1 = "jdbc:mysql://"+ ip +":"+ port +"/"+my_sql_name+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                conn1 = DriverManager.getConnection(DB_URL1,USER,PASS);
                String sql1;
                sql1 = "select\n" +
                        "\thostname ,\n" +
                        "\tGROUP_CONCAT(sub_code) business_type,\n" +
                        "\tPORT nPort\n" +
                        "from\n" +
                        "\tlog_host_subcode_dim_tab\n" +
                        "where\n" +
                        "\t(\n" +
                        "\tselect\n" +
                        "\t\tcount(1)\n" +
                        "\tfrom\n" +
                        "\t\tlog_store_file_info\n" +
                        "\twhere\n" +
                        "\t\tlog_store_file_info.sub_code = log_host_subcode_dim_tab.sub_code\n" +
                        "\t\tand log_store_file_info.current_ip = log_host_subcode_dim_tab.hostname\n" +
                        "\t\t and file_time > ?\n" +
                        "                ) = 0\n" +
                        "group by\n" +
                        "\thostname,PORT";

                prepare1 = conn1.prepareStatement(sql1);
                prepare1.setString(1,beforenowtim4hour);

                ResultSet rs1 =prepare1.executeQuery();
                // 展开结果集数据库
                while(rs1.next()){
                    // 通过字段检索
                    System.out.println("=====");

                    //服务器地址
                    String hostname = rs1.getString("hostname");
                    //通过服务器地址获取省份,如果未配置码表,则返回未知省份
                    String province;
                    if (dataCache.dataMaper.containsKey(hostname)){
                        province = dataCache.dataMaper.get(hostname).getProvince();
                    } else {
                        province ="未知省份";
                    }
                    String checkTime = nowtimstr;
                    String businessType = rs1.getString("business_type");
                    String nPort = rs1.getString("nPort");

                    // 输出数据
                    tSouthUploadAlr.setProvince(province);
                    tSouthUploadAlr.setIp(hostname);
                    tSouthUploadAlr.setCheckTime(checkTime);
                    tSouthUploadAlr.setBusinessType(businessType);
                    tSouthUploadAlr.setPort(nPort);
                    iTSouthUploadAlrService.insert(tSouthUploadAlr);



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
            logUtil.toDb("SouthNotUploadData","success");
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
