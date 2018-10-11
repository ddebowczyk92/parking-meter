package com.ddebowczyk.parkingmeter.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class IncomeResponse {

    private final String income;
    @JsonFormat(pattern = "dd-MM-yy")
    private final LocalDate date;

    @JsonCreator
    public IncomeResponse(@JsonProperty("income") String income, @JsonProperty("date") LocalDate date) {
        this.income = income;
        this.date = date;
    }
}
