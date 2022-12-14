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
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String service;

    private String status;

    private String updateTime;


}
