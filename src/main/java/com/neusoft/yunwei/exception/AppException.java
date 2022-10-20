package com.neusoft.yunwei.exception;

/**
 * 封装异常类
 * @author Delen
 * @Created 2015年1月8日
 */
public class AppException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 656298048685356093L;

    /**
     * 构造方法
     * 
     * @param msg 错误信息
     */
    public AppException(String msg) {
        super(msg);
    }
    
    /**
     * 构造方法
     * 
     */
    public AppException() {
        super();
    }
    
    /**
     * 构造方法
     * 
     * @param msg 错误信息
     * 
     * @param t 异常堆栈
     */
    public AppException(String msg, Throwable t) {
        super(msg, t);
    }
}
