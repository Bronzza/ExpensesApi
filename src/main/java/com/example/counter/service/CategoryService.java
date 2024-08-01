package com.example.counter.service;

import com.example.counter.dto.CategoryDto;
import com.example.counter.dto.CategorySubCategoryDto;
import com.example.counter.entiry.Category;
import com.example.counter.entiry.User;
import com.example.counter.mapper.CategoryMapper;
import com.example.counter.repository.CategoryRepository;
import com.example.counter.util.DefaultCategoriesCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService {

    public static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final DefaultCategoriesCreator creator = new DefaultCategoriesCreator();

    @Lazy
    private final CategoryMapper categoryMapper;
    private final CategoryRepository repository;

    public CategoryService(CategoryMapper categoryMapper, CategoryRepository repository) {
        this.categoryMapper = categoryMapper;
        this.repository = repository;
    }

    @Autowired
    @Lazy
    private UserService userService;

    public CategoryDto createCategoryFromDto(CategoryDto categoryDto, String username) {
        Category categoryToCreate = categoryMapper.dtoToCategory(categoryDto);
        categoryToCreate.setUser(userService.findUserByEmail(username));
        System.out.println("Category was created: " + categoryToCreate);
        return categoryMapper.categoryToDto(repository.save(categoryToCreate));
    }

    public List<Category> createCategoriesAndAttachToUser(User user, Boolean isAdmin) {
        List<Category> categories = isAdmin ? creator.createAdminCategories() : creator.createDefaultCategories();
        logger.info("Categories for user {}, created", user);
        categories.forEach(category -> category.setUser(user));
        return repository.saveAll(categories);
    }

    public List<Category> createCategory(List<Category> categories) {
        return repository.saveAll(categories);
    }

    public Category findCategoryById(Long categoryId) {
        return repository.findById(categoryId).orElseThrow();
    }


    public List<CategorySubCategoryDto> findAll() {
        return repository.findAll()
                .stream()
                .map(categoryMapper::categoryToNestedDto)
                .collect(Collectors.toList());
    }
}
