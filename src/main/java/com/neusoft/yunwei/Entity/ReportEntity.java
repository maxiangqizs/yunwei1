package com.neusoft.yunwei.Entity;

import java.io.File;
import java.io.Serializable;

public class ReportEntity implements Serializable{
	private static final long serialVersionUID = -3926102330796208568L;
	private String city;
	
	private String dataFileName;
	
	private String checkFileName;
	
	private String filePath;
	
//	private String bizCode;
	
	private Integer fileRows;
	
	private Integer checkState;
	
	private Long fileSize;
	
	private Integer reportState;
	
	private String reportCause;
	
	private String failCause;
	
	private String reportTime;
	
	private String reportInfo;
	
	private String subcode;

	@Override
	public String toString() {
		return "ReportEntity [city=" + city + ", dataFileName=" + dataFileName
				+ ", checkFileName=" + checkFileName + ", filePath=" + filePath
				+ ", fileRows=" + fileRows
				+ ", checkState=" + checkState + ", fileSize=" + fileSize
				+ ", reportState=" + reportState + ", reportCause="
				+ reportCause + ", failCause=" + failCause + ", reportTime="
				+ reportTime + ", reportInfo=" + reportInfo + ", subcode="
				+ subcode + "]";
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public String getCheckFileName() {
		return checkFileName;
	}

	public void setCheckFileName(String checkFileName) {
		this.checkFileName = checkFileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

//	public String getBizCode() {
//		return bizCode;
//	}
//
//	public void setBizCode(String bizCode) {
//		this.bizCode = bizCode;
//	}

	public Integer getFileRows() {
		return fileRows;
	}

	public void setFileRows(Integer fileRows) {
		this.fileRows = fileRows;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getReportState() {
		return reportState;
	}

	public void setReportState(Integer reportState) {
		this.reportState = reportState;
	}

	public String getReportCause() {
		return reportCause;
	}

	public void setReportCause(String reportCause) {
		this.reportCause = reportCause;
	}

	public String getFailCause() {
		return failCause;
	}

	public void setFailCause(String failCause) {
		this.failCause = failCause;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getReportInfo() {
		return reportInfo;
	}

	public void setReportInfo(String reportInfo) {
		this.reportInfo = reportInfo;
	}

	public String getSubcode() {
		return subcode;
	}

	public void setSubcode(String subcode) {
		this.subcode = subcode;
	}
	
	
	
}
