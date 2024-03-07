package com.example.counter.repository;

import com.example.counter.entiry.Expanse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpanseRepository extends JpaRepository<Expanse, Long> {

    List<Expanse> findByUserEmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);
}
