package com.irengine.sandbox.web.rest.util;

import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;

import org.apache.commons.lang3.ArrayUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class SignUtils {
	static final String init = "merchant_keys.propertes";
	static Map<String, String> paramsMap = new HashMap<>();
	static final String merchantno = "jct.merchantno";
	static final String storepass = "jct.storepass";
	static final String keypass = "jct.keypass";
	static final String publickey = "jct.publickey";
	static boolean flag = true;

	static {
		Properties p = new Properties();
		try {
			p.load(SignUtils.class.getClassLoader().getResourceAsStream("merchant_keys.propertes"));
			paramsMap.put("merchantno", (String) p.get("jct.merchantno"));
			paramsMap.put("storepass", (String) p.get("jct.storepass"));
			paramsMap.put("keypass", (String) p.get("jct.keypass"));
			paramsMap.put("publickey", URLDecoder.decode((String) p.get("jct.publickey"), "gbk"));
		} catch (Exception e) {
			System.err.println("初始化配置信息异常：请检查配置文件路径以及内容");
			e.printStackTrace();
			flag = false;
		}
	}

	public static String genSignByRSA(String src) throws Exception {
		if (!flag) {
			System.err.println("初始化配置未成功,genSignByRSA无法执行");
			throw new Exception("初始化配置未成功,genSignByRSA无法执行");
		}
		if ((src == null) || ("".equals(src.trim()))) {
			System.err.println("文本为空,genSignByRSA无法执行");
			throw new Exception("文本为空,genSignByRSA无法执行");
		}
		String merchantno = (String) paramsMap.get("merchantno");
		String storepass = (String) paramsMap.get("storepass");
		String keypass = (String) paramsMap.get("keypass");

		String mac = null;
		try {
			InputStream in = SignUtils.class.getClassLoader().getResourceAsStream("merchant_" + merchantno + ".jks");
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(in, storepass.toCharArray());
			PrivateKey privateKey = (PrivateKey) ks.getKey(merchantno, keypass.toCharArray());

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(1, privateKey);
			byte[] sbt = src.getBytes();

			byte[] epByte = (byte[]) null;
			for (int i = 0; i < sbt.length; i += 64) {
				byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(sbt, i, i + 64));
				epByte = ArrayUtils.addAll(epByte, doFinal);
			}
			BASE64Encoder encoder = new BASE64Encoder();
			mac = URLEncoder.encode(encoder.encode(epByte), "gbk");
		} catch (Exception e) {
			System.err.println("RSA genSignByRSA Exception");
			e.printStackTrace();
			throw new Exception(e);
		}
		// PrivateKey privateKey;
		return mac;
	}

	public static boolean verifySignByRSA(String src, String sign) throws Exception {
		if (!flag) {
			System.err.println("初始化配置未成功,verifySignatureByRSA无法执行");
			throw new Exception("初始化配置未成功,verifySignatureByRSA无法执行");
		}
		if ((src == null) || ("".equals(src.trim()))) {
			System.err.println("初始化配置未成功,verifySignatureByRSA无法执行");
			throw new Exception("src is empty ,verifySignatureByRSA无法执行");
		}
		if ((sign == null) || ("".equals(sign.trim()))) {
			System.err.println("初始化配置未成功,verifySignatureByRSA无法执行");
			throw new Exception("dit is empty ,verifySignatureByRSA无法执行");
		}
		String signStr = URLDecoder.decode(sign, "gbk");
		String publicKey = null;
		try {
			publicKey = (String) paramsMap.get("publickey");
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] keyBytes = decoder.decodeBuffer(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicK = keyFactory.generatePublic(keySpec);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(2, publicK);
			byte[] data = decoder.decodeBuffer(signStr);

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i += 128) {
				byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
				sb.append(new String(doFinal));
			}
			if (!src.equals(new String(sb.toString()))) {
				return false;
			}
			return true;
		} catch (Exception e) {
			System.err.println("verifySignByRSA Exception");
			e.printStackTrace();
			throw new Exception(e);
		}
	}
}
