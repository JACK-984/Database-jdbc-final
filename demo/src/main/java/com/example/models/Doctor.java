package com.example.models;

import com.example.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Doctor {

  private int doctorId;
  private String name;
  private String specialty;
  private int yearsOfExperience;
  private String contactInformation;

  // Constructors
  public Doctor() {}

  public Doctor(
    String name,
    String specialty,
    int yearsOfExperience,
    String contactInformation
  ) {
    this.name = name;
    this.specialty = specialty;
    this.yearsOfExperience = yearsOfExperience;
    this.contactInformation = contactInformation;
  }

  // Getters and Setters
  public int getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(int doctorId) {
    this.doctorId = doctorId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public int getYearsOfExperience() {
    return yearsOfExperience;
  }

  public void setYearsOfExperience(int yearsOfExperience) {
    this.yearsOfExperience = yearsOfExperience;
  }

  public String getContactInformation() {
    return contactInformation;
  }

  public void setContactInformation(String contactInformation) {
    this.contactInformation = contactInformation;
  }

  // Database operations
  public void save() throws SQLException {
    if (this.doctorId == 0) {
      // This is a new doctor, so insert
      String sql =
        "INSERT INTO Doctors (Name, Specialty, YearsOfExperience, ContactInformation) VALUES (?, ?, ?, ?)";
      try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
          sql,
          Statement.RETURN_GENERATED_KEYS
        )
      ) {
        pstmt.setString(1, this.name);
        pstmt.setString(2, this.specialty);
        pstmt.setInt(3, this.yearsOfExperience);
        pstmt.setString(4, this.contactInformation);

        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
          try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
              this.doctorId = generatedKeys.getInt(1);
            }
          }
        }
      }
    } else {
      // This is an existing doctor, so update
      String sql =
        "UPDATE Doctors SET Name = ?, Specialty = ?, YearsOfExperience = ?, ContactInformation = ? WHERE DoctorID = ?";
      try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)
      ) {
        pstmt.setString(1, this.name);
        pstmt.setString(2, this.specialty);
        pstmt.setInt(3, this.yearsOfExperience);
        pstmt.setString(4, this.contactInformation);
        pstmt.setInt(5, this.doctorId);

        pstmt.executeUpdate();
      }
    }
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Doctors WHERE DoctorID = ?";
    try (
      Connection conn = DBConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {
      pstmt.setInt(1, this.doctorId);
      pstmt.executeUpdate();
    }
  }

  public static Doctor getById(int doctorId) throws SQLException {
    String sql = "SELECT * FROM Doctors WHERE DoctorID = ?";
    try (
      Connection conn = DBConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {
      pstmt.setInt(1, doctorId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          Doctor doctor = new Doctor();
          doctor.setDoctorId(rs.getInt("DoctorID"));
          doctor.setName(rs.getString("Name"));
          doctor.setSpecialty(rs.getString("Specialty"));
          doctor.setYearsOfExperience(rs.getInt("YearsOfExperience"));
          doctor.setContactInformation(rs.getString("ContactInformation"));
          return doctor;
        }
      }
    }
    return null;
  }

  public static List<Doctor> getAll() throws SQLException {
    List<Doctor> doctors = new ArrayList<>();
    String sql = "SELECT * FROM Doctors";
    try (
      Connection conn = DBConnection.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql)
    ) {
      while (rs.next()) {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("DoctorID"));
        doctor.setName(rs.getString("Name"));
        doctor.setSpecialty(rs.getString("Specialty"));
        doctor.setYearsOfExperience(rs.getInt("YearsOfExperience"));
        doctor.setContactInformation(rs.getString("ContactInformation"));
        doctors.add(doctor);
      }
    }
    return doctors;
  }

  @Override
  public String toString() {
    return (
      "Doctor{" +
      "doctorId=" +
      doctorId +
      ", name='" +
      name +
      '\'' +
      ", specialty='" +
      specialty +
      '\'' +
      ", yearsOfExperience=" +
      yearsOfExperience +
      ", contactInformation='" +
      contactInformation +
      '\'' +
      '}'
    );
  }
}
