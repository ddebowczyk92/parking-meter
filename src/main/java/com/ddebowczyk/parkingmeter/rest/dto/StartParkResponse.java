package com.ddebowczyk.parkingmeter.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StartParkResponse {

    private final String licensePlateNumber;
    private final LocalDateTime startDateTime;

    @JsonCreator
    public StartParkResponse(@JsonProperty("licensePlateNumber") String licensePlateNumber,
                             @JsonProperty("startDateTime") LocalDateTime startDateTime) {
        this.licensePlateNumber = licensePlateNumber;
        this.startDateTime = startDateTime;
    }
}
