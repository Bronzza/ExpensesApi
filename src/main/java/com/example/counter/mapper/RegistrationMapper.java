package com.example.counter.mapper;

import com.example.counter.dto.RegistrationDto;
import com.example.counter.dto.RegistrationResponseDto;
import com.example.counter.entiry.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RegistrationMapper {
    RegistrationResponseDto userToDto(User user);

    User dtoToUser(RegistrationDto dto);
}
