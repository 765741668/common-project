package com.app.security;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

/**
*
* @author yangzhao
* AES128 算法
*
* CBC 模式
*
* PKCS7Padding 填充模式
*
* CBC模式需要添加一个参数iv
*
* 介于java 不支持PKCS7Padding，只支持PKCS5Padding 但是PKCS7Padding 和 PKCS5Padding 没有什么区别
* 要实现在java端用PKCS7Padding填充，需要用到bouncycastle组件来实现
*/
public class AESUtil {

	final String KEY_ALGORITHM = "AES";

	// 加解密算法/模式/填充方式
	final String algorithmStr = "AES/CBC/PKCS7Padding";

	private Key key;

	private Cipher cipher;

	boolean isInited = false;

	byte[] iv = { 0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38 };

	private String privateKey = "";

	public AESUtil() {
		// 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
		int base = 16;
		byte[] keyBytes = privateKey.getBytes();
		if (keyBytes.length % base != 0) {
			int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
			byte[] temp = new byte[groups * base];
			Arrays.fill(temp, (byte) 0);
			System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
			keyBytes = temp;
		}
		// 初始化
		Security.addProvider(new BouncyCastleProvider());
		// 转化成JAVA的密钥格式
		key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
		// 初始化cipher
		try {
			cipher = Cipher.getInstance(algorithmStr, "BC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final AESUtil instance = new AESUtil();

	public static AESUtil getInstance() {
		return instance;
	}

	/**
	 * 加密
	 * @param content
	 * @return
	 */
	public String encrypt(byte[] content) {
		byte[] encryptedText = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			encryptedText = cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64Util.encode(encryptedText);
	}
	/**
	 * 解密
	 * @param data
	 * @return
	 */
	public String decrypt(String data) {
		byte[] decode = Base64Util.decode(data);
		byte[] encryptedText = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			encryptedText = cipher.doFinal(decode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(encryptedText);
	}

	public static void main(String[] args) throws Exception {
		String encrypt = AESUtil.getInstance().encrypt("杨钊".getBytes());
		System.out.println(encrypt);
		String decrypt = AESUtil.getInstance().decrypt(encrypt);
		System.out.println(decrypt);
	}
}