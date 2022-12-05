package com.neusoft.yunwei.Config;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class generator2 {
    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("Ma");
        gc.setOpen(false);
        gc.setFileOverride(true);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        /*dsc.setUrl("jdbc:oracle:thin:@10.4.66.80:1521:bighead");*/
        dsc.setUrl("jdbc:mysql://localhost:3306/mxq01?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
       /* dsc.setUsername("fraud_neu");
        dsc.setPassword("fraud_neu");*/
        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.neusoft.yunwei");
        pc.setEntity("pojo");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        pc.setServiceImpl("service.impl");

        Map<String,String> map = new HashMap<>();
        map.put("entity_path",projectPath+"/src/main/java/com/neusoft/yunwei/pojo");
        map.put("mapper_path",projectPath+"/src/main/java/com/neusoft/yunwei/mapper");
        map.put("xml_path",projectPath+"/src/main/resources/com/neusoft/yunwei/mapper");
        map.put("controller_path",projectPath+"/src/main/java/com/neusoft/yunwei/controller");
        map.put("service_path",projectPath+"/src/main/java/com/neusoft/yunwei/service");
        map.put("service_impl_path",projectPath+"/src/main/java/com/neusoft/yunwei/service/impl");
        pc.setPathInfo(map);
        mpg.setPackageInfo(pc);
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass(Basepojo.class);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        //strategy.setSuperEntityColumns("id");ANTI_D_MODEL_MATCH_DATA
        strategy.setInclude("T_progress");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

    }
}
