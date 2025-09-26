package com.sporty.airport.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String LOG_SYMBOL = "❌";

    @ExceptionHandler(InvalidIcaoException.class)
    public ResponseEntity<String> handleInvalidIcao(InvalidIcaoException ex) {
        String message = "Invalid ICAO code received";
        log.warn("{} {}: {}", LOG_SYMBOL, message, ex.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFound(NoResourceFoundException ex) {
        String message = "Could not find the specified resource, did you specify a valid ICAO?";
        log.warn("{} {}: {}", LOG_SYMBOL, message, ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        log.error("{} Unexpected error occurred", LOG_SYMBOL, ex);
        return ResponseEntity.internalServerError().body("Internal server error");
    }


}