package com.ddebowczyk.parkingmeter.rest;

import com.ddebowczyk.parkingmeter.rest.dto.StartParkRequest;
import com.ddebowczyk.parkingmeter.rest.dto.StartParkResponse;
import com.ddebowczyk.parkingmeter.service.ParkingMeterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/rest/parkingMeter")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public final class ParkingMeterController {

    private final ParkingMeterService parkingMeterService;

    @PostMapping(path = "/start")
    public ResponseEntity<StartParkResponse> startParkingMeter(@RequestBody @Valid StartParkRequest request) {
        StartParkResponse response = parkingMeterService.startParkingMeter(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
