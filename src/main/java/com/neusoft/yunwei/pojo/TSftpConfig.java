package com.neusoft.yunwei.pojo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhang
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TSftpConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String businessType;

    private String userName;

    private String password;


}
