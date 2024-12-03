package com.example.demo.authorization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.models.PermissionCheckType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {

	String[] value();

	PermissionCheckType permissionCheckType() default PermissionCheckType.ANY;

}
