package com.neusoft.yunwei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.yunwei.mapper.TSouthUploadAlrMapper;
import com.neusoft.yunwei.pojo.TSouthUploadAlr;
import com.neusoft.yunwei.service.ITSouthUploadAlrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ma
 * @since 2022-10-24
 */
@Service
public class TSouthUploadAlrServiceImpl extends ServiceImpl<TSouthUploadAlrMapper, TSouthUploadAlr> implements ITSouthUploadAlrService {
    @Autowired
    TSouthUploadAlrMapper tSouthUploadAlrMapper;
    @Override
    public void insert(TSouthUploadAlr tSouthUploadAlr) {
        tSouthUploadAlrMapper.insert(tSouthUploadAlr);
    }
}
