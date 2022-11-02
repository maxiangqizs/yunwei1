package com.neusoft.yunwei.service.impl;

import com.neusoft.yunwei.pojo.TOverStockAlr;
import com.neusoft.yunwei.mapper.TOverStockAlrMapper;
import com.neusoft.yunwei.service.ITOverStockAlrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-10-31
 */
@Service
public class TOverStockAlrServiceImpl extends ServiceImpl<TOverStockAlrMapper, TOverStockAlr> implements ITOverStockAlrService {
    @Autowired
    TOverStockAlrMapper tOverStockAlrMapper;

    @Override
    public void insert(TOverStockAlr tOverStockAlr) {
        tOverStockAlrMapper.insert(tOverStockAlr);
    }
}
