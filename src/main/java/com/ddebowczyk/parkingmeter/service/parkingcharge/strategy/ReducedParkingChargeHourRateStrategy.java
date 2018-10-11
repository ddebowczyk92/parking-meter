package com.ddebowczyk.parkingmeter.service.parkingcharge.strategy;

import com.ddebowczyk.parkingmeter.domain.DriverType;
import com.ddebowczyk.parkingmeter.service.parkingcharge.CurrencyAndDriverTypeHourRateChargeModel;
import com.ddebowczyk.parkingmeter.service.parkingcharge.ParkingChargeModel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(value = 1)
public class ReducedParkingChargeHourRateStrategy extends ParkingChargeHourRateStrategy {

    private static final BigDecimal FIRST_HOUR_CHARGE = new BigDecimal("0");
    private static final BigDecimal SECOND_HOUR_CHARGE = new BigDecimal("2");
    private static final BigDecimal NEXT_HOUR_RATE = new BigDecimal("1.2");

    @Override
    public boolean applies(ParkingChargeModel parkingChargeModel) {
        if (parkingChargeModel instanceof CurrencyAndDriverTypeHourRateChargeModel) {
            return DriverType.DISABLED == ((CurrencyAndDriverTypeHourRateChargeModel) parkingChargeModel).getDriverType();
        }
        return false;
    }

    @Override
    BigDecimal firstHourCharge() {
        return FIRST_HOUR_CHARGE;
    }

    @Override
    BigDecimal secondHourCharge() {
        return SECOND_HOUR_CHARGE;
    }

    @Override
    BigDecimal nextHourRate() {
        return NEXT_HOUR_RATE;
    }
}
