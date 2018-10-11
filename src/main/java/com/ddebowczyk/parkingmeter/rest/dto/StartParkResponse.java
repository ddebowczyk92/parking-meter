package com.ddebowczyk.parkingmeter.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StartParkResponse {

    private final String licensePlateNumber;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private final LocalDateTime startDateTime;

    @JsonCreator
    public StartParkResponse(@JsonProperty("licensePlateNumber") String licensePlateNumber,
                             @JsonProperty("startDateTime") LocalDateTime startDateTime) {
        this.licensePlateNumber = licensePlateNumber;
        this.startDateTime = startDateTime;
    }
}
