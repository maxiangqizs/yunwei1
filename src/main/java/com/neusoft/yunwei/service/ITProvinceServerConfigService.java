package com.neusoft.yunwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.yunwei.pojo.TProvinceServerConfig;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ma
 * @since 2022-10-26
 */

public interface ITProvinceServerConfigService extends IService<TProvinceServerConfig> {


    public List<TProvinceServerConfig> select(Object o);


    Integer CountByProvince(String provice, String key);

    TProvinceServerConfig selectByIp(String lastIp);
}
