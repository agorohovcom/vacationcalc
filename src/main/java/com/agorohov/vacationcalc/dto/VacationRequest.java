package com.agorohov.vacationcalc.dto;

import java.util.Objects;

public class VacationRequest {
    private double averageSalary;
    private int vacationDays;

    public VacationRequest() {
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(int vacationDays) {
        this.vacationDays = vacationDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacationRequest that = (VacationRequest) o;
        return Double.compare(averageSalary, that.averageSalary) == 0 && vacationDays == that.vacationDays;
    }

    @Override
    public int hashCode() {
        return Objects.hash(averageSalary, vacationDays);
    }

    @Override
    public String toString() {
        return "VacationRequest{" +
                "averageSalary=" + averageSalary +
                ", vacationDays=" + vacationDays +
                '}';
    }
}
