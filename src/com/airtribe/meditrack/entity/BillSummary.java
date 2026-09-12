package com.airtribe.meditrack.entity;

// Immutable snapshot of a finalized bill — no setters, all fields final
public final class BillSummary {

    private final String billId;
    private final String appointmentId;
    private final double baseFee;
    private final double taxAmount;
    private final double totalAmount;

    public BillSummary(String billId, String appointmentId, double baseFee, double taxRate) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.baseFee = baseFee;
        this.taxAmount = baseFee * taxRate; // computed once, never changes after this
        this.totalAmount = baseFee + taxAmount; // derived value, fixed at creation time
    }

    // Only getters — no setters anywhere in this class, by design
    public String getBillId() {
        return billId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public double getBaseFee() {
        return baseFee;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return "Bill[" + billId + "] Appointment: " + appointmentId
                + ", Base: " + baseFee + ", Tax: " + taxAmount + ", Total: " + totalAmount;
    }
}