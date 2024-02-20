package com.example.counter.service;

import com.example.counter.dto.ExpanseDto;
import com.example.counter.entiry.Expanse;
import com.example.counter.mapper.ExpanseMapper;
import com.example.counter.repository.ExpanseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpansesService {

    private final ExpanseMapper expanseMapper;
    private final ExpanseRepository expanseRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    public ExpanseDto createExpanse(ExpanseDto expanseDto) {
        Expanse expanseToCreate = expanseMapper.dtoToExpanse(expanseDto);
        Expanse createdExpanse = expanseRepository.save(expanseToCreate);
        return expanseMapper.expanseToDto(createdExpanse);

    }

    public ExpanseDto updateExpanse(ExpanseDto expanseDto) {
        Expanse expanseToChange = updateValues(expanseRepository.findById(expanseDto.getId()).orElseThrow(), expanseDto);
        Expanse createdExpanse = expanseRepository.save(expanseToChange);
        return expanseMapper.expanseToDto(createdExpanse);
    }

    private Expanse updateValues(Expanse expanse, ExpanseDto expanseDto) {
        if (!expanse.getAmount().equals(expanseDto.getAmount())) {
            expanse.setAmount(expanseDto.getAmount());
        }
        if (!expanse.getCategory().getId().equals(expanseDto.getCategoryId())) {
            expanse.setCategory(categoryService.findCategoryById(expanseDto.getCategoryId()));
        }
        if (!expanse.getSubCategory().getId().equals(expanseDto.getSubCategoryId())) {
            expanse.setSubCategory(subCategoryService.findById(expanseDto.getSubCategoryId()));
        }

        if (!expanse.getDate().isEqual(expanseDto.getDate())) {
            expanse.setDate(expanseDto.getDate());
        }

        if (!expanse.getDescription().equals(expanseDto.getDescription())) {
            expanse.setDescription(expanseDto.getDescription());
        }
        return expanse;
    }
}
