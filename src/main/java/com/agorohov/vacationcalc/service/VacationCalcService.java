package com.agorohov.vacationcalc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Set;

@Service
public class VacationCalcService {
    Logger log = LoggerFactory.getLogger(VacationCalcService.class);

    @Value("${vacation-calc.avg-days-per-month}")
    private double averageDaysPerMonth;

    private final Set<MonthDay> holidays;

    public VacationCalcService(Set<MonthDay> holidays) {
        this.holidays = holidays;
    }

    public BigDecimal calculate(double averageSalary, int vacationDays, LocalDate startDate) {
        log.info("Method calculate called with parameters: {}, {}, {}", averageSalary, vacationDays, startDate);

        BigDecimal averageDaySalary = BigDecimal.valueOf(averageSalary).divide(
                BigDecimal.valueOf(averageDaysPerMonth),
                2,
                RoundingMode.HALF_UP);

        if (startDate != null) {
            vacationDays = paidVacationDays(vacationDays, startDate);
        }
        BigDecimal vacationPay = averageDaySalary.multiply(BigDecimal.valueOf(vacationDays));

        log.info("Method calculate completed with result: {}, paid days: {}", vacationPay, vacationDays);
        return vacationPay;
    }

    private int paidVacationDays(int vacationDays, LocalDate startDate) {
        int paidVacationDays = 0;
        LocalDate currentDay = startDate;

        for (int i = 0; i < vacationDays; i++) {
            if (!currentDay.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && !currentDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                    && !holidays.contains(MonthDay.from(currentDay))) {
                paidVacationDays++;
            }
            currentDay = currentDay.plusDays(1);
        }
        return paidVacationDays;
    }
}
