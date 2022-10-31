package com.neusoft.yunwei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.yunwei.mapper.TFileStatusIndMapper;
import com.neusoft.yunwei.pojo.TFileStatusInd;
import com.neusoft.yunwei.service.ITFileStatusIndService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ma
 * @since 2022-10-27
 */
@Service
public class TFileStatusIndServiceImpl extends ServiceImpl<TFileStatusIndMapper, TFileStatusInd> implements ITFileStatusIndService {
    @Autowired
    TFileStatusIndMapper tFileStatusIndMapper;
    @Override
    public void insert(TFileStatusInd tFileStatusInd) {
        tFileStatusIndMapper.insert(tFileStatusInd);
    }
}
