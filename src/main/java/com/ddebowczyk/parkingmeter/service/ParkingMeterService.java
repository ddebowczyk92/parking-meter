package com.ddebowczyk.parkingmeter.service;

import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment;
import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket;
import com.ddebowczyk.parkingmeter.domain.repository.ParkingChargePaymentRepository;
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository;
import com.ddebowczyk.parkingmeter.exception.VehicleAlreadyParkedException;
import com.ddebowczyk.parkingmeter.exception.VehicleNotParkedException;
import com.ddebowczyk.parkingmeter.rest.dto.DtosFactory;
import com.ddebowczyk.parkingmeter.rest.dto.StartParkRequest;
import com.ddebowczyk.parkingmeter.rest.dto.StartParkResponse;
import com.ddebowczyk.parkingmeter.rest.dto.StopParkRequest;
import com.ddebowczyk.parkingmeter.rest.dto.StopParkResponse;
import com.ddebowczyk.parkingmeter.service.parkingcharge.ParkingChargeCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingMeterService {

    private final ParkingMeterTicketRepository parkingMeterTicketRepository;
    private final ParkingChargePaymentRepository parkingChargePaymentRepository;
    private final CurrentDateProvider currentDateProvider;
    private final ParkingChargeCalculator parkingChargeCalculator;

    @Transactional
    public StartParkResponse startParkingMeter(StartParkRequest startParkRequest) {
        log.info("starting parking meter {}", startParkRequest);
        final String licensePlateNumber = startParkRequest.getLicensePlateNumber();
        checkIfVehicleAlreadyParked(licensePlateNumber);

        ParkingMeterTicket parkingMeterTicket = new ParkingMeterTicket(startParkRequest.getLicensePlateNumber(),
                currentDateProvider.getCurrentLocalDateTime(),
                startParkRequest.getDriverType());
        parkingMeterTicketRepository.save(parkingMeterTicket);

        return DtosFactory.buildStartParkResponse(licensePlateNumber, parkingMeterTicket.getStartDateTime());
    }

    @Transactional
    public StopParkResponse stopParkingMeter(StopParkRequest stopParkRequest) {
        log.info("stopping parking meter {}", stopParkRequest);
        final String licensePlateNumber = stopParkRequest.getLicensePlateNumber();
        ParkingMeterTicket parkingMeterTicket = parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber)
                .orElseThrow(VehicleNotParkedException::new);
        final LocalDateTime endDateTime = currentDateProvider.getCurrentLocalDateTime();
        ParkingChargePayment parkingChargePayment = parkingChargeCalculator.calculate(parkingMeterTicket.getStartDateTime(), endDateTime, parkingMeterTicket.getDriverType(), stopParkRequest.getCurrency());
        parkingChargePaymentRepository.save(parkingChargePayment);

        parkingMeterTicket.setEndDateTime(endDateTime);
        parkingMeterTicket.setParkingChargePayment(parkingChargePayment);
        parkingMeterTicketRepository.save(parkingMeterTicket);

        return DtosFactory.buildStopParkResponse(parkingMeterTicket);
    }

    private void checkIfVehicleAlreadyParked(String licensePlateNumber) {
        log.info("Checking if vehicle with license plate number {} already parked", licensePlateNumber);
        parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber)
                .ifPresent(parkingMeterTicket -> {
                    log.warn("vehicle with given license plate number {} already parked", licensePlateNumber);
                    throw new VehicleAlreadyParkedException(parkingMeterTicket.getLicensePlateNumber());
                });
    }
}
