package com.ddebowczyk.parkingmeter.service

import com.ddebowczyk.parkingmeter.domain.Currency
import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment
import com.ddebowczyk.parkingmeter.domain.repository.ParkingChargePaymentRepository
import com.ddebowczyk.parkingmeter.rest.dto.IncomeResponse
import spock.lang.Specification

import java.time.LocalDate

class IncomeServiceSpec extends Specification {

    IncomeService incomeService
    ParkingChargePaymentRepository parkingChargePaymentRepository

    def setup() {
        parkingChargePaymentRepository = Mock(ParkingChargePaymentRepository)
        incomeService = new IncomeService(parkingChargePaymentRepository)
    }

    def "should return income"() {
        given:
        LocalDate createDate = LocalDate.of(2018, 10, 10)

        and:
        ParkingChargePayment parkingChargePayment1 = new ParkingChargePayment(new BigDecimal("2"), Currency.PLN, createDate)
        ParkingChargePayment parkingChargePayment2 = new ParkingChargePayment(new BigDecimal("2"), Currency.PLN, createDate)

        and:
        parkingChargePaymentRepository.findByCreateDate(createDate) >> [parkingChargePayment1, parkingChargePayment2]

        and:
        IncomeResponse response = new IncomeResponse("4.00", createDate)

        expect:
        response.income == incomeService.getIncomeForGivenDay(createDate).income
    }

}
