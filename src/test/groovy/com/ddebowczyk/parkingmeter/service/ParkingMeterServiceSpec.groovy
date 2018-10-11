package com.ddebowczyk.parkingmeter.service

import com.ddebowczyk.parkingmeter.domain.Currency
import com.ddebowczyk.parkingmeter.domain.DriverType
import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment
import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket
import com.ddebowczyk.parkingmeter.domain.repository.ParkingChargePaymentRepository
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository
import com.ddebowczyk.parkingmeter.exception.VehicleAlreadyParkedException
import com.ddebowczyk.parkingmeter.exception.VehicleNotParkedException
import com.ddebowczyk.parkingmeter.rest.dto.StartParkRequest
import com.ddebowczyk.parkingmeter.rest.dto.StopParkRequest
import com.ddebowczyk.parkingmeter.rest.dto.StopParkResponse
import com.ddebowczyk.parkingmeter.service.parkingcharge.ParkingChargeCalculator
import spock.lang.Specification

import java.time.LocalDateTime

class ParkingMeterServiceSpec extends Specification {

    ParkingMeterService parkingMeterService
    ParkingMeterTicketRepository parkingMeterTicketRepository
    ParkingChargeCalculator parkingChargeCalculator
    ParkingChargePaymentRepository parkingChargePaymentRepository
    CurrentDateProvider currentDateProvider

    def setup() {
        currentDateProvider = Mock(CurrentDateProvider)
        parkingMeterTicketRepository = Mock(ParkingMeterTicketRepository)
        parkingChargePaymentRepository = Mock(ParkingChargePaymentRepository)
        parkingChargeCalculator = Mock(ParkingChargeCalculator)
        parkingMeterService = new ParkingMeterService(parkingMeterTicketRepository, parkingChargePaymentRepository, currentDateProvider, parkingChargeCalculator)
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

    def "should throw exception when there's no parked vehicle when stop park request"() {
        given:
        String licensePlateNumber = "WX123"
        StopParkRequest startParkRequest = new StopParkRequest(licensePlateNumber, null)

        and:
        parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber) >> Optional.ofNullable(null)

        when:
        parkingMeterService.stopParkingMeter(startParkRequest)

        then:
        thrown(VehicleNotParkedException)
    }

    def "should stop parking meter"() {
        given:
        String licensePlateNumber = "WX123"
        StopParkRequest stopParkRequest = new StopParkRequest(licensePlateNumber, null)

        and:
        LocalDateTime pastDateTime = LocalDateTime.of(2018, 10, 10, 12, 0)
        LocalDateTime currentDateTime = LocalDateTime.of(2018, 10, 10, 13, 0)
        currentDateProvider.getCurrentLocalDateTime() >> currentDateTime

        and:
        ParkingMeterTicket parkingMeterTicket = new ParkingMeterTicket(licensePlateNumber, pastDateTime, DriverType.REGULAR)
        parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber) >> Optional.of(parkingMeterTicket)

        and:
        ParkingChargePayment parkingChargePayment = new ParkingChargePayment(BigDecimal.ONE, Currency.PLN, currentDateTime.toLocalDate())
        parkingChargeCalculator.calculate(pastDateTime, currentDateTime, DriverType.REGULAR, null) >> parkingChargePayment

        when:
        StopParkResponse stopParkResponse = parkingMeterService.stopParkingMeter(stopParkRequest)

        then:
        stopParkResponse.endDateTime == currentDateTime
        stopParkResponse.licensePlateNumber == licensePlateNumber
        stopParkResponse.parkingChargeWithCurrency == "1PLN"

        and:
        1 * parkingChargePaymentRepository.save(parkingChargePayment)
        1 * parkingMeterTicketRepository.save(parkingMeterTicket)
    }

}
