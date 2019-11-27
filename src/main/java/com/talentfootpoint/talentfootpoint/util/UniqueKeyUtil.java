package com.talentfootpoint.talentfootpoint.util;

import java.util.Random;
import java.util.UUID;

public class UniqueKeyUtil {
	public UniqueKeyUtil() {
	}
	public static String getKey() {
		String strKey = UUID.randomUUID().toString();
		return  strKey.replace("-","");
	}

	public static String randomNum(int len) {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < len; i++) {
			result += random.nextInt(10);
		}
		return result;
	}

	// 学生编号(学校所在地区区号+8位随机数)
	public static String getStuCode(String areaCode) {

		String stuCode = null;
		if (null != areaCode && !"".equals(areaCode)) {
			stuCode = areaCode + randomNum(8);
		}
		return stuCode;
	}
}
