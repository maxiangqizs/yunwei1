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
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TSouthUploadAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String ip;

    private String checkTime;

    private String businessType;

    private String port;

    private String collectStartTime;

    private String collectEndTime;


}
