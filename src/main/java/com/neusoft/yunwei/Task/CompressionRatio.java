package com.neusoft.yunwei.Task;


import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TDataCompressRatioInd;
import com.neusoft.yunwei.service.ITDataCompressRatioIndService;
import com.neusoft.yunwei.service.ITProvinceServerConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 压缩比 待修改点
 * 1.如何获取45G具体机器数
 * 2.待码表建立好修改查询
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CompressionRatio extends TaskInfo{
    static final String JDBC_DRIVER = ConfigDb.getInstance().getString("jdbc.driver.class");
    static final String DB_URL = ConfigDb.getInstance().getString("jdbc.connection.url");
    static final String USER = ConfigDb.getInstance().getString("jdbc.connection.username");
    static final String PASS = ConfigDb.getInstance().getString("jdbc.connection.password");

    Map<String, String> data=new HashMap<>();

    //记录日志工具
    @Autowired
    LogUtil logUtil;
    @Autowired
    TDataCompressRatioInd tDataCompressRatioInd;
    @Autowired
    ITProvinceServerConfigService itProvinceServerConfigService;
    @Autowired
    ITDataCompressRatioIndService itDataCompressRatioIndService;
    public void find() {
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
            String sqlconfig = "select ip,mysql_dbname,port from T_mysql_url_config ";
            prepare = conn.prepareStatement(sqlconfig);
            ResultSet rs = prepare.executeQuery();
            Runtime rt = Runtime.getRuntime();
           while (rs.next()) {
                String ip = rs.getString("ip");
                String my_sql_name = rs.getString("mysql_dbname");
                String port = rs.getString("port");
                String DB_URL1 = "jdbc:mysql://"+ ip +":"+ port +"/"+my_sql_name+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                conn1 = DriverManager.getConnection(DB_URL1,USER,PASS);
                String sql1;
                sql1 = "select sum(size_mb)/1024/1024 as '压缩前',sum(case when \n" +
                        "biz_code like '4G%' or biz_code like '24IOT%' or \n" +
                        "biz_code like 'GM%' then size_mb else 0 end)/1024/1024 as '4g压缩前' ,\n" +
                        "sum(case when biz_code like '5G%'then size_mb else 0 end)/1024/1024 as '5g压缩前',\n" +
                        "b.province\n" +
                        "from log_metrics a  inner join t_province_server_config b on a.ip=b.business_ip  \n" +
                        "where stage='read' and log_time >= '" + DateUtils.lastday() +"' AND log_time < '"+ DateUtils.today()+"'\n" +
                        "group by b.province;";

                prepare1 = conn1.prepareStatement(sql1);

                ResultSet rs1 =prepare1.executeQuery();
                // 展开结果集数据库
                while(rs1.next()){
                    // 通过字段检索
                    System.out.println("=====");
                    String BeforeCompression = rs1.getString("压缩前");
                    String demo1 = rs1.getString("4g压缩前");
                    String demo2 = rs1.getString("5g压缩前");
                    String provice = rs1.getString("province");
                    String totalAfterCompress = null;
                    String count = null;
                    data.put("4G",demo1);
                    data.put("5G",demo2);

                    // 输出数据
                /*    System.out.print("压缩前 " + BeforeCompression + "4g压缩前" + demo1 + "5g压缩前" + demo2);*/
                    Set<Map.Entry<String,String>> en=data.entrySet();
                    for (Map.Entry<String, String> entry : en) {
                        String key=entry.getKey();
                        String value=entry.getValue();
                        System.out.println(key+"->>>"+value);
                        Integer number =itProvinceServerConfigService.CountByProvince(provice,key);
                        tDataCompressRatioInd.setProvince(provice);
                        tDataCompressRatioInd.setTotalBeforeCompress(BeforeCompression);
//                       Process p = rt.exec("hadoop fs -du -s /warehouse/tablespace/managed/hive/*/hour_id=20221025*|awk -F ' '  '{sum +=$2};END {print sum/1024/1024/1024/1024}'");
//                        tDataCompressRatioInd.setTotalAfterCompress(String.valueOf(p));
                        tDataCompressRatioInd.setDataType(key);
                        tDataCompressRatioInd.setBeforeCompress(value);
/*                        tDataCompressRatioInd.setCompressRatio(Float.valueOf(BeforeCompression)/Float.valueOf(String.valueOf(p)) +"");*/
                        tDataCompressRatioInd.setCheckTime(DateUtils.today());
                        tDataCompressRatioInd.setProcessPerformance(Float.parseFloat(value)*1024*1024/(24*60*60)/number+"");
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
            logUtil.toDb("CompressionRatio","success");
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
