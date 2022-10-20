package com.neusoft.yunwei.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TimerUtils implements Runnable {
	
	private final static Logger logger = LoggerFactory.getLogger(TimerUtils.class);
	
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	static {
		new Thread(new TimerUtils(), "TimerUtil-Thread").start();
	}
	
	public static volatile long currentTimeMillis = System.currentTimeMillis();
	
	public static volatile long currentTimeSeconds = System.currentTimeMillis() / 1000;
	
	public static volatile long currentTimeMinutes = System.currentTimeMillis() / 1000 / 60;
	
	public static volatile String currentTimeFormat = format.format(new Date(currentTimeMillis));
	
	public static volatile long currentTimeFormatLong = Long.parseLong(format2.format(new Date(currentTimeMillis)));

	public static volatile long currentTimeMinutesLong = currentTimeFormatLong/100;
	
	@Override
	public void run() {
		while(true){
			currentTimeMillis = System.currentTimeMillis();
			currentTimeSeconds = currentTimeMillis / 1000;
			currentTimeMinutes = currentTimeSeconds / 60;
			currentTimeFormat = format.format(new Date(currentTimeMillis));
			currentTimeFormatLong = Long.parseLong(format2.format(new Date(currentTimeMillis)));
			currentTimeMinutesLong = currentTimeFormatLong/100;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("TimerUtil InterruptedException", e);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(TimerUtils.currentTimeFormatLong / 1000000);
		
	}
}
