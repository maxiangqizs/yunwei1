package com.neusoft.yunwei.Task;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompressionRatio extends TaskInfo{
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/bj?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";

    public void find() {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            String sql2;
            String sql3;
            sql = "select sum(size_mb)/1024/1024 as '压缩前' from log_metrics where stage='read' and log_time >= '20220523000000' AND log_time < '20220524000000';";
            sql2 = "select sum(size_mb)/1024/1024 as '4G压缩前' from log_metrics where stage='read' and log_time >= '20220523000000' AND log_time < '20220524000000' AND (biz_code like '4G%' or biz_code like '24IOT%' or biz_code like 'GM%');;";
            sql3 = "select sum(size_mb)/1024/1024 as '5G压缩前' from log_metrics where stage='read' and log_time >= '20220523000000' AND log_time < '20220524000000'and biz_code like '5G%';";

            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                System.out.println("=====");
                String BeforeCompression = rs.getString("压缩前");
                // 输出数据
                System.out.print("压缩前 " + BeforeCompression);

            }

            ResultSet rs2 = stmt.executeQuery(sql2);
            while(rs2.next()){
                // 通过字段检索
                System.out.println("=====");
                String BeforeCompression = rs2.getString("4G压缩前");
                // 输出数据
                System.out.print("4G压缩前 " + BeforeCompression);
            }

            ResultSet rs3 = stmt.executeQuery(sql3);
            while(rs3.next()){
                // 通过字段检索
                System.out.println("=====");
                String BeforeCompression = rs3.getString("5G压缩前");
                // 输出数据
                System.out.print("5G压缩前 " + BeforeCompression);
            }

            // 完成后关闭
            rs.close();
            rs2.close();
            rs3.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
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
