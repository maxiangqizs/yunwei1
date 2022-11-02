package com.neusoft.yunwei.service.impl;

import com.neusoft.yunwei.pojo.TSftpConfig;
import com.neusoft.yunwei.mapper.TSftpConfigMapper;
import com.neusoft.yunwei.service.ITSftpConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-11-01
 */
@Service
public class TSftpConfigServiceImpl extends ServiceImpl<TSftpConfigMapper, TSftpConfig> implements ITSftpConfigService {

    @Autowired
    TSftpConfigMapper tSftpConfigMapper;


    @Override
    public List<String> selecttype(String province) {
        return tSftpConfigMapper.selecttype(province);
    }

    @Override
    public TSftpConfig selectAll(String province, String business_type) {
        return tSftpConfigMapper.selectAll(province,business_type);
    }
}
