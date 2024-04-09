package com.example.counter.controller;

import com.example.counter.Util;
import com.example.counter.dto.ExpanseDto;
import com.example.counter.dto.ExpanseResponseDto;
import com.example.counter.service.ExpansesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expanse")
@CrossOrigin(origins = "*")
public class ExpansesController {

    private final ExpansesService expansesService;

    @PostMapping
    public ResponseEntity<ExpanseDto> createExpanse(@RequestBody ExpanseDto expanseDto, Authentication auth) {
        if (expanseDto.getDate() == null) {
            expanseDto.setDate(LocalDate.now());
        }
        Object credentials = auth.getPrincipal();
        return new ResponseEntity<>(expansesService.createExpanse(expanseDto, credentials.toString()), HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<ExpanseDto> updateExpanse(@RequestBody ExpanseDto expanseDto) {
        expansesService.updateExpanse(expanseDto);
        return null;
    }

    @GetMapping
    public ResponseEntity findByParameters(@RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) Long subCategoryId,
                                           @RequestParam(defaultValue = "01/01/2000") String startDate,
                                           @RequestParam(defaultValue = "01/01/2100") String endDate,
                                           @RequestParam(required = false) BigDecimal moreThan,
                                           @RequestParam(required = false) BigDecimal lessThan,
                                           Authentication auth) {
        List<ExpanseDto> searchResult = expansesService.findByParameters(auth.getPrincipal().toString(),
                categoryId, subCategoryId,
                Util.formatDateToLocalDate(startDate), Util.formatDateToLocalDate(endDate),
                moreThan, lessThan);
        return searchResult.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expanses for the range")
                : ResponseEntity.status(HttpStatus.OK).body(searchResult);
    }

    @GetMapping("/category")
    public ResponseEntity findByParametersGroupByCategory(@RequestParam(required = false) Long categoryId,
                                                          @RequestParam(required = false) Long subCategoryId,
                                                          @RequestParam(defaultValue = "01/01/2000") String startDate,
                                                          @RequestParam(defaultValue = "01/01/2100") String endDate,
                                                          @RequestParam(required = false) BigDecimal moreThan,
                                                          @RequestParam(required = false) BigDecimal lessThan,
                                                          Authentication auth) {
        Map<String, BigDecimal> result = new HashMap<>();
        expansesService.findByParametersResponse(auth.getPrincipal().toString(),
                        categoryId, subCategoryId,
                        Util.formatDateToLocalDate(startDate), Util.formatDateToLocalDate(endDate),
                        moreThan, lessThan)

                .forEach(expanse -> {
                    String categoryName = expanse.getCategoryName();
                    result.merge(categoryName, expanse.getAmount(), BigDecimal::add);
                });
        return result.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expanses for the range")
                : ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/lastten")
    @CrossOrigin
    public ResponseEntity findLastTenExpanse(Authentication auth) {
        List<ExpanseResponseDto> result = expansesService.findLastTenByUserEmail(auth.getPrincipal().toString());

        return result.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expanses for the range")
                : ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/lastten2")
    @CrossOrigin
    public ResponseEntity findLastNExpanse(@RequestParam(defaultValue = "10") Integer records,
                                            Authentication auth) {
        List<ExpanseResponseDto> result = expansesService.findLastNByUserEmail(auth.getPrincipal().toString(), records);

        return result.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expanses for the range")
                : ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
