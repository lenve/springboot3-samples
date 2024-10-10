package org.javaboy.demo.utils;


import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
public class PkcsUtils {

    /**
     * 生成证书
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static KeyPair getKey() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA",
                new BouncyCastleProvider());
        generator.initialize(1024);
        // 证书中的密钥 公钥和私钥
        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }

    /**
     * 生成证书
     *
     * @param password
     * @param issuerStr
     * @param subjectStr
     * @param certificateCRL
     * @return
     */
    public static Map<String, byte[]> createCert(String password, String issuerStr, String subjectStr, String certificateCRL) {
        Map<String, byte[]> result = new HashMap<String, byte[]>();
        try(ByteArrayOutputStream out= new ByteArrayOutputStream()) {
            // 标志生成PKCS12证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
            keyStore.load(null, null);
            KeyPair keyPair = getKey();
            // issuer与 subject相同的证书就是CA证书
            X509Certificate cert = generateCertificateV3(issuerStr, subjectStr,
                    keyPair, result, certificateCRL);
            // 证书序列号
            keyStore.setKeyEntry("cretkey", keyPair.getPrivate(),
                    password.toCharArray(), new X509Certificate[]{cert});
            cert.verify(keyPair.getPublic());
            keyStore.store(out, password.toCharArray());
            byte[] keyStoreData = out.toByteArray();
            result.put("keyStoreData", keyStoreData);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成证书
     * @param issuerStr
     * @param subjectStr
     * @param keyPair
     * @param result
     * @param certificateCRL
     * @return
     */
    public static X509Certificate generateCertificateV3(String issuerStr,
                                                        String subjectStr, KeyPair keyPair, Map<String, byte[]> result,
                                                        String certificateCRL) {
        ByteArrayInputStream bint = null;
        X509Certificate cert = null;
        try {
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            Date notBefore = new Date();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(notBefore);
            // 日期加1年
            rightNow.add(Calendar.YEAR, 1);
            Date notAfter = rightNow.getTime();
            // 证书序列号
            BigInteger serial = BigInteger.probablePrime(256, new Random());
            X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                    new X500Name(issuerStr), serial, notBefore, notAfter,
                    new X500Name(subjectStr), publicKey);
            JcaContentSignerBuilder jBuilder = new JcaContentSignerBuilder(
                    "SHA1withRSA");
            SecureRandom secureRandom = new SecureRandom();
            jBuilder.setSecureRandom(secureRandom);
            ContentSigner singer = jBuilder.setProvider(
                    new BouncyCastleProvider()).build(privateKey);
            // 分发点
            ASN1ObjectIdentifier cRLDistributionPoints = new ASN1ObjectIdentifier(
                    "2.5.29.31");
            GeneralName generalName = new GeneralName(
                    GeneralName.uniformResourceIdentifier, certificateCRL);
            GeneralNames seneralNames = new GeneralNames(generalName);
            DistributionPointName distributionPoint = new DistributionPointName(
                    seneralNames);
            DistributionPoint[] points = new DistributionPoint[1];
            points[0] = new DistributionPoint(distributionPoint, null, null);
            CRLDistPoint cRLDistPoint = new CRLDistPoint(points);
            builder.addExtension(cRLDistributionPoints, true, cRLDistPoint);
            // 用途
            ASN1ObjectIdentifier keyUsage = new ASN1ObjectIdentifier(
                    "2.5.29.15");
            // | KeyUsage.nonRepudiation | KeyUsage.keyCertSign
            builder.addExtension(keyUsage, true, new KeyUsage(
                    KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
            // 基本限制 X509Extension.java
            ASN1ObjectIdentifier basicConstraints = new ASN1ObjectIdentifier(
                    "2.5.29.19");
            builder.addExtension(basicConstraints, true, new BasicConstraints(
                    true));
            X509CertificateHolder holder = builder.build(singer);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            bint = new ByteArrayInputStream(holder.toASN1Structure()
                    .getEncoded());
            cert = (X509Certificate) cf.generateCertificate(bint);
            byte[] certBuf = holder.getEncoded();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // 证书数据
            result.put("certificateData", certBuf);
            //公钥
            result.put("publicKey", publicKey.getEncoded());
            //私钥
            result.put("privateKey", privateKey.getEncoded());
            //证书有效开始时间
            result.put("notBefore", format.format(notBefore).getBytes("utf-8"));
            //证书有效结束时间
            result.put("notAfter", format.format(notAfter).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bint != null) {
                try {
                    bint.close();
                } catch (IOException e) {
                }
            }
        }
        return cert;
    }

    public static void main(String[] args) throws Exception {
        // CN: 名字与姓氏    OU : 组织单位名称
        // O ：组织名称  L : 城市或区域名称  E : 电子邮件
        // ST: 州或省份名称  C: 单位的两字母国家代码
        String issuerStr = "CN=javaboy,OU=产品研发部,O=江南一点雨,C=CN,E=javaboy@gmail.com,L=华南,ST=深圳";
        String subjectStr = "CN=javaboy,OU=产品研发部,O=江南一点雨,C=CN,E=javaboy@gmail.com,L=华南,ST=深圳";
        String certificateCRL = "http://www.javaboy.org";
        Map<String, byte[]> result = createCert("123456", issuerStr, subjectStr, certificateCRL);

        FileOutputStream outPutStream = new FileOutputStream("keystore.p12");
        outPutStream.write(result.get("keyStoreData"));
        outPutStream.close();
        FileOutputStream fos = new FileOutputStream(new File("keystore.cer"));
        fos.write(result.get("certificateData"));
        fos.flush();
        fos.close();
    }

}
