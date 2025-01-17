package com.example.demo.constant;

import java.time.LocalTime;

public class TimesheetConst {

	private TimesheetConst() {

		throw new IllegalStateException("Utility class");

	}

	public static final LocalTime START_TIME_MORNING = LocalTime.parse("08:00:00");

	public static final LocalTime END_TIME_MORNING = LocalTime.parse("12:00:00");

	public static final LocalTime START_TIME_AFTERNOON = LocalTime.parse("13:30:00");

	public static final LocalTime END_TIME_AFTERNOON = LocalTime.parse("17:30:00");

	public static final int ALLOWED_LATE_MINUTES = 60;

}
