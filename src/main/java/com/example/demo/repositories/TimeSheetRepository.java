package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.TimeSheet;
import com.example.demo.models.User;

@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, String> {

	Optional<TimeSheet> findByUserAndDate(User user, LocalDate date);

	List<TimeSheet> findByDateBetween(LocalDate startDate, LocalDate endDate);

	List<TimeSheet> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

}
