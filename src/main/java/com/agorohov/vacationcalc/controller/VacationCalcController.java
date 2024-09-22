package com.agorohov.vacationcalc.controller;

import com.agorohov.vacationcalc.service.VacationCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class VacationCalcController {

    Logger log = LoggerFactory.getLogger(VacationCalcController.class);

    private final VacationCalcService vacationCalcService;

    public VacationCalcController(VacationCalcService vacationCalcService) {
        this.vacationCalcService = vacationCalcService;
    }

    @GetMapping("calculate")
    public ResponseEntity<String> calculate(
            @RequestParam("avg_salary") double avgSalary,
            @RequestParam("vacation_days") int vacationDays,
            @RequestParam(name = "start_date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate) {
        if (avgSalary < 0.0) {
            String msg = "Average salary can't be negative";
            log.warn(msg);
            return ResponseEntity.badRequest().body(msg);
        }
        if (vacationDays < 1) {
            String msg = "Vacation days can't be less than 1";
            log.warn(msg);
            return ResponseEntity.badRequest().body(msg);
        }

        return ResponseEntity.ok(vacationCalcService.calculate(avgSalary, vacationDays, startDate).toString());
    }
}
