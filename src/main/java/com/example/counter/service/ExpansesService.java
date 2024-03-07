package com.example.counter.service;

import com.example.counter.dto.ExpanseDto;
import com.example.counter.entiry.Expanse;
import com.example.counter.entiry.User;
import com.example.counter.mapper.ExpanseMapper;
import com.example.counter.repository.ExpanseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpansesService {

    private final ExpanseMapper expanseMapper;
    private final ExpanseRepository expanseRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final UserService userService;

    public ExpanseDto createExpanse(ExpanseDto expanseDto, String username) {
        Expanse expanseToCreate = expanseMapper.dtoToExpanse(expanseDto);
        User userToUpdate = userService.findUserByEmail(username);
        expanseToCreate.setUser(userToUpdate);

        Expanse createdExpanse = expanseRepository.save(expanseToCreate);
        return expanseMapper.expanseToDto(createdExpanse);
    }

    public ExpanseDto updateExpanse(ExpanseDto expanseDto) {
        Expanse expanseToChange = updateValues(expanseRepository.findById(expanseDto.getId()).orElseThrow(), expanseDto);
        Expanse createdExpanse = expanseRepository.save(expanseToChange);
        return expanseMapper.expanseToDto(createdExpanse);
    }

    public List<ExpanseDto> findByParameters(String username,
                                             Long categoryId,
                                             Long subCategoryId,
                                             LocalDate startDate,
                                             LocalDate endDate,
                                             BigDecimal moreThan,
                                             BigDecimal lessThan
    ) {
        List<Expanse> searchResult = expanseRepository.findByUserEmailAndDateBetween(username, startDate,
                endDate.plusDays(1));

        return searchResult.isEmpty()
                ? Collections.emptyList()
                : prepareStream(searchResult, categoryId, subCategoryId, moreThan, lessThan)
                .map(expanseMapper::expanseToDto)
                .collect(Collectors.toList());
    }

    private static Stream<Expanse> prepareStream(List<Expanse> listToFilter, Long categoryId, Long subCategoryId, BigDecimal moreThan,
                                                 BigDecimal lessThan) {
        Stream<Expanse> stream = listToFilter.stream();
        if (categoryId != null) {
            stream = stream.filter(expanse -> expanse.getCategory().getId().equals(categoryId));
        }

        if (subCategoryId != null) {
            stream = stream.filter(expanse -> expanse.getSubCategory().getId().equals(subCategoryId));
        }

        if (moreThan != null) {
            stream = stream.filter(expanse -> expanse.getAmount().compareTo(moreThan) >= 0);
        }

        if (lessThan != null) {
            stream = stream.filter(expanse -> expanse.getAmount().compareTo(lessThan) <= 0);
        }
        return stream;
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
