package com.talentfootpoint.talentfootpoint.entity;

//import com.SchoolCloud.app.parent.vo.AppChildrenVo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@SuppressWarnings("serial")
public final class App extends HashMap<String, Object> {
    private final static Logger logger = LoggerFactory.getLogger(App.class);

	private Integer resultStatus = null;
	private String resultMessage = null;
	private String headVer;
	private String headImei;
	private String headClientType;
	private String headSign;
	private String headToken;
	private String headAppType;
	private String headBundleId;
	
	private String teaId;//当前教师id

	private HttpServletRequest request;
	private HttpServletResponse response;

	/*private static ConcurrentMap<String, AppTeacher> teachers = new ConcurrentHashMap<String, AppTeacher>();
	private static ConcurrentMap<String, AppParent> parents = new ConcurrentHashMap<String, AppParent>();

	private static ConcurrentMap<String, AppTeacher> teacherQRcode = new ConcurrentHashMap<String, AppTeacher>();*/

	private App() {
	}

	private static final ThreadLocal<App> local = new ThreadLocal<App>();

	public static App params(final Map<String, String[]> maps,
                             final Map<String, List<MultipartFile>> files, String ver,
                             String imei, String clientType, String sign, String token,
                             String appType, String bundleId, String teaId, HttpServletRequest request,
                             HttpServletResponse response) {
		App map = local.get();
		if (map == null) {
			map = new App();
			local.set(map);
		}
		map.headVer = ver;
		map.headImei = imei;
		map.headClientType = clientType;
		map.headSign = sign;
		map.headToken = token;
		map.headAppType = appType;
		map.headBundleId = bundleId;
		
		map.teaId = teaId;
		
		map.request = request;
		map.response = response;
		map.resultStatus = null;
		map.resultMessage = null;
		map.clear();
		for (Iterator<String> ite = maps.keySet().iterator(); ite.hasNext();) {
			String key = ite.next();
			String[] keyArray = maps.get(key);
			if (keyArray.length == 0) {
				map.put(key, null);
			} else if (keyArray.length == 1) {
				map.put(key, keyArray[0]);
			} else {
				map.put(key, Arrays.asList(keyArray));
			}
		}
		for (Iterator<String> ite = files.keySet().iterator(); ite.hasNext();) {
			String key = ite.next();
			List<MultipartFile> keyArray = files.get(key);
			if (keyArray.size() == 0) {
				map.put(key, null);
			} else if (keyArray.size() == 1) {
				map.put(key, keyArray.get(0));
			} else {
				map.put(key, keyArray.toArray(new MultipartFile[0]));
			}
		}
		return map;
	}

	public static App params() {
		return local.get();
	}

//	// 教师缓存
//	private static void setTeacherCache(String key, AppTeacher val) {
//		RedisUtils.setObject(key, val);
//	}
//
//	private static AppTeacher getTeacherCache(String key) {
//		Object obj = RedisUtils.getObject(key);
//		if (obj != null) {
//			return (AppTeacher) obj;
//		}
//		return null;
//	}
//
//	private static boolean removeTeacherCache(String key) {
//		return RedisUtils.removeSetValue(key);
//	}
//
//	// 家长缓存
//	private static void setParentCache(String key, AppParent val) {
//		RedisUtils.setObject(key, val);
//	}
//
//	private static AppParent getParentCache(String key) {
//		Object obj = RedisUtils.getObject(key);
//		if (obj != null) {
//			return (AppParent) obj;
//		}
//		return null;
//	}
//
//	private static boolean removeParentCache(String key) {
//		return RedisUtils.removeSetValue(key);
//	}


	/**
	 * 获取request所有的参数
	 *
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getParams(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			result.put(paraName, request.getParameter(paraName));
		}
		return result;
	}

	/**
	 * 转java beanu
	 *
	 * @param request
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> T getParams(HttpServletRequest request, Class t) {
		Map<String, Object> result = getParams(request);
		return (T) JSONObject.parseObject(JSON.toJSONString(result), t);
	}
//	// 教师缓存
//	private static void setTeacherCache(String key, AppTeacher val)	 {
//		//App.teachers.put(key, val);
//
//		RedisUtils.saveObjectEx(key, val);
//	}
//
//	private static AppTeacher getTeacherCache(String key) {
//		//Object obj = App.teachers.get(key);
//		Object obj = RedisUtils.getObject(key);
//		if (obj != null) {
//			return (AppTeacher) obj;
//		}
//        logger.info("token-key-null:"+key);
//		return null;
//	}
//
//	private static long removeTeacherCache(String key) {
//		return RedisUtils.removeObject(key);
//	}
//
//	// 家长缓存
//	private static void setParentCache(String key, AppParent val) {
//		//App.parents.put(key, val);
//		RedisUtils.saveObjectEx(key, val);
//	}
//
//	private static AppParent getParentCache(String key) {
//		//Object obj = App.parents.get(key);
//		Object obj = RedisUtils.getObject(key);
//		if (obj != null) {
//			return (AppParent) obj;
//		}
//		return null;
//	}
//
//	private static long removeParentCache(String key) {
//		return RedisUtils.removeObject(key);
//	}
//
//	public static void setQrCode(String code, AppTeacher teacher) {
//		//teacherQRcode.put(code, teacher);
//		RedisUtils.saveObjectEx(code, teacher);
//	}
//
//	public static AppTeacher getQrCode(String code) {
//		//return teacherQRcode.get(code);
//		AppTeacher res = (AppTeacher) RedisUtils.getObject(code);
//		return res;
//	}
//
//	public static void removeQrCode(String code) {
//		//teacherQRcode.remove(code);
//		RedisUtils.removeObject(code);
//	}
//
//	public void removeCache(AppUser user) {
//		if ("1".equals(getClientType())) {
//			removeTeacherCache(PPPString.md5("t_account" + user.getAccount()));
//			removeTeacherCache(PPPString.md5("t_token" + user.getToken()));
//		} else if ("2".equals(getClientType())) {
//			removeParentCache(PPPString.md5("p_account" + user.getAccount()));
//			removeParentCache(PPPString.md5("p_token" + user.getToken()));
//		}
//	}
//
////	public void removeCache(AppUser user) {
////		if ("1".equals(getClientType())) {
////			App.teachers.remove(PPPString.md5("t_account" + user.getAccount()));
////			App.teachers.remove(PPPString.md5("t_token" + user.getToken()));
////		} else if ("2".equals(getClientType())) {
////			App.parents.remove(PPPString.md5("p_account" + user.getAccount()));
////			App.parents.remove(PPPString.md5("p_token" + user.getToken()));
////		}
////	}
//
//	public void cacheTeacher(AppTeacher teacher) {
//        logger.info("cache-teacher:"+teacher);
//		AppTeacher oldTeacher = getTeacherCache(PPPString.md5("t_account"
//				+ teacher.getAccount()));
//		if (oldTeacher != null) {
//			String token = oldTeacher.getToken();
//			if (token != null) {
//				removeTeacherCache(PPPString.md5("t_token" + token));
//			}
//		}
//		removeTeacherCache(PPPString.md5("t_account" + teacher.getAccount()));
//		teacher.setToken(UniqueKeyUtil.getKey());
//		setTeacherCache(PPPString.md5("t_account" + teacher.getAccount()),
//				teacher);
//		setTeacherCache(PPPString.md5("t_token" + teacher.getToken()), teacher);
//	}
//
//	public void cacheParent(AppParent parent) {
//		AppParent oldParent = getParentCache(PPPString.md5("p_account"
//				+ parent.getAccount()));
//		if (oldParent != null) {
//			String token = oldParent.getToken();
//			if (token != null) {
//				removeParentCache(PPPString.md5("p_token" + token));
//			}
//		}
//		removeParentCache(PPPString.md5("p_account" + parent.getAccount()));
//		parent.setToken(UniqueKeyUtil.getKey());
//		setParentCache(PPPString.md5("p_account" + parent.getAccount()), parent);
//		setParentCache(PPPString.md5("p_token" + parent.getToken()), parent);
//	}
//
//	public AppTeacher teacher() {
//		if (getToken() == null) {
//			return null;
//		}
//		AppTeacher userObj = getTeacherCache(PPPString.md5("t_token"
//				+ getToken()));
//		// Assert.notNull(userObj, new AssertErrorMsg(3,"教师用户尚未登录."));
//		return userObj;
//	}
//
//	public AppParent parent() {
//		if (getToken() == null) {
//			return null;
//		}
//		AppParent userObj = getParentCache(PPPString
//				.md5("p_token" + getToken()));
//
//		if(userObj!=null){
//			//获取入参的schoolId
//			Object schoolId = App.params().get("schoolId");
//			if(schoolId!=null){
//				userObj.setSchoolId(Long.parseLong(schoolId+""));
//			}
//			//设置当前孩子信息
//			List<AppChildrenVo> children = userObj.getChildren();
//			if(CollectionUtils.isNotEmpty(children)){
//					String childUserId = (String) App.params().get("childUserId");
//				for(AppChildrenVo appChildrenVo:children){
//					if(appChildrenVo.getUid().equals(childUserId)){
//						userObj.setAppChildrenVo(appChildrenVo);
//					}
//				}
//			}
//
//		}
//		// Assert.notNull(userObj, new AssertErrorMsg(3,"家长用户尚未登录."));
//		return userObj;
//	}
//
//	public AppUser user() {
//		if (getToken() == null) {
//			return null;
//		}
//		if (getClientType() == null) {
//			throw new AppException("发现客户端类型未知错误.");
//		}
//		AppUser userObj = null;
//		if ("1".equals(getClientType())) {
//			userObj = getTeacherCache(PPPString.md5("t_token" + getToken()));
//		} else if ("2".equals(getClientType())) {
//			userObj = getParentCache(PPPString.md5("p_token" + getToken()));
//		}
//
//		// Assert.notNull(userObj, new AssertErrorMsg(3,"用户尚未登录."));
//		return userObj;
//	}

	public String getVer() {
		return headVer;
	}

	public String getImei() {
		return headImei;
	}

	public String getSign() {
		return headSign;
	}

	public String getClientType() {
		return headClientType;
	}
	
	public String getBundleId() {
		return headBundleId;
	}

	public String getAppType() {
		return headAppType;
	}

	public String getToken() {
		return headToken;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void resultMsg(Integer status, String msg) {
		resultStatus = status;
		resultMessage = msg;
	}

	public void resultMsg(String msg) {
		resultMessage = msg;
	}

	public Integer status() {
		return resultStatus;
	}

	public String message() {
		return resultMessage;
	}
	
}
