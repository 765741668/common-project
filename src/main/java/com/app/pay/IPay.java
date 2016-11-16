package com.app.pay;

import java.util.List;

public interface IPay {
	/**
	 * 创建订单
	 * @param order
	 */
	public Object createOrder(Object order);
	/**
	 * 退款
	 * @param orders
	 */
	public Object refund(List<Object> orders);
	/**
	 * 生成客户端请求支付的参数
	 * @param order
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object createPayParams(Object order);
}
