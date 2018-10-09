package com.ddebowczyk.parkingmeter.rest

import com.ddebowczyk.parkingmeter.ParkingMeterApplication
import com.ddebowczyk.parkingmeter.domain.DriverType
import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository
import com.ddebowczyk.parkingmeter.rest.dto.StartParkRequest
import com.ddebowczyk.parkingmeter.rest.dto.StartParkResponse
import com.ddebowczyk.parkingmeter.service.CurrentDateProvider
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import java.time.LocalDateTime

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = ParkingMeterApplication)
@AutoConfigureMockMvc
class ParkingMeterControllerIntegrationSpec extends Specification {

    @Autowired
    ParkingMeterTicketRepository parkingMeterTicketRepository

    @Autowired
    CurrentDateProvider currentDateProviderMock

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def "should start parking meter"() {
        given:
        String licensePlateNumber = "XXX123"

        and:
        LocalDateTime startDateTime = LocalDateTime.of(2018, 10, 8, 12, 0)
        currentDateProviderMock.getCurrentLocalDateTime() >> startDateTime

        and:
        StartParkRequest request = new StartParkRequest(licensePlateNumber, DriverType.REGULAR)

        when:
        def response = mockMvc.perform(post("/rest/parkingMeter/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonOutput.toJson(request)))
                .andReturn().response
        then:
        response.status == HttpStatus.CREATED.value()

        and:
        def responseJson = objectMapper.readValue(response.contentAsString, StartParkResponse)
        responseJson.licensePlateNumber == licensePlateNumber
        responseJson.startDateTime == startDateTime

        and:
        def startParkOptional = parkingMeterTicketRepository.findByLicensePlateNumberAndEndDateTimeIsNull(licensePlateNumber)
        startParkOptional.isPresent()
        def startParkEntity = startParkOptional.get()
        startParkEntity.licensePlateNumber == licensePlateNumber
        startParkEntity.startDateTime == startDateTime
        startParkEntity.endDateTime == null
        startParkEntity.driverType == DriverType.REGULAR
    }

    def "should return validation error when license plate number is empty"() {
        expect:
        mockMvc.perform(post("/rest/parkingMeter/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonOutput.toJson(new StartParkRequest("", DriverType.REGULAR))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("License plate number cant be empty")))
    }

    def "should return validation error when driver type is null"() {
        expect:
        mockMvc.perform(post("/rest/parkingMeter/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonOutput.toJson(new StartParkRequest("XXX", null))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Driver type must be selected")))
    }

    def "should return validation error when vehicle already parked"() {
        given:
        String licensePlateNumber = "WA666"

        and:
        parkingMeterTicketRepository.save(new ParkingMeterTicket(licensePlateNumber, LocalDateTime.now(), DriverType.REGULAR))

        expect:
        mockMvc.perform(post("/rest/parkingMeter/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonOutput.toJson(new StartParkRequest(licensePlateNumber, DriverType.REGULAR))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Vehicle with given license plate number " + licensePlateNumber + " already parked")))
    }

    @TestConfiguration
    static class IntegrationTestMockingConfig {
        private DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        CurrentDateProvider currentDateProvider() {
            factory.Mock(CurrentDateProvider)
        }
    }
}