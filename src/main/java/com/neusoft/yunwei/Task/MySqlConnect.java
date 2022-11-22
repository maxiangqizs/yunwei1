package com.neusoft.yunwei.Task;


import com.neusoft.yunwei.Config.DataCache;
import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TMysqlConnectInd;
import com.neusoft.yunwei.service.ITMysqlConnectIndService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;

/*
 功能:mysql连通性
 通过jdbc链接来确认mysql服务器连通性
 Author:ma
 */

public class MySqlConnect extends TaskInfo {
    public Logger logger = LoggerFactory.getLogger(MySqlConnect.class);

    static final String JDBC_DRIVER = ConfigDb.getInstance().getString("jdbc.driver.class");
    static final String DB_URL = ConfigDb.getInstance().getString("jdbc.connection.url");
    static final String USER = ConfigDb.getInstance().getString("jdbc.connection.username");
    static final String PASS = ConfigDb.getInstance().getString("jdbc.connection.password");

    String ip ;
    String my_sql_name ;
    String port ;
    String province;
    String cluster ;
    //记录日志工具
    @Autowired
    LogUtil logUtil;
    //缓存的省份码表
    @Autowired
    DataCache dataCache;
    @Autowired
    TMysqlConnectInd tMysqlConnectInd;
    @Autowired
    ITMysqlConnectIndService itMysqlConnectIndService;

    public String connectStatus(String dbUrl,String user, String pass){
        //返回标志1成功连接,0失败
        String flag ;
        try {
            Connection coon = DriverManager.getConnection( dbUrl,user,pass);
            flag="1";
            coon.close();
        }
        catch (SQLException se){

            flag="0";
        }
        return flag;
    }
    public void taskConnect() {
        Connection conn = null;
        //轮询各打通池下mysql库链接
        Connection conn1 = null;
        PreparedStatement prepare = null;
        PreparedStatement prepare1 = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            String sqlconfig = "select ip,mysql_dbname,port,province,cluster from T_mysql_url_config where cluster='南向' ";
            prepare = conn.prepareStatement(sqlconfig);
            ResultSet rs = prepare.executeQuery();
            //脚本的开始时间结束时间
            //String startTime = DateUtils.lastday();
            //String endTime = DateUtils.today();
            while (rs.next()) {
                ip = rs.getString("ip");
                my_sql_name = rs.getString("mysql_dbname");
                port = rs.getString("port");
                province = rs.getString("province");
                cluster = rs.getString("cluster");
                String DB_URL1 = "jdbc:mysql://"+ ip +":"+ port +"/"+my_sql_name+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                //判断连接是否正常 flag=1正常 flag=0 不正常
                String flag =connectStatus(DB_URL1,USER,PASS);
                if (flag.equals("1")) {
                    conn1 = DriverManager.getConnection(DB_URL1,USER,PASS);
                    String sql1;
                    sql1 = "show status like 'wsrep_cluster_size'";
                    //测试用
                    //sql1 = "show status like 'Aborted_clients'";
                    prepare1 = conn1.prepareStatement(sql1);
                    ResultSet rs1 =prepare1.executeQuery();
                    // 展开结果集数据库
                    while(rs1.next()){
                        // 通过字段检索
                        System.out.println("=====");
                        String variableName = rs1.getString("Variable_name");
                        String value = rs1.getString("Value");
                        //插入集群节点数
                        tMysqlConnectInd.setClusterSize(value);
                    }
                    //插入其他字段
                    tMysqlConnectInd.setProvince(province);
                    tMysqlConnectInd.setCluster(cluster);
                    tMysqlConnectInd.setIp(ip);
                    //插入1代表可以通信
                    tMysqlConnectInd.setMysqlClusterConnect("1");
                    tMysqlConnectInd.setCheckTime(DateUtils.checkTime());
                    tMysqlConnectInd.setCollectStartTime(DateUtils.nowTime());
                    tMysqlConnectInd.setCollectEndTime(DateUtils.nowTime());
                    itMysqlConnectIndService.save(tMysqlConnectInd);
                    // 完成后关闭
                    rs1.close();
                    prepare1.close();
                    conn1.close();
                } else {
                    tMysqlConnectInd.setProvince(province);
                    tMysqlConnectInd.setCluster(cluster);
                    tMysqlConnectInd.setIp(ip);
                    //插入0代表不可以通信
                    tMysqlConnectInd.setMysqlClusterConnect("0");
                    tMysqlConnectInd.setCheckTime(DateUtils.checkTime());
                    tMysqlConnectInd.setCollectStartTime(DateUtils.nowTime());
                    tMysqlConnectInd.setCollectEndTime(DateUtils.nowTime());
                    itMysqlConnectIndService.save(tMysqlConnectInd);
                }

            }


            // 完成后关闭
            rs.close();
            prepare.close();
            conn.close();
            logUtil.toDb("MySqlConnect","success");
        }catch(SQLException se){
            // 处理 JDBC 错误

            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(prepare!=null)
                    prepare.close();
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
        taskConnect();
    }
}
