package com.neusoft.yunwei.pojo;

import java.math.BigDecimal;
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
 * @since 2022-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Repository
public class TDataCompressRatioInd implements Serializable {

    private static final long serialVersionUID = 1L;

    private String province;

    private String dataType;

    private String beforeCompress;

    private String totalBeforeCompress;

    private String totalAfterCompress;

    private String compressRatio;

    private String checkTime;

    private String processPerformance;


}
