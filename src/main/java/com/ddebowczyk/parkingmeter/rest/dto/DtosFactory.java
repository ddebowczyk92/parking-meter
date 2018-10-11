package com.ddebowczyk.parkingmeter.rest.dto;

import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment;
import com.ddebowczyk.parkingmeter.domain.ParkingMeterTicket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtosFactory {

    public static StartParkResponse buildStartParkResponse(String licensePlateNumber, LocalDateTime startDateTime) {
        return new StartParkResponse(licensePlateNumber, startDateTime);
    }

    public static StopParkResponse buildStopParkResponse(ParkingMeterTicket parkingMeterTicket) {
        ParkingChargePayment parkingChargePayment = parkingMeterTicket.getParkingChargePayment();
        return new StopParkResponse(
                parkingMeterTicket.getLicensePlateNumber(),
                parkingChargePayment.getChargeAmount().toString() + parkingChargePayment.getCurrency().name(),
                parkingMeterTicket.getEndDateTime());
    }

    public static IncomeResponse buildIncomeResponse(BigDecimal income, LocalDate date) {
        return new IncomeResponse(income.toString(), date);
    }

}
