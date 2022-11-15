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
 * @since 2022-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TNorthLogProcessAlr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String ip;

    private String checkTime;

    private String schedulerErrorLog;

    private String ferryBuilderErrorLog;

    private String northFaceLog;

    private String schedulerErrorProc;

    private String ferryBuilderErrorProc;

    private String northFaceProc;


}
