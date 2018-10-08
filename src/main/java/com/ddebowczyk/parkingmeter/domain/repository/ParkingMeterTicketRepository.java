package com.ddebowczyk.parkingmeter.domain.repository;

import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingMeterTicketRepository extends JpaRepository<ParkingMeterTicket, Long> {

    Optional<ParkingMeterTicket> findByLicensePlateNumberAndEndDateTimeIsNull(String licensePlateNumber);

}
