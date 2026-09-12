package com.airtribe.meditrack.entity;

// Base class for anyone in the system who is a "person" — Doctor and Patient extend this
public abstract class Person extends MedicalEntity {

    private String name;
    private int age;
    private String contactNumber;
    private String address;

    public Person(String id, String name, int age, String contactNumber, String address) {
        super(id); // delegates id storage to MedicalEntity
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Short one-line summary, used for lists/search results
    @Override
    public String getSummary() {
        return name + " (ID: " + getId() + ", Age: " + age + ")";
    }

    // Full detail printout, used when viewing a single record
    @Override
    public void displayInfo() {
        System.out.println("ID: " + getId());
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Contact: " + contactNumber);
        System.out.println("Address: " + address);
    }
}