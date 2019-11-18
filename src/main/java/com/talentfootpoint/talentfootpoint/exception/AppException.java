package com.talentfootpoint.talentfootpoint.exception;

@SuppressWarnings("serial")
public class AppException extends RuntimeException {

	private Integer type = 2;//1,响应成功; 2,响应失败; 4,用户名密码错误;5,省平台注册-需完善手机号;6,用户信息已失效,7-用户注册手机号已存在;14，密码不能为123456
	
	public AppException(Integer type, String msg){
		super(msg);
		this.type = type;
	}
	
	public AppException(String msg){
		super(msg);
	}

	public Integer getType() {
		return type;
	}
}
