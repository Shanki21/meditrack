package com.airtribe.meditrack.util;

public class IdGenerator {

    // Counters for generating unique IDs
    private static int doctorCounter = 0;
    private static int patientCounter = 0;
    private static int appointmentCounter = 0;
    private static int billCounter = 0;

    // Generates a unique Doctor ID
    public static String generateDoctorId(){
        doctorCounter++;
        return String.format("DOC%03d", doctorCounter);
    }

    // Generates a unique Patient ID
    public static String generatePatientId(){
        patientCounter++;
        return String.format("PAT%03d", patientCounter);
    }

    // Generates a unique Appointment ID
    public static String generateAppointmentId() {
        appointmentCounter++;
        return String.format("APP%03d", appointmentCounter);
    }

    // Generates a unique Bill ID
    public static String generateBillId() {
        billCounter++;
        return String.format("BILL%03d", billCounter);
    }
}

