package com.neusoft.yunwei.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhang
 * @since 2022-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String bizCode;

    private String stage;

    private Integer rowCount;

    private BigDecimal sizeMb;

    private Integer errorRowCount;

    private String ip;

    private Long logTime;


}
