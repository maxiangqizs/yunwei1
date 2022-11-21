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
 * @since 2022-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TMysqlConnectInd implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String cluster;

    private String checkTime;

    private String mysqlClusterConnect;

    private String clusterSize;

    private String ip;


}
