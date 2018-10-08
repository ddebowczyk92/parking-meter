package com.ddebowczyk.parkingmeter.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ParkingMeterTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String licensePlateNumber;

    private LocalDateTime startDateTime;

    @Enumerated(EnumType.STRING)
    private DriverType driverType;

    @Setter
    private LocalDateTime endDateTime;

    @Setter
    @OneToOne
    private ParkingChargePayment parkingChargePayment;

    public ParkingMeterTicket(String licensePlateNumber, LocalDateTime startDateTime, DriverType driverType) {
        this.licensePlateNumber = licensePlateNumber;
        this.startDateTime = startDateTime;
        this.driverType = driverType;
    }

}
