package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.intf.Searchable;

// Represents a doctor — adds specialization and fee on top of base Person fields
public class Doctor extends Person implements Cloneable, Searchable {

    private Specialization specialization;
    private double consultationFee;

    public Doctor(String id, String name, int age, String contactNumber, String address,
                  Specialization specialization, double consultationFee) {
        super(id, name, age, contactNumber, address);
        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getSummary() {
        return super.getSummary() + ", Specialization: " + specialization;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("ConsultationFee: " + consultationFee);
        System.out.println("Specialization: " + specialization);
    }

    // Same pattern as Patient — all fields here are primitives/String/enum (all effectively
    // immutable or safely shared), so super.clone() alone is a safe, complete copy.
    @Override
    public Doctor clone() {
        try {
            return (Doctor) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported for Doctor", e);
        }
    }

    @Override
    public boolean matches(String keyword) {
        return getName().toLowerCase().contains(keyword.toLowerCase())
                || specialization.toString().toLowerCase().contains(keyword.toLowerCase());
    }
}
