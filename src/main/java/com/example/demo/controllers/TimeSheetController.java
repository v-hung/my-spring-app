package com.example.demo.controllers;

import java.time.YearMonth;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.TimeSheet;
import com.example.demo.models.User;
import com.example.demo.models.WorkTime;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.TimeSheetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class TimeSheetController {

	private final TimeSheetService timeSheetService;

	private final AuthenticationService authenticationService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkin")
	public ResponseEntity<TimeSheet> checkIn() {

		User user = authenticationService.getCurrentUser();

		TimeSheet timeSheet = timeSheetService.performCheckIn(user.getId());

		return ResponseEntity.ok(timeSheet);

	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkout")
	public ResponseEntity<TimeSheet> checkOut() {

		User user = authenticationService.getCurrentUser();

		TimeSheet timeSheet = timeSheetService.performCheckOut(user.getId());

		return ResponseEntity.ok(timeSheet);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/today")
	public ResponseEntity<TimeSheet> getTodayTimeSheet() {

		User user = authenticationService.getCurrentUser();

		TimeSheet todayTimeSheet = timeSheetService.getTodayTimeSheet(user.getId());

		return ResponseEntity.ok(todayTimeSheet);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/monthly")
	public ResponseEntity<List<TimeSheet>> getMonthlyTimeSheets(@RequestParam(required = false) YearMonth month) {

		if (month == null) {

			month = YearMonth.now();

		}

		User user = authenticationService.getCurrentUser();
		List<TimeSheet> monthlyTimeSheets = timeSheetService.getMonthlyTimeSheets(user.getId(), month);

		return ResponseEntity.ok(monthlyTimeSheets);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/work-times")
	public ResponseEntity<WorkTime> getTimes() {

		User user = authenticationService.getCurrentUser();
		return ResponseEntity.ok(timeSheetService.getUserWorkTime(user.getId()));

	}
}
