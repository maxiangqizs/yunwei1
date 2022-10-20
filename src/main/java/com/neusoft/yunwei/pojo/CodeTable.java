package com.neusoft.yunwei.pojo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class CodeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String provice;

    private String cluster;

    private String ip;

    private String updateTime;


}
