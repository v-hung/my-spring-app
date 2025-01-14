package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Timesheet;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, String> {

	Optional<Timesheet> findByUserIdAndDate(long userId, LocalDate date);

	List<Timesheet> findByDateBetween(LocalDate startDate, LocalDate endDate);

	List<Timesheet> findByUserIdAndDateBetween(long userId, LocalDate startDate, LocalDate endDate);

}
