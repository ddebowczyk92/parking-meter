package com.ddebowczyk.parkingmeter.service;

import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment;
import com.ddebowczyk.parkingmeter.domain.repository.ParkingChargePaymentRepository;
import com.ddebowczyk.parkingmeter.rest.dto.DtosFactory;
import com.ddebowczyk.parkingmeter.rest.dto.IncomeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncomeService {

    private final ParkingChargePaymentRepository parkingChargePaymentRepository;

    public IncomeResponse getIncomeForGivenDay(LocalDate date) {
        log.info("Calculating income for {}", date);
        BigDecimal income = parkingChargePaymentRepository.findByCreateDate(date).stream()
                .map(ParkingChargePayment::getChargeAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        return DtosFactory.buildIncomeResponse(income, date);
    }
}
