package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.service.*;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.List;
import java.util.Scanner;

// Entry point — console menu that delegates all real work to the service layer
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static DoctorService doctorService = new DoctorService();
    private static PatientService patientService = new PatientService();
    private static AppointmentService appointmentService = new AppointmentService(doctorService, patientService);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1": registerDoctor(); break;
                    case "2": registerPatient(); break;
                    case "3": bookAppointment(); break;
                    case "4": viewAllDoctors(); break;
                    case "5": viewAllPatients(); break;
                    case "6": viewAllAppointments(); break;
                    case "7": cancelAppointment(); break;
                    case "8": searchDoctor(); break;
                    case "9": searchPatient(); break;
                    case "10": deleteDoctor(); break;
                    case "11": deletePatient(); break;
                    case "12": viewAppointmentById(); break;
                    case "13": generateBill(); break;
                    case "0": running = false; break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (InvalidDataException | AppointmentNotFoundException | NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Exiting MediTrack. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n===== MediTrack =====");
        System.out.println("1. Register Doctor");
        System.out.println("2. Register Patient");
        System.out.println("3. Book Appointment");
        System.out.println("4. View All Doctors");
        System.out.println("5. View All Patients");
        System.out.println("6. View All Appointments");
        System.out.println("7. Cancel Appointment");
        System.out.println("8. Search Doctor");
        System.out.println("9. Search Patient");
        System.out.println("10. Delete Doctor");
        System.out.println("11. Delete Patient");
        System.out.println("12. View Appointment by ID");
        System.out.println("13. Generate Bill");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void registerDoctor() throws InvalidDataException {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Contact (10 digits): ");
        String contact = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Specialization (CARDIOLOGY/NEUROLOGY/ORTHOPEDICS/DERMATOLOGY): ");
        Specialization spec = Specialization.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Consultation Fee: ");
        double fee = Double.parseDouble(scanner.nextLine());

        Doctor d = doctorService.registerDoctor(name, age, contact, address, spec, fee);
        System.out.println("Registered: " + d.getSummary());
    }

    private static void registerPatient() throws InvalidDataException {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Contact (10 digits): ");
        String contact = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Medical History: ");
        String history = scanner.nextLine();
        System.out.print("Blood Group: ");
        String bloodGroup = scanner.nextLine();

        Patient p = patientService.registerPatient(name, age, contact, address, history, bloodGroup);
        System.out.println("Registered: " + p.getSummary());
    }

    private static void bookAppointment() throws InvalidDataException {
        System.out.print("Doctor ID: ");
        String docId = scanner.nextLine();
        System.out.print("Patient ID: ");
        String patId = scanner.nextLine();
        System.out.print("Date (e.g. 2026-09-15): ");
        String date = scanner.nextLine();

        Appointment a = appointmentService.bookAppointment(docId, patId, date);
        System.out.println("Booked: " + a.getSummary());
    }

    private static void viewAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        for (Doctor d : doctors) System.out.println(d.getSummary());
        if (doctors.isEmpty()) System.out.println("No doctors registered.");
    }

    private static void viewAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        for (Patient p : patients) System.out.println(p.getSummary());
        if (patients.isEmpty()) System.out.println("No patients registered.");
    }

    private static void viewAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        for (Appointment a : appointments) System.out.println(a.getSummary());
        if (appointments.isEmpty()) System.out.println("No appointments booked.");
    }

    private static void cancelAppointment() throws AppointmentNotFoundException {
        System.out.print("Appointment ID to cancel: ");
        String id = scanner.nextLine();
        appointmentService.cancelAppointment(id);
        System.out.println("Cancelled appointment " + id);
    }

    // Search doctors by id, name, age, or specialization — submenu picks which
    private static void searchDoctor() {
        System.out.println("Search by: 1-ID  2-Name  3-Age  4-Specialization");
        String type = scanner.nextLine();
        switch (type) {
            case "1":
                System.out.print("Doctor ID: ");
                Doctor d = doctorService.searchDoctor(scanner.nextLine());
                System.out.println(d != null ? d.getSummary() : "Not found.");
                break;
            case "2":
                System.out.print("Name (partial ok): ");
                printDoctorList(doctorService.searchDoctorByName(scanner.nextLine()));
                break;
            case "3":
                System.out.print("Age: ");
                printDoctorList(doctorService.searchDoctorByAge(Integer.parseInt(scanner.nextLine())));
                break;
            case "4":
                System.out.print("Specialization (CARDIOLOGY/NEUROLOGY/ORTHOPEDICS/DERMATOLOGY): ");
                Specialization spec = Specialization.valueOf(scanner.nextLine().toUpperCase());
                printDoctorList(doctorService.searchDoctorBySpecialization(spec));
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void printDoctorList(List<Doctor> doctors) {
        if (doctors.isEmpty()) System.out.println("No matches found.");
        for (Doctor d : doctors) System.out.println(d.getSummary());
    }

    // Search patients by id, name, or age — submenu picks which
    private static void searchPatient() {
        System.out.println("Search by: 1-ID  2-Name  3-Age");
        String type = scanner.nextLine();
        switch (type) {
            case "1":
                System.out.print("Patient ID: ");
                Patient p = patientService.searchPatient(scanner.nextLine());
                System.out.println(p != null ? p.getSummary() : "Not found.");
                break;
            case "2":
                System.out.print("Name (partial ok): ");
                printPatientList(patientService.searchPatientByName(scanner.nextLine()));
                break;
            case "3":
                System.out.print("Age: ");
                printPatientList(patientService.searchPatientByAge(Integer.parseInt(scanner.nextLine())));
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void printPatientList(List<Patient> patients) {
        if (patients.isEmpty()) System.out.println("No matches found.");
        for (Patient p : patients) System.out.println(p.getSummary());
    }

    private static void deleteDoctor() {
        System.out.print("Doctor ID to delete: ");
        String id = scanner.nextLine();
        boolean deleted = doctorService.deleteDoctor(id);
        System.out.println(deleted ? "Deleted doctor " + id : "No doctor found with id " + id);
    }

    private static void deletePatient() {
        System.out.print("Patient ID to delete: ");
        String id = scanner.nextLine();
        boolean deleted = patientService.deletePatient(id);
        System.out.println(deleted ? "Deleted patient " + id : "No patient found with id " + id);
    }

    private static void viewAppointmentById() throws AppointmentNotFoundException {
        System.out.print("Appointment ID: ");
        String id = scanner.nextLine();
        Appointment a = appointmentService.viewAppointment(id);
        a.displayInfo();
    }

    // Creates a Bill for an existing appointment and prints the immutable BillSummary
    private static void generateBill() throws AppointmentNotFoundException {
        System.out.print("Appointment ID to bill: ");
        String appointmentId = scanner.nextLine();
        Appointment appointment = appointmentService.viewAppointment(appointmentId); // throws if not found

        System.out.print("Base Fee: ");
        double baseFee = Double.parseDouble(scanner.nextLine());

        String billId = IdGenerator.generateBillId();
        Bill bill = new Bill(billId, appointment, baseFee);
        BillSummary summary = bill.generateSummary();

        bill.displayInfo();
        System.out.println("Summary: " + summary);
    }
}