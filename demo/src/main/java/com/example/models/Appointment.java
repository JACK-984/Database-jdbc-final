package com.example.models;

import com.example.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Appointment {

  private int appointmentId;
  private int patientId;
  private String patientName;
  private int doctorId;
  private String doctorName;
  private Date date;
  private Time time;
  private String status;

  // Constructors
  public Appointment() {}

  public Appointment(
    int patientId,
    int doctorId,
    Date date,
    Time time,
    String status
  ) {
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.date = date;
    this.time = time;
    this.status = status;
  }

  // Getters and Setters
  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getDoctorName() {
    return doctorName;
  }

  public void setDoctorName(String doctorName) {
    this.doctorName = doctorName;
  }

  public int getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(int appointmentId) {
    this.appointmentId = appointmentId;
  }

  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public int getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(int doctorId) {
    this.doctorId = doctorId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Time getTime() {
    return time;
  }

  public void setTime(Time time) {
    this.time = time;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  // Database operations
  public void save() throws SQLException {
    if (this.appointmentId == 0) {
      // This is a new appointment, so insert
      String sql =
        "INSERT INTO Appointments (PatientID, DoctorID, Date, Time, Status) VALUES (?, ?, ?, ?, ?)";
      try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
          sql,
          Statement.RETURN_GENERATED_KEYS
        )
      ) {
        pstmt.setInt(1, this.patientId);
        pstmt.setInt(2, this.doctorId);
        pstmt.setDate(3, this.date);
        pstmt.setTime(4, this.time);
        pstmt.setString(5, this.status);

        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
          try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
              this.appointmentId = generatedKeys.getInt(1);
            }
          }
        }
      }
    } else {
      // This is an existing appointment, so update
      String sql =
        "UPDATE Appointments SET PatientID = ?, DoctorID = ?, Date = ?, Time = ?, Status = ? WHERE AppointmentID = ?";
      try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)
      ) {
        pstmt.setInt(1, this.patientId);
        pstmt.setInt(2, this.doctorId);
        pstmt.setDate(3, this.date);
        pstmt.setTime(4, this.time);
        pstmt.setString(5, this.status);
        pstmt.setInt(6, this.appointmentId);

        pstmt.executeUpdate();
      }
    }
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Appointments WHERE AppointmentID = ?";
    try (
      Connection conn = DBConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {
      pstmt.setInt(1, this.appointmentId);
      pstmt.executeUpdate();
    }
  }

  public static Appointment getById(int appointmentId) throws SQLException {
    String sql =
      "SELECT a.appointmentID, p.patientID, p.Name as PatientName, d.doctorID, d.Name as DoctorName, a.Date, a.Time, a.Status " +
      "FROM appointments a " +
      "JOIN patients p ON a.patientID = p.patientID " +
      "JOIN doctors d ON a.doctorID = d.doctorID " +
      "WHERE a.appointmentID = ?";

    try (
      Connection conn = DBConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {
      pstmt.setInt(1, appointmentId);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          Appointment appointment = new Appointment();
          appointment.setAppointmentId(rs.getInt("appointmentID"));
          appointment.setPatientId(rs.getInt("patientID"));
          appointment.setPatientName(rs.getString("PatientName"));
          appointment.setDoctorId(rs.getInt("doctorID"));
          appointment.setDoctorName(rs.getString("DoctorName"));
          appointment.setDate(rs.getDate("Date"));
          appointment.setTime(rs.getTime("Time"));
          appointment.setStatus(rs.getString("Status"));
          return appointment;
        }
      }
    }
    return null;
  }

  public static List<Appointment> getAll() throws SQLException {
    List<Appointment> appointments = new ArrayList<>();
    String sql =
      "SELECT a.appointmentID, p.patientID, p.Name as PatientName, d.doctorID, d.Name as DoctorName, a.Date, a.Time, a.Status " +
      "FROM appointments a " +
      "JOIN patients p ON a.patientID = p.patientID " +
      "JOIN doctors d ON a.doctorID = d.doctorID";
    try (
      Connection conn = DBConnection.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql)
    ) {
      while (rs.next()) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointmentID"));
        appointment.setPatientId(rs.getInt("patientID"));
        appointment.setPatientName(rs.getString("PatientName"));
        appointment.setDoctorId(rs.getInt("doctorID"));
        appointment.setDoctorName(rs.getString("DoctorName"));
        appointment.setDate(rs.getDate("Date"));
        appointment.setTime(rs.getTime("Time"));
        appointment.setStatus(rs.getString("Status"));
        appointments.add(appointment);
      }
    }
    return appointments;
  }

  @Override
  public String toString() {
    return String.format(
      "Appointment Details:\n" +
      "  ID:        %d\n" +
      "  Patient:   %s (ID: %d)\n" +
      "  Doctor:    %s (ID: %d)\n" +
      "  Date:      %s\n" +
      "  Time:      %s\n" +
      "  Status:    %s",
      appointmentId,
      patientName,
      patientId,
      doctorName,
      doctorId,
      date,
      time,
      status
    );
  }
}
