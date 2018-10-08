package com.ddebowczyk.parkingmeter.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ParkingChargePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal chargeAmount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private LocalDate createDate;

    public ParkingChargePayment(BigDecimal chargeAmount, Currency currency, LocalDate createDate) {
        this.chargeAmount = chargeAmount;
        this.currency = currency;
        this.createDate = createDate;
    }
}
