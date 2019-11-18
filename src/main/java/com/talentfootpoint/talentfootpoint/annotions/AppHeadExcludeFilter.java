package com.talentfootpoint.talentfootpoint.annotions;

import com.onething.oneyard.entity.Constants;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AppHeadExcludeFilter {

	/**
	 * 在value中的选项不验证.
	 * 未在value中的必须验证.
	 * @return
	 */
	String[] value() default {Constants.HEAD_APP_TYPE,Constants.HEAD_SIGN,Constants.HEAD_IMEI,Constants.HEAD_CLIENT_TYPE,Constants.HEAD_TOKEN};
}
