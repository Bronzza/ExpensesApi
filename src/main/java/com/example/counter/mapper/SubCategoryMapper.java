package com.example.counter.mapper;

import com.example.counter.dto.SubCategoryDto;
import com.example.counter.entiry.SubCategory;
import com.example.counter.service.CategoryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SubCategoryMapper {

    @Autowired
    @Lazy
    protected CategoryService categoryService;

    @Mappings({
            @Mapping(target = "categoryId", expression = "java(subCategory.getCategory().getId())")
    })
    public abstract SubCategoryDto subCategoryToDto(SubCategory subCategory);

    @Mappings({
            @Mapping(target = "category", expression = "java(categoryService.findCategoryById(subCategoryDto.getCategoryId()))")
    })
    public abstract SubCategory dtoToSubCategory(SubCategoryDto subCategoryDto);
}
