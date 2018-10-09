package com.ddebowczyk.parkingmeter.service

import com.ddebowczyk.parkingmeter.domain.DriverType
import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository
import com.ddebowczyk.parkingmeter.rest.dto.VehicleState
import spock.lang.Specification

import java.time.LocalDateTime

class ParkingMeterOperatorSpec extends Specification {

    ParkingMeterTicketRepository parkingMeterTicketRepository
    ParkingMeterOperatorService parkingMeterOperatorService

    def setup() {
        parkingMeterTicketRepository = Mock(ParkingMeterTicketRepository)
        parkingMeterOperatorService = new ParkingMeterOperatorService(parkingMeterTicketRepository)
    }

    def "should return STARTED state"() {
        given:
        String licensePlateNumber = "WW123"

        and:
        ParkingMeterTicket parkingMeterTicket = new ParkingMeterTicket(licensePlateNumber, LocalDateTime.now(), DriverType.REGULAR)
        parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber) >> Optional.of(parkingMeterTicket)

        expect:
        VehicleState.STARTED == parkingMeterOperatorService.getVehicleState(licensePlateNumber)
    }

    def "should return NOT_STARTED state"() {
        given:
        String licensePlateNumber = "WW123"

        and:
        parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber) >> Optional.ofNullable(null)

        expect:
        VehicleState.NOT_STARTED == parkingMeterOperatorService.getVehicleState(licensePlateNumber)
    }
}
