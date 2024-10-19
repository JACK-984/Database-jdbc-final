package com.example;

import com.example.models.Appointment;
import com.example.models.Doctor;
import com.example.models.Patient;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("\nHospital Management System");
      System.out.println("1. Manage Patients");
      System.out.println("2. Manage Doctors");
      System.out.println("3. Manage Appointments");
      System.out.println("4. Generate Bill");
      System.out.println("5. Check Doctor Availability");
      System.out.println("6. Exit");
      System.out.print("Enter your choice: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline

      switch (choice) {
        case 1:
          managePatients(scanner);
          break;
        case 2:
          manageDoctors(scanner);
          break;
        case 3:
          manageAppointments(scanner);
          break;
        case 4:
          generateBill(scanner);
          break;
        case 5:
          checkDoctorAvailability(scanner);
          break;
        case 6:
          System.out.println("Exiting...");
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private static void generateBill(Scanner scanner) {
    System.out.println("\nBill Generation");
    System.out.print("Enter appointment ID: ");
    int appointmentId = scanner.nextInt();
    System.out.print("Enter total amount: ");
    double totalAmount = scanner.nextDouble();

    String sql = "CALL GenerateBill(?, ?)";

    try (
      Connection conn = DBConnection.getConnection();
      CallableStatement cstmt = conn.prepareCall(sql)
    ) {
      cstmt.setInt(1, appointmentId);
      cstmt.setDouble(2, totalAmount);

      cstmt.execute();
      System.out.println("Bill generated successfully.");
    } catch (SQLException e) {
      System.out.println("Error generating bill: " + e.getMessage());
    }
  }

  private static void checkDoctorAvailability(Scanner scanner) {
    System.out.println("\nDoctor Availability");
    System.out.print("Enter doctor ID: ");
    int doctorId = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    System.out.print("Enter date (YYYY-MM-DD): ");
    String dateStr = scanner.nextLine();

    String sql =
      "SELECT Time FROM Appointments WHERE DoctorID = ? AND Date = ? AND Status = 'Scheduled'";

    try (
      Connection conn = DBConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {
      pstmt.setInt(1, doctorId);
      pstmt.setDate(2, Date.valueOf(dateStr));

      ResultSet rs = pstmt.executeQuery();

      System.out.println(
        "Booked time slots for the doctor on " + dateStr + ":"
      );
      while (rs.next()) {
        System.out.println(rs.getTime("Time"));
      }
    } catch (SQLException e) {
      System.out.println(
        "Error checking doctor availability: " + e.getMessage()
      );
    }
  }

  private static void managePatients(Scanner scanner) {
    while (true) {
      System.out.println("\nManage Patients");
      System.out.println("1. Add Patient");
      System.out.println("2. Update Patient");
      System.out.println("3. Delete Patient");
      System.out.println("4. View Patient");
      System.out.println("5. List All Patients");
      System.out.println("6. Back to Main Menu");
      System.out.print("Enter your choice: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline

      switch (choice) {
        case 1:
          addPatient(scanner);
          break;
        case 2:
          updatePatient(scanner);
          break;
        case 3:
          deletePatient(scanner);
          break;
        case 4:
          viewPatient(scanner);
          break;
        case 5:
          listAllPatients();
          break;
        case 6:
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private static void manageDoctors(Scanner scanner) {
    while (true) {
      System.out.println("\nManage Doctors");
      System.out.println("1. Add Doctor");
      System.out.println("2. Update Doctor");
      System.out.println("3. Delete Doctor");
      System.out.println("4. View Doctor");
      System.out.println("5. List All Doctors");
      System.out.println("6. Back to Main Menu");
      System.out.print("Enter your choice: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline

      switch (choice) {
        case 1:
          addDoctor(scanner);
          break;
        case 2:
          updateDoctor(scanner);
          break;
        case 3:
          deleteDoctor(scanner);
          break;
        case 4:
          viewDoctor(scanner);
          break;
        case 5:
          listAllDoctors();
          break;
        case 6:
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private static void manageAppointments(Scanner scanner) {
    while (true) {
      System.out.println("\nManage Appointments");
      System.out.println("1. Schedule Appointment");
      System.out.println("2. Update Appointment");
      System.out.println("3. Cancel Appointment");
      System.out.println("4. View Appointment");
      System.out.println("5. List All Appointments");
      System.out.println("6. Back to Main Menu");
      System.out.print("Enter your choice: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline

      switch (choice) {
        case 1:
          scheduleAppointment(scanner);
          break;
        case 2:
          updateAppointment(scanner);
          break;
        case 3:
          cancelAppointment(scanner);
          break;
        case 4:
          viewAppointment(scanner);
          break;
        case 5:
          listAllAppointments();
          break;
        case 6:
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  // Patient management methods
  private static void addPatient(Scanner scanner) {
    System.out.println("\nAdd Patient");
    System.out.print("Enter patient name: ");
    String name = scanner.nextLine();
    System.out.print("Enter patient age: ");
    int age = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    System.out.print("Enter patient gender (Male/Female/Other): ");
    String gender = scanner.nextLine();
    System.out.print("Enter patient address: ");
    String address = scanner.nextLine();
    System.out.print("Enter patient contact number: ");
    String contactNumber = scanner.nextLine();
    System.out.print("Enter patient medical history: ");
    String medicalHistory = scanner.nextLine();

    Patient patient = new Patient(
      name,
      age,
      gender,
      address,
      contactNumber,
      medicalHistory
    );
    try {
      patient.save();
      System.out.println(
        "Patient added successfully with ID: " + patient.getPatientId()
      );
    } catch (SQLException e) {
      System.out.println("Error adding patient: " + e.getMessage());
    }
  }

  private static void updatePatient(Scanner scanner) {
    System.out.println("\nUpdate Patient");
    System.out.print("Enter patient ID: ");
    int patientId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Patient patient = Patient.getById(patientId);
      if (patient == null) {
        System.out.println("Patient not found.");
        return;
      }

      System.out.print("Enter new name (press enter to keep current): ");
      String name = scanner.nextLine();
      if (!name.isEmpty()) patient.setName(name);

      System.out.print("Enter new age (enter 0 to keep current): ");
      int age = scanner.nextInt();
      scanner.nextLine(); // Consume newline
      if (age != 0) patient.setAge(age);

      System.out.print("Enter new gender (press enter to keep current): ");
      String gender = scanner.nextLine();
      if (!gender.isEmpty()) patient.setGender(gender);

      System.out.print("Enter new address (press enter to keep current): ");
      String address = scanner.nextLine();
      if (!address.isEmpty()) patient.setAddress(address);

      System.out.print(
        "Enter new contact number (press enter to keep current): "
      );
      String contactNumber = scanner.nextLine();
      if (!contactNumber.isEmpty()) patient.setContactNumber(contactNumber);

      System.out.print(
        "Enter new medical history (press enter to keep current): "
      );
      String medicalHistory = scanner.nextLine();
      if (!medicalHistory.isEmpty()) patient.setMedicalHistory(medicalHistory);

      patient.save();
      System.out.println("Patient updated successfully.");
    } catch (SQLException e) {
      System.out.println("Error updating patient: " + e.getMessage());
    }
  }

  private static void deletePatient(Scanner scanner) {
    System.out.println("\nDelete Patient");
    System.out.print("Enter patient ID: ");
    int patientId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Patient patient = Patient.getById(patientId);
      if (patient == null) {
        System.out.println("Patient not found.");
        return;
      }

      patient.delete();
      System.out.println("Patient deleted successfully.");
    } catch (SQLException e) {
      System.out.println("Error deleting patient: " + e.getMessage());
    }
  }

  private static void viewPatient(Scanner scanner) {
    System.out.println("\nView Patient");
    System.out.print("Enter patient ID: ");
    int patientId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Patient patient = Patient.getById(patientId);
      if (patient == null) {
        System.out.println("Patient not found.");
        return;
      }

      System.out.println(patient);
    } catch (SQLException e) {
      System.out.println("Error viewing patient: " + e.getMessage());
    }
  }

  private static void listAllPatients() {
    System.out.println("\nAll Patients");
    try {
      List<Patient> patients = Patient.getAll();
      for (Patient patient : patients) {
        System.out.println(patient);
      }
    } catch (SQLException e) {
      System.out.println("Error listing patients: " + e.getMessage());
    }
  }

  // Doctor management methods
  private static void addDoctor(Scanner scanner) {
    System.out.println("\nAdd Doctor");
    System.out.print("Enter doctor name: ");
    String name = scanner.nextLine();
    System.out.print("Enter doctor specialty: ");
    String specialty = scanner.nextLine();
    System.out.print("Enter years of experience: ");
    int yearsOfExperience = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    System.out.print("Enter contact information: ");
    String contactInformation = scanner.nextLine();

    Doctor doctor = new Doctor(
      name,
      specialty,
      yearsOfExperience,
      contactInformation
    );
    try {
      doctor.save();
      System.out.println(
        "Doctor added successfully with ID: " + doctor.getDoctorId()
      );
    } catch (SQLException e) {
      System.out.println("Error adding doctor: " + e.getMessage());
    }
  }

  private static void updateDoctor(Scanner scanner) {
    System.out.println("\nUpdate Doctor");
    System.out.print("Enter doctor ID: ");
    int doctorId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Doctor doctor = Doctor.getById(doctorId);
      if (doctor == null) {
        System.out.println("Doctor not found.");
        return;
      }

      System.out.print("Enter new name (press enter to keep current): ");
      String name = scanner.nextLine();
      if (!name.isEmpty()) doctor.setName(name);

      System.out.print("Enter new specialty (press enter to keep current): ");
      String specialty = scanner.nextLine();
      if (!specialty.isEmpty()) doctor.setSpecialty(specialty);

      System.out.print(
        "Enter new years of experience (enter 0 to keep current): "
      );
      int yearsOfExperience = scanner.nextInt();
      scanner.nextLine(); // Consume newline
      if (yearsOfExperience != 0) doctor.setYearsOfExperience(
        yearsOfExperience
      );

      System.out.print(
        "Enter new contact information (press enter to keep current): "
      );
      String contactInformation = scanner.nextLine();
      if (!contactInformation.isEmpty()) doctor.setContactInformation(
        contactInformation
      );

      doctor.save();
      System.out.println("Doctor updated successfully.");
    } catch (SQLException e) {
      System.out.println("Error updating doctor: " + e.getMessage());
    }
  }

  private static void deleteDoctor(Scanner scanner) {
    System.out.println("\nDelete Doctor");
    System.out.print("Enter doctor ID: ");
    int doctorId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Doctor doctor = Doctor.getById(doctorId);
      if (doctor == null) {
        System.out.println("Doctor not found.");
        return;
      }

      doctor.delete();
      System.out.println("Doctor deleted successfully.");
    } catch (SQLException e) {
      System.out.println("Error deleting doctor: " + e.getMessage());
    }
  }

  private static void viewDoctor(Scanner scanner) {
    System.out.println("\nView Doctor");
    System.out.print("Enter doctor ID: ");
    int doctorId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Doctor doctor = Doctor.getById(doctorId);
      if (doctor == null) {
        System.out.println("Doctor not found.");
        return;
      }

      System.out.println(doctor);
    } catch (SQLException e) {
      System.out.println("Error viewing doctor: " + e.getMessage());
    }
  }

  private static void listAllDoctors() {
    System.out.println("\nAll Doctors");
    try {
      List<Doctor> doctors = Doctor.getAll();
      for (Doctor doctor : doctors) {
        System.out.println(doctor);
      }
    } catch (SQLException e) {
      System.out.println("Error listing doctors: " + e.getMessage());
    }
  }

  // Appointment management methods
  private static void scheduleAppointment(Scanner scanner) {
    System.out.println("\nSchedule Appointment");
    System.out.print("Enter patient ID: ");
    int patientId = scanner.nextInt();
    System.out.print("Enter doctor ID: ");
    int doctorId = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    System.out.print("Enter appointment date (YYYY-MM-DD): ");
    String dateStr = scanner.nextLine();
    System.out.print("Enter appointment time (HH:MM): ");
    String timeStr = scanner.nextLine();

    Appointment appointment = new Appointment(
      patientId,
      doctorId,
      Date.valueOf(dateStr),
      Time.valueOf(timeStr + ":00"),
      "Scheduled"
    );
    try {
      appointment.save();
      System.out.println(
        "Appointment scheduled successfully with ID: " +
        appointment.getAppointmentId()
      );
    } catch (SQLException e) {
      System.out.println("Error scheduling appointment: " + e.getMessage());
    }
  }

  private static void updateAppointment(Scanner scanner) {
    System.out.println("\nUpdate Appointment");
    System.out.print("Enter appointment ID: ");
    int appointmentId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Appointment appointment = Appointment.getById(appointmentId);
      if (appointment == null) {
        System.out.println("Appointment not found.");
        return;
      }

      System.out.print("Enter new patient ID (press enter to keep current): ");
      String patientIdStr = scanner.nextLine();
      if (!patientIdStr.isEmpty()) {
        appointment.setPatientId(Integer.parseInt(patientIdStr));
      }

      System.out.print("Enter new doctor ID (press enter to keep current): ");
      String doctorIdStr = scanner.nextLine();
      if (!doctorIdStr.isEmpty()) {
        appointment.setDoctorId(Integer.parseInt(doctorIdStr));
      }

      System.out.print(
        "Enter new date (YYYY-MM-DD) (press enter to keep current): "
      );
      String dateStr = scanner.nextLine();
      if (!dateStr.isEmpty()) {
        appointment.setDate(Date.valueOf(dateStr));
      }

      System.out.print(
        "Enter new time (HH:MM) (press enter to keep current): "
      );
      String timeStr = scanner.nextLine();
      if (!timeStr.isEmpty()) {
        appointment.setTime(Time.valueOf(timeStr + ":00"));
      }

      System.out.print(
        "Enter new status (Scheduled/Completed/Cancelled) (press enter to keep current): "
      );
      String status = scanner.nextLine();
      if (!status.isEmpty()) {
        appointment.setStatus(status);
      }

      appointment.save();
      System.out.println("Appointment updated successfully.");
    } catch (SQLException e) {
      System.out.println("Error updating appointment: " + e.getMessage());
    }
  }

  private static void cancelAppointment(Scanner scanner) {
    System.out.println("\nCancel Appointment");
    System.out.print("Enter appointment ID: ");
    int appointmentId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Appointment appointment = Appointment.getById(appointmentId);
      if (appointment == null) {
        System.out.println("Appointment not found.");
        return;
      }

      appointment.setStatus("Cancelled");
      appointment.save();
      System.out.println("Appointment cancelled successfully.");
    } catch (SQLException e) {
      System.out.println("Error cancelling appointment: " + e.getMessage());
    }
  }

  private static void viewAppointment(Scanner scanner) {
    System.out.println("\nView Appointment");
    System.out.print("Enter appointment ID: ");
    int appointmentId = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    try {
      Appointment appointment = Appointment.getById(appointmentId);
      if (appointment == null) {
        System.out.println("Appointment not found.");
        return;
      }

      System.out.println(appointment);
    } catch (SQLException e) {
      System.out.println("Error viewing appointment: " + e.getMessage());
    }
  }

  private static void listAllAppointments() {
    System.out.println("\nAll Appointments");
    try {
      List<Appointment> appointments = Appointment.getAll();
      for (Appointment appointment : appointments) {
        System.out.println(appointment);
      }
    } catch (SQLException e) {
      System.out.println("Error listing appointments: " + e.getMessage());
    }
  }
}
