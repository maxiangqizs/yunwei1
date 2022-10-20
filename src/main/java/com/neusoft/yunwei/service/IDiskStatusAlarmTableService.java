package com.neusoft.yunwei.service;

import com.neusoft.yunwei.pojo.DiskStatusAlarmTable;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhang
 * @since 2022-10-19
 */
public interface IDiskStatusAlarmTableService extends IService<DiskStatusAlarmTable> {

    int insterByIp(DiskStatusAlarmTable diskStatusAlarmTable);
}
