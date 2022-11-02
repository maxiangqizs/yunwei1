package com.neusoft.yunwei.service;

import com.neusoft.yunwei.pojo.TOverStockAlr;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhang
 * @since 2022-10-31
 */
@Service
public interface ITOverStockAlrService extends IService<TOverStockAlr> {


   void insert(TOverStockAlr tOverStockAlr);
}
