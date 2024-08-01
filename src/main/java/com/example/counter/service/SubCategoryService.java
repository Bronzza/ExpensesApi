package com.example.counter.service;

import com.example.counter.dto.SubCategoryDto;
import com.example.counter.entiry.Category;
import com.example.counter.entiry.SubCategory;
import com.example.counter.mapper.SubCategoryMapper;
import com.example.counter.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<Category, List<SubCategory>> getCategorySubCategoryMap() {
        Map<Category, List<SubCategory>> result = new HashMap<Category, List<SubCategory>>();
        Map<Category, SubCategory> collect = repository.findAll()
                .stream()
                .collect(Collectors.toMap(SubCategory::getCategory, subCategory -> subCategory));

        repository.findAll()
                .forEach(subCategory -> {
                    Category category = subCategory.getCategory();
                    List<SubCategory> subCategories = result.get(category);
                    if(subCategories != null) {
                        subCategories.add(subCategory);
                    } else {
                        ArrayList<SubCategory> listOfSubCategories = new ArrayList<>();
                        listOfSubCategories.add(subCategory);
                        result.put(category, listOfSubCategories);
                    }
                });

        return result;
    }
}
