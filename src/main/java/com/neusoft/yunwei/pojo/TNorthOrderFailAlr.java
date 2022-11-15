package com.neusoft.yunwei.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ma
 * @since 2022-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TNorthOrderFailAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String checkTime;

    private BigDecimal orderFailCount;


}
