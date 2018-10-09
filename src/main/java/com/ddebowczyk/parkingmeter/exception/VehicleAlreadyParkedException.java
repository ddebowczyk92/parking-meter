package com.ddebowczyk.parkingmeter.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VehicleAlreadyParkedException extends ParkingMeterException {

    private final String licensePlateNumber;

    @Override
    public Object[] getObjects() {
        return new Object[]{licensePlateNumber};
    }
}
