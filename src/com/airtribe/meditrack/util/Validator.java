package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exception.InvalidDataException;

// Centralized validation — every service calls here instead of validating inline
public class Validator {

    public static void validateName(String name) throws InvalidDataException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Name cannot be empty");
        }
    }

    public static void validateAge(int age) throws InvalidDataException {
        if (age <= 0 || age > 120) {
            throw new InvalidDataException("Age must be between 1 and 120");
        }
    }

    public static void validateContactNumber(String contactNumber) throws InvalidDataException {
        if (contactNumber == null || !contactNumber.matches("\\d{10}")) {
            throw new InvalidDataException("Contact number must be exactly 10 digits");
        }
    }

    public static void validateFee(double fee) throws InvalidDataException {
        if (fee <= 0) {
            throw new InvalidDataException("Consultation fee must be greater than 0");
        }
    }
}