package com.app.security;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DESUtil {
	
	private static final Logger logger=LogManager.getLogger(DESUtil.class);
	
	// 密钥  
	private final static String secretKey = "";
	// 向量  
	private final static String iv = "01234567";
	// 加解密统一使用的编码方式  
	private final static String encoding = "utf-8";

	/** 
	 * 3DES加密 
	 *  
	 * @param plainText 普通文本 
	 * @return 
	 * @throws Exception  
	 */
	public static String encode(String plainText) {
		Key deskey = null;
		DESedeKeySpec spec;
		try {
			spec = new DESedeKeySpec(secretKey.getBytes());
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);

			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
			byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
			return Base64Util.encode(encryptData);
		} catch (Exception e) {
			logger.error("加密失败----",e);
		}
		return null;
	}

	/** 
	 * 3DES解密 
	 *  
	 * @param encryptText 加密文本 
	 * @return 
	 * @throws Exception 
	 */
	public static String decode(String encryptText){
		Key deskey = null;
		DESedeKeySpec spec;
		try {
			spec = new DESedeKeySpec(secretKey.getBytes());
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
			byte[] decryptData = cipher.doFinal(Base64Util.decode(encryptText));
			return new String(decryptData, encoding);
		} catch (Exception e) {
			logger.error("解密失败----",e);
		}
		return null;
	}
}
