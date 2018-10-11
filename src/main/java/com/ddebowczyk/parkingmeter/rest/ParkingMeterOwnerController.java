package com.ddebowczyk.parkingmeter.rest;

import com.ddebowczyk.parkingmeter.rest.dto.IncomeResponse;
import com.ddebowczyk.parkingmeter.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "/rest/owner")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingMeterOwnerController {

    private final IncomeService incomeService;

    @GetMapping(path = "/income/{date}")
    public IncomeResponse getIncomeForGivenDate(@PathVariable
                                                @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return incomeService.getIncomeForGivenDay(date);
    }
}
