package com.neusoft.yunwei.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public abstract class TaskInfo implements Job{
	private static Set<TaskInfo> monitorResult=new CopyOnWriteArraySet();
	public static Set<TaskInfo> getMonitorResult(){
		return monitorResult;
	}
	private String taskName=this.getClass().getName()+"@"+this.hashCode();
	private int taskState=-1;
	private Date nextRunTime=new Date();
	private Integer sleepMin;
	private Calendar cr=new GregorianCalendar();
	private Thread excuteThread;
	
	public int getTaskState() {
		return taskState;
	}
	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}
	public Date getNextRunTime() {
		return nextRunTime;
	}
	public void setNextRunTime(Date nextRunTime) {
		this.nextRunTime = nextRunTime;
	}
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public void setNextRunTime() {
		if(sleepMin!=null){
			if(nextRunTime==null){
				nextRunTime=new Date();
			}
			cr.setTime(nextRunTime);
			cr.add(12, sleepMin);
			nextRunTime=cr.getTime();
		}
	}
	public Integer getSleepMin() {
		return sleepMin;
	}
	public void setSleepMin(Integer sleepMin) {
		this.sleepMin = sleepMin;
	}
	public void run(){
		setTaskState(1);
		setNextRunTime();
		monitorResult.add(this);
		this.setExcuteThread(Thread.currentThread());
//		try{
//		throw new RuntimeException("");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		try {
			excuteTask();
			setTaskState(0);
		}catch (Exception e){
			e.printStackTrace();
		}

	};
	 public static String printStr(Object[] a) {
	        if (a == null)
	            return "null";

	        int iMax = a.length - 1;
	        if (iMax == -1)
	            return "[]";
	        for (int i = 0;i< a.length; i++) {
	        	String line=String.valueOf(a[i]);
	        	int index1=line.lastIndexOf("(");
	        	String tmpStr=line;
	        	if(index1>0){
	        		tmpStr=line.substring(0,index1);
	        	}
	        	int index2=tmpStr.lastIndexOf(".");
	        	String className = null;
	        	if(index2>0){
	        		className=tmpStr.substring(0,index2);
	        	}
	        	if(className!=null){
		        	try {
						if(TaskInfo.class.isAssignableFrom(Class.forName(className))){
							return line;
						}
					} catch (ClassNotFoundException e) {
						return "";
					}
	        	}
	        }
	        return String.valueOf(a[0]);
	    }
	@Override
	public String toString() {
		String pos=excuteThread==null?"":",currentPosition:"+printStr(excuteThread.getStackTrace());
		return "TaskInfo [taskName=" + taskName + ", taskState=" + taskState
				+ ", nextRunTime=" + nextRunTime + ", sleepMin=" + sleepMin
				+pos +"]";
	}
	public Thread getExcuteThread() {
		return excuteThread;
	}
	public void setExcuteThread(Thread excuteThread) {
		this.excuteThread = excuteThread;
	}
	public abstract void excuteTask();
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		run();
		
	}
	public static void main(String[] a){
		 Set<String> monitorResult=new CopyOnWriteArraySet();
		 monitorResult.add("");
		 monitorResult.add("1");
		 monitorResult.remove("");
		 System.out.println(monitorResult);
		 System.out.println(monitorResult.size());
	}
}
