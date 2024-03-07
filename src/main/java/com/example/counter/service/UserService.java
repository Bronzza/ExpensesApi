package com.example.counter.service;

import com.example.counter.dto.RegistrationDto;
import com.example.counter.dto.RegistrationResponseDto;
import com.example.counter.entiry.Category;
import com.example.counter.entiry.User;
import com.example.counter.entiry.enums.Role;
import com.example.counter.mapper.RegistrationMapper;
import com.example.counter.repository.UserRepository;
import com.example.counter.util.DefaultCategoriesCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RegistrationMapper mapper;
    private final CategoryService categoryService;
    private DefaultCategoriesCreator creator = new DefaultCategoriesCreator();

    public RegistrationResponseDto registerUserFromDto(RegistrationDto dto) {
        User userToSave = mapper.dtoToUser(dto);
        userToSave.setCategories(saveDefaultCategoriesToDB(false));
        return mapper.userToDto(userRepository.save(userToSave));
    }

    public void registerAdminUser() {
        User adminUser = createAdminUser();
        adminUser.setCategories(saveDefaultCategoriesToDB(true));
        userRepository.save(adminUser);
    }

    private List<Category> saveDefaultCategoriesToDB(Boolean isAdmin) {
        List<Category> categoriesToSave = isAdmin ? creator.createAdminCategories() : creator.createDefaultCategories();
        List<Category> categories = categoryService.createCategory(categoriesToSave);
        return categories;
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
}
