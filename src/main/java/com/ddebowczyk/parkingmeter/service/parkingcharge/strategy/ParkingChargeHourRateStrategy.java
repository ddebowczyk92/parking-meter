package com.ddebowczyk.parkingmeter.service.parkingcharge.strategy;

import com.ddebowczyk.parkingmeter.service.parkingcharge.CurrencyAndDriverTypeHourRateChargeModel;
import com.ddebowczyk.parkingmeter.service.parkingcharge.ParkingChargeModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class ParkingChargeHourRateStrategy implements ParkingChargeCalculateStrategy {

    abstract BigDecimal firstHourCharge();

    abstract BigDecimal secondHourCharge();

    abstract BigDecimal nextHourRate();

    @Override
    public BigDecimal caluculate(ParkingChargeModel parkingChargeModel) {
        CurrencyAndDriverTypeHourRateChargeModel chargeModel = (CurrencyAndDriverTypeHourRateChargeModel) parkingChargeModel;
        return calculateChargeAmount(chargeModel.getStartDateTime(), chargeModel.getEndDateTime());
    }

    private BigDecimal calculateChargeAmount(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        final long hours = Duration.between(startDateTime, endDateTime).toHours();
        if (hours == 0) {
            return firstHourCharge();
        }
        if (hours == 1) {
            return secondHourCharge().add(firstHourCharge());
        }
        return calculate3rdAndEachNextHour(hours).add(firstHourCharge()).add(secondHourCharge()).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculate3rdAndEachNextHour(long hours) {
        BigDecimal previousHourCharge = secondHourCharge();
        BigDecimal nextHourCharge = secondHourCharge().multiply(nextHourRate());
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = (int) hours; i > 1; i--) {
            sum = sum.add(nextHourCharge);
            previousHourCharge = nextHourCharge;
            nextHourCharge = previousHourCharge.multiply(nextHourRate());
        }
        return sum;
    }
}
