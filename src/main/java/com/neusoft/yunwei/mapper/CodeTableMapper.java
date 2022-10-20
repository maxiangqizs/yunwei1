package com.neusoft.yunwei.mapper;

import com.neusoft.yunwei.pojo.CodeTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-10-19
 */

@Repository
public interface CodeTableMapper extends BaseMapper<CodeTable> {


    CodeTable selectByIp(String lastIp);
}
