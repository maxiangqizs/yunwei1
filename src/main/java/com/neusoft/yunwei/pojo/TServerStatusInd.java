package com.neusoft.yunwei.pojo;

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
public class TServerStatusInd implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String cluster;

    private String ip;

    private String checkTime;

    private String ifServerReset;

    private String resetTime;

    private String loadBalance;

    private String collectStartTime;

    private String collectEndTime;


}
