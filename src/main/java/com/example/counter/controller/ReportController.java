package com.example.counter.controller;

import com.example.counter.DateUtil;
import com.example.counter.service.ExpansesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ExpansesService expansesService;

    @GetMapping("/category")
    public ResponseEntity findByParametersGroupByCategory(@RequestParam(defaultValue = "01/01/2000") String startDate,
                                                          @RequestParam(defaultValue = "01/01/2100") String endDate,
                                                          Authentication auth) {
        Map<String, BigDecimal> result = new HashMap<>();
        expansesService.findByParametersResponse(auth.getPrincipal().toString(),
                        null, null,
                        DateUtil.formatDateToLocalDate(startDate), DateUtil.formatDateToLocalDate(endDate),
                        null, null)

                .forEach(expanse -> {
                    String categoryName = expanse.getCategoryName();
                    result.merge(categoryName, expanse.getAmount(), BigDecimal::add);
                });
        return result.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expanses for the range")
                : ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/daystotal")
    public ResponseEntity categoryTotalReport(@RequestParam(defaultValue = "01/01/2000") String startDate,
                                              @RequestParam(defaultValue = "01/01/2100") String endDate,
                                              Authentication auth) {
        List<LocalDate> list = DateUtil.createListOfDates(startDate, endDate);
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        Map<String, BigDecimal> resultMapWithSkippedDates = new HashMap<>();
        expansesService.findByParametersResponse(auth.getPrincipal().toString(),
                        null, null,
                        DateUtil.formatDateToLocalDate(startDate), DateUtil.formatDateToLocalDate(endDate),
                        null, null)

                .forEach(expanse -> {
                    String expanseDate = DateUtil.formatLocalDateToString(expanse.getDate());
                    resultMapWithSkippedDates.merge(expanseDate, expanse.getAmount(), BigDecimal::add);
                });
        list
                .stream()
                .forEach(date -> {
                    String dateString = DateUtil.formatLocalDateToString(date);
                    BigDecimal amount = resultMapWithSkippedDates.get(dateString);
                    result.put(dateString, amount == null ? BigDecimal.ZERO : amount);
                });
        return result.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expanses for the range")
                : ResponseEntity.status(HttpStatus.OK).body(result);
    }



}
