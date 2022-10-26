package com.neusoft.yunwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.yunwei.pojo.TSouthUploadAlr;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ma
 * @since 2022-10-24
 */

public interface ITSouthUploadAlrService extends IService<TSouthUploadAlr> {
    void insert(TSouthUploadAlr tSouthUploadAlr);
}
