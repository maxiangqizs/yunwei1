package com.neusoft.yunwei.service.impl;

import com.neusoft.yunwei.pojo.CodeTable;
import com.neusoft.yunwei.mapper.CodeTableMapper;
import com.neusoft.yunwei.service.ICodeTableService;
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
 * @since 2022-10-19
 */
@Service
public class CodeTableServiceImpl extends ServiceImpl<CodeTableMapper, CodeTable> implements ICodeTableService {
    @Autowired
    CodeTableMapper codeTableMapper;

    @Override
    public CodeTable selectByIp(String lastIp) {
        return codeTableMapper.selectByIp(lastIp);
    }
}
