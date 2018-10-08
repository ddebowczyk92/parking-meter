package com.ddebowczyk.parkingmeter.rest;

import com.ddebowczyk.parkingmeter.exception.ParkingMeterException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingMeterExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({ParkingMeterException.class})
    protected ResponseEntity<ParkingMeterApiError> handleEshopException(ParkingMeterException ex, WebRequest request) {
        String message = messageSource.getMessage(ex.getClass().getCanonicalName(), ex.getObjects(), Locale.getDefault());
        final ParkingMeterApiError eshopError = ParkingMeterApiError.messageError(message);
        return ResponseEntity.badRequest().body(eshopError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final BindingResult bindingResult = ex.getBindingResult();
        List<ParkingMeterFieldError> parkingMeterFieldErrors = bindingResult.getFieldErrors().stream()
                .map(this::toParkingMeterFieldError).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(ParkingMeterApiError.fieldErrors(parkingMeterFieldErrors));
    }

    private ParkingMeterFieldError toParkingMeterFieldError(FieldError fieldError) {
        String message = messageSource.getMessage(fieldError, Locale.getDefault());
        return new ParkingMeterFieldError(fieldError.getField(), fieldError.getRejectedValue(), message);
    }

    @Getter
    private static class ParkingMeterApiError {
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        private final LocalDateTime timestamp = LocalDateTime.now();
        private final String message;
        private final List<ParkingMeterFieldError> fieldErrors;

        private static ParkingMeterApiError fieldErrors(List<ParkingMeterFieldError> fieldErrors) {
            return new ParkingMeterApiError(null, fieldErrors);
        }

        private static ParkingMeterApiError messageError(String message) {
            return new ParkingMeterApiError(message, null);
        }

        @JsonCreator
        public ParkingMeterApiError(@JsonProperty("message") String message, @JsonProperty("fieldErrors") List<ParkingMeterFieldError> fieldErrors) {
            this.message = message;
            this.fieldErrors = fieldErrors;
        }
    }

    @Getter
    private static class ParkingMeterFieldError {
        private final String field;
        private final Object rejectedValue;
        private final String message;

        @JsonCreator
        public ParkingMeterFieldError(@JsonProperty("field") String field,
                                      @JsonProperty("rejectedValue") Object rejectedValue,
                                      @JsonProperty("message") String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }
    }

}
