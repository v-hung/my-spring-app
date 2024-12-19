package com.example.demo.authorization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.models.PermissionType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {

	PermissionType value();

}
