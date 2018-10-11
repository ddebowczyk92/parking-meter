package com.ddebowczyk.parkingmeter.rest

import com.ddebowczyk.parkingmeter.domain.Currency
import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment
import com.ddebowczyk.parkingmeter.domain.repository.ParkingChargePaymentRepository
import com.ddebowczyk.parkingmeter.rest.dto.IncomeResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ParkingMeterOwnerIntegrationSpec extends Specification {

    @Autowired
    ParkingChargePaymentRepository parkingChargePaymentRepository

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def "should return income"() {
        given:
        LocalDate createDate = LocalDate.of(2018, 10, 10)

        and:
        ParkingChargePayment parkingChargePayment1 = new ParkingChargePayment(new BigDecimal("2"), Currency.PLN, createDate)
        ParkingChargePayment parkingChargePayment2 = new ParkingChargePayment(new BigDecimal("2"), Currency.PLN, createDate)
        ParkingChargePayment parkingChargePayment3 = new ParkingChargePayment(new BigDecimal("3"), Currency.PLN, createDate.plusDays(1))
        parkingChargePaymentRepository.saveAll([parkingChargePayment1, parkingChargePayment2, parkingChargePayment3])

        when:
        def response = mockMvc.perform(get("/rest/owner/income/{0}", "10-10-2018"))
                .andExpect(status().isOk()).andReturn().response

        then:
        response.status == HttpStatus.OK.value()

        and:
        def responseJson = objectMapper.readValue(response.contentAsString, IncomeResponse)
        responseJson.income == "4.00"
        responseJson.date == createDate
    }

    def "should return 0 income"() {
        given:
        LocalDate createDate = LocalDate.of(2018, 10, 10)

        when:
        def response = mockMvc.perform(get("/rest/owner/income/{0}", "10-10-2018"))
                .andExpect(status().isOk()).andReturn().response

        then:
        response.status == HttpStatus.OK.value()

        and:
        def responseJson = objectMapper.readValue(response.contentAsString, IncomeResponse)
        responseJson.income == "0.00"
        responseJson.date == createDate
    }


    def cleanup() {
        parkingChargePaymentRepository.deleteAll()
    }
}
