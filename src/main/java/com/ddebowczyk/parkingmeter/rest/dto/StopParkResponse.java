package com.ddebowczyk.parkingmeter.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StopParkResponse {

    public final String licensePlateNumber;
    public final String parkingChargeWithCurrency;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    public final LocalDateTime endDateTime;

    @JsonCreator
    public StopParkResponse(@JsonProperty("licensePlateNumber") String licensePlateNumber,
                            @JsonProperty("parkingChargeWithCurrency") String parkingChargeWithCurrency,
                            @JsonProperty("endDateTime") LocalDateTime endDateTime) {
        this.licensePlateNumber = licensePlateNumber;
        this.parkingChargeWithCurrency = parkingChargeWithCurrency;
        this.endDateTime = endDateTime;
    }
}
