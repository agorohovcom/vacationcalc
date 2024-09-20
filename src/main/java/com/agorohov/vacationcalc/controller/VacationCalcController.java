package com.agorohov.vacationcalc.controller;

import com.agorohov.vacationcalc.dto.VacationRequest;
import com.agorohov.vacationcalc.service.VacationCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class VacationCalcController {

    Logger log = LoggerFactory.getLogger(VacationCalcController.class);

    private final VacationCalcService vacationCalcService;

    public VacationCalcController(VacationCalcService vacationCalcService) {
        this.vacationCalcService = vacationCalcService;
    }

    @GetMapping("calculate")
    public ResponseEntity<String> calculate(@RequestBody VacationRequest request) {
        if (request.getAverageSalary() <= 0 || request.getVacationDays() <= 0) {
            String msg = "Invalid request data: " + request;
            log.error(msg);
            return ResponseEntity.badRequest().body(msg);
        }
        BigDecimal averageSalary = BigDecimal.valueOf(request.getAverageSalary());
        int vacationDays = request.getVacationDays();

        BigDecimal vacationPay = vacationCalcService.calculate(averageSalary, vacationDays);
        return ResponseEntity.ok(vacationPay.toString());
    }
}
