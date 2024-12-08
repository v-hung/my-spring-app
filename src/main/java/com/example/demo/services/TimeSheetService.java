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
import com.example.demo.models.User;
import com.example.demo.repositories.TimeSheetRepository;
import com.example.demo.utils.TimeSheetUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeSheetService {

	private final TimeSheetRepository timeSheetRepository;

	public TimeSheet performCheckIn(User user) {

		if (timeSheetRepository.findByUserAndDate(user, LocalDate.now()).isPresent()) {

			throw new BusinessException(HttpStatus.CONFLICT, "TimeSheet already exists");

		}

		TimeSheet timeSheet = new TimeSheet()
			.setUser(user)
			.setDate(LocalDate.now())
			.setStartTime(LocalTime.now());

		return timeSheetRepository.save(timeSheet);

	}

	public TimeSheet performCheckOut(User user) {

		TimeSheet timeSheet = timeSheetRepository.findByUserAndDate(user, LocalDate.now())
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Not checked in yet"));

		if (timeSheet.getEndTime() != null) {

			throw new BusinessException(HttpStatus.CONFLICT, "Already checked out");

		}

		var endTime = LocalTime.now();

		timeSheet.setEndTime(endTime);
		timeSheet.setWorkMinutes(TimeSheetUtil.calculateWorkDay(timeSheet.getStartTime(), endTime));

		return timeSheetRepository.save(timeSheet);

	}

	public TimeSheet getTodayTimeSheet(User user) {

		return timeSheetRepository.findByUserAndDate(user, LocalDate.now()).orElse(null);

	}

	public List<TimeSheet> getMonthlyTimeSheets(User user, YearMonth month) {

		LocalDate startDate = month.atDay(1);
		LocalDate endDate = month.atEndOfMonth();

		List<TimeSheet> timeSheets = timeSheetRepository.findByUserAndDateBetween(user, startDate, endDate);

		Map<LocalDate, TimeSheet> timeSheetMap = timeSheets.stream()
			.collect(Collectors.toMap(TimeSheet::getDate, ts -> ts));

		List<TimeSheet> allDaysInMonth = new ArrayList<>();

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

			TimeSheet timeSheetOfDate = timeSheetMap.getOrDefault(date, new TimeSheet()
				.setId(UUID.randomUUID().toString())
				.setUser(user)
				.setDate(date));

			allDaysInMonth.add(timeSheetOfDate);

		}

		return allDaysInMonth;

	}

}
