package com.app.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {
	public static String sha256(String cnt) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = cnt.getBytes();
		try {
			String encName = "SHA-256";
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}

	public static String sha1(String cnt) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = cnt.getBytes();
		try {
			String encName = "SHA-1";
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

}
