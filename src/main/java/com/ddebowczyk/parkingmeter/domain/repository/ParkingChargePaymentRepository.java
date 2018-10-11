package com.ddebowczyk.parkingmeter.domain.repository;

import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ParkingChargePaymentRepository extends JpaRepository<ParkingChargePayment, Long> {

    List<ParkingChargePayment> findByCreateDate(LocalDate createDate);
}
