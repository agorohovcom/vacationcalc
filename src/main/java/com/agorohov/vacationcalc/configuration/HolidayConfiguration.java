package com.agorohov.vacationcalc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.MonthDay;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class HolidayConfiguration {

    @Value("${vacation-calc.holidays}")
    private String holidaysString;

    @Bean
    public Set<MonthDay> holidays() {
        return Stream.of(holidaysString.split(","))
                .map(MonthDay::parse)
                .collect(Collectors.toSet());
    }
}
