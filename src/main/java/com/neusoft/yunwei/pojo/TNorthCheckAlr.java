package com.neusoft.yunwei.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class TNorthCheckAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String checkTime;

    private String apiType;

    @TableField("Issue_task_count")
    private BigDecimal issueTaskCount;

    private BigDecimal queryConditionCount;

    private BigDecimal splitSqlCount;

    private BigDecimal packSqlCount;

    private BigDecimal executeSuccessCount;

    private BigDecimal queryFailCount;

    private BigDecimal uploadFailCount;

    @TableField("Querying_count")
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
