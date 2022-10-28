package com.neusoft.yunwei.mapper;

import com.neusoft.yunwei.pojo.TProvinceServerConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ma
 * @since 2022-10-26
 */
@Repository
public interface TProvinceServerConfigMapper extends BaseMapper<TProvinceServerConfig> {


    Integer count(String provice, String key);

    TProvinceServerConfig select(String lastIp);
}
