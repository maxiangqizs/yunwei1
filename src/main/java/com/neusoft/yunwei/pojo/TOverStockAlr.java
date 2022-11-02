package com.neusoft.yunwei.pojo;

import java.math.BigDecimal;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhang
 * @since 2022-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TOverStockAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String checkTime;

    private String ip;

    private String overStockSize;

    private String overStockDirectory;


}
