package com.example.counter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateUtil {

    public static final String MONTH_DATE_FORMAT = "dd/MM";
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

    /**
     * Format LocalDate into String using dd/MM format
     *
     * @param date localDate to convert into String
     * @return String in dd/MM format
     */
    public static String formatLocalDateToString(LocalDate date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);
        return date.format(dateFormat);
    }

    static public List<LocalDate> createListOfDates(String startDate, String endDate) {
        LocalDate startDateLocalDate = DateUtil.formatDateToLocalDate(startDate);
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDateLocalDate,
                DateUtil.formatDateToLocalDate(endDate).plusDays(1));
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(startDateLocalDate::plusDays)
                .collect(Collectors.toList());
    }

    static public List<LocalDate> createListOfDates(LocalDate startDate, LocalDate endDate) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate,
                endDate.plusDays(1));
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(startDate::plusDays)
                .collect(Collectors.toList());
    }
}
