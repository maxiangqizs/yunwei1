package com.neusoft.yunwei.Task;


import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.neusoft.yunwei.Config.AppConfiguration;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;




/**
 *	定时任务处理数据
 *	
 */
@Component
public class TimeTask {
	
	private final static Logger LOG = LoggerFactory.getLogger(TimeTask.class);
	
	@Autowired
	AppConfiguration appConfiguration;
	@Autowired
	ApplicationContext applicationContext;

	@Resource
    private Scheduler scheduler ;
	private static Set<TaskInfo> waitTask=new HashSet();
	private static ExecutorService cachedThreadPool = null;
	private void runTask(){
		new Thread(new Runnable(){
			@Override
			public void run() {
		while(true){
			try {
				Thread.sleep(500);
				Date now=new Date();
				Iterator<TaskInfo> it=waitTask.iterator();
				while(it.hasNext()){
					TaskInfo task=it.next();
						if((task.getTaskState()==0||task.getTaskState()==-1)&&(task.getNextRunTime()==null||task.getNextRunTime().before(now))){
//								while(true){
									try{
									Future future = cachedThreadPool.submit(new Runnable(){
										@Override
										public void run() {
											// TODO Auto-generated method stub
//											task.setExcuteThread(Thread.currentThread());
											task.run();
										}
								});
//								 break;
									}catch(RejectedExecutionException r){
//										Thread.sleep(100);
									}catch(Exception e){
										LOG.error("",e);
									}
									
//								}
								 
//								 try {
//							            if(future.get()==null){//如果Future's get返回null，任务完成
//							                System.out.println("任务完成");
//							            }
//							        } catch (InterruptedException e) {
//							        } catch (Exception e) {
//							            //否则我们可以看看任务失败的原因是什么
//							           e.printStackTrace();
//							        }
							}
				}
			} catch (InterruptedException e) {
				LOG.error("",e);
			}
		}}},"dispatcher").start();
	}
	private void monitor(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					Iterator<TaskInfo> ir=TaskInfo.getMonitorResult().iterator();
					int quartzfinishNum = 0;
					int quartzwait=0;
					int userfinishNum = 0;
					int userwait=0;
					while(ir.hasNext()){
						TaskInfo to=ir.next();
						String taskType=to.getSleepMin()==null?"quartz":"userDefine";
						if(to.getTaskState()==0){
							String taskName=to.getTaskName();
							LOG.info(taskType+" task:"+taskName+" excute finished.");
							TaskInfo.getMonitorResult().remove(to);
							if(taskType.equals("quartz")){
								quartzfinishNum++;
							}else{
								userfinishNum++;
							}
						}else{
							if(taskType.equals("quartz")){
								quartzwait++;
							}else{
								userwait++;
							}
						}
					}
//					for(Future future:monitorResult.keySet()){
//							TaskInfo to=monitorResult.get(future);
//							if(to.getTaskState()==0){
//								String taskName=to.getTaskName();
//								System.out.println("task:"+taskName+" excute finished.");
//								monitorResult.remove(future);
//							}
//						}
					LOG.info("general excute view:quartz_finished("+quartzfinishNum+")/quartz_current_total("+(quartzfinishNum+quartzwait)+"),define_finished("+quartzfinishNum+")/define_current_total("+(quartzfinishNum+quartzwait)+")");
					if(!TaskInfo.getMonitorResult().isEmpty()){
						LOG.info("not end detailes:"+TaskInfo.getMonitorResult());
					}
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
			}
		},"monitor").start();
	}
	public void register(TaskInfo task){
		waitTask.add(task);
	}
	private TaskInfo getTaskInfo(String className){
		TaskInfo task = null;
		Class cs = null;
		try {
			cs=Class.forName(className);
//			TaskInfo task=(TaskInfo) cs.newInstance();
		} catch (Exception e) {
			LOG.error("",e);
			return null;
		}
		try{
			task=(TaskInfo) applicationContext.getBean(cs);
		}catch(NoSuchBeanDefinitionException e){
			try {
				task=(TaskInfo) cs.newInstance();
			} catch (Exception e1) {
				LOG.error("",e);
				return null;
			}
		}
		return task;
	}
	@PostConstruct
	public void init(){
		monitor();
		Map<Integer,Set<String>> taskInfo=appConfiguration.getTaskInfo();
		if(!taskInfo.isEmpty()&&appConfiguration.getThreadPoolSize()>0){//自定义方式
			cachedThreadPool = new ThreadPoolExecutor(0, appConfiguration.getThreadPoolSize(),
	                60L, TimeUnit.SECONDS,
	                new SynchronousQueue<Runnable>());
			runTask();
			
			
			for(Integer sleepMin:taskInfo.keySet()){
				Set<String> classSet=taskInfo.get(sleepMin);
				Iterator<String> ic=classSet.iterator();
				Date now=new Date();
				while(ic.hasNext()){
					String cn=ic.next();
					TaskInfo task = getTaskInfo(cn);
					if(task!=null){
						task.setSleepMin(sleepMin);
						task.setNextRunTime(now);
						register(task);
					}
				}
			}
		}
		List<Map<String,Object>> quartzInfos=appConfiguration.getQuartzInfos();
		if(!quartzInfos.isEmpty()){//quartz方式
			for(int i=0;i<quartzInfos.size();i++){
				Map<String,Object> quartzInfo=quartzInfos.get(i);
				String cronStr=(String) quartzInfo.get("cron");
				LinkedHashMap<String,String> classSet=(LinkedHashMap) quartzInfo.get("tasks");
				for(String className:classSet.values()){
					try {
//						Class clazz=Class.forName(className);
//						JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(clazz.getName(), "dispatcher-group").build();
//						CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger() // 创建一个新的TriggerBuilder来规范一个触发器
//								.withIdentity(clazz.getName(), "dispatcher-trigger") // 给触发器起一个名字和组名
//								.withSchedule(CronScheduleBuilder.cronSchedule(cronStr)).build();
						TaskInfo task=getTaskInfo(className);
//						JobDetail jobDetail = (JobDetail) task;
						JobDetail jobDetail = JobBuilder.newJob(task.getClass()).withIdentity(task.getTaskName(), "dispatcher-group").build();
						CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger() // 创建一个新的TriggerBuilder来规范一个触发器
								.withIdentity(task.getTaskName(), "dispatcher-trigger") // 给触发器起一个名字和组名
								.withSchedule(CronScheduleBuilder.cronSchedule(cronStr)).build();
						scheduler.scheduleJob(jobDetail, trigger);
					} catch (Exception e) {
						LOG.error("",e);
					}
				}
			}
//				for(String cronStr:quartzInfo.keySet()){
//					Set<String> classSet=quartzInfo.get(cronStr);
//					Iterator<String> ic=classSet.iterator();
//					while(ic.hasNext()){
//						String cn=ic.next();
//						Class cs=null;
//						try {
//							cs = Class.forName(cn);
//							JobDetail jobDetail = JobBuilder.newJob(cs).withIdentity(cs.getName(), "dispatcher-group").build();
//							CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger() // 创建一个新的TriggerBuilder来规范一个触发器
//									.withIdentity(cs.getName(), "dispatcher-trigger") // 给触发器起一个名字和组名
//									.withSchedule(CronScheduleBuilder.cronSchedule(cronStr)).build();
//							scheduler.scheduleJob(jobDetail, trigger);
//						} catch (Exception e) {
//							LOG.error("",e);
//						}
//					}
//				}
				
//			try {
////				 SchedulerFactoryBean factory = new SchedulerFactoryBean();
//				
//				Scheduler scheduler=StdSchedulerFactory.getDefaultScheduler();
//				scheduler.
//				JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build(); // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
//				CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger() // 创建一个新的TriggerBuilder来规范一个触发器
//						.withIdentity(jobName, TRIGGER_GROUP_NAME) // 给触发器起一个名字和组名
//						.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
//				scheduler.scheduleJob(jobDetail, trigger);
//				if (!scheduler.isShutdown()) {
//					scheduler.start(); // 启动
//				}
//			} catch (SchedulerException e) {
//				e.printStackTrace();
//			}
		}
		
//		
//		System.out.println("111"+appConfiguration.getTaskInfo());
//		dealService.test();
//		System.out.println("222"+dealService.appConfiguration);
	}
	public static void main(String[] arg){
		TimeTask tt=new TimeTask();
		for(int i=0;i<10;i++){
//			TimeTask.TestTask ttt=tt.new TestTask();
//			ttt.setSleepMin(1);
//			tt.register(ttt);
		}
	}
}
