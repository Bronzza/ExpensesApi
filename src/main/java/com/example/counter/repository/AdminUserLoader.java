package com.example.counter.repository;

import com.example.counter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserLoader implements ApplicationRunner {

    private final UserService userService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.registerAdminUser();
    }
}
