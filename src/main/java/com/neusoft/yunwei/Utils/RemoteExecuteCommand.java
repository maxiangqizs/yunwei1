package  com.neusoft.yunwei.Utils;

/**
 * 远程执行linux的shell script
 */
public class RemoteExecuteCommand {
//    //字符编码默认是utf-8
//    private static String  DEFAULTCHART="UTF-8";
//    private Connection conn;
//    private String ip;
//    private int port=22;
//    private String userName;
//    private String userPwd;
//
//    public RemoteExecuteCommand(String ip, String userName, String userPwd) {
//        this.ip = ip;
//        this.userName = userName;
//        this.userPwd = userPwd;
//    }
//    public RemoteExecuteCommand(String ip, int port, String userName, String userPwd) {
//        this.ip = ip;
//        this.port=port;
//        this.userName = userName;
//        this.userPwd = userPwd;
//    }
//
//
//    public RemoteExecuteCommand() {
//
//    }
//
//    /**
//     * 远程登录linux的主机
//     * @return
//     *      登录成功返回true，否则返回false
//     */
//    public Boolean login(){
//        boolean flg=false;
//        try {
//            conn = new Connection(ip,port);
//            conn.connect();//连接
//            //conn.connect(null, 10000000, 10000000);
//            flg=conn.authenticateWithPassword(userName, userPwd);//认证
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return flg;
//    }
//    /**
//     * 远程执行shll脚本或者命令
//     * @param cmd
//     *      即将执行的命令
//     * @return
//     *      命令执行完后返回的结果值
//     */
//    public String execute(String cmd){
//        String result="";
//        try {
//            if(login()){
//                Session session= conn.openSession();//打开一个会话
//                session.execCommand(cmd);//执行命令
//                result=processStdout(session.getStdout(),DEFAULTCHART);
//                //如果为得到标准输出为空，说明脚本执行出错了
//                if(StringUtils.isBlank(result)){
//                    result=processStdout(session.getStderr(),DEFAULTCHART);
//                }
//                conn.close();
//                session.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//        return result;
//    }
//
//
//    /**
//     * 远程执行shll脚本或者命令
//     * @param cmd
//     *      即将执行的命令
//     * @return
//     *      命令执行成功后返回的结果值，如果命令执行失败，返回空字符串，不是null
//     */
//    /*public String executeSuccess(String cmd){
//        String result="";
//        try {
//            if(login()){
//                Session session= conn.openSession();//打开一个会话
//                session.execCommand(cmd);//执行命令
//                result=processStdout(session.getStdout(),DEFAULTCHART);
//                conn.close();
//                session.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }*/
//
//    public String executeSuccess2(String cmd)throws Exception{
//        String result="";
//        try {
//            if(login()){
//                Session session= conn.openSession();//打开一个会话
//                session.execCommand(cmd);//执行命令
//
//                InputStream    stdout = new StreamGobbler(session.getStdout());
//                InputStream    stderr = new StreamGobbler(session.getStderr());
//                System.out.println("-------------命令开始执行------------");
//                result=processStdout(stdout,DEFAULTCHART);
//                result+="@@"+processStdout(stderr,DEFAULTCHART);
//                conn.close();
//                session.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw e;
//        }
//        return result;
//    }
//
//    public String executeSuccess3(String cmd,String taskName)throws Exception{
//        String result="";
//        InputStream    stdout=null;
//        InputStream    stderr=null;
//        try {
//            if(login()){
//                Session session= conn.openSession();//打开一个会话
//                session.execCommand(cmd);//执行命令
//
//                stdout = new StreamGobbler(session.getStdout());
//                stderr = new StreamGobbler(session.getStderr());
//                StreamGobbler gobbler=new StreamGobbler(session.getStdout());
//                System.out.println("-------------任务开始执行-------------");
//
//                LogResult logResult=new LogResult("stdout");
//                LogResult errLogResult=new LogResult("stderr");
//                LogProcessThread stdoutProcess=new LogProcessThread(stdout,Thread.currentThread().getName(),logResult,taskName);
//                LogProcessThread stderrProcess=new LogProcessThread(stderr,Thread.currentThread().getName(),errLogResult,taskName);
//                stdoutProcess.start();
//                stderrProcess.start();
//
//                while(true) {
//                    Thread.currentThread().sleep(1000);
//
//                    if(logResult.getStopFlag().get()&&errLogResult.getStopFlag().get()) {
//                        break;
//                    }
//                }
//                result+="stdout:"+logResult.getResult();
//                result+="stderr:"+errLogResult.getResult();
//                conn.close();
//                session.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw e;
//        }  catch (InterruptedException e1) {
//            stdout.close();
//            stderr.close();
//            throw e1;
//        }
//        return result;
//    }
//    class LogResult{
//        private String result;
//        private AtomicBoolean stopFlag=new AtomicBoolean(false);
//        private String type;
//        LogResult(String type){
//            this.type=type;
//        }
//        public String getResult() {
//            return result;
//        }
//        public void setResult(String result) {
//            this.result = result;
//        }
//        public AtomicBoolean getStopFlag() {
//            return stopFlag;
//        }
//        public void setStopFlag(AtomicBoolean stopFlag) {
//            this.stopFlag = stopFlag;
//        }
//        public String getType() {
//            return type;
//        }
//        public void setType(String type) {
//            this.type = type;
//        }
//    }
//    //日志处理线程
//    class LogProcessThread extends Thread{
//        private InputStream is;
//        private String threadName;
//        private LogResult logResult;
//        private String taskName;
//        LogProcessThread(InputStream is,String threadName,LogResult logResult,String taskName){
//            this.is=is;
//            this.threadName=threadName;
//            this.logResult=logResult;
//            this.taskName=taskName;
//        }
//        public void run() {
//            Thread.currentThread().setName(threadName+"_"+logResult.getType());
//
//            StringBuffer buffer = new StringBuffer();
//            try {
//                BufferedReader br = new BufferedReader(new InputStreamReader(is,DEFAULTCHART));
//                String line=null;
//                while((line=br.readLine()) != null){
//                    System.out.println(taskName+":"+line);
//	            	/*int urlIndex = line.indexOf("tracking URL:");
//	            	if (urlIndex > 0) {
//						String url = line.substring(urlIndex);
//						String applicationId = url.substring(url.indexOf("application"));
//						applicationId = applicationId.substring(0, applicationId.indexOf("/"));
//						System.out.println(logResult.getType()+"##########applicationId:" + applicationId+",processCode:"+processCode);
//					}*/
//
//                    buffer.append(line+"\n");
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            logResult.setResult(buffer.toString());
//            logResult.setStopFlag(new AtomicBoolean(true));
//        }
//
//    }
//    /**
//     * 解析脚本执行返回的结果集
//     * @param in 输入流对象
//     * @param charset 编码
//     * @return
//     *       以纯文本的格式返回
//     */
//    private String processStdout(InputStream in, String charset){
//        //InputStream    stdout = new StreamGobbler(in);
//        StringBuffer buffer = new StringBuffer();
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(in,charset));
//            String line=null;
//            while((line=br.readLine()) != null){
//
//                buffer.append(line+"\n");
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return buffer.toString();
//    }
//    private String processStdout2(InputStream in, String charset,String taskName){
//        //InputStream    stdout = new StreamGobbler(in);
//        StringBuffer buffer = new StringBuffer();
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(in,charset));
//            String line=null;
//            while((line=br.readLine()) != null){
//                int urlIndex = line.indexOf("tracking URL:");
//                if (urlIndex > 0) {
//                    String url = line.substring(urlIndex);
//                    String applicationId = url.substring(url.indexOf("application"));
//                    applicationId = applicationId.substring(0, applicationId.indexOf("/"));
//                    System.out.println("##########applicationId:" + applicationId+",taskName:"+taskName);
//                }else{
//
//                }
//
//                buffer.append(line+"\n");
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return buffer.toString();
//    }
//
//    public static void setCharset(String charset) {
//        DEFAULTCHART = charset;
//    }
//    public Connection getConn() {
//        return conn;
//    }
//    public void setConn(Connection conn) {
//        this.conn = conn;
//    }
//    public String getIp() {
//        return ip;
//    }
//    public void setIp(String ip) {
//        this.ip = ip;
//    }
//    public String getUserName() {
//        return userName;
//    }
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//    public String getUserPwd() {
//        return userPwd;
//    }
//    public void setUserPwd(String userPwd) {
//        this.userPwd = userPwd;
//    }
//
//    /*
//     * @param cmd -- command命令
//     * @param processCode -- 流程名
//     * @return CmdResult
//     * @throws Exception
//     */
//    public CmdResult executeCmd(String cmd, String processCode)throws Exception{
//        CmdResult result=  new CmdResult();
//        InputStream stdout=null;
//        InputStream stderr=null;
//        Session session= null;
//        try {
//            if(login()){
//                session= conn.openSession();//打开一个会话
//                session.execCommand(cmd);//执行命令
//                stdout = new StreamGobbler(session.getStdout());
//                stderr = new StreamGobbler(session.getStderr());
//                System.out.println("-------------任务开始执行-------------");
//                LogResult logResult=new LogResult("stdout");
//                LogResult errLogResult=new LogResult("stderr");
//                LogProcessThread stdoutProcess=new LogProcessThread(stdout,Thread.currentThread().getName(),logResult,processCode);
//                LogProcessThread stderrProcess=new LogProcessThread(stderr,Thread.currentThread().getName(),errLogResult,processCode);
//                stdoutProcess.start();
//                stderrProcess.start();
//                while(true) {
//                    Thread.currentThread().sleep(1000);
//                    if(logResult.getStopFlag().get()&&errLogResult.getStopFlag().get()) {
//                        break;
//                    }
//                }
//                result.setInfo(logResult.getResult());
//                result.setError(errLogResult.getResult());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw e;
//        }  catch (InterruptedException e1) {
//            throw e1;
//        }finally{
//            if(stdout!=null){
//                stdout.close();
//            }
//            if(stderr!=null){
//                stderr.close();
//            }
//            if(session!=null){
//                session.close();
//            }
//            if(conn!=null){
//                conn.close();
//            }
//        }
//        return result;
//    }
}
