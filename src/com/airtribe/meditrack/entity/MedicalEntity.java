package com.airtribe.meditrack.entity;

public abstract class MedicalEntity {

    protected String id;

    public MedicalEntity(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract String getSummary();

    public abstract void displayInfo();
}
