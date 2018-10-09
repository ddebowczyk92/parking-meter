package com.ddebowczyk.parkingmeter.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class CurrentDateProvider {

    public LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }
}
