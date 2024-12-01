package com.example.demo.utils;

import java.time.Duration;
import java.time.LocalTime;

import com.example.demo.constant.TimeSheetConst;

public class TimeSheetUtil {
	private TimeSheetUtil() {

		throw new IllegalStateException("Utility class");

	}

	public static int calculateWorkDay(LocalTime starTime, LocalTime endTime) {

		int totalWorkingMinutes = 0;

		// Calculate morning work minutes
		if (starTime.isBefore(TimeSheetConst.END_TIME_MORNING)) {

			LocalTime validMorningStart = starTime.isBefore(TimeSheetConst.START_TIME_MORNING)
				? TimeSheetConst.START_TIME_MORNING
				: starTime;
			LocalTime validMorningEnd = endTime.isAfter(TimeSheetConst.END_TIME_MORNING)
				? TimeSheetConst.END_TIME_MORNING
				: endTime;

			if (validMorningStart.isBefore(validMorningEnd)) {

				totalWorkingMinutes += minutesBetween(validMorningStart, validMorningEnd);

			}

		}

		// Calculate afternoon work minutes
		if (endTime.isAfter(TimeSheetConst.START_TIME_AFTERNOON)) {

			LocalTime validAfternoonStart = starTime.isAfter(TimeSheetConst.START_TIME_AFTERNOON) ? starTime
				: TimeSheetConst.START_TIME_AFTERNOON;
			LocalTime validAfternoonEnd = endTime.isAfter(TimeSheetConst.END_TIME_AFTERNOON)
				? TimeSheetConst.END_TIME_AFTERNOON
				: endTime;
			// LocalTime validAfternoonEnd = endTime;

			if (validAfternoonStart.isBefore(validAfternoonEnd)) {

				totalWorkingMinutes += minutesBetween(validAfternoonStart, validAfternoonEnd);

			}

		}

		return totalWorkingMinutes;

	}

	public static int minutesBetween(LocalTime start, LocalTime end) {

		return (int)Duration.between(start, end).toMinutes();

	}
}
