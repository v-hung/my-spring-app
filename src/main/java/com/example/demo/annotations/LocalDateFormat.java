package com.example.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JsonFormat(pattern = "yyyy-MM-dd")
@Schema(type = "string", format = "date", example = "2024-01-01")
public @interface LocalDateFormat {
}
