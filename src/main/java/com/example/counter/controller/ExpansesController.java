package com.example.counter.controller;

import com.example.counter.dto.ExpanseDto;
import com.example.counter.service.ExpansesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expanse")
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
                formatDateToLocalDate(startDate), formatDateToLocalDate(endDate),
                moreThan, lessThan);
        return searchResult.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expanses for the range")
                : ResponseEntity.status(HttpStatus.OK).body(searchResult);
    }

    private static LocalDate formatDateToLocalDate(String startDate) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(startDate, dateFormat);
    }


}
