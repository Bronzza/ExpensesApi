package com.example.counter.mapper;


import com.example.counter.dto.CategoryDto;
import com.example.counter.entiry.Category;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper {

    CategoryDto categoryToDto(Category category);

    Category dtoToCategory(CategoryDto categoryDto);
}
