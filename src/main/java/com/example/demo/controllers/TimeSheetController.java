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

import com.example.demo.dto.TimesheetDto;
import com.example.demo.models.Timesheet;
import com.example.demo.models.User;
import com.example.demo.models.WorkTime;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.TimesheetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class TimesheetController {

	private final TimesheetService timesheetService;

	private final AuthenticationService authenticationService;

	private final ModelMapper mapper;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkin")
	public ResponseEntity<TimesheetDto> checkIn() {

		User user = authenticationService.getCurrentUser();

		Timesheet timesheet = timesheetService.performCheckIn(user);

		return ResponseEntity.ok(mapper.map(timesheet, TimesheetDto.class));

	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkout")
	public ResponseEntity<TimesheetDto> checkOut() {

		User user = authenticationService.getCurrentUser();

		Timesheet timesheet = timesheetService.performCheckOut(user);

		return ResponseEntity.ok(mapper.map(timesheet, TimesheetDto.class));

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/today")
	public ResponseEntity<TimesheetDto> getTodayTimesheet() {

		User user = authenticationService.getCurrentUser();

		Timesheet todayTimesheet = timesheetService.getTodayTimesheet(user);

		if (todayTimesheet != null) {

			return ResponseEntity.ok(mapper.map(todayTimesheet, TimesheetDto.class));

		}

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/monthly")
	public ResponseEntity<List<TimesheetDto>> getMonthlyTimesheets(@RequestParam(required = false) YearMonth month) {

		if (month == null) {

			month = YearMonth.now();

		}

		User user = authenticationService.getCurrentUser();
		List<Timesheet> monthlyTimesheets = timesheetService.getMonthlyTimesheets(user, month);

		List<TimesheetDto> timesheetDtos = monthlyTimesheets.stream()
			.map(timesheet -> mapper.map(timesheet, TimesheetDto.class))
			.toList();

		return ResponseEntity.ok(timesheetDtos);

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
