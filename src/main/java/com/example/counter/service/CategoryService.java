package com.example.counter.service;

import com.example.counter.dto.CategoryDto;
import com.example.counter.entiry.Category;
import com.example.counter.mapper.CategoryMapper;
import com.example.counter.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository repository;

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category categoryToCreate = categoryMapper.dtoToCategory(categoryDto);
        System.out.println("Category was created: " + categoryToCreate);
        return categoryMapper.categoryToDto(repository.save(categoryToCreate));
    }

    public Category findCategoryById(Long categoryId) {
        return repository.findById(categoryId).orElseThrow();
    }


}
