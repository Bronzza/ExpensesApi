package com.example.counter.controller;

import com.example.counter.dto.CategoryDto;
import com.example.counter.dto.SubCategoryDto;
import com.example.counter.entiry.Category;
import com.example.counter.mapper.SubCategoryMapper;
import com.example.counter.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.createCategoryFromDto(categoryDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getAllCategoriesWithSubcategories() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }
}
