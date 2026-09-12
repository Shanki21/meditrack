package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.intf.Searchable;

// Represents a patient — adds medical history and blood group on top of base Person fields
public class Patient extends Person implements Cloneable, Searchable {

    private String medicalHistory;
    private String bloodGroup;

    public Patient(String id, String name, int age, String contactNumber, String address,
                   String medicalHistory, String bloodGroup) {
        super(id, name, age, contactNumber, address);
        this.medicalHistory = medicalHistory;
        this.bloodGroup = bloodGroup;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @Override
    public String getSummary() {
        return super.getSummary() + ", Blood Group: " + bloodGroup;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Medical History: " + medicalHistory);
        System.out.println("Blood Group: " + bloodGroup);
    }


    @Override
    public Patient clone() {
        try {
            return (Patient) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported for Patient", e);
        }
    }
    @Override
    public boolean matches(String keyword) {
        return getName().toLowerCase().contains(keyword.toLowerCase())
                || bloodGroup.toLowerCase().contains(keyword.toLowerCase());
    }
}