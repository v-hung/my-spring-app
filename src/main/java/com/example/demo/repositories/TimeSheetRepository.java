package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.TimeSheet;

@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, UUID> {

	Optional<TimeSheet> findByUserIdAndDate(int userId, LocalDate date);

	List<TimeSheet> findByDateBetween(LocalDate startDate, LocalDate endDate);

	List<TimeSheet> findByUserIdAndDateBetween(int userId, LocalDate startDate, LocalDate endDate);

}
