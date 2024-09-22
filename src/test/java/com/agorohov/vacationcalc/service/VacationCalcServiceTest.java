package com.agorohov.vacationcalc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VacationCalcServiceTest {

    private VacationCalcService out;
    private Set<MonthDay> holidays;

    double avgMonthSalary;
    int vacationDays;
    LocalDate startDate;

    double avgDaysPerMonthEasy;

    @BeforeEach
    void setUp() throws Exception {
        holidays = new HashSet<>();
        holidays.add(MonthDay.of(1, 1));
        holidays.add(MonthDay.of(1, 2));
        holidays.add(MonthDay.of(1, 3));
        holidays.add(MonthDay.of(1, 4));
        holidays.add(MonthDay.of(1, 5));
        holidays.add(MonthDay.of(1, 6));
        holidays.add(MonthDay.of(1, 7));
        holidays.add(MonthDay.of(1, 8));

        out = new VacationCalcService(holidays);

        avgDaysPerMonthEasy = 29.3;
        avgMonthSalary = 55000.0;
        vacationDays = 14;
        startDate = LocalDate.of(2024, 1, 5);

        Field avgDaysPerMonthEasyField = VacationCalcService.class.getDeclaredField("avgDaysPerMonthEasy");
        avgDaysPerMonthEasyField.setAccessible(true);
        avgDaysPerMonthEasyField.set(out, avgDaysPerMonthEasy);
    }

    @Test
    void calculateWithoutStartDateTest() {
        BigDecimal averageDaySalary = BigDecimal.valueOf(avgMonthSalary).divide(
                BigDecimal.valueOf(avgDaysPerMonthEasy),
                2,
                RoundingMode.HALF_UP
        );
        BigDecimal expected = averageDaySalary.multiply(BigDecimal.valueOf(vacationDays));
        BigDecimal actual = out.calculate(avgMonthSalary, vacationDays, null);

        assertEquals(expected, actual);
    }
}