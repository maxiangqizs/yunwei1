package com.neusoft.yunwei.Entity;

import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
public class BaseInfo {
	private transient CountDownLatch countDownLatch;
	private transient String stateMsg="search";
	public void close(){
		if(!stateMsg.equals("close")){
			countDownLatch.countDown();
			stateMsg="close";
		}
	}
}
