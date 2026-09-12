package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.ArrayList;
import java.util.List;

// Business logic for patients — validation, creation, search. Main.java talks to this, never directly to DataStore.
public class PatientService {

    private DataStore<Patient> patientStore = new DataStore<>();

    public Patient registerPatient(String name, int age, String contactNumber, String address,
                                   String medicalHistory, String bloodGroup) throws InvalidDataException {
        // validate before creating anything — fail fast, don't generate an id for bad data
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validateContactNumber(contactNumber);

        String id = IdGenerator.generatePatientId();
        Patient patient = new Patient(id, name, age, contactNumber, address, medicalHistory, bloodGroup);
        patientStore.add(id, patient);
        return patient;
    }

    // Overload 1 — search by exact id
    public Patient searchPatient(String id) {
        return patientStore.getById(id);
    }

    // Overload 2 — search by name (partial match, case-insensitive)
    public List<Patient> searchPatientByName(String name) {
        List<Patient> results = new ArrayList<>();
        for (Patient p : patientStore.getAll()) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }

    // Overload 3 — search by exact age
    public List<Patient> searchPatientByAge(int age) {
        List<Patient> results = new ArrayList<>();
        for (Patient p : patientStore.getAll()) {
            if (p.getAge() == age) {
                results.add(p);
            }
        }
        return results;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientStore.getAll());
    }

    public boolean deletePatient(String id) {
        boolean exists = patientStore.exists(id);
        if (exists) {
            patientStore.remove(id);
        }
        return exists;
    }
}