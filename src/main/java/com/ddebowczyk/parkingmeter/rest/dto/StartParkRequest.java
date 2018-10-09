package com.ddebowczyk.parkingmeter.rest.dto;

import com.ddebowczyk.parkingmeter.domain.DriverType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class StartParkRequest {

    @NotEmpty
    private final String licensePlateNumber;

    @NotNull
    private final DriverType driverType;

    @JsonCreator
    public StartParkRequest(@JsonProperty("licensePlateNumber") String licensePlateNumber,
                            @JsonProperty("driverType") DriverType driverType) {
        this.licensePlateNumber = licensePlateNumber;
        this.driverType = driverType;
    }
}
