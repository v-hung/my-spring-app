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
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.Timesheet;
import com.example.demo.models.User;
import com.example.demo.repositories.TimesheetRepository;
import com.example.demo.utils.TimesheetUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimesheetService {

	private final TimesheetRepository timesheetRepository;

	@Transactional
	public Timesheet performCheckIn(User user) {

		if (timesheetRepository.findByUserIdAndDate(user.getId(), LocalDate.now()).isPresent()) {

			throw new BusinessException(HttpStatus.CONFLICT, "Timesheet already exists");

		}

		Timesheet timesheet = new Timesheet()
			.setUser(user)
			.setDate(LocalDate.now())
			.setStartTime(LocalTime.now());

		timesheetRepository.save(timesheet);

		return timesheet;

	}

	@Transactional
	public Timesheet performCheckOut(User user) {

		Timesheet timesheet = timesheetRepository.findByUserIdAndDate(user.getId(), LocalDate.now())
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Not checked in yet"));

		if (timesheet.getEndTime() != null) {

			throw new BusinessException(HttpStatus.CONFLICT, "Already checked out");

		}

		var endTime = LocalTime.now();

		timesheet.setEndTime(endTime);
		timesheet.setWorkMinutes(TimesheetUtils.calculateWorkDay(timesheet.getStartTime(), endTime));

		return timesheetRepository.save(timesheet);

	}

	public Timesheet getTodayTimesheet(User user) {

		return timesheetRepository.findByUserIdAndDate(user.getId(), LocalDate.now()).orElse(null);

	}

	@Transactional(readOnly = true)
	public List<Timesheet> getMonthlyTimesheets(User user, YearMonth month) {

		LocalDate startDate = month.atDay(1);
		LocalDate endDate = month.atEndOfMonth();

		List<Timesheet> timesheets = timesheetRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);

		Map<LocalDate, Timesheet> timesheetMap = timesheets.stream()
			.collect(Collectors.toMap(Timesheet::getDate, ts -> ts));

		List<Timesheet> allDaysInMonth = new ArrayList<>();

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

			Timesheet timesheetOfDate = timesheetMap.getOrDefault(date, new Timesheet()
				.setId(UUID.randomUUID().toString())
				.setUser(user)
				.setDate(date));

			allDaysInMonth.add(timesheetOfDate);

		}

		return allDaysInMonth;

	}

}
