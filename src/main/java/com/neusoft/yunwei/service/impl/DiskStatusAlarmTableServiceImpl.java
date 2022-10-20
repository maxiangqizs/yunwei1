package com.neusoft.yunwei.service.impl;

import com.neusoft.yunwei.pojo.DiskStatusAlarmTable;
import com.neusoft.yunwei.mapper.DiskStatusAlarmTableMapper;
import com.neusoft.yunwei.service.IDiskStatusAlarmTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-10-19
 */
@Service
public class DiskStatusAlarmTableServiceImpl extends ServiceImpl<DiskStatusAlarmTableMapper, DiskStatusAlarmTable> implements IDiskStatusAlarmTableService {
    @Autowired
    DiskStatusAlarmTableMapper diskStatusAlarmTableMapper;
    @Override
    public int insterByIp(DiskStatusAlarmTable diskStatusAlarmTable) {
        int insert = diskStatusAlarmTableMapper.insert(diskStatusAlarmTable);
        return insert;
    }
}
