package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.TimeSheet;
import com.example.demo.models.User;
import com.example.demo.repositories.TimeSheetRepository;
import com.example.demo.utils.TimeSheetUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeSheetService {

	private final TimeSheetRepository timeSheetRepository;

	public TimeSheet performCheckIn(int userId) {

		if (timeSheetRepository.findByUserIdAndDate(userId, LocalDate.now()).isPresent()) {

			throw new BusinessException(HttpStatus.CONFLICT, "TimeSheet already exists for user with ID: " + userId);

		}

		TimeSheet timeSheet = new TimeSheet()
			.setUser(new User().setId(userId))
			.setDate(LocalDate.now())
			.setStartTime(LocalTime.now());

		return timeSheetRepository.save(timeSheet);

	}

	public TimeSheet performCheckOut(int userId) {

		TimeSheet timeSheet = timeSheetRepository.findByUserIdAndDate(userId, LocalDate.now())
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, userId + " has not checked in yet"));

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

		List<TimeSheet> timeSheets = timeSheetRepository.findByDateBetween(startDate, endDate);

		Map<LocalDate, TimeSheet> timeSheetMap = timeSheets.stream()
			.collect(Collectors.toMap(TimeSheet::getDate, ts -> ts));

		List<TimeSheet> allDaysInMonth = new ArrayList<>();

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

			TimeSheet timeSheetOfDate = timeSheetMap.getOrDefault(date, new TimeSheet()
				.setId(null)
				.setDate(date)
				.setUser(null));

			allDaysInMonth.add(timeSheetOfDate);

		}

		return allDaysInMonth;

	}

}
