package com.airtribe.meditrack.exception;

// Thrown when an appointment lookup by id fails (e.g., trying to cancel a non-existent appointment)
public class AppointmentNotFoundException extends Exception {

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}