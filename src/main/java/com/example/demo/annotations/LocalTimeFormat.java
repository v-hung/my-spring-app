package com.example.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JsonFormat(pattern = "HH:mm:ss")
@Schema(type = "string", format = "time", example = "14:30:00")
public @interface LocalTimeFormat {
}
