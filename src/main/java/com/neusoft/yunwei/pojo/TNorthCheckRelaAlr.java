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
 * @since 2022-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TNorthCheckRelaAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String checkTime;

    private String apiType;

    private BigDecimal issueTaskCount;

    private BigDecimal queryConditionCount;

    private BigDecimal splitSqlCount;

    private BigDecimal packSqlCount;

    private BigDecimal executeSuccessCount;

    private BigDecimal queryFailCount;

    private BigDecimal uploadFailCount;

    private BigDecimal queryingCount;

    private BigDecimal uploadingCount;

    private BigDecimal lockConditionInCount;

    private BigDecimal lockConditionOutCount;

    private BigDecimal notLockConditionCount;

    private BigDecimal fetureQueryHisCount;

    private BigDecimal fetureQueryCount;

    private BigDecimal rangeMore24hCount;

    private BigDecimal rangeLess24hCount;

    private BigDecimal uploadCount;

    private BigDecimal splitMaxTime;

    private BigDecimal sparkMaxTime;

    private BigDecimal uploadMaxTime;

    private BigDecimal totalMaxTime;

    private BigDecimal totalConditionCount;

    private String collectStartTime;

    private String collectEndTime;


}
