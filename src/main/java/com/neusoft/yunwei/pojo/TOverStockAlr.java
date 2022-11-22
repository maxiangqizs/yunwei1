package com.neusoft.yunwei.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ma
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TOverStockAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String checkTime;

    private String ip;

    private BigDecimal overStockSize;

    private String overStockDirectory;

    private String collectStartTime;

    private String collectEndTime;


}
