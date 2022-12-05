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
 * @author Ma
 * @since 2022-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private BigDecimal progressValue;


}
