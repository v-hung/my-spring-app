package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.WorkTime;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long> {

}
