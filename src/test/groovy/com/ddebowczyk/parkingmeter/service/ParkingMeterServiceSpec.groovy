package com.ddebowczyk.parkingmeter.service

import com.ddebowczyk.parkingmeter.domain.DriverType
import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository
import com.ddebowczyk.parkingmeter.exception.VehicleAlreadyParkedException
import com.ddebowczyk.parkingmeter.rest.dto.StartParkRequest
import spock.lang.Specification

import java.time.LocalDateTime

class ParkingMeterServiceSpec extends Specification {

    ParkingMeterService parkingMeterService
    ParkingMeterTicketRepository parkingMeterTicketRepository
    CurrentDateProvider currentDateProvider

    def setup() {
        currentDateProvider = Mock(CurrentDateProvider)
        parkingMeterTicketRepository = Mock(ParkingMeterTicketRepository)
        parkingMeterService = new ParkingMeterService(parkingMeterTicketRepository, currentDateProvider)
    }

    def "should throw exception when there's already ticket with given license plate number"() {
        given:
        String licensePlateNumber = "WX123"
        StartParkRequest startParkRequest = new StartParkRequest(licensePlateNumber, DriverType.REGULAR)

        and:
        ParkingMeterTicket parkingMeterTicket = new ParkingMeterTicket(licensePlateNumber, LocalDateTime.now(), DriverType.REGULAR)
        parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber) >> Optional.of(parkingMeterTicket)

        when:
        parkingMeterService.startParkingMeter(startParkRequest)

        then:
        thrown(VehicleAlreadyParkedException)
    }

    def "should save ticket"() {
        given:
        String licensePlateNumber = "WX123"
        StartParkRequest startParkRequest = new StartParkRequest(licensePlateNumber, DriverType.REGULAR)

        and:
        parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber) >> Optional.ofNullable(null)

        when:
        parkingMeterService.startParkingMeter(startParkRequest)

        then:
        1 * parkingMeterTicketRepository.save(_ as ParkingMeterTicket)
    }
}
