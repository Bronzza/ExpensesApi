package com.example.counter.mapper;

import com.example.counter.dto.ExpanseDto;
import com.example.counter.dto.ExpanseResponseDto;
import com.example.counter.dto.SubCategoryDto;
import com.example.counter.entiry.Expanse;
import com.example.counter.entiry.SubCategory;
import com.example.counter.service.CategoryService;
import com.example.counter.service.SubCategoryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ExpanseMapper {
    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected SubCategoryService subCategoryService;

    @Mappings({
            @Mapping(target = "categoryId", expression = "java(expanse.getCategory().getId())"),
            @Mapping(target = "subCategoryId", expression = "java(expanse.getSubCategory().getId())"),
    })
    public abstract ExpanseDto expanseToDto(Expanse expanse);

    @Mappings({
            @Mapping(target = "category", expression = "java(categoryService." +
                    "findCategoryById(expanseDto.getCategoryId()))"),
            @Mapping(target = "subCategory", expression = "java(subCategoryService" +
                    ".findById(expanseDto.getSubCategoryId()))")
    })
    public abstract Expanse dtoToExpanse(ExpanseDto expanseDto);

    @Mappings({
            @Mapping(target = "categoryName", expression = "java(expanse.getCategory().getName())"),
            @Mapping(target = "subCategoryName", expression = "java(expanse.getSubCategory().getName())"),
    })
    public abstract ExpanseResponseDto expanseToResponseDto(Expanse expanse);
}
