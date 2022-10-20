package com.neusoft.yunwei.Config;

import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;



public class ApplicationContextHodler extends AdaptableJobFactory{
	private static ApplicationContext applicationContext;
	private transient AutowireCapableBeanFactory beanFactory;
	private final static Logger LOG = LoggerFactory.getLogger(ApplicationContextHodler.class);
	public void setApplicationContext(final ApplicationContext applicationContext){
		ApplicationContextHodler.applicationContext=applicationContext;
		beanFactory=applicationContext.getAutowireCapableBeanFactory();
	}
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception{
		final Object job=super.createJobInstance(bundle);
		try{
			beanFactory.autowireBean(job);
		}catch(Exception e){
			LOG.error("", e);
		}
		return job;
	}
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
}
