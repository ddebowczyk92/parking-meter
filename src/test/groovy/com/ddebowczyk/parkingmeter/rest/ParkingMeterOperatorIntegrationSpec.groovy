package com.ddebowczyk.parkingmeter.rest

import com.ddebowczyk.parkingmeter.domain.DriverType
import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket
import com.ddebowczyk.parkingmeter.domain.repository.ParkingMeterTicketRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.LocalDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ParkingMeterOperatorIntegrationSpec extends Specification {

    @Autowired
    ParkingMeterTicketRepository parkingMeterTicketRepository

    @Autowired
    MockMvc mockMvc

    def "should return STARTED state"() {
        given:
        String licensePlateNumber = "WA123"

        and:
        ParkingMeterTicket parkingMeterTicket = new ParkingMeterTicket(licensePlateNumber, LocalDateTime.now(), DriverType.REGULAR)
        parkingMeterTicketRepository.save(parkingMeterTicket)

        expect:
        mockMvc.perform(get("/rest/operator/state/{0}", licensePlateNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("\"STARTED\""))
    }


    def "should return NOT_STARTED state"() {
        given:
        String licensePlateNumber = "WA123"

        expect:
        mockMvc.perform(get("/rest/operator/state/{0}", licensePlateNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("\"NOT_STARTED\""))
    }


    def cleanup() {
        parkingMeterTicketRepository.deleteAll()
    }
}
