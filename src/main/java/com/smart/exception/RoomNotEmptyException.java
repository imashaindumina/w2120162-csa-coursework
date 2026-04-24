package com.smart.exception;

// Custom exception thrown when attempting to delete a room 
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message) {
        super(message);
    }
}