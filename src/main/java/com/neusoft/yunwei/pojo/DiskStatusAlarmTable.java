package com.neusoft.yunwei.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhang
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
@AllArgsConstructor
@NoArgsConstructor
public class DiskStatusAlarmTable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String cluster;

    private String ip;

    private String diskName;

    private String diskUsage;

    private String patrolTime;



}
