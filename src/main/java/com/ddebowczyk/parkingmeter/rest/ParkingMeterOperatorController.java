package com.ddebowczyk.parkingmeter.rest;

import com.ddebowczyk.parkingmeter.rest.dto.VehicleState;
import com.ddebowczyk.parkingmeter.service.ParkingMeterOperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/operator")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public final class ParkingMeterOperatorController {

    private final ParkingMeterOperatorService parkingMeterOperatorService;

    @GetMapping("/state/{licensePlateNumber}")
    public VehicleState getVehicleState(@PathVariable String licensePlateNumber) {
        return parkingMeterOperatorService.getVehicleState(licensePlateNumber);
    }
}
