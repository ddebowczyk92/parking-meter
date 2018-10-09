package com.ddebowczyk.parkingmeter.service;

import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket;
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository;
import com.ddebowczyk.parkingmeter.exception.VehicleAlreadyParkedException;
import com.ddebowczyk.parkingmeter.rest.dto.StartParkRequest;
import com.ddebowczyk.parkingmeter.rest.dto.StartParkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingMeterService {

    private final ParkingMeterTicketRepository parkingMeterTicketRepository;
    private final CurrentDateProvider currentDateProvider;

    @Transactional
    public StartParkResponse startParkingMeter(StartParkRequest startParkRequest) {
        parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(startParkRequest.getLicensePlateNumber())
                .ifPresent(parkingMeterTicket -> {
                    throw new VehicleAlreadyParkedException(parkingMeterTicket.getLicensePlateNumber());
                });

        ParkingMeterTicket parkingMeterTicket = new ParkingMeterTicket(startParkRequest.getLicensePlateNumber(),
                currentDateProvider.getCurrentLocalDateTime(),
                startParkRequest.getDriverType());
        parkingMeterTicketRepository.save(parkingMeterTicket);

        return new StartParkResponse(parkingMeterTicket.getLicensePlateNumber(), parkingMeterTicket.getStartDateTime());
    }
}
