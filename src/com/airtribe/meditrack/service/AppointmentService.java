package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

// Business logic for appointments — depends on DoctorService/PatientService to validate references
public class AppointmentService {

    private DataStore<Appointment> appointmentStore = new DataStore<>();
    private DoctorService doctorService;
    private PatientService patientService;

    // dependencies are passed in, not created here — makes this service easy to test/reuse
    public AppointmentService(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public Appointment bookAppointment(String doctorId, String patientId, String appointmentDate)
            throws InvalidDataException {
        Doctor doctor = doctorService.searchDoctor(doctorId);
        Patient patient = patientService.searchPatient(patientId);

        // fail fast if either reference is invalid — don't create an appointment pointing to nothing
        if (doctor == null) {
            throw new InvalidDataException("No doctor found with id: " + doctorId);
        }
        if (patient == null) {
            throw new InvalidDataException("No patient found with id: " + patientId);
        }

        String id = IdGenerator.generateAppointmentId();
        Appointment appointment = new Appointment(id, doctor, patient, appointmentDate, AppointmentStatus.PENDING);
        appointmentStore.add(id, appointment);
        return appointment;
    }

    public Appointment viewAppointment(String id) throws AppointmentNotFoundException {
        Appointment appointment = appointmentStore.getById(id);
        if (appointment == null) {
            throw new AppointmentNotFoundException("No appointment found with id: " + id);
        }
        return appointment;
    }

    public void cancelAppointment(String id) throws AppointmentNotFoundException {
        Appointment appointment = appointmentStore.getById(id);
        if (appointment == null) {
            throw new AppointmentNotFoundException("No appointment found with id: " + id);
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointmentStore.getAll());
    }
}