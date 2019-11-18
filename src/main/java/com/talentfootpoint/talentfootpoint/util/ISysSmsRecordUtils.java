package com.talentfootpoint.talentfootpoint.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ISysSmsRecordUtils {
	private static final String SMS_URL="http://118.89.158.83:9000/sendXSms.do";
	private static final String SMS_USER_NAME="haixia";
	private static final String SMS_PASSWORD="ca1d9a235e7e563f54454f4b0edf5bf9";
	private static final String SMS_PRODUCTID="149075";
//	private static final Integer SMS_MAX_NUM=100; 	//短信最大的条数
	
	public String doWork(String mobiles, String content){
		String res = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		try{
			StringBuffer sb = new StringBuffer(SMS_URL).append("?");
			sb.append("username=").append(SMS_USER_NAME);
			sb.append("&password=").append(SMS_PASSWORD);
			sb.append("&productid=").append(SMS_PRODUCTID);
			sb.append("&mobile="+mobiles);
			sb.append("&content="+URLEncoder.encode(content,"utf-8"));
			sb.append("&dstime=");
			sb.append("&xh=");
			URL url = new URL(sb.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			res = in.readLine();
			return res;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(connection!=null){
				connection.disconnect();
			}
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		ISysSmsRecordUtils iu=new ISysSmsRecordUtils();
		iu.doWork("18060960803", "2019.7.1短信测试");
		
	}
}
