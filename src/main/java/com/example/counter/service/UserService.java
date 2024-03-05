package com.example.counter.service;

import com.example.counter.dto.RegistrationDto;
import com.example.counter.dto.RegistrationResponseDto;
import com.example.counter.entiry.User;
import com.example.counter.mapper.RegistrationMapper;
import com.example.counter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RegistrationMapper mapper;

    public RegistrationResponseDto registerUser(RegistrationDto dto) {
      return mapper.userToDto(userRepository.save(mapper.dtoToUser(dto))) ;
    }
}
