package com.dbs.loyalty.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAuditApi {

	String name();
	
	boolean saveRequest() default false;
	
	boolean saveResponse() default false;

}
