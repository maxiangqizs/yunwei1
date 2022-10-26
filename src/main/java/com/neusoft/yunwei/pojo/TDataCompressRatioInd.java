package com.neusoft.yunwei.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhang
 * @since 2022-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TDataCompressRatioInd implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String dataType;

    private String beforeCompress;

    private String totalBeforeCompress;

    private String totalAfterCompress;

    private String compressRatio;

    private String checkTime;

    private String processPerformance;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getBeforeCompress() {
        return beforeCompress;
    }

    public void setBeforeCompress(String beforeCompress) {
        this.beforeCompress = beforeCompress;
    }

    public String getTotalBeforeCompress() {
        return totalBeforeCompress;
    }

    public void setTotalBeforeCompress(String totalBeforeCompress) {
        this.totalBeforeCompress = totalBeforeCompress;
    }

    public String getTotalAfterCompress() {
        return totalAfterCompress;
    }

    public void setTotalAfterCompress(String totalAfterCompress) {
        this.totalAfterCompress = totalAfterCompress;
    }

    public String getCompressRatio() {
        return compressRatio;
    }

    public void setCompressRatio(String compressRatio) {
        this.compressRatio = compressRatio;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getProcessPerformance() {
        return processPerformance;
    }

    public void setProcessPerformance(String processPerformance) {
        this.processPerformance = processPerformance;
    }
}
