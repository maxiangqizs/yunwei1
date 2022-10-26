package com.neusoft.yunwei.Config;


import com.neusoft.yunwei.pojo.TProvinceServerConfig;
import com.neusoft.yunwei.service.ITProvinceServerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataCache implements CommandLineRunner {
    @Autowired
    public TProvinceServerConfig tProvinceServerConfig;

    //存储数据
    public static Map<String,TProvinceServerConfig> dataMaper = new HashMap<>();

    //原始数据
    public static List<TProvinceServerConfig> allProviceData;

    //加载数据服务
    @Autowired
    ITProvinceServerConfigService iTProvinceServerConfigService;
    //查询数据
    public List<TProvinceServerConfig> selectAllProvice () {
        List<TProvinceServerConfig> allProvice = iTProvinceServerConfigService.select(null);
        return allProvice;
    }

    //加入内存
    public void addCache () {
        this.allProviceData = selectAllProvice();
        for(TProvinceServerConfig data: this.allProviceData){
            this.dataMaper.put(data.getBusinessIp(),data);
        }
    }
    @Override
    public void run(String... args) throws Exception {

       //需求初始化的方法
        addCache ();
        System.out.println("预加载省份码表执行完成"+dataMaper);

    }

}
