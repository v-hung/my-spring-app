package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.TimeSheet;
import com.example.demo.models.WorkTime;
import com.example.demo.repositories.TimeSheetRepository;
import com.example.demo.repositories.WorkTimeRepository;
import com.example.demo.utils.TimeSheetUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeSheetService {

	private final TimeSheetRepository timeSheetRepository;

	private final WorkTimeRepository workTimeRepository;

	public TimeSheet performCheckIn(int userId) {

		if (timeSheetRepository.findByUserIdAndDate(userId, LocalDate.now()).isPresent()) {

			throw new BusinessException(HttpStatus.CONFLICT, "TimeSheet already exists");

		}

		TimeSheet timeSheet = new TimeSheet()
			.setUserId(userId)
			.setDate(LocalDate.now())
			.setStartTime(LocalTime.now());

		return timeSheetRepository.save(timeSheet);

	}

	public TimeSheet performCheckOut(int userId) {

		TimeSheet timeSheet = timeSheetRepository.findByUserIdAndDate(userId, LocalDate.now())
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Not checked in yet"));

		if (timeSheet.getEndTime() != null) {

			throw new BusinessException(HttpStatus.CONFLICT, "Already checked out");

		}

		var endTime = LocalTime.now();

		timeSheet.setEndTime(endTime);
		timeSheet.setWorkMinutes(TimeSheetUtil.calculateWorkDay(timeSheet.getStartTime(), endTime));

		return timeSheetRepository.save(timeSheet);

	}

	public TimeSheet getTodayTimeSheet(int userId) {

		return timeSheetRepository.findByUserIdAndDate(userId, LocalDate.now()).orElse(null);

	}

	public List<TimeSheet> getMonthlyTimeSheets(int userId, YearMonth month) {

		LocalDate startDate = month.atDay(1);
		LocalDate endDate = month.atEndOfMonth();

		List<TimeSheet> timeSheets = timeSheetRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

		Map<LocalDate, TimeSheet> timeSheetMap = timeSheets.stream()
			.collect(Collectors.toMap(TimeSheet::getDate, ts -> ts));

		List<TimeSheet> allDaysInMonth = new ArrayList<>();

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

			TimeSheet timeSheetOfDate = timeSheetMap.getOrDefault(date, new TimeSheet()
				.setId(UUID.randomUUID().toString())
				.setUserId(userId)
				.setDate(date));

			allDaysInMonth.add(timeSheetOfDate);

		}

		return allDaysInMonth;

	}

	public WorkTime getUserWorkTime(int userId) {

		return workTimeRepository.findByUserId(userId).orElse(new WorkTime());

	}

}
