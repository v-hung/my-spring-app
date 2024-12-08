package com.example.demo.utils;

import java.time.Duration;
import java.time.LocalTime;

import com.example.demo.models.WorkTime;

public class TimeSheetUtil {
	private TimeSheetUtil() {

		throw new IllegalStateException("Utility class");

	}

	public static int calculateWorkDay(LocalTime startTime, LocalTime endTime) {

		return calculateWorkDay(startTime, endTime, new WorkTime());

	}

	public static int calculateWorkDay(LocalTime startTime, LocalTime endTime, WorkTime workTime) {

		if (isInvalidTimeRange(startTime, endTime, workTime)) {

			return 0;

		}

		LocalTime adjustedEndTimeAfternoon = calculateAdjustedEndTimeAfternoon(startTime, workTime);
		int morningSeconds = calculateMorningSeconds(startTime, endTime, workTime);
		int afternoonSeconds = calculateAfternoonSeconds(startTime, endTime, adjustedEndTimeAfternoon, workTime);

		return (morningSeconds + afternoonSeconds) / 60;

	}

	private static LocalTime calculateAdjustedEndTimeAfternoon(LocalTime startTime, WorkTime workTime) {

		if (startTime.isAfter(workTime.getStartTimeMorning())) {

			int lateSeconds = secondsBetween(workTime.getStartTimeMorning(), startTime);
			int adjustedSeconds = Math.min(workTime.getAllowedLateMinutes() * 60, lateSeconds);
			return workTime.getEndTimeAfternoon().plusSeconds(adjustedSeconds);

		}

		return workTime.getEndTimeAfternoon();

	}

	private static int calculateMorningSeconds(LocalTime startTime, LocalTime endTime, WorkTime workTime) {

		if (startTime.isBefore(workTime.getEndTimeMorning())) {

			LocalTime validMorningStart = startTime.isBefore(workTime.getStartTimeMorning())
				? workTime.getStartTimeMorning()
				: startTime;
			LocalTime validMorningEnd = endTime.isAfter(workTime.getEndTimeMorning())
				? workTime.getEndTimeMorning()
				: endTime;

			if (validMorningStart.isBefore(validMorningEnd)) {

				return secondsBetween(validMorningStart, validMorningEnd);

			}

		}

		return 0;

	}

	private static int calculateAfternoonSeconds(LocalTime startTime, LocalTime endTime,
		LocalTime adjustedEndTimeAfternoon, WorkTime workTime) {

		if (endTime.isAfter(workTime.getStartTimeAfternoon())) {

			LocalTime validAfternoonStart = startTime.isAfter(workTime.getStartTimeAfternoon())
				? startTime
				: workTime.getStartTimeAfternoon();
			LocalTime validAfternoonEnd = endTime.isAfter(adjustedEndTimeAfternoon)
				? adjustedEndTimeAfternoon
				: endTime;

			if (validAfternoonStart.isBefore(validAfternoonEnd)) {

				return secondsBetween(validAfternoonStart, validAfternoonEnd);

			}

		}

		return 0;

	}

	public static int secondsBetween(LocalTime start, LocalTime end) {

		return (int)Duration.between(start, end).toSeconds();

	}

	private static boolean isInvalidTimeRange(LocalTime startTime, LocalTime endTime, WorkTime workTime) {

		return startTime == null || endTime == null || startTime.isAfter(endTime)
			|| startTime.isAfter(workTime.getEndTimeAfternoon()) || endTime.isBefore(workTime.getStartTimeMorning());

	}
}
