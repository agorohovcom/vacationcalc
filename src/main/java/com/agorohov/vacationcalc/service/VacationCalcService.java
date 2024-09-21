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
    @Value("${vacation-calc.avg-days-per-month-easy}")
    private double avgDaysPerMonthEasy;

    // среднее кол-во дней в месяце без упрощённого учёта выходных
    @Value("${vacation-calc.avg-days-per-month-total}")
    private double avgDaysPerMonthTotal;

    private final Set<MonthDay> holidays;

    public VacationCalcService(Set<MonthDay> holidays) {
        this.holidays = holidays;
    }

    public BigDecimal calculate(double averageSalary, int vacationDays, LocalDate startDate) {
        log.info("Method calculate called with parameters: {}, {}, {}", averageSalary, vacationDays, startDate);

        double currentAvgDaysPerMonth;

        // если в запросе передана дата начала отпуска, берём упрощённое количество дней в месяце,
        // иначе берем среднее количество дней в месяце и высчитываем выходные и праздники
        if (startDate == null) {
            currentAvgDaysPerMonth = avgDaysPerMonthEasy;
        } else {
            currentAvgDaysPerMonth = avgDaysPerMonthTotal;
            vacationDays = paidVacationDays(vacationDays, startDate);
        }

        // средняя дневная зарплата
        BigDecimal averageDaySalary = BigDecimal.valueOf(averageSalary).divide(
                BigDecimal.valueOf(currentAvgDaysPerMonth),
                2,
                RoundingMode.HALF_UP);

        // отпускные
        BigDecimal vacationPay = averageDaySalary.multiply(BigDecimal.valueOf(vacationDays));

        log.info("Method calculate completed with result: {}, paid days: {}", vacationPay, vacationDays);
        return vacationPay;
    }

    // количество оплачиваемых дней отпуска (без выходных)
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
