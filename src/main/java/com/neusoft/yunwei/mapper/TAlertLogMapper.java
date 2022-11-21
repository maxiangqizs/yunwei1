package com.neusoft.yunwei.mapper;

import com.neusoft.yunwei.pojo.TAlertLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TAlertLogMapper {
    int deleteByPrimaryKey(String alertid);

    int insert(TAlertLog record);

    int insertSelective(TAlertLog record);

    List<TAlertLog> selectByPrimaryKey(String alertid);

    int updateByPrimaryKeySelective(TAlertLog record);

    int updateByPrimaryKey(TAlertLog record);
}