package com.ddebowczyk.parkingmeter.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CurrentDateProvider {

    public LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

}
