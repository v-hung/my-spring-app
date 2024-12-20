package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.TimeSheet;

@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, String> {

	Optional<TimeSheet> findByUserIdAndDate(long userId, LocalDate date);

	List<TimeSheet> findByDateBetween(LocalDate startDate, LocalDate endDate);

	List<TimeSheet> findByUserIdAndDateBetween(long userId, LocalDate startDate, LocalDate endDate);

}
