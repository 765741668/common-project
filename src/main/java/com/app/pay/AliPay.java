package com.app.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import com.app.config.Constant;
import com.app.security.RSAUtil;
import com.app.utils.DateUtils;
import com.app.utils.HttpUtil;

public class AliPay implements IPay {

	@Override
	public Object createOrder(Object order) {
		return order;
	}

	@Override
	public String refund(List<Object> orders) {
		
		Map<String, String> params=new HashMap<String, String>();
		params.put("service", "refund_fastpay_by_platform_pwd");
		params.put("partner", AlipayUtil.privateKey);
		params.put("_input_charset", Constant.DEFAULT_CHARSET_NAME);
		params.put("notify_url", Constant.SERVER_URL+AlipayUtil.refund_notify_url);
		params.put("seller_email",AlipayUtil.seller);
		params.put("seller_user_id", "");
		params.put("refund_date", DateUtils.FormatFullDate(new Date()));
		String batchNo=DateUtils.FormatDate(new Date(), "yyyyMMddhhmmss");
		batchNo+=(int) ((Math.random() * 9 + 1) * 1000);
		params.put("batch_no", batchNo);
		params.put("batch_num", String.valueOf(orders.size()));
		StringBuilder detailData=new StringBuilder();
//		for(Order order:orders){
//			order.setStatus(9);
//			detailData.append(order.getThirdId()).append("^").append((double)order.getActualAmount()/100.0).append("^").append("协商退款").append("#");
//		}TODO 支付宝支付待处理
		String data=detailData.toString();
		data=data.substring(0, data.length()-1);
		params.put("detail_data", data);
		Map<String, String> paraFilter = AlipayUtil.paraFilter(params);
		String createLinkString = AlipayUtil.createLinkString(paraFilter);
		String sign=RSAUtil.sign(createLinkString, AlipayUtil.privateKey);
		createLinkString.concat("&sign="+sign+"&sign_type=RSA");
		String resData = HttpUtil.getRequest(AlipayUtil.refund_url+createLinkString);
		return resData;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String createPayParams(Object order) {
		Map<String,String> map=new LinkedHashMap<String, String>();
		map.put("service", "mobile.securitypay.pay");
		map.put("partner", AlipayUtil.partner);
		map.put("_input_charset", Constant.DEFAULT_CHARSET_NAME);
		map.put("notify_url", Constant.SERVER_URL+AlipayUtil.pay_notify_url);
		map.put("out_trade_no", "");
		map.put("subject", "");
		map.put("payment_type", "1");
		map.put("seller_id", "bjmitaokeji@163.com");
		map.put("total_fee", "");
		map.put("body", "");
		map.put("it_b_pay", "30m");//订单超时时间
		Map<String, String> paraFilter = AlipayUtil.paraFilter(map);
		String createLinkString = AlipayUtil.createLinkString(paraFilter);
		String sign =AlipayUtil.sign(createLinkString, AlipayUtil.privateKey,Constant.DEFAULT_CHARSET_NAME);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		map.put("sign", sign);
		map.put("sign_type", "RSA");
		Set<String> keys = map.keySet();
		Iterator<String> iterator = keys.iterator();
		String payParams = "";
		StringBuilder stringBuilder = new StringBuilder();
		while (iterator.hasNext()){
			String key = iterator.next();
			String value = map.get(key);
			stringBuilder.append(key+"="+value+"&&");
		}
		payParams = stringBuilder.toString();
		return payParams;
	}

	@Override
	public String queryOrder(String orderId) {
		Map map = new HashMap();
		map.put("app_id",AlipayUtil.application_app_id);
		map.put("method","alipay.trade.query");
		map.put("charset","utf-8");
		map.put("sign_type",AlipayUtil.sign_type);
		map.put("timestamp",DateUtils.FormatFullDate(new Date()));
		map.put("version","1.0");
		map.put("biz_content", "{\"out_trade_no\":\""+orderId+"\"}");
		Map paraFilter = AlipayUtil.paraFilter(map);
		String linkString = AlipayUtil.createLinkString(paraFilter);
		String sign = AlipayUtil.sign(linkString, AlipayUtil.application_privateKey, "utf-8");
		map.put("sign",sign);
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		StringBuilder stringBuilder = new StringBuilder();
		while (iterator.hasNext()){
			String key = iterator.next().toString();
			String value = map.get(key).toString();
			stringBuilder.append(key+"="+URLEncoder.encode(value)+"&");

		}
		String params = stringBuilder.toString();
		params = params.substring(0,params.length()-1);
		String request = HttpUtil.getRequest(AlipayUtil.order_query_url + params);
		return request;
	}
}
