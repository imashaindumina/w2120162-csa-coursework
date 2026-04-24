package com.smart.exception;

// Thrown when a linked resource is required but not found.
 
public class LinkedResourceNotFoundException extends RuntimeException {
    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}