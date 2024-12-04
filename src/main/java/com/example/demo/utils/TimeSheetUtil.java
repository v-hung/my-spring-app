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

		int totalWorkingMinutes = 0;

		if (isInvalidTimeRange(startTime, endTime)) {

			return totalWorkingMinutes;

		}

		// Establish a flexible afternoon end time
		LocalTime adjustedEndTimeAfternoon = workTime.getEndTimeAfternoon();

		if (startTime.isAfter(workTime.getStartTimeMorning())) {

			// Calculate the number of minutes late
			int lateMinutes = minutesBetween(workTime.getStartTimeMorning(), startTime);

			// Limit lateness to a maximum
			if (lateMinutes <= workTime.getLateMinutes()) {

				adjustedEndTimeAfternoon = workTime.getEndTimeAfternoon().plusMinutes(lateMinutes);

			}

		}

		// Calculate morning work minutes
		if (startTime.isBefore(workTime.getEndTimeMorning())) {

			LocalTime validMorningStart = startTime.isBefore(workTime.getStartTimeMorning())
				? workTime.getStartTimeMorning()
				: startTime;
			LocalTime validMorningEnd = endTime.isAfter(workTime.getEndTimeMorning())
				? workTime.getEndTimeMorning()
				: endTime;

			if (validMorningStart.isBefore(validMorningEnd)) {

				totalWorkingMinutes += minutesBetween(validMorningStart, validMorningEnd);

			}

		}

		// Calculate afternoon work minutes
		if (endTime.isAfter(workTime.getStartTimeAfternoon())) {

			LocalTime validAfternoonStart = startTime.isAfter(workTime.getStartTimeAfternoon()) ? startTime
				: workTime.getStartTimeAfternoon();
			LocalTime validAfternoonEnd = endTime.isAfter(workTime.getEndTimeAfternoon())
				? workTime.getEndTimeAfternoon()
				: endTime;
			// LocalTime validAfternoonEnd = endTime; //NOSONA 

			if (validAfternoonStart.isBefore(validAfternoonEnd)) {

				totalWorkingMinutes += minutesBetween(validAfternoonStart, validAfternoonEnd);

			}

		}

	}

	private static int calculateMorningWorkMinutes(LocalTime startTime, LocalTime endTime, WorkTime workTime) {

		if (!startTime.isBefore(workTime.getEndTimeMorning())) {

			return 0;

		}

		LocalTime validMorningStart = startTime.isBefore(workTime.getStartTimeMorning())
			? workTime.getStartTimeMorning()
			: startTime;
		LocalTime validMorningEnd = endTime.isAfter(workTime.getEndTimeMorning())
			? workTime.getEndTimeMorning()
			: endTime;

		return validMorningStart.isBefore(validMorningEnd)
			? minutesBetween(validMorningStart, validMorningEnd)
			: 0;

	}

	private static int calculateAfternoonWorkMinutes(LocalTime startTime, LocalTime endTime, WorkTime workTime) {

		if (!endTime.isAfter(workTime.getStartTimeAfternoon())) {

			return 0;

		}

		LocalTime validAfternoonStart = startTime.isAfter(workTime.getStartTimeAfternoon())
			? startTime
			: workTime.getStartTimeAfternoon();
		LocalTime validAfternoonEnd = endTime.isAfter(workTime.getEndTimeAfternoon())
			? workTime.getEndTimeAfternoon()
			: endTime;

		return validAfternoonStart.isBefore(validAfternoonEnd)
			? minutesBetween(validAfternoonStart, validAfternoonEnd)
			: 0;

	}

	public static int minutesBetween(LocalTime start, LocalTime end) {

		return (int)Duration.between(start, end).toMinutes();

	}

	private static boolean isInvalidTimeRange(LocalTime startTime, LocalTime endTime) {

		return startTime == null || endTime == null || startTime.isAfter(endTime);

	}
}
