package com.example.counter.mapper;


import com.example.counter.dto.CategoryDto;
import com.example.counter.dto.CategorySubCategoryDto;
import com.example.counter.entiry.Category;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CategoryMapper {

    @Autowired
    @Lazy
    protected SubCategoryMapper subCategoryMapper;

    public abstract CategoryDto categoryToDto(Category category);

    public abstract Category dtoToCategory(CategoryDto categoryDto);

    @Mappings({
            @Mapping(target = "subCategoryList", expression = "java(category.getSubCategories().stream().map(subCategoryMapper::subCategoryToDto).toList())")
    })
    public abstract CategorySubCategoryDto categoryToNestedDto(Category category);
}