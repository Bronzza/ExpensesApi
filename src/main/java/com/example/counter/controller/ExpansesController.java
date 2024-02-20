package com.example.counter.controller;

import com.example.counter.dto.CategoryDto;
import com.example.counter.dto.ExpanseDto;
import com.example.counter.service.ExpansesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expanse")
public class ExpansesController {

    private final ExpansesService expansesService;

    @PostMapping
    public ResponseEntity<ExpanseDto> createExpanse(@RequestBody ExpanseDto expanseDto) {
        if (expanseDto.getDate() == null) {
            expanseDto.setDate(LocalDate.now());
        }
        return new ResponseEntity<>(expansesService.createExpanse(expanseDto), HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<ExpanseDto>updateExpanse(@RequestBody ExpanseDto expanseDto) {
        expansesService.updateExpanse(expanseDto);
        return null;
    }
}
