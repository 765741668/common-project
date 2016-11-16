package com.app.security;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static String sKey = "";

	private static final String ALGORITHM = "AES/ECB/PKCS7Padding";

	private static byte[] raw = null;

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		try {
//			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//			sr.setSeed(sKey.getBytes());
//			kgen.init(128, sr);
//			SecretKey secret = kgen.generateKey();
//			raw = secret.getEncoded();
			raw = sKey.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 加密
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String Encrypt(String data) {
		byte[] encrypted=null;
		try{
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");//"算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(data.getBytes("utf-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return Base64.encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	/**
	 * 解密
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String Decrypt(String data) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] encrypted1 = Base64.decode(data);//先用base64解密
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original, "utf-8");
		return originalString;
	}
}
