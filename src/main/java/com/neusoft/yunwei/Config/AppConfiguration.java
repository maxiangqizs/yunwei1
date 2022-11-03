package com.neusoft.yunwei.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;



//@AutoConfigureBefore(TimeTask.class)
//@Configuration
@Component
@Data
@ConfigurationProperties(prefix = "yunwei")
public class AppConfiguration {
	@Value("#{${yunwei.task-info:{}}}")
	private Map<Integer,Set<String>> taskInfo= new HashMap<>();
	@Value("${yunwei.threadPoolSize:4}")
	private Integer threadPoolSize;
	@Value("${yunwei.quartzIsStore:0}")
	private Integer quartzIsStore;
	@Value("${yunwei.quartzPoolSize:4}")
	private Integer quartzPoolSize;
	private List<Map<String,Object>> quartzInfos= new ArrayList<Map<String,Object>>();
//	public Map<Integer, Set<String>> getTaskInfo() {
//		return taskInfo;
//	}
//	public void setTaskInfo(Map<Integer, Set<String>> taskInfo) {
//		this.taskInfo = taskInfo;
//	}
//	public Integer getThreadPoolSize() {
//		return threadPoolSize;
//	}
//	public void setThreadPoolSize(Integer threadPoolSize) {
//		this.threadPoolSize = threadPoolSize;
//	}
	
}
