package com.sporty.airport.exception;

public class InvalidIcaoException extends RuntimeException{

    public InvalidIcaoException(String message) {
        super(message);
    }
}
