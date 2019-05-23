package com.kipa.http.ssl;

import com.kipa.http.emuns.SSLProtocolVersion;
import com.kipa.http.exception.HttpProcessException;
import com.kipa.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @Author: Yadong Qin
 * @Date: 2019/3/30
 * https 关于安全证书的验证类
 *
 *
 * 针对https有两种方式
 * 1. 绕过https验证
 * 2.通过获取安全证书进行校验
 */
@Slf4j
public class SSLSocketFactoryManager {

    private static final String CERTIFICATE_CODE= "X.509";

    public static SSLSocketFactory getSSLSocketFactory(boolean verifySSLCertificate, String certificatesPath, String keyStorePath, String keyStorePass) {
        //
        return verifySSLCertificate ? getCheckCertificate(certificatesPath, keyStorePath, keyStorePass) : getAvoidCertificate();
    }

    /**
     * 1.绕过安全正式
     * @return
     */
    private static SSLSocketFactory getAvoidCertificate() {
        try {
            SSLContext sslContext = SSLContext.getInstance(SSLProtocolVersion.SSL.getName());
            sslContext.init(null, new TrustManager[]{new SSLSocketFactoryHandler()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new HttpProcessException(e);
        }
    }

    private static SSLSocketFactory getCheckCertificate( String certificatesPath, String keyStorePath, String keyStorePass) {

        PreCheckUtils.checkEmpty(certificatesPath, "证书路径不能为空");

        FileInputStream certificatesInputStream = null;
        FileInputStream keyStorePathInputStream = null;
        // 2.校验安全证书
        try {
            certificatesInputStream = new FileInputStream(certificatesPath);
            keyStorePathInputStream = new FileInputStream(keyStorePath);
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_CODE);
            Certificate certificate = certificateFactory.generateCertificate(certificatesInputStream);
            String keyStoreType = KeyStore.getDefaultType();
            //创建信任证书的keyStore
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            //不传keyStorePath和keyStorePass 默认为空 传null
            keyStore.load(keyStorePathInputStream, keyStorePass.toCharArray());
            keyStore.setCertificateEntry("certificate",certificate);
            //创建一个信任管理证书
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
            trustManagerFactory.init(keyStore);
            //创建SSL
            SSLContext sslContext = SSLContext.getInstance(SSLProtocolVersion.SSL.getName());
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | IOException | KeyManagementException e) {
            throw new HttpProcessException(e);
        } finally {
            close(keyStorePathInputStream);
            close(certificatesInputStream);
        }

    }

    private static void close(FileInputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("文件流关闭失败，错误原因：{}",e);
            }
        }

    }
    /**
     * 实现X509TrustManager和HostnameVerifier获取TrustManager和获取HostnameVerifier
     */
    public static class SSLSocketFactoryHandler implements X509TrustManager, HostnameVerifier{

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
}
