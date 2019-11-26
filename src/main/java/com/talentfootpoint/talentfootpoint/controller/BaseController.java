package com.talentfootpoint.talentfootpoint.controller;


import com.alibaba.fastjson.JSONObject;
import com.talentfootpoint.talentfootpoint.annotions.AppHeadExcludeFilter;
import com.talentfootpoint.talentfootpoint.annotions.AppInterface;
import com.talentfootpoint.talentfootpoint.annotions.AppParameterName;
import com.talentfootpoint.talentfootpoint.entity.App;
import com.talentfootpoint.talentfootpoint.entity.Constants;
import com.talentfootpoint.talentfootpoint.exception.AppException;
import com.talentfootpoint.talentfootpoint.util.HttpUtil;
import com.talentfootpoint.talentfootpoint.util.JsonResult;
import com.talentfootpoint.talentfootpoint.util.SpringContextUtil;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/app")
public class BaseController {
        private final Logger logger = LoggerFactory.getLogger(BaseController.class);


        @ResponseBody
        @RequestMapping(value = "/{service}/{method}")
        public String access(@PathVariable("service") String service,
                             @PathVariable("method") String methodName,
                             HttpServletRequest request, HttpServletResponse response) {

            response.setContentType("application/json;charset=utf-8");
            response.setHeader("Cache-Control", "no-cache");
//
            String ver = "2.0.0000";//UserUtils.getHeader("ver");
            String clientType = "2"; //UserUtils.getHeader("clientType");
          String appType ="1"; //UserUtils.getHeader("appType");
            String sign ="123"; //UserUtils.getHeader("sign");
            String imei ="123"; //UserUtils.getHeader("imei");
            String token ="123"; //UserUtils.getHeader("token");
            String tempBundleId ="123"; //UserUtils.getHeader("bundleId");

            String teaId =""; //UserUtils.getHeader("teaId");

            String bundleId = "";
//            if(tempBundleId==null || tempBundleId.isEmpty()){
//                bundleId = "0";
//            }else if(tempBundleId.charAt(tempBundleId.length()-1)=='x'){
//                bundleId = "2";
//            }else{
//                bundleId = "1";
//            }

            JsonResult jsonResult = new JsonResult();

            Map<String, List<MultipartFile>> fileParameters = new HashMap<String, List<MultipartFile>>();

            // 检查是否上传文件
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)){
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                for (Iterator<String> ite = multiRequest.getFileNames(); ite
                        .hasNext();) {
                    // 记录上传过程起始时的时间，用来计算上传时间
                    int pre = (int) System.currentTimeMillis();
                    String key = ite.next();
                    List<MultipartFile> files = multiRequest.getFiles(key);
                    if (files != null && !files.isEmpty()) {
                        fileParameters.put(key, files);
                        // System.out.println(myFileName);
                        // // 重命名上传后的文件名
                        // String fileName = "demoUpload"
                        // + file.getOriginalFilename();
                        // // 定义上传路径
                        // String path = "H:/" + fileName;
                        // File localFile = new File(path);
                        // try {
                        // file.transferTo(localFile);
                        // } catch (IllegalStateException | IOException e) {
                        // e.printStackTrace();
                        // }
                    }
                    // 记录上传该文件后的时间
                    int finaltime = (int) System.currentTimeMillis();
                    System.out.println(finaltime - pre);
                }
            }

            Map<String, String[]> parameters = request.getParameterMap();

            // 获取业务类
            Object beanObj = null;
            try {
                beanObj = SpringContextUtil.getBean(service);
            } catch (NoSuchBeanDefinitionException e) {
                beanObj = null;
            } finally {
                if (beanObj == null) {
                    jsonResult.setStatus(JsonResult.FAILED_CODE);
                    jsonResult.setMsg("Error Code: "
                           + ", 请求服务不存在.");
                    logException(request, "Error Code: "
                            + ", 请求服务不存在.", null);
                    return result(jsonResult);
                }
            }

            if (ver == null
                    || (ver.indexOf(".") == -1)
                    || !Pattern.matches("d*[1-9]\\.d*[0-9]\\.[0-9][0-9][0-9][0-9]",
                    ver)) {
                jsonResult.setStatus(JsonResult.TOKEN_FAILED_CODE);
                jsonResult.setMsg("Error Code: "
                        + ", 请求头参数不正确.");
                logException(request, "Error Code: "
                        + ", 请求头参数不正确.", null);
                return result(jsonResult);
            }

            // 获取对应版本方法
            Map<String, Method> appMethodMap = new HashMap<String, Method>();
            try {
                Method proxySource = beanObj.getClass().getDeclaredMethod(
                        "getTargetSource");
                if (proxySource != null) {
                    SingletonTargetSource target = (SingletonTargetSource) proxySource
                            .invoke(beanObj);
                    beanObj = target.getTarget();
                }
            } catch (NoSuchMethodException | SecurityException
                    | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e1) {
            }
            for (Method method : beanObj.getClass().getDeclaredMethods()) {
                AppInterface version = method.getAnnotation(AppInterface.class);
                if (version == null) {
                    continue;
                }
                if (version.name().equals(methodName)) {
                    appMethodMap.put(version.ver(), method);
                }
            }

            Method appMethod = getAppMethod(appMethodMap, ver);

            if (appMethod == null) {
                jsonResult.setStatus(JsonResult.TOKEN_FAILED_CODE);
                jsonResult.setMsg("Error Code: "
                        + ", 请求服务或者服务版本不存在.");
                logException(request, "Error Code: "
                        + ", 请求服务或者服务版本不存在.", null);
                return result(jsonResult);
            }

            AppHeadExcludeFilter filter = appMethod
                    .getAnnotation(AppHeadExcludeFilter.class);
            boolean noFilter = false;
            if (filter == null) {
                noFilter = true;
            }

            // 验证imei
            if (noFilter || !ArrayUtils.contains(filter.value(), Constants.HEAD_IMEI)) {
                if (imei == null || imei.isEmpty()) {
                    jsonResult.setStatus(JsonResult.TOKEN_FAILED_CODE);
                    jsonResult.setMsg("Error Code: "
                            + ", 请求头参数不正确.");
                    return result(jsonResult);
                }
            }

            // 验证客户端clientType类型(教师,家长)
            if (noFilter || !ArrayUtils.contains(filter.value(), Constants.HEAD_CLIENT_TYPE)) {
                if (clientType == null || clientType.isEmpty()) {
                    jsonResult.setStatus(JsonResult.TOKEN_FAILED_CODE);
                    jsonResult.setMsg("Error Code: "
                            + ", 请求头参数不正确.");
                    return result(jsonResult);
                }

            }

            // 验证sign
            // if(bool || !ArrayUtils.contains(filter.value(), App.HEAD_SIGN)){
            // if(sign==null || sign.isEmpty()){
            // jsonResult.setStatus(JsonResult.FAILED_CODE);
            // jsonResult.setMsg("Error Code: "+Constants.App.ERROR_CODE_HEADER_PARAMETER_SIGN+", 请求头参数不正确.");
            // return result(jsonResult);
            // }
            // Object[] akss = parameters.keySet().toArray();
            // Arrays.sort(akss);
            // StringBuilder builder = new StringBuilder();
            // for (Object string : akss) {
            // String[] val = parameters.get(string);
            // if(val.length>1){
            // continue;
            // }
            // builder.append(string+val[0]);
            // }
            // String serverSign = PPPString.md5(builder.toString());
            // if(!serverSign.equals(sign)){
            // jsonResult.setStatus(JsonResult.FAILED_CODE);
            // jsonResult.setMsg("Error Code: "+Constants.App.ERROR_CODE_HEADER_PARAMETER_SIGN+", 请求头参数不正确.");
            // return result(jsonResult);
            // }
            // }

            // 验证appType
            if (noFilter || !ArrayUtils.contains(filter.value(), Constants.HEAD_APP_TYPE)) {
                if (appType == null || appType.isEmpty()) {
                    jsonResult.setStatus(JsonResult.TOKEN_FAILED_CODE);
                    jsonResult.setMsg("Error Code: "

                            + ", 请求头参数不正确.");
                    return result(jsonResult);
                }
            }

            // 验证token
            if (noFilter || !ArrayUtils.contains(filter.value(), Constants.HEAD_TOKEN)) {
                if (token == null || token.isEmpty()) {
                    jsonResult.setStatus(JsonResult.TOKEN_FAILED_CODE);
                    jsonResult.setMsg("Error Code: "
                            + ", 请求头参数不正确.");
                    return result(jsonResult);
                }
            }
            //System.out.println("init token"+token);
            App app = App.params(parameters, fileParameters, ver, imei, clientType,
                    sign, token, appType, bundleId,teaId, request, response);

            if(noFilter || !ArrayUtils.contains(filter.value(), Constants.HEAD_TOKEN)){//没有注解或者注解指定排除token

            }

            try {
                // 获得服务方法参数
                Class<?>[] pts = appMethod.getParameterTypes();
                Annotation[][] anns = appMethod.getParameterAnnotations();
                String[] names = HttpUtil.getParameterName(
                        beanObj.getClass(), appMethod);
                Object[] args;

                if (names == null) {
                    names = new String[0];
                }
                args = new Object[names.length];

                // 参数封装
                for (int i = 0; i < names.length; i++) {
                    Annotation[] as = anns[i];
                    String parameterName = null;
                    for (Annotation annotation : as) {
                        if (annotation instanceof AppParameterName) {
                            parameterName = ((AppParameterName) annotation).value();
                            break;
                        }
                    }
                    if (parameterName == null) {
                        parameterName = names[i];
                    }
                    String[] val = parameters.get(parameterName);
                    Class<?> clazz = pts[i];
                    if (val == null || val.length <= 0) {
                        List<MultipartFile> fileVal = fileParameters
                                .get(parameterName);
                        if (fileVal != null && !fileVal.isEmpty()) {
                            if (clazz == MultipartFile.class) {
                                args[i] = fileVal.get(0);
                            } else {
                                args[i] = fileVal.toArray(new MultipartFile[0]);
                            }
                        } else if (clazz == HttpServletRequest.class) {
                            args[i] = request;
                        } else if (clazz == HttpServletResponse.class) {
                            args[i] = response;
                        } else {
                            if (clazz == Integer.TYPE) {
                                args[i] = 0;
                            } else if (clazz == Long.TYPE) {
                                args[i] = 0;
                            } else if (clazz == Short.TYPE) {
                                args[i] = 0;
                            } else if (clazz == Float.TYPE) {
                                args[i] = 0.0f;
                            } else if (clazz == Double.TYPE) {
                                args[i] = 0.0d;
                            } else if (clazz == Character.TYPE) {
                                args[i] = "\u0000";
                            } else if (clazz == Boolean.TYPE) {
                                args[i] = false;
                            } else {
                                args[i] = null;
                                // Object bean = clazz.newInstance();
                                // try{
                                // bean2Map(bean);
                                // }catch(Exception e){
                                // args[i] = null;
                                // }
                                // Map<String,Object> obj = new HashMap<String,
                                // Object>();
                                // for (Iterator<String> ite =
                                // parameters.keySet().iterator();ite.hasNext();) {
                                // String key = ite.next();
                                // String[] v = parameters.get(parameterName);
                                //
                                // }
                                // obj.put(key, value);
                                // }
                                // BeanUtils.populate(bean, temp);
                            }
                        }
                    } else if (val.length == 1) {
                        if (clazz == Integer.class || clazz == Integer.TYPE) {
                            args[i] = Integer.parseInt(val[0]);
                        } else if (clazz == Long.class || clazz == Long.TYPE) {
                            args[i] = Long.parseLong(val[0]);
                        } else if (clazz == Short.class || clazz == Short.TYPE) {
                            args[i] = Short.parseShort(val[0]);
                        } else if (clazz == Float.class || clazz == Float.TYPE) {
                            args[i] = Float.parseFloat(val[0]);
                        } else if (clazz == Double.class || clazz == Double.TYPE) {
                            args[i] = Double.parseDouble(val[0]);
                        } else if (clazz == Character.class
                                || clazz == Character.TYPE) {
                            args[i] = val[0].charAt(0);
                        } else if (clazz == Boolean.class || clazz == Boolean.TYPE) {
                            args[i] = Boolean.parseBoolean(val[0]);
                        } else {
                            args[i] = val[0];
                        }
                    } else {
                        if (clazz == Integer[].class || clazz == int[].class) {
                            Integer[] array = new Integer[val.length];
                            for (int j = 0; j < val.length; j++) {
                                array[j] = Integer.parseInt(val[j]);
                            }
                            args[i] = Arrays.asList(array);
                        } else if (clazz == Long[].class || clazz == long[].class) {
                            Long[] array = new Long[val.length];
                            for (int j = 0; j < val.length; j++) {
                                array[j] = Long.parseLong(val[j]);
                            }
                            args[i] = Arrays.asList(array);
                        } else if (clazz == Short[].class || clazz == short[].class) {
                            Short[] array = new Short[val.length];
                            for (int j = 0; j < val.length; j++) {
                                array[j] = Short.parseShort(val[j]);
                            }
                            args[i] = Arrays.asList(array);
                        } else if (clazz == Double[].class
                                || clazz == double[].class) {
                            Double[] array = new Double[val.length];
                            for (int j = 0; j < val.length; j++) {
                                array[j] = Double.parseDouble(val[j]);
                            }
                            args[i] = Arrays.asList(array);
                        } else if (clazz == Character[].class
                                || clazz == char[].class) {
                            Character[] array = new Character[val.length];
                            for (int j = 0; j < val.length; j++) {
                                array[j] = val[j].charAt(0);
                            }
                            args[i] = Arrays.asList(array);
                        } else if (clazz == Boolean[].class
                                || clazz == boolean[].class) {
                            Boolean[] array = new Boolean[val.length];
                            for (int j = 0; j < val.length; j++) {
                                array[j] = Boolean.parseBoolean(val[j]);
                            }
                            args[i] = Arrays.asList(array);
                        } else {
                            args[i] = Arrays.asList(val);
                        }
                    }
                }

                try {
                    Object resultData = null;

//				//检查登录状态
//				if(filter==null || !ArrayUtils.contains(filter.value(), AppConstants.HEAD_TOKEN)){
//					if("1".equals(app.getClientType())){
//						Assert.notNull(App.params().teacher(), new AssertErrorMsg(3,"您的登录信息已失效，请重新登录."));
//					}else if("2".equals(app.getClientType())){
//						Assert.notNull(App.params().parent(), new AssertErrorMsg(3,"您的登录信息已失效，请重新登录."));
//					}
//				}
                    if (args == null || ArrayUtils.isEmpty(args)) {
                        resultData = appMethod.invoke(beanObj);
                    } else {
                        resultData = appMethod.invoke(beanObj, args);
                    }
                    if (resultData != null) {
                        jsonResult.setResult(resultData);
                    }
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    if("1".equals(app.getClientType())){
                        logException(request, ">>>教师端接口调用异常:[" +  e.getMessage() + "]", e);
                    }else if("2".equals(app.getClientType())){
                        logException(request, ">>>家长端接口调用异常:[" + e.getMessage() + "]", e);
                    }
                    jsonResult.setStatus(JsonResult.FAILED_CODE);
                    jsonResult.setMsg(JsonResult.FAILED_MSG);
                    return result(jsonResult);
                }
                jsonResult.setStatus(JsonResult.SUCCESS_CODE);
                jsonResult.setMsg(JsonResult.SUCCESS_MSG);
            } catch (AppException e) {
//			logger.info(e.getMessage(), e);
                jsonResult.setStatus(e.getType().toString());
                jsonResult.setMsg(e.getMessage());
                return result(jsonResult);
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
                Throwable t = e.getTargetException();
                if (t != null && t instanceof AppException) {
//				logger.info(t.getMessage(), t);
                    jsonResult.setStatus(((AppException) t).getType().toString());
                    jsonResult.setMsg(t.getMessage());
                } else {
                    if("1".equals(app.getClientType())){
                        logException(request, ">>>教师端接口调用异常:[" +  e.getMessage() + "]", e);
                    }else if("2".equals(app.getClientType())){
                        logException(request, ">>>家长端接口调用异常:[" + e.getMessage() + "]", e);
                    }
                    jsonResult.setStatus(JsonResult.FAILED_CODE);
                    jsonResult.setMsg(JsonResult.FAILED_MSG);
                }

                return result(jsonResult);
            }
            if (app.status() != null) {
                jsonResult.setStatus(app.status().toString());
            }
            if (app.message() != null) {
                jsonResult.setMsg(app.message());
            }
            return result(jsonResult);
        }

        private void logException(HttpServletRequest request, String msg, Exception e) {
//            String reqIp = WebUtils.getRealPath(request);
            String message = ">>>请求IP[" + "" + "] - ";
            message += msg;
            if(e != null){
                logger.error(message,e);
            }else{
                logger.error(message);
            }
        }

        @RequestMapping("/test")
        public String appPlatTest() {
            return "AppPlatTest";
        }

        private String result(JsonResult jsonResult) {
            String ver = "3.0.0000"; //UserUtils.getHeader("ver");
            String[] vers = ver.split("\\.");
            if((Integer.parseInt(vers[0])>=2 && Integer.parseInt(vers[1])>=2) || Integer.parseInt(vers[0])>=3){
                try {
                    return URLEncoder.encode(JSONObject.toJSON(jsonResult).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                return JSONObject.toJSON(jsonResult).toString();
            }

            return null;
        }

        @SuppressWarnings("unused")
        private static Map<String, Object> bean2Map(Object obj) throws Exception {

            if (obj == null) {
                return null;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo
                    .getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }

            return map;

        }

        private Method getAppMethod(Map<String, Method> appMethodMap, String ver){
            if(logger.isDebugEnabled()){
                StringBuffer allVer = new StringBuffer();
                for(String key:appMethodMap.keySet()){
                    allVer.append("version:"+key+",");
                }
                logger.debug(allVer.toString());
            }

            Method appMethod = null;
            if (appMethodMap.containsKey(ver)) {
                logger.debug("调用版本ver=[{}]",ver);
                appMethod = appMethodMap.get(ver);
            } else {
                String closestVer = null;
                for(String key:appMethodMap.keySet()){
                    /**
                     * 小于等于版本的中选择最接近版本的
                     */
//                    if(AppVersionUtils.compareTo(key, ver)<=0){
//                        if(closestVer==null){
//                            closestVer = key;
//                            continue;
//                        }
//
//                        if(AppVersionUtils.compareTo(key,closestVer)>=0){
//                            closestVer = key;
//                        }
//                    }
                }

                if(closestVer!=null){
                    logger.debug("调用版本closestVer=[{}]",closestVer);
                    appMethod = appMethodMap.get(closestVer);
                }
            }

            return appMethod;
        }
    }

