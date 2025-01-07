package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.WorkTimeDto;
import com.example.demo.requests.WorkTimeCreateUpdateRequest;
import com.example.demo.services.WorkTimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/work-times")
@RequiredArgsConstructor
public class WorkTimeController {

	private final WorkTimeService workTimeService;

	@GetMapping()
	public ResponseEntity<List<WorkTimeDto>> getWorkTimes() {

		List<WorkTimeDto> workTimes = workTimeService.getAll(WorkTimeDto.class);

		return ResponseEntity.ok(workTimes);

	}

	@GetMapping("/{id}")
	public ResponseEntity<WorkTimeDto> getWorkTime(@PathVariable long id) {

		WorkTimeDto workTime = workTimeService.getById(id, WorkTimeDto.class);

		return ResponseEntity.ok(workTime);

	}

	@PostMapping()
	public ResponseEntity<WorkTimeDto> createWorkTime(@RequestBody WorkTimeCreateUpdateRequest model) {

		WorkTimeDto workTime = workTimeService.createWorkTime(model, WorkTimeDto.class);

		return ResponseEntity.ok(workTime);

	}

	@PutMapping("/{id}/edit")
	@PatchMapping("/{id}/edit")
	public ResponseEntity<WorkTimeDto> updateWorkTime(@PathVariable long id,
		@RequestBody WorkTimeCreateUpdateRequest model) {

		WorkTimeDto workTime = workTimeService.updateWorkTime(id, model, WorkTimeDto.class);

		return ResponseEntity.ok(workTime);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteWorkTime(@PathVariable long id) {

		workTimeService.deleteWorkTimeById(id);

		return ResponseEntity.ok("WorkTime is deleted");

	}

}
