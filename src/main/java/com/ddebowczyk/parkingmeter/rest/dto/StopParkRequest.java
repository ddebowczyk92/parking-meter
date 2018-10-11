package com.ddebowczyk.parkingmeter.rest.dto;

import com.ddebowczyk.parkingmeter.domain.Currency;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class StopParkRequest {

    @NotEmpty
    private final String licensePlateNumber;
    private final Currency currency;

    @JsonCreator
    public StopParkRequest(@JsonProperty("licensePlateNumber") String licensePlateNumber, @JsonProperty("currency") Currency currency) {
        this.licensePlateNumber = licensePlateNumber;
        this.currency = currency;
    }
}
