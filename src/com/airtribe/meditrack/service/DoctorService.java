package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.ArrayList;
import java.util.List;

// Business logic for doctors — validation, creation, search. Mirrors PatientService's structure.
public class DoctorService {

    private DataStore<Doctor> doctorStore = new DataStore<>();

    public Doctor registerDoctor(String name, int age, String contactNumber, String address,
                                 Specialization specialization, double consultationFee) throws InvalidDataException {
        // validate before creating anything — fail fast, don't generate an id for bad data
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validateContactNumber(contactNumber);
        Validator.validateFee(consultationFee);

        String id = IdGenerator.generateDoctorId();
        Doctor doctor = new Doctor(id, name, age, contactNumber, address, specialization, consultationFee);
        doctorStore.add(id, doctor);
        return doctor;
    }

    // Search by exact id
    public Doctor searchDoctor(String id) {
        return doctorStore.getById(id);
    }

    // Search by name (partial match, case-insensitive)
    public List<Doctor> searchDoctorByName(String name) {
        List<Doctor> results = new ArrayList<>();
        for (Doctor d : doctorStore.getAll()) {
            if (d.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(d);
            }
        }
        return results;
    }

    // Search by exact age
    public List<Doctor> searchDoctorByAge(int age) {
        List<Doctor> results = new ArrayList<>();
        for (Doctor d : doctorStore.getAll()) {
            if (d.getAge() == age) {
                results.add(d);
            }
        }
        return results;
    }

    // Filter by specialization enum — exact match, since specialization is a fixed set of values
    public List<Doctor> searchDoctorBySpecialization(Specialization specialization) {
        List<Doctor> results = new ArrayList<>();
        for (Doctor d : doctorStore.getAll()) {
            if (d.getSpecialization() == specialization) {
                results.add(d);
            }
        }
        return results;
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctorStore.getAll());
    }

    public boolean deleteDoctor(String id) {
        boolean exists = doctorStore.exists(id);
        if (exists) {
            doctorStore.remove(id);
        }
        return exists;
    }
}