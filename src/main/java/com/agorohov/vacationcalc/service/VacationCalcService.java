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

    // среднее кол-во дней в месяце для упрощённого расчёта отпускных
    @Value("${vacation-calc.avg-days-per-month}")
    private double avgDaysPerMonthEasy;

    private final Set<MonthDay> holidays;

    public VacationCalcService(Set<MonthDay> holidays) {
        this.holidays = holidays;
    }

    public BigDecimal calculate(double avgMonthSalary, int vacationDays, LocalDate startDate) {
        log.info("Method calculate called with parameters: averageSalary={}, vacationDays={}, startDate={}",
                avgMonthSalary, vacationDays, startDate);

        double avgDaysPerMonth;

        // если в запросе не передана дата начала отпуска, берём упрощённое количество дней в месяце,
        // иначе считаем рабочие дни за прошлый год и за период отпуска без праздников и выходных
        if (startDate == null) {
            avgDaysPerMonth = avgDaysPerMonthEasy;
        } else {
            avgDaysPerMonth = getWorkDaysForLastYear(startDate) / 12.0;
            vacationDays = getPaidVacationDays(vacationDays, startDate);
        }

        // отпускные
        BigDecimal vacationPay = BigDecimal.valueOf(avgMonthSalary / avgDaysPerMonth * vacationDays)
                .setScale(2, RoundingMode.HALF_UP);

        log.info("Method calculate completed with result: vacationPay={}, paid days: {}", vacationPay, vacationDays);
        return vacationPay;
    }

    // количество рабочих дней за год до начала отпуска
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

    // количество оплачиваемых дней отпуска
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

    // рабочий день или нет
    private boolean isWorkDay(LocalDate day) {
        return !day.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                && !day.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                && !holidays.contains(MonthDay.from(day));
    }
}
