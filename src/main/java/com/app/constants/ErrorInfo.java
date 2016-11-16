package com.app.constants;

public class ErrorInfo {
	
	public static final int request_params_exception = 0;//请求参数异常

	public static final int phone_number_error = 1;//输入手机号错误

	public static final int operate_fail = 2;//操作失败

	public static final int server_exception = 3;//服务器异常

	public static final int sms_code_is_exist = 4;// 请留意短信验证码信息

	public static final int please_login = 5;// 请登录

	public static final int please_register = 6;// 该用户不存在，请先注册

	public static final int please_input_nikename = 7;// 昵称不能为空

	public static final int update_fail = 8;// 修改信息失败
	
	public static final int file_type_error = 9;// 上传文件类型不正确
	
	public static final int file_upload_error = 10;// 上传文件失败
	
	public static final int sms_code_null = 11;//短信验证码为空
	
	public static final int phone_null = 12;//手机号为空
	
	public static final int send_sms_fail = 13;//短信验证码发送失败
	
	public static final int sms_code_invalid = 14;//短信验证码失效
	
	public static final int sms_code_is_send = 15;//短信验证码已发送，请耐心等待
	
	public static final int sms_code_error = 16;//短信验证码有误
	
	public static final int create_order_fail = 17;//订单创建失败
	
	public static final int query_order_fail = 18;//订单查询失败
	
	public static final int favorite_fail = 19;//收藏失败
	
	public static final int no_pay = 20;//请先支付
	
	public static final int login_fail = 21;//第三方登陆失败
	
	public static final int please_select_pay_way= 22;//请选择支付方式
	
	public static final int checkin_fail= 23;// 签到失败
	
	public static final int is_checkin= 24;// 已签到
	
	public static final int commit_data_fail= 25;// 数据提交失败
	
	public static final int pay_fail= 26;// 支付失败
	
	public static final int order_timeout= 27;//订单超时
	
	public static final int integral_print_error= 28;//积分输入有误
	
	public static final int is_pay= 29;//已支付
	
	public static final int integral_insufficient= 30;//积分不足
	
	public static final int bind_phone_fail= 31;//绑定手机号失败
	
	public static final int update_password_fail= 32;//修改密码失败
	
	public static final int phone_not_register= 33;//手机号未被注册
	
	public static final int request_frequent= 34;//请求过于频繁
	
	public static final int please_bind_phone= 35;//请绑定手机
	
	public static final int phone_is_bind= 36;//手机号已绑定
	
	public static final int sign_out_fail = 37;//退出登录失败
	
	public static final int platform_no_order_info = 38;//平台无订单信息

	public static final int task_not_finish = 39;//任务未完成

	public static final int task_is_finished = 40;//任务已完成

	public static final int modify_phone_fail = 41;//修改手机号失败

	public static final int pay_way_error = 42;//支付方式有误

}
