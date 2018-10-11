package com.ddebowczyk.parkingmeter.service.parkingcharge

import com.ddebowczyk.parkingmeter.domain.Currency
import com.ddebowczyk.parkingmeter.domain.DriverType
import com.ddebowczyk.parkingmeter.service.parkingcharge.strategy.ReducedParkingChargeHourRateStrategy
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime

class ReducedParkingChargeHourRateStrategySpec extends Specification {

    static rootDateTime = LocalDateTime.of(2018, 10, 10, 12, 1)

    @Shared
    ReducedParkingChargeHourRateStrategy strategy

    def setupSpec() {
        strategy = new ReducedParkingChargeHourRateStrategy()
    }

    def "should return charge amount"(LocalDateTime startDateTime, LocalDateTime endDateTime, BigDecimal chargeAmount) {

        given:
        ParkingChargeModel parkingChargeModel = new CurrencyAndDriverTypeHourRateChargeModel(startDateTime, endDateTime, DriverType.DISABLED, Currency.PLN)

        expect:
        chargeAmount == strategy.caluculate(parkingChargeModel)

        where:
        startDateTime | endDateTime                               || chargeAmount
        rootDateTime  | rootDateTime.plusMinutes(1)               || new BigDecimal("0")
        rootDateTime  | rootDateTime.plusMinutes(58)              || new BigDecimal("0")
        rootDateTime  | rootDateTime.plusHours(1)                 || new BigDecimal("2")
        rootDateTime  | rootDateTime.plusHours(2)                 || new BigDecimal("4.4")
        rootDateTime  | rootDateTime.plusHours(2).plusMinutes(15) || new BigDecimal("4.4")
        rootDateTime  | rootDateTime.plusHours(3)                 || new BigDecimal("7.28")
        rootDateTime  | rootDateTime.plusHours(4)                 || new BigDecimal("10.74")
        rootDateTime  | rootDateTime.plusHours(5)                 || new BigDecimal("14.88")
    }

    def "should apply"() {
        given:
        ParkingChargeModel parkingChargeModel = new CurrencyAndDriverTypeHourRateChargeModel(LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1), DriverType.DISABLED, Currency.PLN)

        expect:
        strategy.applies(parkingChargeModel)
    }

    def "should not apply"() {
        given:
        ParkingChargeModel parkingChargeModel = new CurrencyAndDriverTypeHourRateChargeModel(LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1), DriverType.REGULAR, Currency.PLN)

        expect:
        !strategy.applies(parkingChargeModel)
    }

}
