package com.agorohov.vacationcalc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VacationCalcServiceTest {

    private VacationCalcService out;

    private Set<MonthDay> holidays;

    private double avgMonthSalary;
    private int vacationDays;
    private LocalDate startDate;

    private double avgDaysPerMonthEasy;

    @BeforeEach
    void setUp() throws Exception {
        holidays = new HashSet<>();
        // случайные дни в качестве праздников
        holidays.add(MonthDay.of(1, 1));
        holidays.add(MonthDay.of(2, 2));
        holidays.add(MonthDay.of(3, 3));
        holidays.add(MonthDay.of(4, 4));
        holidays.add(MonthDay.of(5, 5));
        holidays.add(MonthDay.of(6, 6));
        holidays.add(MonthDay.of(7, 7));
        holidays.add(MonthDay.of(8, 8));
        holidays.add(MonthDay.of(9, 9));
        holidays.add(MonthDay.of(10, 10));
        holidays.add(MonthDay.of(11, 11));
        holidays.add(MonthDay.of(12, 12));

        out = new VacationCalcService(holidays);

        avgDaysPerMonthEasy = 29.3;
        avgMonthSalary = 55000.0;
        vacationDays = 28;
        startDate = LocalDate.of(2024, 1, 5);

        Field avgDaysPerMonthEasyField = VacationCalcService.class.getDeclaredField("avgDaysPerMonthEasy");
        avgDaysPerMonthEasyField.setAccessible(true);
        avgDaysPerMonthEasyField.set(out, avgDaysPerMonthEasy);
    }

    @Test
    void calculateWithoutStartDateTest() {
        BigDecimal expected = BigDecimal.valueOf(avgMonthSalary / avgDaysPerMonthEasy * vacationDays)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = out.calculate(avgMonthSalary, vacationDays, null);

        assertEquals(expected, actual);
    }

    @Test
    void calculateWithStartDateTest() {
        double avgDayPerMonth = getWorkDaysForLastYear(startDate) / 12.0;
        int vacationPaidDays = getPaidVacationDays(vacationDays, startDate);

        BigDecimal expected = BigDecimal.valueOf(avgMonthSalary / avgDayPerMonth * vacationPaidDays)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal actual = out.calculate(avgMonthSalary, vacationDays, startDate);

        assertEquals(expected, actual);
    }

    // методы для расчета количества рабочих дней в отпуске и за прошедший год
    private int getWorkDaysForLastYear(LocalDate startVacationDate) {
        LocalDate currentDayOfLastYear = startVacationDate.minusYears(1);
        int workingDays = 0;

        while (currentDayOfLastYear.isBefore(startVacationDate)) {
            if (isWorkDay(currentDayOfLastYear)) {
                workingDays++;
            }
            currentDayOfLastYear = currentDayOfLastYear.plusDays(1);
        }

        return workingDays;
    }

    private int getPaidVacationDays(int vacationDays, LocalDate startDate) {
        int paidVacationDays = 0;
        LocalDate currentDay = startDate;

        for (int i = 0; i < vacationDays; i++) {
            if (isWorkDay(currentDay)) {
                paidVacationDays++;
            }
            currentDay = currentDay.plusDays(1);
        }

        return paidVacationDays;
    }

    private boolean isWorkDay(LocalDate day) {
        return !day.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                && !day.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                && !holidays.contains(MonthDay.from(day));
    }
}