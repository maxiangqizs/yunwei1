package com.neusoft.yunwei.service.impl;

import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.pojo.TNorthCheckRelaAlr;
import com.neusoft.yunwei.pojo.TProgress;
import com.neusoft.yunwei.service.AcceptServiceConnectService;
import com.neusoft.yunwei.service.ITNorthCheckRelaAlrService;
import com.neusoft.yunwei.service.ITProgressService;
import com.neusoft.yunwei.vo.TNorthCheckVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;

/*
 接收nms-ConnectServerController请求,
 实时执行北向定时查询
 */
@Service
class AcceptServiceConnectServiceImpl implements AcceptServiceConnectService {
    @Autowired
    ITNorthCheckRelaAlrService itNorthCheckRelaAlrService;
    @Autowired
    ITProgressService itProgressService;
    static  String JDBC_DRIVER = ConfigDb.getInstance().getString("jdbc.driver.class");
    static  String DB_URL = ConfigDb.getInstance().getString("jdbc.connection.url");
    static  String USER = ConfigDb.getInstance().getString("jdbc.connection.username");
    static  String PASS = ConfigDb.getInstance().getString("jdbc.connection.password");
    public class MutiExecute extends Thread{
        //省份的连接
        Connection connToProvince;
        //省份
        String provinceName;
        //接收post请求推送时间
        String startTime ;
        String endTime ;
        //北向查询临时表
        ITNorthCheckRelaAlrService itNorthCheckRelaAlrService;
        //北向进度表
        ITProgressService itProgressService;
        //每更新完一个省份都算10%
        BigDecimal bdProgress = new BigDecimal(12.5);
        MutiExecute(){}

        MutiExecute(String name,Connection connection,String sTime ,String eTime,ITNorthCheckRelaAlrService service,ITProgressService itProgressService){
            this.provinceName = name;
            this.connToProvince = connection;
            this.startTime = sTime;
            this.endTime = eTime;
            this.itNorthCheckRelaAlrService = service;
            this.itProgressService = itProgressService;
        }

        public  void insertSql(){
            try{System.out.println("当前线程"+getName() +"当前省份"+provinceName);
                //获取当前时间
                String nowtimstr = DateUtils.checkTime();
                PreparedStatement prepare0 = null;
                PreparedStatement prepare1 = null;
                TNorthCheckRelaAlr tNorthCheckRelaAlr = new TNorthCheckRelaAlr();
                String sql0;
                sql0 = "SELECT COUNT(1) as '总查询条件数' FROM api_req_msg_detail_tab a WHERE a.crt_time >= ? and a.crt_time < ?";

                prepare0 = connToProvince.prepareStatement(sql0);
                prepare0.setString(1,startTime);
                prepare0.setString(2,endTime);
                ResultSet rs0 =prepare0.executeQuery();
                // 展开结果集数据库
                while(rs0.next()){
                    BigDecimal total=  rs0.getBigDecimal("总查询条件数");
                    tNorthCheckRelaAlr.setTotalConditionCount(total);
                }
                String sql1;
                sql1 = "SELECT \n" +
                        "\t\tt1.api_type apiType,\n" +
                        "\t\tt1.下发任务数,\n" +
                        "\t\tt1.查询条件数,\n" +
                        "\t\tt1.拆分sql数,\n" +
                        "\t\tt2.pack_num as '打包sql数',\n" +
                        "\t\tt1.执行成功,\n" +
                        "\t\tt1.查询失败,\n" +
                        "\t\tt1.上报失败,\n" +
                        "\t\tt1.查询执行中,\n" +
                        "\t\tt1.上报执行中,\n" +
                        "\t\tt1.固定条件7天内,\n" +
                        "\t\tt1.固定条件7天外,\n" +
                        "\t\tt1.非固定条件,\n" +
                        "\t\tt1.未来查询含历史部分,\n" +
                        "\t\tt1.未来查询,\n" +
                        "\t\tt1.search_start_time as '范围小于24小时',\n" +
                        "\t\tt1.search_end_time as '范围大于24小时',\n" +
                        "\t\tt1.上报条数,\n" +
                        "\t\tt1.split_time as '拆分打包最大耗时',\n" +
                        "\t\tt1.spark_time as 'spark执行最大耗时',\n" +
                        "\t\tt1.upload_time as '上报最大耗时',\n" +
                        "\t\tt1.total_time as '最大总耗时'\t\n" +
                        "\tFROM\n" +
                        "\t\t(SELECT\n" +
                        "\t\t\t( CASE WHEN a.api_type = '01' THEN '用户信息查询接口（6）'\n" +
                        "\t\t\t\tWHEN a.api_type = '02' THEN '用户日志号码查询接口（7）'\n" +
                        "\t\t\t\tWHEN a.api_type = '03' THEN '物联网日志查询接口（8）'\n" +
                        "\t\t\t\tWHEN a.api_type = '04' THEN 'idc日志查询接口（9）' END ) api_type,\n" +
                        "\t\t\tCOUNT(DISTINCT a.req_id) AS '下发任务数',\n" +
                        "\t\t\tcount(0) AS '查询条件数',\n" +
                        "\t\t\tsum(t.all_single) AS '拆分sql数',\n" +
                        "\t\t\tCOUNT(IF(a. STATUS = '4', 1, NULL)) AS '执行成功',\n" +
                        "\t\t\tcount(IF( a. STATUS = '2' AND t.task_1 > 0, 1, NULL )) AS '查询失败',\n" +
                        "\t\t\tcount(IF( a.STATUS = '3' AND t.task_2 > 0, 1, NULL )) AS '上报失败',\n" +
                        "\t\t\tcount(IF(a. STATUS = '2', 1, NULL)) - count( IF (a. STATUS = '2' AND t.task_1 > 0, 1, NULL ) ) AS '查询执行中',\n" +
                        "\t\t\tcount(IF(a. STATUS = '3', 1, NULL)) - count( IF ( a. STATUS = '3' AND t.task_2 > 0, 1, NULL ) ) AS '上报执行中',\n" +
                        "\t\t\tcount(IF(a.buz_type = '1', 1, NULL)) AS '固定条件7天内',\n" +
                        "\t\t\tcount(IF(a.buz_type = '2', 1, NULL)) AS '固定条件7天外',\n" +
                        "\t\t\tcount(IF(a.buz_type = '3', 1, NULL)) AS '非固定条件',\n" +
                        "\t\t\tcount(IF(a.buz_type = '4', 1, NULL)) AS '未来查询含历史部分',\n" +
                        "\t\t\tcount(IF(a.buz_type = '5', 1, NULL)) AS '未来查询',\n" +
                        "\t\t\tcount(0) - count(IF((UNIX_TIMESTAMP(str_to_date(REPLACE((CASE WHEN a.api_type = '01' or a.api_type = '03' THEN REPLACE(json_extract(a.query_condition, '$.endTime'), '-','') WHEN a.api_type = '02' THEN json_extract(a.query_condition, '$.jssj') end), '\"',''),'%Y%m%d%H%i%s')) - UNIX_TIMESTAMP(str_to_date(REPLACE((CASE WHEN a.api_type = '01' or a.api_type = '03' THEN REPLACE(json_extract(a.query_condition, '$.startTime'), '-','') WHEN a.api_type = '02' THEN json_extract(a.query_condition, '$.kssj') end), '\"',''),'%Y%m%d%H%i%s'))) / 60 / 60 > 24, 1, null)) as 'search_start_time',\n" +
                        "\t\t\tcount(IF((UNIX_TIMESTAMP(str_to_date(REPLACE((CASE WHEN a.api_type = '01' or a.api_type = '03' THEN REPLACE(json_extract(a.query_condition, '$.endTime'), '-','') WHEN a.api_type = '02' THEN json_extract(a.query_condition, '$.jssj') end), '\"',''),'%Y%m%d%H%i%s')) - UNIX_TIMESTAMP(str_to_date(REPLACE((CASE WHEN a.api_type = '01' or a.api_type = '03' THEN REPLACE(json_extract(a.query_condition, '$.startTime'), '-','') WHEN a.api_type = '02' THEN json_extract(a.query_condition, '$.kssj') end), '\"',''),'%Y%m%d%H%i%s'))) / 60 / 60 > 24, 1, null)) as 'search_end_time',\n" +
                        "\t\t\tsum(IF(a.STATUS = '4', a.upload_record_count, 0)) AS '上报条数',\n" +
                        "\t\t\tmax(UNIX_TIMESTAMP(str_to_date(t.max_crt_time,'%Y%m%d%H%i%s'))-UNIX_TIMESTAMP(str_to_date(main.req_time,'%Y%m%d%H%i%s')))/60 as split_time,\n" +
                        "\t\t\tmax(spark_time) as spark_time,\n" +
                        "\t\t\tmax(upload_time) as upload_time,\n" +
                        "\t\t\tmax(UNIX_TIMESTAMP(str_to_date(t3.upload_end_time,'%Y%m%d%H%i%s'))-UNIX_TIMESTAMP(str_to_date(main.req_time,'%Y%m%d%H%i%s')))/60 as total_time,\n" +
                        "\t\t\t'1' AS JOIN_FIELD\n" +
                        "\t\tFROM api_req_msg_detail_tab a\n" +
                        "\t\tLEFT JOIN (\n" +
                        "\t\t\tSELECT\n" +
                        "\t\t\t\tt.cxdh,\n" +
                        "\t\t\t\tt.cxtjbh,\n" +
                        "\t\t\t\tcount( IF ( t.task_status = '4' OR t.task_status = '10', 1, NULL ) ) AS 'task_1',\n" +
                        "\t\t\t\tcount( IF ( t.task_status = '6' OR t.task_status = '11', 1, NULL ) ) AS 'task_2',\n" +
                        "\t\t\t\tcount(*) AS all_single,\n" +
                        "\t\t\t\tmax(create_time) max_crt_time\n" +
                        "\t\t\tFROM task_single_sql_tab t\n" +
                        "\t\t\tWHERE create_time >= ? and create_time < ?\n" +
                        "\t\t\tGROUP BY t.cxtjbh, t.cxdh\n" +
                        "\t\t) t ON t.cxdh = a.req_id AND t.cxtjbh = a.sub_req_id JOIN api_req_msg_tab main on main.req_id=a.req_id\n" +
                        "\t\tLEFT JOIN (\n" +
                        "\t\t\tSELECT\n" +
                        "\t\t\t\tt.cxdh,\n" +
                        "\t\t\t\tt.cxtjbh,\n" +
                        "\t\t\t\tmax(UNIX_TIMESTAMP(ifnull(str_to_date(log.end_time,'%Y%m%d%H%i%s'),now()))-UNIX_TIMESTAMP(str_to_date(t.create_time,'%Y%m%d%H%i%s')))/60 as spark_time\n" +
                        "\t\t\tFROM task_single_sql_tab t\n" +
                        "\t\t\tleft join task_operation_log_tab log on t.pack_id=log.task_name\n" +
                        "\t\t\tGROUP BY t.cxtjbh, t.cxdh\n" +
                        "\t\t) t2 on t2.cxdh=a.req_id and t2.cxtjbh=a.sub_req_id\n" +
                        "\t\tLEFT JOIN (\n" +
                        "\t\t\tSELECT req_id,sub_req_id,max(cost)/1000 as upload_time,max(upload_end_time) as upload_end_time\n" +
                        "\t\t\tFROM upload_file_detail_tab\n" +
                        "\t\t\tgroup by req_id,sub_req_id\n" +
                        "\t\t) t3 on t3.req_id=a.req_id and t3.sub_req_id=a.sub_req_id\n" +
                        "\t\tWHERE a.crt_time >= ? and a.crt_time < ?\n" +
                        "\t\tGROUP BY a.api_type\n" +
                        "\t) t1\n" +
                        "\tJOIN (\n" +
                        "\t\tselect count(distinct pack_id) as pack_num,\n" +
                        "\t\t'1' as JOIN_FIELD from task_single_sql_tab\n" +
                        "\t\tWHERE create_time >= ? and create_time < ?\n" +
                        "\t) t2 on t1.JOIN_FIELD = t2.JOIN_FIELD";
                prepare1 = connToProvince.prepareStatement(sql1);
                prepare1.setString(1,startTime);
                prepare1.setString(2,endTime);
                prepare1.setString(3,startTime);
                prepare1.setString(4,endTime);
                prepare1.setString(5,startTime);
                prepare1.setString(6,endTime);
                ResultSet rs1 =prepare1.executeQuery();
                // 展开结果集数据库
                while(rs1.next()){
                    // 通过字段检索
                    System.out.println("当前线程"+getName()+"=====开始插入====="+provinceName);
                    String apiType = rs1.getString("apiType");
                    BigDecimal issueTaskCount = rs1.getBigDecimal("下发任务数");
                    BigDecimal queryConditionCount = rs1.getBigDecimal("查询条件数");
                    BigDecimal splitSqlCount = rs1.getBigDecimal("拆分sql数");
                    BigDecimal packSqlCount = rs1.getBigDecimal("打包sql数");
                    BigDecimal executeSuccessCount = rs1.getBigDecimal("执行成功");
                    BigDecimal queryFailCount = rs1.getBigDecimal("查询失败");
                    BigDecimal uploadFailCount = rs1.getBigDecimal("上报失败");
                    BigDecimal queryingCount = rs1.getBigDecimal("查询执行中");
                    BigDecimal uploadingCount = rs1.getBigDecimal("上报执行中");
                    BigDecimal lockConditionInCount = rs1.getBigDecimal("固定条件7天内");
                    BigDecimal lockConditionOutCount = rs1.getBigDecimal("固定条件7天外");
                    BigDecimal notLockConditionCount = rs1.getBigDecimal("非固定条件");
                    BigDecimal fetureQueryHisCount = rs1.getBigDecimal("未来查询含历史部分");
                    BigDecimal fetureQueryCount = rs1.getBigDecimal("未来查询");
                    BigDecimal rangeMore24hCount = rs1.getBigDecimal("范围小于24小时");
                    BigDecimal rangeLess24hCount = rs1.getBigDecimal("范围大于24小时");
                    BigDecimal uploadCount = rs1.getBigDecimal("上报条数");
                    BigDecimal splitMaxTime = rs1.getBigDecimal("拆分打包最大耗时");
                    BigDecimal sparkMaxTime = rs1.getBigDecimal("spark执行最大耗时");
                    BigDecimal uploadMaxTime = rs1.getBigDecimal("上报最大耗时");
                    BigDecimal totalMaxTime = rs1.getBigDecimal("最大总耗时");
                    String checkTime = nowtimstr;
                    //输出数据
                    tNorthCheckRelaAlr.setProvince(provinceName);
                    tNorthCheckRelaAlr.setCheckTime(checkTime);
                    tNorthCheckRelaAlr.setApiType(apiType);
                    tNorthCheckRelaAlr.setIssueTaskCount(issueTaskCount);
                    tNorthCheckRelaAlr.setQueryConditionCount(queryConditionCount);
                    tNorthCheckRelaAlr.setSplitSqlCount(splitSqlCount);
                    tNorthCheckRelaAlr.setPackSqlCount(packSqlCount);
                    tNorthCheckRelaAlr.setExecuteSuccessCount(executeSuccessCount);
                    tNorthCheckRelaAlr.setQueryFailCount(queryFailCount);
                    tNorthCheckRelaAlr.setUploadFailCount(uploadFailCount);
                    tNorthCheckRelaAlr.setQueryingCount(queryingCount);
                    tNorthCheckRelaAlr.setUploadingCount(uploadingCount);
                    tNorthCheckRelaAlr.setLockConditionInCount(lockConditionInCount);
                    tNorthCheckRelaAlr.setLockConditionOutCount(lockConditionOutCount);
                    tNorthCheckRelaAlr.setNotLockConditionCount(notLockConditionCount);
                    tNorthCheckRelaAlr.setFetureQueryHisCount(fetureQueryHisCount);
                    tNorthCheckRelaAlr.setFetureQueryCount(fetureQueryCount);
                    tNorthCheckRelaAlr.setRangeMore24hCount(rangeMore24hCount);
                    tNorthCheckRelaAlr.setRangeLess24hCount(rangeLess24hCount);
                    tNorthCheckRelaAlr.setUploadCount(uploadCount);
                    tNorthCheckRelaAlr.setSplitMaxTime(splitMaxTime);
                    tNorthCheckRelaAlr.setSparkMaxTime(sparkMaxTime);
                    tNorthCheckRelaAlr.setUploadMaxTime(uploadMaxTime);
                    tNorthCheckRelaAlr.setTotalConditionCount(totalMaxTime);
                    tNorthCheckRelaAlr.setCollectStartTime(startTime);
                    tNorthCheckRelaAlr.setCollectEndTime(endTime);
                    //插入数据
                    itNorthCheckRelaAlrService.save(tNorthCheckRelaAlr);
                    System.out.println("当前线程"+getName()+"=====成功插入====="+provinceName);
                }
                //更新进度表
                TProgress tProgress = new TProgress();
                tProgress.setProvince(provinceName);
                tProgress.setProgressValue(bdProgress);
                itProgressService.save(tProgress);
                // 完成后关闭
                rs1.close();
                prepare1.close();
                connToProvince.close();
                } catch (Exception e){
                    System.out.println("当前线程:"+getName()+"当前省份:"+provinceName+"出现异常"+e);
                }

        }

        @Override
        public void run() {
            insertSql();
        }
    }
    @Override
    public String relaTask(TNorthCheckVo vo) {
            Connection conn = null;

            PreparedStatement prepare = null;
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
                    Connection connToProvince = DriverManager.getConnection(DB_URL1,USER,PASS);
                    String startTime = vo.getStartTime();
                    String endTime = vo.getEndTime();
                    MutiExecute mutiExecute = new MutiExecute(province,connToProvince,startTime,endTime,itNorthCheckRelaAlrService,itProgressService);
                    mutiExecute.start();
                }


                // 完成后关闭
                rs.close();
                prepare.close();
                conn.close();
                //1:成功 0 :失败
                return "1";
    //            logUtil.toDb("NorthCheck","success");
            }catch(SQLException se){
                // 处理 JDBC 错误
                se.printStackTrace();
               return "0";
            }catch(Exception e){
                // 处理 Class.forName 错误
                e.printStackTrace();
               return "0";
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
//    public static void main(String[] args) {
//        AcceptServiceConnectServiceImpl mm = new AcceptServiceConnectServiceImpl();
//
//        mm.relaTask();
//    }

}