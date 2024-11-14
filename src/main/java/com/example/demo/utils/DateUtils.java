package com.example.demo.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	private DateUtils() {

		throw new IllegalStateException("Utility class");

	}

	public static LocalDate convertDateToLocalDate(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}

	public static LocalDateTime convertDateToLocalDateTime(Date date) {

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

	}

}
