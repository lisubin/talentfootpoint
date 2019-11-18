package com.talentfootpoint.talentfootpoint.annotions;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AppParameterName {

	String value(); 
}
