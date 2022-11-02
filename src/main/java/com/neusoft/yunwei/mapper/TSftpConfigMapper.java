package com.neusoft.yunwei.mapper;

import com.neusoft.yunwei.pojo.TSftpConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2022-11-01
 */
@Repository
public interface TSftpConfigMapper extends BaseMapper<TSftpConfig> {


    List<String> selecttype(String province);

    TSftpConfig selectAll(String province, String business_type);
}
