package com.example.counter.service;

import com.example.counter.dto.CategoryDto;
import com.example.counter.dto.CategorySubCategoryDto;
import com.example.counter.entiry.Category;
import com.example.counter.mapper.CategoryMapper;
import com.example.counter.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    @Lazy
    private final CategoryMapper categoryMapper;
    private final CategoryRepository repository;

    public CategoryDto createCategoryFromDto(CategoryDto categoryDto) {
        Category categoryToCreate = categoryMapper.dtoToCategory(categoryDto);
        System.out.println("Category was created: " + categoryToCreate);
        return categoryMapper.categoryToDto(repository.save(categoryToCreate));
    }

    public Category createCategory(Category category) {
        return repository.save(category);
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
