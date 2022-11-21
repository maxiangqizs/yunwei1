package com.neusoft.yunwei.service;

import com.neusoft.yunwei.pojo.TAlertLog;

import java.util.List;
import java.util.Map;

public interface TAlertLogService {
    public List<TAlertLog> selectByTime(Map<String, Object> map);
}
