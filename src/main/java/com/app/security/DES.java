package com.app.security;

import com.app.security.Base64Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

public class DES implements ISecurity{
	
	private static final Logger logger=LogManager.getLogger(com.app.security.DESUtil.class);
	
	// 密钥  
	private final static String secretKey = "";
	// 向量  
	private final static String iv = "01234567";
	// 加解密统一使用的编码方式  
	private final static String encoding = "utf-8";

	@Override
	public String Encrypt(String data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec;
		spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(data.getBytes(encoding));
		return Base64Util.encode(encryptData);
	}

	@Override
	public String Decrypt(String data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec;
		spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] decryptData = cipher.doFinal(Base64Util.decode(data));
		return new String(decryptData, encoding);
	}

	@Override
	public String Sign(String content, String privateKey) throws Exception {
		return null;
	}

}
