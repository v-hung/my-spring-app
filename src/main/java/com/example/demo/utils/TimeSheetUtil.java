package com.example.demo.utils;

import java.time.Duration;
import java.time.LocalTime;

import com.example.demo.models.WorkTime;

public class TimeSheetUtil {
	private TimeSheetUtil() {

		throw new IllegalStateException("Utility class");

	}

	public static int calculateWorkDay(LocalTime starTime, LocalTime endTime) {

		return calculateWorkDay(starTime, endTime, new WorkTime());

	}

	public static int calculateWorkDay(LocalTime starTime, LocalTime endTime, WorkTime workTime) {

		int totalWorkingMinutes = 0;

		// Calculate morning work minutes
		if (starTime.isBefore(workTime.getEndTimeMorning())) {

			LocalTime validMorningStart = starTime.isBefore(workTime.getStartTimeMorning())
				? workTime.getStartTimeMorning()
				: starTime;
			LocalTime validMorningEnd = endTime.isAfter(workTime.getEndTimeMorning())
				? workTime.getEndTimeMorning()
				: endTime;

			if (validMorningStart.isBefore(validMorningEnd)) {

				totalWorkingMinutes += minutesBetween(validMorningStart, validMorningEnd);

			}

		}

		// Calculate afternoon work minutes
		if (endTime.isAfter(workTime.getStartTimeAfternoon())) {

			LocalTime validAfternoonStart = starTime.isAfter(workTime.getStartTimeAfternoon()) ? starTime
				: workTime.getStartTimeAfternoon();
			LocalTime validAfternoonEnd = endTime.isAfter(workTime.getEndTimeAfternoon())
				? workTime.getEndTimeAfternoon()
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
