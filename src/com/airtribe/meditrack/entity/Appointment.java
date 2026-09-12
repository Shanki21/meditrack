package com.airtribe.meditrack.entity;

// Represents a booked appointment between a doctor and a patient
public class Appointment extends MedicalEntity implements Cloneable {

    private Doctor doctor;
    private Patient patient;
    private String appointmentDate;
    private AppointmentStatus status;

    public Appointment(String id, Doctor doctor, Patient patient, String appointmentDate, AppointmentStatus status) {
        super(id); // MedicalEntity handles id storage
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    // Short one-line summary — required since MedicalEntity declares this abstract
    @Override
    public String getSummary() {
        return "Appointment[" + getId() + "] " + patient.getName() + " with Dr. " + doctor.getName()
                + " on " + appointmentDate + " - " + status;
    }

    // Full detail printout
    public void displayInfo() {
        System.out.println("Appointment ID: " + getId());
        System.out.println("Patient: " + patient.getName());
        System.out.println("Doctor: " + doctor.getName());
        System.out.println("Date: " + appointmentDate);
        System.out.println("Status: " + status);
    }


    @Override
    public Appointment clone() {
        try {
            Appointment cloned = (Appointment) super.clone(); // shallow copy first — copies references as-is
            cloned.patient = this.patient.clone();            // now replace with independent deep copies
            // note: Doctor doesn't implement Cloneable yet — see note below
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported for Appointment", e);
        }
    }
}