package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.intf.Payable;

// Represents a bill for a completed appointment — implements Payable
public class Bill extends MedicalEntity implements Payable {

    private Appointment appointment;
    private double baseFee;

    public Bill(String id, Appointment appointment, double baseFee) {
        super(id);
        this.appointment = appointment;
        this.baseFee = baseFee;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public double getBaseFee() {
        return baseFee;
    }

    // Required by Payable — this is the "how" behind calculateTotal, using Constants.TAX_RATE
    @Override
    public double calculateTotal() {
        return baseFee + (baseFee * com.airtribe.meditrack.constants.Constants.TAX_RATE);
    }

    // Converts this Bill into an immutable BillSummary snapshot
    public BillSummary generateSummary() {
        return new BillSummary(getId(), appointment.getId(), baseFee, com.airtribe.meditrack.constants.Constants.TAX_RATE);
    }

    @Override
    public String getSummary() {
        return "Bill[" + getId() + "] for Appointment " + appointment.getId() + " - Total: " + calculateTotal();
    }

    public void displayInfo() {
        System.out.println("Bill ID: " + getId());
        System.out.println("Appointment ID: " + appointment.getId());
        System.out.println("Base Fee: " + baseFee);
        System.out.println("Tax: " + (baseFee * com.airtribe.meditrack.constants.Constants.TAX_RATE));
        System.out.println("Total: " + calculateTotal());
    }
}