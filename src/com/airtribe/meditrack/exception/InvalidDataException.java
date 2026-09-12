package com.airtribe.meditrack.exception;

// Thrown when data fails validation (e.g., invalid age, empty name)
public class InvalidDataException extends Exception {

    public InvalidDataException(String message) {
        super(message); // pass the error message up to the base Exception class
    }
}