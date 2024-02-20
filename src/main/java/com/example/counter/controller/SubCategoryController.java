package com.example.counter.controller;

import com.example.counter.dto.CategoryDto;
import com.example.counter.dto.SubCategoryDto;
import com.example.counter.service.CategoryService;
import com.example.counter.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subcategory")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @PostMapping
    public ResponseEntity<SubCategoryDto> createSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        return new ResponseEntity<>(subCategoryService.createSubCategory(subCategoryDto), HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategoriesForCategory(@PathVariable Long categoryId) {
        List<SubCategoryDto> subCategories = subCategoryService.findAllSubCategoriesForCategory(categoryId);
        return subCategories.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(subCategories);
    }
}
