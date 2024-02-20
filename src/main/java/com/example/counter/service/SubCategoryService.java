package com.example.counter.service;

import com.example.counter.dto.SubCategoryDto;
import com.example.counter.entiry.SubCategory;
import com.example.counter.mapper.SubCategoryMapper;
import com.example.counter.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubCategoryService {
    private final SubCategoryRepository repository;
    private final SubCategoryMapper mapper;
    private final CategoryService categoryService;

    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto) {
        SubCategory subCategory = mapper.dtoToSubCategory(subCategoryDto);
        SubCategory createdSubCategory = repository.save(subCategory);
        return mapper.subCategoryToDto(createdSubCategory);

//        return mapper.categoryToDto(repository.createdSubCategory(mapper.dtoToCategory(subCategoryDto)));
    }

    public List<SubCategoryDto> findAllSubCategoriesForCategory(Long categoryId) {
        List<SubCategory> subCategories = categoryService.findCategoryById(categoryId).getSubCategories();

        return subCategories
                .stream()
                .map(mapper::subCategoryToDto)
                .toList();

    }

    public SubCategory findById(Long subCategoryId) {
        return repository.findById(subCategoryId).orElseThrow();
    }
}
