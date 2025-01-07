package com.example.demo.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.BusinessException;
import com.example.demo.models.WorkTime;
import com.example.demo.repositories.WorkTimeRepository;
import com.example.demo.requests.WorkTimeCreateUpdateRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WorkTimeService {

	private final WorkTimeRepository workTimeRepository;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public <D> List<D> getAll(Class<D> dtoClass) {

		List<WorkTime> workTimes = workTimeRepository.findAll();

		return workTimes.stream().map(t -> modelMapper.map(t, dtoClass)).toList();

	}

	@Transactional(readOnly = true)
	public <D> D getById(long id, Class<D> dtoClass) {

		WorkTime workTime = workTimeRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "WorkTime does not exits"));

		return modelMapper.map(workTime, dtoClass);

	}

	@Transactional
	public <D> D createWorkTime(WorkTimeCreateUpdateRequest data, Class<D> dtoClass) {

		WorkTime workTime = new WorkTime();

		setWorkTimeInformation(workTime, data);

		workTimeRepository.save(workTime);

		return modelMapper.map(workTime, dtoClass);

	}

	@Transactional
	public <D> D updateWorkTime(long id, WorkTimeCreateUpdateRequest data, Class<D> dtoClass) {

		WorkTime workTime = workTimeRepository.findById(id)
			.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "WorkTime does not exits"));

		setWorkTimeInformation(workTime, data);

		workTimeRepository.save(workTime);

		return modelMapper.map(workTime, dtoClass);

	}

	public void deleteWorkTimeById(long id) {

		workTimeRepository.deleteById(id);

	}

	private void setWorkTimeInformation(WorkTime workTime, WorkTimeCreateUpdateRequest data) {

		workTime.setTitle(data.getTitle())
			.setStartTimeMorning(data.getStartTimeMorning())
			.setEndTimeMorning(data.getEndTimeMorning())
			.setStartTimeAfternoon(data.getStartTimeAfternoon())
			.setEndTimeAfternoon(data.getEndTimeAfternoon())
			.setAllowedLateMinutes(data.getAllowedLateMinutes());

	}

}
