package com.app.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * 消息摘要
 * @author yangzhao
 *
 */
public class MessageDigestUtil {
	
	private static final String SHA_TYPE = "SHA-256";

	private static final String Hmac_TYPE = "HmacMD5";

	/**
	 * MD5加密
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptMD5(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data.getBytes());
		byte[] digest = md.digest();
		return formBytesToHex(digest);
	}

	/**
	 * 将字节转换为16进制字符串
	 * @param data
	 * @return
	 */
	public static String formBytesToHex(byte[] data) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			if (Integer.toHexString(0xFF & data[i]).length() == 1) {
				builder.append("0");
			}
			builder.append(Integer.toHexString(0xFF & data[i]));
		}
		return builder.toString();
	}

	/**
	 * SHA消息摘要
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String SHA(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(SHA_TYPE);
		md.update(data.getBytes());
		byte[] digest = md.digest();
		return formBytesToHex(digest);
	}

	/**
	 * Hmac消息摘要
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String Hmac(String data) throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance(Hmac_TYPE);
		//生成秘钥
		SecretKey secretKey = keyGen.generateKey();
		//得到秘钥字节数组
		//byte[] key = secretKey.getEncoded();
		Mac mac = Mac.getInstance(Hmac_TYPE);
		mac.init(secretKey);
		//执行消息摘要
		byte[] doFinal = mac.doFinal();
		return formBytesToHex(doFinal);
	}
}
