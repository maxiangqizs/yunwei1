package com.neusoft.yunwei.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 功能:读取配置文件jdbc链接
 author:mxq
 */
public class ConfigDb {
    private static ConfigDb configDb;
    private static Properties properties;
    private ConfigDb(){
        String configFile="db.properties"; // 数据库配置文件
        properties = new Properties();
        InputStream is = ConfigDb.class.getClassLoader().getResourceAsStream(configFile);
        try{
            properties.load(is);
            is.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static ConfigDb getInstance(){

        if(configDb == null){
            configDb=new ConfigDb();
        }
        return configDb;
    }

    // 通过配置文件Key的名称获取到Key的值。
    public String getString(String key){
        return properties.getProperty(key);
    }
}
