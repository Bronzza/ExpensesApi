package com.example.counter;

import jakarta.persistence.Id;

import java.lang.annotation.Documented;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util {
    public static LocalDate formatDateToLocalDate(String date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, dateFormat);
    }

    /**
     * @param date
     * @param format examples: "dd/MM/yyyy", "dd-MM-yyyy", "dd/MM"
     * @return
     */
    public static String formatLocalDateToString(LocalDate date, String format) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        return date.format(dateFormat);
    }
}
