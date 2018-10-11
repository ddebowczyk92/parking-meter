package com.ddebowczyk.parkingmeter.domain.repository

import com.ddebowczyk.parkingmeter.domain.Currency
import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import java.time.LocalDate

@DataJpaTest
class ParkingChargePaymentRepositorySpec extends Specification {

    @Autowired
    ParkingChargePaymentRepository parkingChargePaymentRepository


    def "should save and return parking charge payments for given day"() {
        given:
        LocalDate localDate = LocalDate.of(2018, 10, 10)

        and:
        parkingChargePaymentRepository.save(new ParkingChargePayment(BigDecimal.ONE, Currency.PLN, localDate))
        parkingChargePaymentRepository.save(new ParkingChargePayment(BigDecimal.ONE, Currency.PLN, localDate))
        parkingChargePaymentRepository.save(new ParkingChargePayment(BigDecimal.ONE, Currency.PLN, localDate.plusDays(1)))

        when:
        List<ParkingChargePayment> parkingChargePaymentList = parkingChargePaymentRepository.findByCreateDate(localDate)

        then:
        parkingChargePaymentList.size() == 2

    }

    def cleanup() {
        parkingChargePaymentRepository.deleteAll()
    }

}

