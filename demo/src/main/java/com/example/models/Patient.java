package com.example.models;

import com.example.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Patient {

  private int patientId;
  private String name;
  private int age;
  private String gender;
  private String address;
  private String contactNumber;
  private String medicalHistory;

  // Constructors
  public Patient() {}

  public Patient(
    String name,
    int age,
    String gender,
    String address,
    String contactNumber,
    String medicalHistory
  ) {
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.address = address;
    this.contactNumber = contactNumber;
    this.medicalHistory = medicalHistory;
  }

  // Getters and Setters

  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getMedicalHistory() {
    return medicalHistory;
  }

  public void setMedicalHistory(String medicalHistory) {
    this.medicalHistory = medicalHistory;
  }

  // Database operations
  public void save() throws SQLException {
    if (this.patientId == 0) {
      // This is a new patient, so insert
      String sql =
        "INSERT INTO Patients (Name, Age, Gender, Address, ContactNumber, MedicalHistory) VALUES (?, ?, ?, ?, ?, ?)";
      try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
          sql,
          Statement.RETURN_GENERATED_KEYS
        )
      ) {
        pstmt.setString(1, this.name);
        pstmt.setInt(2, this.age);
        pstmt.setString(3, this.gender);
        pstmt.setString(4, this.address);
        pstmt.setString(5, this.contactNumber);
        pstmt.setString(6, this.medicalHistory);

        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
          try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
              this.patientId = generatedKeys.getInt(1);
            }
          }
        }
      }
    } else {
      // This is an existing patient, so update
      String sql =
        "UPDATE Patients SET Name = ?, Age = ?, Gender = ?, Address = ?, ContactNumber = ?, MedicalHistory = ? WHERE PatientID = ?";
      try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)
      ) {
        pstmt.setString(1, this.name);
        pstmt.setInt(2, this.age);
        pstmt.setString(3, this.gender);
        pstmt.setString(4, this.address);
        pstmt.setString(5, this.contactNumber);
        pstmt.setString(6, this.medicalHistory);
        pstmt.setInt(7, this.patientId);

        pstmt.executeUpdate();
      }
    }
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Patients WHERE PatientID = ?";
    try (
      Connection conn = DBConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {
      pstmt.setInt(1, this.patientId);
      pstmt.executeUpdate();
    }
  }

  public static Patient getById(int patientId) throws SQLException {
    String sql = "SELECT * FROM Patients WHERE PatientID = ?";
    try (
      Connection conn = DBConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {
      pstmt.setInt(1, patientId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          Patient patient = new Patient();
          patient.setPatientId(rs.getInt("PatientID"));
          patient.setName(rs.getString("Name"));
          patient.setAge(rs.getInt("Age"));
          patient.setGender(rs.getString("Gender"));
          patient.setAddress(rs.getString("Address"));
          patient.setContactNumber(rs.getString("ContactNumber"));
          patient.setMedicalHistory(rs.getString("MedicalHistory"));
          return patient;
        }
      }
    }
    return null;
  }

  public static List<Patient> getAll() throws SQLException {
    List<Patient> patients = new ArrayList<>();
    String sql = "SELECT * FROM Patients";
    try (
      Connection conn = DBConnection.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql)
    ) {
      while (rs.next()) {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("PatientID"));
        patient.setName(rs.getString("Name"));
        patient.setAge(rs.getInt("Age"));
        patient.setGender(rs.getString("Gender"));
        patient.setAddress(rs.getString("Address"));
        patient.setContactNumber(rs.getString("ContactNumber"));
        patient.setMedicalHistory(rs.getString("MedicalHistory"));
        patients.add(patient);
      }
    }
    return patients;
  }

  @Override
  public String toString() {
    return String.format(
      "Patient Details:\n" +
      "  ID:               %d\n" +
      "  Name:             %s\n" +
      "  Age:              %d\n" +
      "  Gender:           %s\n" +
      "  Address:          %s\n" +
      "  Contact Number:   %s\n" +
      "  Medical History:  %s",
      patientId,
      name,
      age,
      gender,
      address,
      contactNumber,
      medicalHistory
    );
  }
}
