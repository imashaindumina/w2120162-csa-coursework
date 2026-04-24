package com.smart.exception;

// Thrown when an operation is attempted on a sensor 
public class InvalidSensorStateException extends RuntimeException {
    public InvalidSensorStateException(String message) {
        super(message);
    }
}