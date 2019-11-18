package com.talentfootpoint.talentfootpoint.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ldx
 * @date 2019/7/2 21:56
 * @Description:
 */
public class CookiesUtils {
    public static String getCookieValue(HttpServletRequest request, String cookieName){

        Cookie[] cookies =  request.getCookies();
        String cookieValue=new String();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    cookieValue=cookie.getValue();
                }
            }
        }
        return cookieValue;
    }
}



