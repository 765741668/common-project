
package com.app.security;

import java.util.UUID;
/**
 * 
 * @author yangzhao 2015年10月16日
 *
 */
public class AccountPwdUtil {

	public static String saltHash(String pwd, String salt) {
		String hash = null;
		String p = MD5Util.md5(pwd + ".mtkj.msyg");
		String cnt = salt.substring(0, salt.length() / 2) + p + salt;
		hash = SHAUtil.sha256(cnt);
		return hash;
	}

	public static String genSalt() {
		return UUID.randomUUID().toString();
	}
}
