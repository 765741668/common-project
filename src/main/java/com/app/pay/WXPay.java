package com.app.pay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.app.constants.Constant;
import com.app.enums.OrderStatus;
import com.app.security.MD5Util;
import com.app.utils.DateUtils;
import com.app.utils.HttpUtil;
import com.app.utils.UUIDUtil;
import com.app.utils.XMLUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WXPay implements IPay {

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@Override
	public Object createOrder(Object order) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", WXPayUtil.APPID));
		params.add(new BasicNameValuePair("mch_id", WXPayUtil.MCHID));
		params.add(new BasicNameValuePair("nonce_str", genNonceStr()));
		params.add(new BasicNameValuePair("body", ""));
		//附加数据（自定义数据）
		params.add(new BasicNameValuePair("attach", ""));
		params.add(new BasicNameValuePair("out_trade_no", ""));
		params.add(new BasicNameValuePair("total_fee", ""));
		params.add(new BasicNameValuePair("spbill_create_ip", ""));
		String timeExpire=DateUtils.FormatDate(DateUtils.getDate(new Date(System.currentTimeMillis()),+15), "yyyyMMddHHmmss");
		logger.debug("微信生成预支付订单交易结束时间："+timeExpire);
		params.add(new BasicNameValuePair("time_expire",timeExpire));//订单失效时间
		params.add(new BasicNameValuePair("notify_url", WXPayUtil.refund_notify_url));//异步通知
		params.add(new BasicNameValuePair("trade_type", "APP"));
		String sign = WXPayUtil.getSign(params);//签名
		params.add(new BasicNameValuePair("sign", sign));
		String reqData = XMLUtil.toXml(params);
		String resData = HttpUtil.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", reqData);
		Map<String, Object> map = XMLUtil.getMapFromXML(resData);
		logger.debug("微信生成预支付订单返回数据："+map.toString());
		String returnCode = map.get("return_code").toString();
		if (returnCode.equalsIgnoreCase("FAIL")) {
			return null;
		}
		String resultCode = map.get("result_code").toString();
		if (resultCode.equalsIgnoreCase("SUCCESS")) {
			//预支付订单生成成功，返回prepay_id
			String prepayId = map.get("prepay_id").toString();
		}
		return order;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object> refund(List<Object> orders) {
		HttpClient client=HttpUtil.createSSL(Constant.WX_SIGN_CERTIFICATE_PATH, WXPayUtil.MCHID);
		List<Object> returnParams = new ArrayList<>();
		for(Object order:orders){
			Map<String,String> params=new HashMap<>();
			params.put("appid",WXPayUtil. APPID);
			params.put("mch_id", WXPayUtil.MCHID);
			params.put("nonce_str", genNonceStr());
			params.put("out_trade_no", "");
			params.put("out_refund_no", "");
			params.put("total_fee", "");
			params.put("refund_fee","");
			String sign = WXPayUtil.getSign(params);
			params.put("sign", sign);
			String xmlstring = XMLUtil.toXml(params);
			HttpPost post=new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
			StringEntity postEntity = new StringEntity(xmlstring, Constant.DEFAULT_CHARSET_NAME);
			post.setEntity(postEntity);
			String result=null;
			try {
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
				Map map=XMLUtil.getMapFromXML(result);
				String resultCode=(String) map.get("result_code");
				if(resultCode.equals(OrderStatus.SUCCESS.toString())){
					//退款成功
				}else if(resultCode.equals(OrderStatus.REFUND_FAIL.toString())){
					//退款失败
				}
				returnParams.add(order);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnParams;
	}
	
	private String genNonceStr() {
		Random random = new Random();
		return MD5Util.md5(String.valueOf(random.nextInt(10000)));
	}
	/**
	 * 查询订单状态
	 * @param orderId
	 * @return
	 */
	public String queryOrder(String orderId){
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", WXPayUtil.APPID));
		params.add(new BasicNameValuePair("mch_id", WXPayUtil.MCHID));
		params.add(new BasicNameValuePair("out_trade_no",orderId) );
		params.add(new BasicNameValuePair("nonce_str", genNonceStr()));
		String sign = WXPayUtil.getSign(params);//签名
		params.add(new BasicNameValuePair("sign", sign));
		String reqData = XMLUtil.toXml(params);
		String resData = HttpUtil.sendPost("https://api.mch.weixin.qq.com/pay/orderquery", reqData);
		return resData;
	}
	/**
	 * 关闭订单
	 * @param order
	 * @return
	 */
	public String closeOrder(Object order){
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", WXPayUtil.APPID));
		params.add(new BasicNameValuePair("mch_id", WXPayUtil.MCHID));
		params.add(new BasicNameValuePair("out_trade_no", ""));
		params.add(new BasicNameValuePair("nonce_str", genNonceStr()));
		String sign = WXPayUtil.getSign(params);//签名
		params.add(new BasicNameValuePair("sign", sign));
		String reqData = XMLUtil.toXml(params);
		String resData = HttpUtil.sendPost("https://api.mch.weixin.qq.com/pay/closeorder", reqData);
		return resData;
	}
	/**
	 * 退款状态查询
	 * @param 
	 * @return
	 */
	public String refundQuery(long orderId){
		Map<String,String> map=new HashMap<>();
		map.put("appid", WXPayUtil.APPID);
		map.put("mch_id", WXPayUtil.MCHID);
		map.put("nonce_str", genNonceStr());
		map.put("out_trade_no",String.valueOf(orderId) );
		String sign =WXPayUtil.getSign(map);
		map.put("sign", sign);
		String xmlstring = XMLUtil.toXml(map);
		String result=HttpUtil.sendPost("https://api.mch.weixin.qq.com/pay/refundquery", xmlstring);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map createPayParams(Object order) {
		Map<String,String> map=new LinkedHashMap<String,String>();
		map.put("appid", WXPayUtil.APPID);
		map.put("partnerid", WXPayUtil.MCHID);
		map.put("prepayid", "");
		map.put("package","Sign=WXPay");
		map.put("noncestr", UUIDUtil.getUUID());
		Date date = new Date(1970);
		long s = date.getTime();
		long e=System.currentTimeMillis();
		map.put("timestamp", String.valueOf((e-s)/1000));
		String sign = WXPayUtil.getSign(map);
		map.put("sign", sign);
		return map;
	}
}
