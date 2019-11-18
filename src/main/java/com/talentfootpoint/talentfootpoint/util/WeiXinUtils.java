package com.talentfootpoint.talentfootpoint.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeiXinUtils {
	private final static String APP_ID= "wxd36c1339963cf5d3";//自己的配置appid
    private final static String APP_SERECT = "0b50dd4ce828c2725b7d559ce2448f3d";//自己的配置APPSECRET;

    public Object getCode(HttpServletRequest request, String code) throws ClientProtocolException, IOException{
//    String code =   request.getParameter("code");
    System.out.println(code);

        //获取openid和access_token的连接
        String getOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APP_ID+"&secret="+APP_SERECT+"&code="+code+"&grant_type=authorization_code";
        //获取返回的code
        String requestUrl = getOpenIdUrl.replace("CODE", code);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        //向微信发送请求并获取response
        String response = httpClient.execute(httpGet,responseHandler);
        System.out.println("=========================获取token===================");
        System.out.println(response);
//        JsonParser parser = new JsonParser();
//        JsonObject jsonObject = (JsonObject) parser.parse(response);
		JSONObject jsonObject=JSONObject.parseObject(response);

        String access_token = jsonObject.get("access_token").toString();
        String openId = jsonObject.get("openid").toString();
        System.out.println("=======================用户access_token==============");
        System.out.println(access_token);
        System.out.println(openId);
        //获取用户基本信息的连接
        String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?  access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        String userInfoUrl = getUserInfo.replace("ACCESS_TOKEN", access_token).replace("OPENID", openId);
        HttpGet httpGetUserInfo = new HttpGet(userInfoUrl);
        String userInfo = httpClient.execute(httpGetUserInfo,responseHandler);
        //微信那边采用的编码方式为ISO8859-1所以需要转化
        String json = new String(userInfo.getBytes("ISO-8859-1"),"UTF-8");
        System.out.println("====================userInfo==============================");
//        JsonObject jsonObject1 = (JsonObject) parser.parse(json);
        JSONObject jsonObject1=JSONObject.parseObject(json);
        jsonObject1.put("openId",openId);
        String nickname = jsonObject1.get("nickname").toString();
        String city = jsonObject1.get("city").toString();
        String province = jsonObject1.get("province").toString();
        String country = jsonObject1.get("country").toString();
        String headimgurl = jsonObject1.get("headimgurl").toString();
        //性别  1 男  2 女  0 未知
        Integer sex =Integer.valueOf(jsonObject1.get("sex").toString()) ;
        System.out.println("昵称"+nickname);
        System.out.println("城市"+city);
        System.out.println("省"+province);
        System.out.println("国家"+country);
        System.out.println("头像"+headimgurl);
        System.out.println("性别"+sex);
        System.out.println(userInfo);
        return jsonObject1;

    }

    public void postJsonData(String code, HttpServletResponse response2, HttpSession session) throws IOException {
    	String requestUrl ="https://api.weixin.qq.com/sns/oauth2/access_token";
	    CloseableHttpClient httpclient = WeiXinUtils.createDefault();
	    HttpPost httppost=new HttpPost(requestUrl);

	    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	    formparams.add(new BasicNameValuePair("appid", APP_ID));
	    formparams.add(new BasicNameValuePair("secret", APP_SERECT));
	    formparams.add(new BasicNameValuePair("code", code));
	    formparams.add(new BasicNameValuePair("grant_type", "authorization_code"));
	    CloseableHttpResponse response=null;
	    try {
	    httppost.setEntity(new UrlEncodedFormEntity(formparams));
	    response = httpclient.execute(httppost);

	    } catch (UnsupportedEncodingException e1) {
	    e1.printStackTrace();
	    } catch (ClientProtocolException e) {
	    e.printStackTrace();
	    } catch (IOException e) {
	    e.printStackTrace();
	    }
	    JSONObject jsonObject=null;
	    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	    HttpEntity httpEntity = response.getEntity();
	    String result=null;
	    try {
	    result = EntityUtils.toString(httpEntity);
	    } catch (ParseException e) {
	    e.printStackTrace();
	    } catch (IOException e) {
	    e.printStackTrace();
	    }// 返回json格式：
	    jsonObject = JSONObject.parseObject(result);
	    }

	    //获取到opendId后，通过opendId获取用户信息
	    String access_token=jsonObject.getString("access_token");
	    String opendId=jsonObject.getString("openid");
	    //调用方法获取微信端信息，自己本类中的方法
	    JSONObject jsonObject1=WeiXinUtils.getUserInfo(access_token, opendId);
	//    User user=new User();
	//    user.setUserOpenId(opendId);
	//    user.setUserImg(jsonObject1.getString("headimgurl"));
	    //把微信默认的字符编码ISO-8859-1转化成utf-8;
	    byte[] bytes=jsonObject1.getString("nickname").getBytes("ISO-8859-1");
	    String name=new String(bytes,"utf-8");
	//    user.setUserName(name);
	    //查询用户的方法
	//    User user2=this.login(user);
	    //放入数据
	    Map<String,Object> map=new HashMap<String,Object>();
	//    map.put("user", user2);
	//    map.put("user2", user);
	//    Gson gson=new Gson();
	    JSONObject gson=JSONObject.parseObject(JSON.toJSONString(map));
	    String data=gson.toString();
	    //设置返回的字符编码
	    response2.setContentType("text/html;charset=utf-8");
	    //返回的数据
	    response2.getWriter().print(data);
	    //这个返回的是获取到的信息
	    //response2.getWriter().print(jsonObject1.toString());
	    }

	    //获取微信端用户信息
	    public static JSONObject getUserInfo(String access_token,String opendId){
	    String requestUrl ="https://api.weixin.qq.com/sns/userinfo?"+
	    "access_token="+access_token+"&openid="+opendId+"&lang=zh_CN";
	    CloseableHttpClient httpclient = WeiXinUtils.createDefault();
	    HttpGet httpget=new HttpGet(requestUrl);
	    CloseableHttpResponse response=null;
	    try {
	    response = httpclient.execute(httpget);
	    } catch (UnsupportedEncodingException e1) {
	    e1.printStackTrace();
	    } catch (ClientProtocolException e) {
	    e.printStackTrace();
	    } catch (IOException e) {
	    e.printStackTrace();
	    }
	    JSONObject jsonObject=null;
	    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	    HttpEntity httpEntity = response.getEntity();
	    String result=null;
	    try {
	    result = EntityUtils.toString(httpEntity);
	    } catch (ParseException e) {
	    e.printStackTrace();
	    } catch (IOException e) {
	    e.printStackTrace();
	    }// 返回json格式：
	    jsonObject = JSONObject.parseObject(result);
	    }
	    //String data=jsonObject.toString();
	    return jsonObject;
    }
    public static CloseableHttpClient createDefault() {
    	return HttpClientBuilder.create().build();
    }

    //对于公众号 OPENid
    //对于填写会员信息 getUserInfo  获取用户唯一标示
    //??为什么  每个用户的唯一标示？
    //判断用户是否已经填写过会员信息
    //而不是登录过的信息，包括后面需要进行推文
    //假如用OPENID 那么可以实现上面的功能吗？自己网上找答案
    //HTML 中在开始的地方进行判断他需要跳转的页面；
    //写一个方法查询该用户是否已经填写过，填写过，给数据页面展示（修改功能暂不开发），没有填写过给NULL，跳转填写页面
}
