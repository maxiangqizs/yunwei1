package com.neusoft.yunwei.service;

import com.neusoft.yunwei.pojo.TSftpConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhang
 * @since 2022-11-01
 */
public interface ITSftpConfigService extends IService<TSftpConfig> {


    List<String> selecttype(String province);

    TSftpConfig selectAll(String province, String business_type);
}
