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
 * @author Ma
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TDiskUseRatioAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String cluster;

    private String ip;

    private String diskName;

    private String diskUse;

    private String checkTime;

    private String collectStartTime;

    private String collectEndTime;


}
