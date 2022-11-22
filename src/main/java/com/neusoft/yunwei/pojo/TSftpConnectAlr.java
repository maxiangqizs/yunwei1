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
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TSftpConnectAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String ip;

    private String checkTime;

    private String collectStartTime;

    private String collectEndTime;


}
