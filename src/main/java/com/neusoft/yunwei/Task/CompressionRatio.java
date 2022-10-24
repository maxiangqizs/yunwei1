package com.neusoft.yunwei.Task;


import com.neusoft.yunwei.pojo.TDataCompressRatioInd;
import com.neusoft.yunwei.service.ITDataCompressRatioIndService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * 压缩比
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CompressionRatio extends TaskInfo{
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/yunwei?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";

    Map<String, String> data=new HashMap<>();

    @Autowired
    TDataCompressRatioInd tDataCompressRatioInd;
    @Autowired
    ITDataCompressRatioIndService itDataCompressRatioIndService;
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
            String sqlconfig = "select ip,my_sql_url,port from T_mysql_url_config ";
            prepare = conn.prepareStatement(sqlconfig);
            ResultSet rs = prepare.executeQuery();



            while (rs.next()) {
                String ip = rs.getString("ip");
                String my_sql_name = rs.getString("my_sql_url");
                String port = rs.getString("port");
                String DB_URL1 = "jdbc:mysql://"+ ip +":"+ port +"/"+my_sql_name+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                conn1 = DriverManager.getConnection(DB_URL1,USER,PASS);
                String sql1;
                sql1 = "select sum(size_mb)/1024/1024 as '压缩前',sum(case when \n" +
                        "biz_code like '4G%' or biz_code like '24IOT%' or \n" +
                        "biz_code like 'GM%' then size_mb else 0 end)/1024/1024 as '4g压缩前' ,\n" +
                        "sum(case when biz_code like '5G%'then size_mb else 0 end)/1024/1024 as '5g压缩前',\n" +
                        "b.provice as '省份'" +
                        "from log_metrics a  inner join code_table b on a.ip=b.ip  \n" +
                        "where stage='read' and log_time >= '20220523000000' AND log_time < '20220524000000'\n" +
                        "group by b.provice;";

                prepare1 = conn1.prepareStatement(sql1);

                ResultSet rs1 =prepare1.executeQuery();
                // 展开结果集数据库
                while(rs1.next()){
                    // 通过字段检索
                    System.out.println("=====");
                    String BeforeCompression = rs1.getString("压缩前");
                    String demo1 = rs1.getString("4g压缩前");
                    String demo2 = rs1.getString("5g压缩前");
                    String provice = rs1.getString("省份");
                    data.put("4G",demo1);
                    data.put("5G",demo2);

                    // 输出数据
                /*    System.out.print("压缩前 " + BeforeCompression + "4g压缩前" + demo1 + "5g压缩前" + demo2);*/
                    Set<Map.Entry<String,String>> en=data.entrySet();
                    for (Map.Entry<String, String> entry : en) {
                        String key=entry.getKey();
                        String value=entry.getValue();
                        System.out.println(key+"->>>"+value);
                        tDataCompressRatioInd.setProvince(provice);
                        tDataCompressRatioInd.setTotalBeforeCompress(BeforeCompression);
                        tDataCompressRatioInd.setDataType(key);
                        tDataCompressRatioInd.setBeforeCompress(value);
                        tDataCompressRatioInd.setCheckTime(format.format(new Date()));
                        tDataCompressRatioInd.setProcessPerformance(Float.parseFloat(value)*1024*1024/(24*60*60)/20+"");
                        System.out.println(Float.parseFloat(value)*1024*1024/(24*60*60)/20+"");
                        itDataCompressRatioIndService.insert(tDataCompressRatioInd);
                        System.out.println("插入成功");
                    }


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
