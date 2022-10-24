package com.neusoft.yunwei.service.impl;

import com.neusoft.yunwei.pojo.TDataCompressRatioInd;
import com.neusoft.yunwei.mapper.TDataCompressRatioIndMapper;
import com.neusoft.yunwei.service.ITDataCompressRatioIndService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhang
 * @since 2022-10-21
 */
@Service
public class TDataCompressRatioIndServiceImpl extends ServiceImpl<TDataCompressRatioIndMapper, TDataCompressRatioInd> implements ITDataCompressRatioIndService {
    @Autowired
    TDataCompressRatioIndMapper tDataCompressRatioIndMapper;
    @Override
    public void insert(TDataCompressRatioInd tDataCompressRatioInd) {
        tDataCompressRatioIndMapper.insert(tDataCompressRatioInd);
    }
}
