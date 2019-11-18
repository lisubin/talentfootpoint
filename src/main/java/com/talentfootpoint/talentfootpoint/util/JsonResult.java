package com.talentfootpoint.talentfootpoint.util;

import java.io.Serializable;

/**
 * 
 * @author jeusion
 *	
 */
public class JsonResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1410599715507720319L;
	private String status = SUCCESS_CODE;//1-成功，2-失败
	private String msg = SUCCESS_MSG;
	private Object result;//可序列化的
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public static final int SUCCESS_INT_CODE = 1;//成功
	public static final int FAILED_INT_CODE=2; //失败
	public static final int TOKEN_FAILED_INT_CODE = 3;//成功
	public static final String SUCCESS_CODE = "1";//成功
	public static final String FAILED_CODE="2"; //失败
	public static final String TOKEN_FAILED_CODE="3"; //Token失效
	public static final String SUCCESS_MSG="成功";
	public static final String FAILED_MSG = "失败";
	public static final String TOKEN_FAILED_MSG="登陆失效"; //失败
}
