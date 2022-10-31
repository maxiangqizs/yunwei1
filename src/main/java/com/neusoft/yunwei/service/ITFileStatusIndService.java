package com.neusoft.yunwei.service;

import com.neusoft.yunwei.pojo.TFileStatusInd;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ma
 * @since 2022-10-27
 */
public interface ITFileStatusIndService extends IService<TFileStatusInd> {

   public void insert(TFileStatusInd tFileStatusInd);
}
