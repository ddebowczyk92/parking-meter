package com.ddebowczyk.parkingmeter.service;

import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket;
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository;
import com.ddebowczyk.parkingmeter.rest.dto.VehicleState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingMeterOperatorService {

    private final ParkingMeterTicketRepository parkingMeterTicketRepository;

    public VehicleState getVehicleState(String licensePlateNumber) {
        Optional<ParkingMeterTicket> parkingMeterTicketOptional = parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber);
        if (parkingMeterTicketOptional.isPresent()) {
            return VehicleState.STARTED;
        }
        return VehicleState.NOT_STARTED;
    }
}
