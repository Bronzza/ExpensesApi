package com.example.counter.controller;

import com.example.counter.Util;
import com.example.counter.dto.ExpanseDto;
import com.example.counter.dto.ExpanseResponseDto;
import com.example.counter.service.ExpansesService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
@CrossOrigin(origins = "*")
public class ReportController {

    public static final String MONTH_DATE_FORMAT = "dd/MM";
    private final ExpansesService expansesService;

    @GetMapping("/category")
    public ResponseEntity findByParametersGroupByCategory(@RequestParam(defaultValue = "01/01/2000") String startDate,
                                                          @RequestParam(defaultValue = "01/01/2100") String endDate,
                                                          Authentication auth) {
        Map<String, BigDecimal> result = new HashMap<>();
        expansesService.findByParametersResponse(auth.getPrincipal().toString(),
                        null, null,
                        Util.formatDateToLocalDate(startDate), Util.formatDateToLocalDate(endDate),
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
        List<LocalDate> list = createListOfDates(startDate, endDate);
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        Map<String, BigDecimal> resultMapWithSkippedDates = new HashMap<>();
        expansesService.findByParametersResponse(auth.getPrincipal().toString(),
                        null, null,
                        Util.formatDateToLocalDate(startDate), Util.formatDateToLocalDate(endDate),
                        null, null)

                .forEach(expanse -> {
                    String expanseDate = Util.formatLocalDateToString(expanse.getDate(), MONTH_DATE_FORMAT);
                    resultMapWithSkippedDates.merge(expanseDate, expanse.getAmount(), BigDecimal::add);
                });
        list
                .stream()
                .forEach(date -> {
                    String dateString = Util.formatLocalDateToString(date, MONTH_DATE_FORMAT);
                    BigDecimal amount = resultMapWithSkippedDates.get(dateString);
                    result.put(dateString, amount == null ? BigDecimal.ZERO : amount);
                });
        return result.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expanses for the range")
                : ResponseEntity.status(HttpStatus.OK).body(result);
    }

    private List<LocalDate> createListOfDates(String startDate, String endDate) {
        LocalDate startDateLocalDate = Util.formatDateToLocalDate(startDate);
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDateLocalDate,
                Util.formatDateToLocalDate(endDate).plusDays(1));
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(startDateLocalDate::plusDays)
                .collect(Collectors.toList());
    }

}
