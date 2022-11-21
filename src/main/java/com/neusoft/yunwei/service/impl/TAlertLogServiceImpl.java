package com.neusoft.yunwei.service.impl;

import com.neusoft.yunwei.mapper.TAlertLogMapper;
import com.neusoft.yunwei.pojo.TAlertLog;
import com.neusoft.yunwei.service.TAlertLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TAlertLogServiceImpl implements TAlertLogService {

    @Autowired
    TAlertLogMapper tAlertLogMapper;
    @Override
    public List<TAlertLog> selectByTime(Map<String, Object> map) {

        Integer pageNo = (Integer) map.get("current");
        Integer pageSize = (Integer) map.get("size");

        if (pageNo == null)
            pageNo = 1;
        if (pageSize == null || pageSize <= 0)
            pageSize = 20;
//        PageHelper.startPage(pageNo, pageSize);
        List<TAlertLog> tabList = tAlertLogMapper.selectByPrimaryKey("1");
//        PageInfo<List<TNorthCheckAlr>> pageInfo = new PageInfo(tabList);
        return tabList;
    }
}
