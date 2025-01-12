package com.example.demo.utils;

import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;

import com.example.demo.exception.BusinessException;

public class ObjectUtils {

	private ObjectUtils() {

		throw new IllegalStateException("Utility class");

	}

	public static <T> boolean hasNonNullProperties(T object) {

		if (object == null) {

			return false;

		}

		try {

			var fields = object.getClass().getDeclaredFields();

			for (var field : fields) {

				String fieldName = field.getName();

				// Determine the getter method name based on field type
				String getterName;

				if (field.getType() == boolean.class || field.getType() == Boolean.class) {

					getterName = "is" + capitalize(fieldName); // for boolean values, use "is" prefix

				} else {

					getterName = "get" + capitalize(fieldName); // standard getter for other types

				}

				Method getter = object.getClass().getMethod(getterName);

				Object value = getter.invoke(object);

				if (value != null) {

					return true;

				}

			}

		} catch (Exception e) {

			throw new BusinessException(HttpStatus.BAD_REQUEST, "Error while checking properties");

		}

		return false;

	}

	public static <T> void copyNonNullProperties(T source, T target) {

		if (source == null || target == null) {

			throw new IllegalArgumentException("Source and target must not be null");

		}

		try {

			var fields = source.getClass().getDeclaredFields();

			for (var field : fields) {

				String fieldName = field.getName();
				String getterName = "get" + capitalize(fieldName);
				String setterName = "set" + capitalize(fieldName);

				// Lấy getter và setter
				Method getter = source.getClass().getMethod(getterName);
				Method setter = null;

				setter = getSetterMethod(target, setterName, field.getType());

				Object value = getter.invoke(source); // Gọi getter

				if (value != null) {

					if (!isPrimitiveOrWrapper(value.getClass())) {

						// Xử lý Deep Copy cho các đối tượng phức tạp
						Object deepCopiedValue = value.getClass().getDeclaredConstructor().newInstance();
						copyNonNullProperties(value, deepCopiedValue); // Đệ quy
						setter.invoke(target, deepCopiedValue);

					} else {

						// Copy giá trị primitive hoặc immutable
						setter.invoke(target, value);

					}

				}

			}

		} catch (Exception e) {

			throw new BusinessException(HttpStatus.BAD_REQUEST, "Error while copying properties");

		}

	}

	private static boolean isPrimitiveOrWrapper(Class<?> type) {

		return type.isPrimitive() ||
			type == String.class ||
			type == Integer.class || type == Long.class || type == Double.class ||
			type == Float.class || type == Boolean.class || type == Byte.class ||
			type == Character.class || type == Short.class;

	}

	private static String capitalize(String str) {

		return str.substring(0, 1).toUpperCase() + str.substring(1);

	}

	private static Method getSetterMethod(Object target, String setterName, Class<?> fieldType) {

		try {

			return target.getClass().getMethod(setterName, fieldType);

		} catch (NoSuchMethodException e) {

			return null;

		}

	}
}
