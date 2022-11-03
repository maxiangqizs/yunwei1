package com.neusoft.yunwei.Task;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.neusoft.yunwei.Utils.ConfigDb;
import com.neusoft.yunwei.Utils.DateUtils;
import com.neusoft.yunwei.Utils.LogUtil;
import com.neusoft.yunwei.pojo.TSftpConfig;
import com.neusoft.yunwei.pojo.TSftpConnectAlr;
import com.neusoft.yunwei.service.ITProvinceServerConfigService;
import com.neusoft.yunwei.service.ITSftpConfigService;
import com.neusoft.yunwei.service.ITSftpConnectAlrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
@Slf4j
public class SftpConnectivity extends TaskInfo {

    //记录日志工具
    @Autowired
    LogUtil logUtil;
    @Autowired
    ITProvinceServerConfigService itProvinceServerConfigService;
    @Autowired
    ITSftpConfigService itSftpConfigService;
    @Autowired
    TSftpConnectAlr tSftpConnectAlr;
    @Autowired
    ITSftpConnectAlrService itSftpConnectAlrService;
    static final String PORT = ConfigDb.getInstance().getString("sftp.connection.port");
    public void SftpConnectivity(){
        String username;
        String password;
        List<String> list = itProvinceServerConfigService.selectProvince();

        System.out.println("----- 增强for循环 -----");
        for (String province : list) {
            System.out.print(province + "  ");
            List<String> list1 = itSftpConfigService.selecttype(province);
            for (String type:list1) {
                System.out.println(type);
                TSftpConfig tSftpConfig = itSftpConfigService.selectAll(province, type);
                username = tSftpConfig.getUserName();
                password = tSftpConfig.getPassword();
                System.out.println(username + "  " + password);
                connect(province,username,password);

            }
        }

    }



    /**
     * 连接登陆远程服务器
     *
     * @return
     */
    public void connect(String provice, String username, String password) {
        JSch jSch = new JSch();
        Session session = null;
        ChannelSftp sftp = null;
        List<String> select = itProvinceServerConfigService.selectByProvince(provice,"南向");
        ListIterator<String> ip = select.listIterator();
        while (ip.hasNext()){
            try {
                tSftpConnectAlr.setIp(ip.next());
                tSftpConnectAlr.setProvince(provice);
                tSftpConnectAlr.setCheckTime(DateUtils.today());
                session = jSch.getSession(username, ip.next(), Integer.parseInt(PORT));
                session.setPassword(password);
                session.setConfig(this.getSshConfig());
                session.connect();
                sftp = (ChannelSftp)session.openChannel("sftp");
                sftp.connect();
                log.error("结果："+session.equals(sftp.getSession()));
                log.info("登录成功:" + sftp.getServerVersion());
            }catch (Exception e) {
                itSftpConnectAlrService.save(tSftpConnectAlr);
                log.error("SSH方式连接FTP服务器时有JSchException异常!", e);
            }finally {
                disconnect(sftp);
            }


        }
        logUtil.toDb("SftpConnectivity","success");
    }

    /**
     * 获取服务配置
     *
     * @return
     */
    private Properties getSshConfig() {
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        return sshConfig;
    }


    /**
     * 关闭连接
     *
     * @param sftp
     */
    public void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.getSession().isConnected()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (Exception e) {
            log.error("关闭与sftp服务器会话连接异常", e);
        }
    }

    @Override
    public void excuteTask() {
        SftpConnectivity();
    }
}
