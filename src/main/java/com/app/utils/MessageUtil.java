package com.app.utils;

/**
 * 消息工具
 * 用户返回移动端数据 s:0表示失败 error:表示错误码 s:1表示处理成功 data：表示返回移动端业务数据
 * @author yangzhao at 2015年12月8日
 *
 */
public class MessageUtil {

	public static String getErrorMessage(int id) {
		StringBuilder builder=new StringBuilder();
		builder.append("{\"s\":0,\"error\":");
		builder.append(id+"}");
		return builder.toString();
	}

	public static String getSuccessResult(Object obj) {
		StringBuilder builder=new StringBuilder();
		builder.append("{\"s\":1,\"data\":");
		builder.append(JsonUtil.parse(obj)).append("}");
		return builder.toString();
	}

	public static String getSuccessResult(String data){
		StringBuilder builder=new StringBuilder();
		builder.append("{\"s\":1,\"data\":"+data+"}");
		return builder.toString();
	}
	
	public static String getSuccess(){
		StringBuilder builder=new StringBuilder();
		builder.append("{\"s\":1}");
		return builder.toString();
	}
}
