package com.example.counter.service;

import com.example.counter.DateUtil;
import com.example.counter.dto.CategorySubCategoryDto;
import com.example.counter.dto.ExpanseDto;
import com.example.counter.dto.ExpanseResponseDto;
import com.example.counter.entiry.Category;
import com.example.counter.entiry.Expanse;
import com.example.counter.entiry.SubCategory;
import com.example.counter.entiry.User;
import com.example.counter.mapper.ExpanseMapper;
import com.example.counter.repository.ExpanseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
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

    public List<ExpanseResponseDto> findByParametersResponse(String username,
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
                .map(expanseMapper::expanseToResponseDto)
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

    public List<ExpanseResponseDto> findLastTenByUserEmail(String userEmail) {
        return expanseRepository.findFirst10ByUserEmailOrderByDateDesc(userEmail)
                .stream()
                .map(expanseMapper::expanseToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ExpanseResponseDto> findLastNByUserEmail(String userEmail, Integer numRecords) {
        Sort sort = Sort.by(
                Sort.Order.desc("date"),
                Sort.Order.desc("id"));

        return expanseRepository.findByUserEmail(userEmail, PageRequest.of(0, numRecords, sort))
                .stream()
                .map(expanseMapper::expanseToResponseDto)
                .toList();
    }

    public void createTestDataInDB(User forUser) {
        Random random = new Random();
        List<Category> categories = forUser.getCategories();
        int categoryCount = categories.size();
        List<LocalDate> listOfDates = DateUtil.createListOfDates(LocalDate.now().minusDays(9), LocalDate.now());

        for (int i = 0; i < 15; i++) {
            Category category = categories.get(random.nextInt(0, categoryCount));
            int subCategoriesCount = category.getSubCategories().size();

            expanseRepository.save(Expanse.builder()
                    .category(category)
                    .subCategory(category.getSubCategories().get(random.nextInt(0, subCategoriesCount)))
                    .date(listOfDates.get(random.nextInt(0, listOfDates.size())))
                    .amount(BigDecimal.valueOf(random.nextInt(10, 250)))
                    .user(forUser)
                    .build());
        }


//        Set<Map.Entry<Category, List<SubCategory>>> subcuts = subCategoryService.getCategorySubCategoryMap().entrySet()
//                .stream().filter(entry -> (forUser.getCategories().contains(entry.getKey())))
//                .collect(Collectors.toSet());
//        ArrayList<Map.Entry<Category, List<SubCategory>>> entries = new ArrayList<>(subcuts);
//
//        List<LocalDate> listOfDates = DateUtil.createListOfDates(LocalDate.now().minusDays(9), LocalDate.now());
//        int dateCount = (int)  listOfDates.stream().count();
//
//        int categoriesCount = (int) entries.stream().count();
//
//        for (int i = 0; i < 15; i++) {
//            Map.Entry<Category, List<SubCategory>> randomEntry =
//                    entries.get(random.nextInt(0, categoriesCount));
//            long subCategoriesCount = randomEntry.getValue().stream().count();
//
//            expanseRepository.save( Expanse.builder()
//                    .category(randomEntry.getKey())
//                    .subCategory(randomEntry.getValue().get((int)random.nextLong(0, subCategoriesCount)))
//                    .date(listOfDates.get(random.nextInt(0, dateCount)))
//                    .amount(BigDecimal.valueOf(random.nextInt(10, 250)))
//                    .user(forUser)
//                    .build());
//        }
    }
}
