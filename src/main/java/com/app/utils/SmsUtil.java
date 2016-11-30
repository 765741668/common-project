package com.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.app.cache.CacheFactory;
import com.app.cache.ICache;
import com.app.security.MD5Util;
/**
 * 短信服务商（互亿无线）
 * @author yangzhao at 2016年7月19日
 *
 */
public class SmsUtil {
	
	private static final String account="";

	private static final String password="";
	
	private static final Logger logger=LogManager.getLogger(SmsUtil.class);
	
	public static boolean sendSms(String iphone,String context) {
		String url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", account) );
		params.add(new BasicNameValuePair("password", MD5Util.md5(password)) );
		params.add(new BasicNameValuePair("mobile", iphone));
		params.add(new BasicNameValuePair("content", context));
		String responseData = HttpUtil.sendPostForm(url, params);
		try {
			Map<String, Object> map = XMLUtil.getMapFromXML(responseData);
			String code = map.get("code").toString();
			if(code.equals("2")){
				return true;
			}
		} catch (Exception e) {
			logger.error("短信发送失败----",e);
		}
		return false;
		
	}
	/**
	 * 验证短信码
	 * @param phone
	 * @param code
	 * @return
	 */
	public static String validateSmsCode(String phone,int code){
		ICache cache = CacheFactory.getInstance().getDefault();
		String s = cache.get("sms_model" + phone);
		//判断验证码是否超时（失效）
		if (StringUtils.isEmpty(s)){
			return "";
		}
		//判断验证码是否正确
		if(code!=Integer.parseInt(s)){
			return "";
		}
		return "ok";
	}
}
