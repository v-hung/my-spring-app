package com.example.demo.utils;

import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;

import com.example.demo.exception.BusinessException;

public class ObjectUtils {

	private ObjectUtils() {

		throw new IllegalStateException("Utility class");

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

				try {

					setter = target.getClass().getMethod(setterName, field.getType());

				} catch (NoSuchMethodException e) {

					// Nếu không có setter, bỏ qua
					continue;

				}

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
}
