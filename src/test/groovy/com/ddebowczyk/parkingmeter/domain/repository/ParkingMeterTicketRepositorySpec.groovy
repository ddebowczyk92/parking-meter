package com.ddebowczyk.parkingmeter.domain.repository

import com.ddebowczyk.parkingmeter.domain.DriverType
import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
class ParkingMeterTicketRepositorySpec extends Specification {

    @Autowired
    ParkingMeterTicketRepository parkingMeterTicketRepository


    def "should save and find parking meter ticket"() {
        given:
        String licensePLateNumber = "WL3123"

        and:
        LocalDateTime localDateTime = LocalDateTime.of(2018, 10, 5, 12, 0)

        parkingMeterTicketRepository.save(new ParkingMeterTicket(licensePLateNumber, localDateTime, DriverType.REGULAR))

        when:
        Optional<ParkingMeterTicket> parkingMeterTicketOptional = parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull("WL3123")

        then:
        parkingMeterTicketOptional.isPresent()
        ParkingMeterTicket parkingMeterTicket = parkingMeterTicketOptional.get()
        parkingMeterTicket.licensePlateNumber == licensePLateNumber
        parkingMeterTicket.endDateTime == null
        parkingMeterTicket.startDateTime != null
        parkingMeterTicket.startDateTime == LocalDateTime.of(2018, 10, 5, 12, 0)
        parkingMeterTicket.driverType == DriverType.REGULAR
    }

    def cleanup() {
        parkingMeterTicketRepository.deleteAll()
    }

}

