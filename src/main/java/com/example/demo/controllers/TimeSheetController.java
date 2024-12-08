package com.example.demo.controllers;

import java.time.YearMonth;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TimeSheetDto;
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

	private final ModelMapper mapper;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkin")
	public ResponseEntity<TimeSheetDto> checkIn() {

		User user = authenticationService.getCurrentUser();

		TimeSheet timeSheet = timeSheetService.performCheckIn(user);

		return ResponseEntity.ok(mapper.map(timeSheet, TimeSheetDto.class));

	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkout")
	public ResponseEntity<TimeSheetDto> checkOut() {

		User user = authenticationService.getCurrentUser();

		TimeSheet timeSheet = timeSheetService.performCheckOut(user);

		return ResponseEntity.ok(mapper.map(timeSheet, TimeSheetDto.class));

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/today")
	public ResponseEntity<TimeSheetDto> getTodayTimeSheet() {

		User user = authenticationService.getCurrentUser();

		TimeSheet todayTimeSheet = timeSheetService.getTodayTimeSheet(user);

		if (todayTimeSheet != null) {

			return ResponseEntity.ok(mapper.map(todayTimeSheet, TimeSheetDto.class));

		}

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/monthly")
	public ResponseEntity<List<TimeSheetDto>> getMonthlyTimeSheets(@RequestParam(required = false) YearMonth month) {

		if (month == null) {

			month = YearMonth.now();

		}

		User user = authenticationService.getCurrentUser();
		List<TimeSheet> monthlyTimeSheets = timeSheetService.getMonthlyTimeSheets(user, month);

		List<TimeSheetDto> timeSheetDtos = monthlyTimeSheets.stream()
			.map(timeSheet -> mapper.map(timeSheet, TimeSheetDto.class))
			.toList();

		return ResponseEntity.ok(timeSheetDtos);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/work-times")
	public ResponseEntity<WorkTime> getTimes() {

		User user = authenticationService.getCurrentUser();

		WorkTime workTime = user.getWorkTime();

		if (workTime == null) {

			workTime = new WorkTime();

		}

		return ResponseEntity.ok(workTime);

	}
}
