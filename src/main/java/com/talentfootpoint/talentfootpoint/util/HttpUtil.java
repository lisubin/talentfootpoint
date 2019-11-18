package com.talentfootpoint.talentfootpoint.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * HttpUtil
 */
public class HttpUtil {
	
	private HttpUtil() {}

    /** 
     * 发送POST请求 
     *  
     * @param url 
     *            目的地址 
     * @param parameters 
     *            请求参数，Map类型。 
     * @return 远程响应结果 
     */  
    public static String sendPost(String url, Map<String, Object> parameters) throws Exception {  
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params = "";// 编码之后的参数  
        try {  
            // 编码请求参数  
            if (parameters.size() == 1) {  
                for (String name : parameters.keySet()) { 
                	if(name != null && name != ""  && parameters.get(name)!= null){
                    sb.append(name).append("=").append(  
                            java.net.URLEncoder.encode(parameters.get(name).toString(),  
                                    "UTF-8"));
                	}
                }  
                params = sb.toString();  
            } else if(parameters.size() > 1) {  
                for (String name : parameters.keySet()) {  
                	if(name != null && name != "" && parameters.get(name)!= null){
                    sb.append(name).append("=").append(  
                            java.net.URLEncoder.encode(parameters.get(name).toString(),  
                                    "UTF-8")).append("&");  
                	}
                }  
                String temp_params = sb.toString();  
                params = temp_params.substring(0, temp_params.length() - 1);  
            }  
            // 创建URL对象  
            java.net.URL connURL = new java.net.URL(url);  
            // 打开URL连接  
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                    .openConnection();  
            // 设置通用属性  
//            httpConn.setRequestProperty("Accept", "*/*");
//            httpConn.setRequestProperty("Connection", "Keep-Alive");
//            httpConn.setRequestProperty("User-Agent",
//                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
             Map<String,String> map = new HashMap<>();

            // 设置POST方式  
            httpConn.setDoInput(true);  
            httpConn.setDoOutput(true);  
            // 获取HttpURLConnection对象对应的输出流  
            out = new PrintWriter(httpConn.getOutputStream());  
            // 发送请求参数  
            out.write(params);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn  
                    .getInputStream(), "UTF-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
        } catch (Exception e) {  
        	throw e;
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
            	throw ex;
            }  
        }
        return result;  
    }  
    
    
    /** 
     * 发送POST请求 
     * @param url 
     * @param parameters
     * @return 
     * @throws Exception 
     */  
    public static String sendPostRequest(String url, Map<String, Object> parameters)
            throws Exception {  
        Set<Entry<String, String>> entrys = null;
//        String appKey = headers.get("appKey");
//        String appKey_value = headers.get("appKeyValue");
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params = "";// 编码之后的参数  
     try {
            // 编码请求参数
            if (parameters != null && parameters.size() == 1) {
                for (String name : parameters.keySet()) {
                	if(name != null && name != ""  && parameters.get(name)!= null){
                    sb.append(name).append("=").append(
                            java.net.URLEncoder.encode(parameters.get(name).toString(),
                                    "UTF-8"));
                	}
                }
                params = sb.toString();
            } else if(parameters != null && parameters.size() > 1) {
                for (String name : parameters.keySet()) {
                	if(name != null && name != "" && parameters.get(name)!= null){
                    sb.append(name).append("=").append(
                            java.net.URLEncoder.encode(parameters.get(name).toString(),
                                    "UTF-8")).append("&");
                	}
                }
                String temp_params = sb.toString();
                params = temp_params.substring(0, temp_params.length() - 1);
            }
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接  
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                    .openConnection();
//            headers.put("Content-Type", "application/json");
//            headers.put("Cache-Control", "no-cache");
//            // 设置请求头
//            if (headers != null && !headers.isEmpty()) {
//                entrys = headers.entrySet();
//                for (Entry<String, String> entry : entrys) {
//                    httpConn.setRequestProperty(entry.getKey(), entry.getValue());
//                }
//            }

            // 设置POST方式
            httpConn.setRequestMethod("POST");
            httpConn.setReadTimeout(3000);
            httpConn.setConnectTimeout(3000);
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);

            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            System.out.println(JSONObject.toJSONString(parameters));
            out.write(params);

            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	throw e;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            	throw ex;
            }
        }
        return result;

    }
    /**
     * 发送GET请求
     * @param url
     * @param parameters
     * @param headers
     * @return
     * @throws Exception
     */
    public static String sendGetRequest(String url,
            Map<String, Object> parameters, Map<String, String> headers)
            throws Exception {

        Set<Entry<String, String>> entrys = null;
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        StringBuffer sb = new StringBuffer(url);// 处理请求参数

        try {
            // 编码请求参数
            if (parameters != null && parameters.size() > 0) {
            	sb.append("?");
                for (String name : parameters.keySet()) {
                	if(name != null && name != "" && parameters.get(name)!= null){
                    sb.append(name).append("=").append(
                            java.net.URLEncoder.encode(parameters.get(name).toString(),
                                    "UTF-8")).append("&");
                	}
                }
                sb.deleteCharAt(sb.length() - 1);
            }

            // 创建URL对象
            System.out.println(sb);
            java.net.URL connURL = new java.net.URL(sb.toString());
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL
                    .openConnection();
            // 设置请求头
            headers.put("Cache-Control", "no-cache");
            headers.put("Content-Type", "application/json");
            if (headers != null && !headers.isEmpty()) {
                entrys = headers.entrySet();
                for (Entry<String, String> entry : entrys) {
                	httpConn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            // 设置GET方式  
            httpConn.setRequestMethod("GET");
            httpConn.setDoOutput(true);  
           
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn  
                    .getInputStream(), "UTF-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
        } catch (Exception e) {  
        	throw e;
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
            	throw ex;
            }  
        }
        return result;  
    }
    /**
     * 获取参数名
     * @param clazz 类
     * @param method 类方法
     * @return String[]
     */
    public static String[] getParameterName(Class clazz, Method method){
        Parameter[] params = method.getParameters();
        int length = params.length;
        String[] paramNames = new String[length];
        for(int i=0;i<length;i++){
            paramNames[i] = params[i].getName();
        }
        return paramNames;
    }
    
    
//    public static void main(String[] args) {
//
//    	/*Map<String,Object> params = new HashMap<String,Object>();
//	    params.put("schoolId", 17240);
//	    params.put("updateTime", "2014-01-01 00:00:00");
//	    params.put("type", 2);
//    	String result;
//		try {
//			result = sendPost("http://www.ijinbu.com/ijinbu/general/oa/getSchoolInfo",params);
//			JSONObject json = JSONObject.fromObject(result);
//	    	System.out.println(json);
//
//	    	@SuppressWarnings("unchecked")
//			List<Map<String,Object>> orgList = (List<Map<String,Object>>)json.get("result");
//	    	System.out.println(orgList);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}*/
//
//    	Map<String,Object> parameters = new HashMap<String,Object>();
//    	parameters.put("page", 1);
//    	parameters.put("size", 2000);
//
//	    Map<String,String> headers = new HashMap<String,String>();
//	    headers.put("Cache-Control", "no-cache");
//	    headers.put("Content-Type", "application/json");
//	    String appSecret =  String.valueOf(new Date().getTime());
//	    String appKey = "app34ZLAF6";
//	    String appKey_value = "a1e300b6324ef69e39b68bed450c541043d3a2cddd6c846987dc5f9f192657d1";
//	    String wjwAuthorization = SHA256Util.getSHA256StrJava(appKey +  appKey_value + appSecret);
//	    headers.put("wjwAuthorization", wjwAuthorization);
//	    headers.put("appSecret", appSecret);
//	    headers.put("appKey", appKey);
//
//	    Map<String,Object> params = new HashMap<String,Object>();
//	    params.put("startTime", "2018-07-09 14:00:00");
//	    params.put("endTime", "2018-07-09 14:45:00");
//	    params.put("teacherName", "庄伟强");
//
//	    try {
//			String result = sendGetRequest("http://192.168.1.108/eop/services/common/get/studentAttendData", params, headers);
//			System.out.println(result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//    }
}
