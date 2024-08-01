package com.example.counter.service;

import com.example.counter.dto.RegistrationDto;
import com.example.counter.dto.RegistrationResponseDto;
import com.example.counter.entiry.Category;
import com.example.counter.entiry.User;
import com.example.counter.entiry.enums.Role;
import com.example.counter.mapper.RegistrationMapper;
import com.example.counter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RegistrationMapper mapper;
    @Lazy
    private final CategoryService categoryService;


    //re-do structure
    //move creator to category service, and on registration, save user, than pass it to category service
    //and attach user to each category
    public RegistrationResponseDto registerUserFromDto(RegistrationDto dto) {
        User userToSave = mapper.dtoToUser(dto);
//        userToSave.setCategories(saveDefaultCategoriesToDB(false));
        User savedUser = userRepository.save(userToSave);
//        categoryService.createCategoriesAndAttachToUser(savedUser, false);
        categoryService.createCategoriesAndAttachToUser(savedUser,false);
        return mapper.userToDto(savedUser);
    }

    public User registerAdminUser() {
        User adminUser = createAdminUser();
//        return userRepository.save(adminUser);
        return adminUser;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    private User createAdminUser() {
        return User.builder()
                .email("repinetskiy@gmail.com")
                .role(Role.ADMIN)
                .password("Sergey1")
                .firstName("Serhii")
                .lastName("Repinetskyi")
                .build();
    }

    public User updateUser(User toUpdate) {
        return userRepository.saveAndFlush(toUpdate);
    }
}
