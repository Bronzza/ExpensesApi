package com.example.counter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySubCategoryDto {
    private Long id;
    public String name;
    private List<SubCategoryDto> subCategoryList;

}
