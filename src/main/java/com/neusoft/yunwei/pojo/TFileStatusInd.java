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
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TFileStatusInd implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String checkTime;

    private String businessType;

    private BigDecimal totalFileCount;

    private BigDecimal totalFileSize;

    private BigDecimal abnormalFileSize;

    private BigDecimal dataCount;

    private BigDecimal abnormalDataCount;

    private BigDecimal dataSize;

    private String collectStartTime;

    private String collectEndTime;


}
