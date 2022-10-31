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
 * @since 2022-10-27
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public BigDecimal getTotalFileCount() {
        return totalFileCount;
    }

    public void setTotalFileCount(BigDecimal totalFileCount) {
        this.totalFileCount = totalFileCount;
    }

    public BigDecimal getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(BigDecimal totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public BigDecimal getAbnormalFileSize() {
        return abnormalFileSize;
    }

    public void setAbnormalFileSize(BigDecimal abnormalFileSize) {
        this.abnormalFileSize = abnormalFileSize;
    }

    public BigDecimal getDataCount() {
        return dataCount;
    }

    public void setDataCount(BigDecimal dataCount) {
        this.dataCount = dataCount;
    }

    public BigDecimal getAbnormalDataCount() {
        return abnormalDataCount;
    }

    public void setAbnormalDataCount(BigDecimal abnormalDataCount) {
        this.abnormalDataCount = abnormalDataCount;
    }

    public BigDecimal getDataSize() {
        return dataSize;
    }

    public void setDataSize(BigDecimal dataSize) {
        this.dataSize = dataSize;
    }
}
