package com.neusoft.yunwei.Task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.yunwei.service.TAlertLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/*
 推送预警日志
 暂停
 */
public class PostAlertLog extends TaskInfo{
    @Autowired
    TAlertLogService tAlertLogService;
    public void postAlertRecord(){
        HashMap map = new HashMap();
        JSONObject jsonObject =new JSONObject();
        String voStr = JSONObject.toJSONString(tAlertLogService.selectByTime(map));
        JSONArray voJson = JSONObject.parseArray(voStr);
        jsonObject.put("record",voJson);
    }

    @Override
    public void excuteTask() {
        postAlertRecord();
    }

}
