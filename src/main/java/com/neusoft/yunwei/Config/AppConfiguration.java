package com.neusoft.yunwei.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



//@AutoConfigureBefore(TimeTask.class)
//@Configuration
@Component
@Data
@ConfigurationProperties(prefix = "dispatcher")
public class AppConfiguration {
	@Value("#{${dispatcher.task-info:{}}}")
	private Map<Integer,Set<String>> taskInfo= new HashMap<>();
	@Value("${dispatcher.threadPoolSize:4}")
	private Integer threadPoolSize;
	@Value("${dispatcher.quartzIsStore:0}")
	private Integer quartzIsStore;
	@Value("${dispatcher.quartzPoolSize:4}")
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
