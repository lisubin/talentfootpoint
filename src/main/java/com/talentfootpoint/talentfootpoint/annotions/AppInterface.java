package com.talentfootpoint.talentfootpoint.annotions;



import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface AppInterface {

	String name();
	String ver() default "2.0.0000";
}
