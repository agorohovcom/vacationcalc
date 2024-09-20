package com.agorohov.vacationcalc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class VacationCalcService {

    Logger log = LoggerFactory.getLogger(VacationCalcService.class);

    @Value("${vacation-calc.avg-days-per-month}")
    private double averageDaysPerMonth;

    public BigDecimal calculate(BigDecimal averageSalary, int vacationDays) {
        log.info("Method calculate called with parameters: {}, {}", averageSalary, vacationDays);
        BigDecimal averageDaySalary = averageSalary.divide(
                BigDecimal.valueOf(averageDaysPerMonth),
                2,
                RoundingMode.HALF_UP);
        BigDecimal vacationPay = averageDaySalary.multiply(BigDecimal.valueOf(vacationDays));

        log.info("Method calculate completed with result: {}", vacationPay);
        return vacationPay;
    }
}
