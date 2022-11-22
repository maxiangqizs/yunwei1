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
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TSouthFileProcessAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String checkTime;

    private String businessType;

    private BigDecimal checkReupload;

    private BigDecimal checkFailReupload;

    private BigDecimal auditReupload;

    private BigDecimal auditFailReupload;

    private BigDecimal abnormalFileName;

    private BigDecimal abnormalCreateTime;

    private BigDecimal abnormalCompleteFile;

    private BigDecimal abnormalReadCompressFile;

    private BigDecimal lostFile;

    private BigDecimal totalFileCount;

    private BigDecimal completeRatio;

    private String collectStartTime;

    private String collectEndTime;


}
