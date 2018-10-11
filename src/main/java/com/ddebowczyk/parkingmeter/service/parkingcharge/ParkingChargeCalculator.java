package com.ddebowczyk.parkingmeter.service.parkingcharge;

import com.ddebowczyk.parkingmeter.domain.Currency;
import com.ddebowczyk.parkingmeter.domain.DriverType;
import com.ddebowczyk.parkingmeter.domain.ParkingChargePayment;
import com.ddebowczyk.parkingmeter.exception.ParkingMeterException;
import com.ddebowczyk.parkingmeter.service.parkingcharge.strategy.ParkingChargeCalculateStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingChargeCalculator {

    private static final Currency defaultCurrency = Currency.PLN;

    private final List<ParkingChargeCalculateStrategy> parkingChargeStrategies;

    public ParkingChargePayment calculate(@NotNull LocalDateTime startDateTime, @NotNull LocalDateTime endDateTime,
                                          @NotNull DriverType driverType,
                                          @Nullable Currency currency) {
        log.info("Generating parking charge payment for {} driver type", driverType);
        final Currency chosenOrDefaultCurrency = currency != null ? currency : defaultCurrency;
        CurrencyAndDriverTypeHourRateChargeModel parkingChargeModel = new CurrencyAndDriverTypeHourRateChargeModel(
                startDateTime, endDateTime, driverType, chosenOrDefaultCurrency);
        final BigDecimal chargeAmount = calculateParkingChargeAmount(parkingChargeModel);
        return new ParkingChargePayment(chargeAmount, chosenOrDefaultCurrency, endDateTime.toLocalDate());
    }

    private BigDecimal calculateParkingChargeAmount(ParkingChargeModel parkingChargeModel) {
        log.info("Calculating parking charge amount for {} parking charge model", parkingChargeModel);
        return parkingChargeStrategies.stream().filter(strategy -> strategy.applies(parkingChargeModel))
                .findFirst()
                .orElseThrow(ParkingMeterException::new)
                .caluculate(parkingChargeModel);
    }
}
