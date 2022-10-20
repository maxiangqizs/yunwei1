package com.neusoft.yunwei.Entity;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("log_store_audit_file_info")
public class LogStoreAuditFileInfo extends BaseInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3534195873592487786L;
	@TableId(type = IdType.AUTO)
	private Long id;
	private String auditFileName;
	private String dataFileName;
	private Long fileTime;
	private Long createTime;
	private Long updateTime;
	private String currentIp;
	private Integer checkState;//0成功，1失败
	private Integer reportState;//0成功，1失败，2上报中
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String failCause;
	private String reportInfo;
	private Integer status;//1核对中，0已稽核，2待核对
	private String bizCode;
	private String subCode;
	
}
