package com.neusoft.yunwei.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientUtils {
    /**
     * 获取一个HTTPS连接客户端
     *
     * @param fileName 证书文件名称 为空时创建跳过SSL
     * @param password 证书秘钥 为空时创建跳过SSL
     * @return HttpClient
     * @throws Exception 创建连接过程中可能出现的异常
     */
    public CloseableHttpClient getHttpsClient(String fileName, String password,String algorithm,Integer timeoutMillSec) throws Exception {
        SSLContext sslContext = null;
        if (!StringUtils.isEmpty(algorithm)&&(StringUtils.isEmpty(fileName) || StringUtils.isEmpty(password))) {
            sslContext = createIgnoreVerifySSL(algorithm);
        } else if(!StringUtils.isEmpty(fileName) && !StringUtils.isEmpty(password)){
            sslContext = getSSLContext(fileName, password);
        }
        //设置协议http和https对应的处理socket链接工厂的对象
        RegistryBuilder rr=RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE);
        if(sslContext!=null){
        	rr.register("https", new SSLConnectionSocketFactory(sslContext,new String[]{algorithm}, null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = rr.build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        if(timeoutMillSec!=null){
        	connManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(timeoutMillSec).build());
        }
        return HttpClients.custom().setConnectionManager(connManager).build();
    }


    /**
     * 绕过SSL验证 HttpClient 4.3.x 版本之前可用，之后的版本默认支持https
     *
     * @return SSLContext
     * @throws NoSuchAlgorithmException 找不到算法类型时抛出
     * @throws KeyManagementException   秘钥错误时抛出
     */
    private SSLContext createIgnoreVerifySSL(String algorithm) throws NoSuchAlgorithmException, KeyManagementException {
        //算法可更换
        SSLContext sc = SSLContext.getInstance(algorithm);
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    /**
     * 生产环境下读取证书文件并构建SSLContext
     *
     * @param fileName 证书文件路径及名称
     * @param password 证书秘钥
     * @return SSLContext
     * @throws KeyStoreException        秘钥错误时抛出
     * @throws NoSuchAlgorithmException 找不到算法类型时抛出
     * @throws IOException              秘钥文件读取错误时抛出
     * @throws CertificateException     证书错误时抛出
     * @throws KeyManagementException   秘钥管理错误时抛出
     */
    public static SSLContext getSSLContext(String fileName, String password) throws
            KeyStoreException, NoSuchAlgorithmException,
            IOException, CertificateException,
            KeyManagementException {
        //秘钥文件,秘钥
    	SSLContext sc = null;
    	FileInputStream instream = null;
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File(fileName));
            trustStore.load(instream, password.toCharArray());
            // 相信自己的CA和所有自签名的证书
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
        } catch (KeyStoreException | NoSuchAlgorithmException| CertificateException | IOException | KeyManagementException e) {
            e.printStackTrace();
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
            }
        }
        return sc;
    }
    public static void main(String[] arg) throws Exception{
//    	HttpPost httpPost = new HttpPost("https://www.baidu.com/");
    	HttpPost httpPost = new HttpPost("http://10.4.114.55:8701/meta/facade/controller/ui/ModelController/getMdGroupTagsUi");
    	RequestConfig.Builder rb = RequestConfig.custom();
//    	rb.setSocketTimeout(10000);
//    	rb.setConnectTimeout(10000);
//    	rb.setConnectionRequestTimeout(10000);
    	RequestConfig requestConfig = rb.build();
		httpPost.setConfig(requestConfig);
//    	CloseableHttpClient ct=new HttpClientUtils().getHttpsClient(null, null, "SSLv3",10);
    	CloseableHttpClient ct=new HttpClientUtils().getHttpsClient(null, null, null,null);
    	Thread t1=new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					
				}
			}
    		
    	});
    	t1.start();
    	new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				t1.stop();
//				try {
//					System.out.println("1111");
//					ct.close();
//					System.out.println("2222");
//					ct.close();
//					System.out.println("3333");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
    		
    	}).start();
//		for(int i=0;i<10;i++){
//			HttpEntity entity = new StringEntity("");
//			httpPost.setEntity(entity);
//			CloseableHttpResponse ce=null;
//			try {
//				ce = ct.execute(httpPost);
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			System.out.println(ce);
//			System.out.println(ce.getStatusLine().toString());
//			ce.close();
//		}
//		ct.close();
    }
}
