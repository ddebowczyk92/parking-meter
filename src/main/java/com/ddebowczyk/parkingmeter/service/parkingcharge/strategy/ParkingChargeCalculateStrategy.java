package com.ddebowczyk.parkingmeter.service.parkingcharge.strategy;

import com.ddebowczyk.parkingmeter.service.parkingcharge.ParkingChargeModel;

import java.math.BigDecimal;

public interface ParkingChargeCalculateStrategy {

    BigDecimal caluculate(ParkingChargeModel parkingChargeModel);

    boolean applies(ParkingChargeModel parkingChargeModel);
}
