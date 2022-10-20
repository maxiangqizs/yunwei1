/**
 * 
 */
package com.neusoft.yunwei.Utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Twitter-Snowflake ID生成算法
 * @author Delen.Yin
 * @Created 2017年12月22日
 */
public class IdUtil {

	//IP地址的D段数字
	private static int lastIpNum = -1 ;
	
	class IdGenerator {
		/** 
	     * SnowFlake算法 64位Long类型生成唯一ID 第一位0，表明正数 2-42，41位，表示毫秒时间戳差值，起始值自定义 
	     * 43-52，10位，机器编号，5位数据中心编号，5位进程编号 53-64，12位，毫秒内计数器 本机内存生成，性能高 
	     *  
	     * 主要就是三部分： 时间戳，进程id，序列号 时间戳41，id10位，序列号12位 
	     */  
	    private final static long beginTs = 1483200000000L;  
	    private long lastTs = 0L;  
	    private long processId;  
	    private int processIdBits = 10;  
	    private long sequence = 0L;  
	    private int sequenceBits = 12;  
	  
	    // 10位进程ID标识  
	    public IdGenerator(long processId) {  
	        if (processId > ((1 << processIdBits) - 1)) {  
	            throw new RuntimeException("进程ID超出范围，设置位数" + processIdBits + "，最大" + ((1 << processIdBits) - 1));  
	        }  
	        this.processId = processId;  
	    }  
	  
	    protected long timeGen() {  
	        return System.currentTimeMillis();  
	    }  
	  
	    public synchronized long nextId() {  
	        long ts = timeGen();  
	        if (ts < lastTs) {// 刚刚生成的时间戳比上次的时间戳还小，出错  
	            throw new RuntimeException("时间戳顺序错误");  
	        }  
	        if (ts == lastTs) {// 刚刚生成的时间戳跟上次的时间戳一样，则需要生成一个sequence序列号  
	            // sequence循环自增  
	            sequence = (sequence + 1) & ((1 << sequenceBits) - 1);
	            // 如果sequence=0则需要重新生成时间戳  
	            if (sequence == 0) {  
	                // 且必须保证时间戳序列往后  
	                ts = nextTs(lastTs);  
	            }  
	        } else {// 如果ts>lastTs，时间戳序列已经不同了，此时可以不必生成sequence了，直接取0  
	            sequence = 0L;  
	        }
	        lastTs = ts;// 更新lastTs时间戳  
	        return ((ts - beginTs) << (processIdBits + sequenceBits)) | (processId << sequenceBits) | sequence;  
	    }  
	  
	    protected long nextTs(long lastTs) {  
	        long ts = timeGen();  
	        while (ts <= lastTs) {  
	            ts = timeGen();  
	        }  
	        return ts;  
	    }  
	  
	}  
	
	/**
	 * 获取进程号
	 * @return
	 */
	public static final int getProcessID() {  
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();  
    } 
	
	/**
	 * 获取本机Ip
	 * @return
	 * @throws Exception
	 */
	public static InetAddress getLocalHostLANAddress() {
	    try {
	        InetAddress candidateAddress = null;
	        // 遍历所有的网络接口
	        for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // 在所有的接口下再遍历IP
	            for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
	                    if (inetAddr.isSiteLocalAddress()) {
	                        // 如果是site-local地址，就是它了
	                        return inetAddr;
	                    } else if (candidateAddress == null) {
	                        // site-local类型的地址未被发现，先记录候选地址
	                        candidateAddress = inetAddr;
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            return candidateAddress;
	        }
	        // 如果没有发现 non-loopback地址.只能用最次选的方案
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        return jdkSuppliedAddress;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
//	/**
//	 * 获取全局唯一ID
//	 * @return
//	 */
//	public static long nextId() {
//		if(lastIpNum == -1){
//			String ip = IdUtil.getLocalHostLANAddress().getHostAddress();
//			lastIpNum = Integer.valueOf(ip.substring(ip.lastIndexOf(".")+1));
//		}
//		return new IdUtil().new IdGenerator(lastIpNum).nextId();
//	}
	

	
	/**
	 * 获取全局唯一ID
	 * @return
	 */
	static IdGenerator idGenerator = null;
	static Random random=new Random();
	public synchronized static long nextId() {
		if(idGenerator == null){
			if(lastIpNum == -1){
				String ip = IdUtil.getLocalHostLANAddress().getHostAddress();
				lastIpNum = Integer.valueOf(ip.substring(ip.lastIndexOf(".")+1));
			}
			idGenerator = new IdUtil().new IdGenerator(lastIpNum);
		}
		try {
			Thread.sleep(random.nextInt(10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return idGenerator.nextId();
	}
	
	public static void main(String[] args) throws Exception {  
//        IdGenerator ig = new IdUtil().new IdGenerator(1023);  
//        String str = "20170101";  
//        System.out.println(new SimpleDateFormat("YYYYMMDD").parse(str).getTime());  
//        Set<Long> set = new HashSet<Long>();  
//        long begin = System.nanoTime();  
//        for (int i = 0; i < 10; i++) {  
//            set.add(ig.nextId());  
//        }  
//        System.out.println("time=" + (System.nanoTime() - begin)/1000.0 + " us");  
//        System.out.println(set.size());  
//        System.out.println(set);
		CountDownLatch latch = new CountDownLatch(5000);
		Set<Long> set = Collections.newSetFromMap(new ConcurrentHashMap<>());
		for(int i=0;i<5000;i++){
			new Thread(new Runnable(){
				@Override
				public void run() {
					set.add(IdUtil.nextId());
					latch.countDown();
//					System.out.println(IdUtil.nextId());
				}}).start();
		}
		latch.await();
		System.out.println(set.size());
		
    }  
	
}
