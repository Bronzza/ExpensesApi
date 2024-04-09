package com.example.counter.repository;

import com.example.counter.entiry.Expanse;
import com.example.counter.entiry.User;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpanseRepository extends JpaRepository<Expanse, Long> {

    List<Expanse> findByUserEmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);

    List<Expanse> findFirst10ByUserEmailOrderByDateDesc (String email);

    List<Expanse> findByUserEmail(String userEmail, PageRequest pageRequest);
}
