package com.example.counter.repository;

import com.example.counter.entiry.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Long> {
    public User findUserByEmail(String email);
}
