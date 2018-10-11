package com.ddebowczyk.parkingmeter.service.parkingcharge;

import com.ddebowczyk.parkingmeter.domain.Currency;
import com.ddebowczyk.parkingmeter.domain.DriverType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@ToString
public class CurrencyAndDriverTypeHourRateChargeModel extends ParkingChargeModel {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final DriverType driverType;
    private final Currency currency;

}
