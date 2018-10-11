package com.ddebowczyk.parkingmeter.service;

import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket;
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository;
import com.ddebowczyk.parkingmeter.rest.dto.VehicleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingMeterOperatorService {

    private final ParkingMeterTicketRepository parkingMeterTicketRepository;

    public VehicleState getVehicleState(String licensePlateNumber) {
        log.info("Checking if vehicle with given license plate number {} is parked", licensePlateNumber);
        Optional<ParkingMeterTicket> parkingMeterTicketOptional = parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber);
        if (parkingMeterTicketOptional.isPresent()) {
            return VehicleState.STARTED;
        }
        return VehicleState.NOT_STARTED;
    }
}
