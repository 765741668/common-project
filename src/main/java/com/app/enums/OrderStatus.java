package com.app.enums;

public enum OrderStatus {
	NO_PAY("未支付",1),
	SUCCESS("支付成功",2),
	CANCEL_ORDER("取消订单",3),
	APPLY_REFUND("申请退款",4),
	REFUND_PROCESS("退款处理中",5),
	REFUND_SUCCESS("退款成功",6),
	REFUND_FAIL("退款失败",7);

	private String name;

	private int status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private OrderStatus(String name, int status){
		this.name = name;
		this.status=status;
	}
}
