package com.neusoft.yunwei.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ma
 * @since 2022-10-31
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
}
