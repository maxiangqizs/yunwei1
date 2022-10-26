package com.neusoft.yunwei.service.impl;

import com.neusoft.yunwei.pojo.TProvinceServerConfig;
import com.neusoft.yunwei.mapper.TProvinceServerConfigMapper;
import com.neusoft.yunwei.service.ITProvinceServerConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ma
 * @since 2022-10-26
 */
@Service
public class TProvinceServerConfigServiceImpl extends ServiceImpl<TProvinceServerConfigMapper, TProvinceServerConfig> implements ITProvinceServerConfigService {

    @Autowired
    TProvinceServerConfigMapper tProvinceServerConfigMapper;
    @Override
    public List<TProvinceServerConfig> select(Object o) {
        return tProvinceServerConfigMapper.selectList(null);
    }
}