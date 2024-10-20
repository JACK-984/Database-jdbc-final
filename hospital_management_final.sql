-- Create and use the database
CREATE DATABASE IF NOT EXISTS hospital_management_system;
USE hospital_management_system;

-- Patients Table 
CREATE TABLE Patients (
    PatientID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Age INT,
    Gender ENUM('Male', 'Female', 'Other'),
    Address VARCHAR(255),
    ContactNumber VARCHAR(15),
    MedicalHistory TEXT
);

-- Doctors Table 
CREATE TABLE Doctors (
    DoctorID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Specialty VARCHAR(100),
    YearsOfExperience INT,
    ContactInformation VARCHAR(255)
);

-- Departments Table 
CREATE TABLE Departments (
    DepartmentID INT AUTO_INCREMENT PRIMARY KEY,
    DepartmentName VARCHAR(100)
);

-- DoctorDepartments Table
CREATE TABLE DoctorDepartments (
    DoctorID INT,
    DepartmentID INT,
    PRIMARY KEY (DoctorID, DepartmentID),
    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID) ON DELETE CASCADE,
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID) ON DELETE CASCADE
);

-- Appointments Table
CREATE TABLE Appointments (
    AppointmentID INT AUTO_INCREMENT PRIMARY KEY,
    PatientID INT,
    DoctorID INT,
    Date DATE,
    Time TIME,
    Status ENUM('Scheduled', 'Completed', 'Cancelled'),
    FOREIGN KEY (PatientID) REFERENCES Patients(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID) ON DELETE CASCADE
);

-- Services Table 
CREATE TABLE Services (
    ServiceID INT AUTO_INCREMENT PRIMARY KEY,
    ServiceName VARCHAR(100),
    Cost DECIMAL(10, 2)
);

-- AppointmentServices Table
CREATE TABLE AppointmentServices (
    AppointmentID INT,
    ServiceID INT,
    PRIMARY KEY (AppointmentID, ServiceID),
    FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID) ON DELETE CASCADE,
    FOREIGN KEY (ServiceID) REFERENCES Services(ServiceID) ON DELETE CASCADE
);

-- Bills Table
CREATE TABLE Bills (
    BillID INT AUTO_INCREMENT PRIMARY KEY,
    AppointmentID INT,
    TotalAmount DECIMAL(10, 2),
    DateOfPayment DATE,
    PaymentStatus ENUM('Paid', 'Unpaid'),
    FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID) ON DELETE CASCADE
);

-- Staff Table
CREATE TABLE Staff (
    StaffID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100),
    Role VARCHAR(50),
    DepartmentID INT,
    ContactInformation VARCHAR(255),
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID) ON DELETE SET NULL
);

-- Rooms Table 
CREATE TABLE Rooms (
    RoomID INT AUTO_INCREMENT PRIMARY KEY,
    RoomNumber VARCHAR(10),
    RoomType ENUM('General', 'ICU', 'Operation Theater'),
    AvailabilityStatus ENUM('Available', 'Occupied')
);

-- PatientAdmissions Table
CREATE TABLE PatientAdmissions (
    AdmissionID INT AUTO_INCREMENT PRIMARY KEY,
    PatientID INT,
    RoomID INT,
    AdmissionDate DATE,
    DischargeDate DATE,
    FOREIGN KEY (PatientID) REFERENCES Patients(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID) ON DELETE SET NULL
);
-- END DATABASE CREATION AND TABLES

-- DATA INSERTION
-- Insert data into Patients table
INSERT INTO Patients (Name, Age, Gender, Address, ContactNumber, MedicalHistory)
VALUES 
('John Doe', 35, 'Male', '123 Main St, Anytown', '555-1234', 'No significant medical history'),
('Jane Smith', 28, 'Female', '456 Elm St, Othertown', '555-5678', 'Allergic to penicillin'),
('Sam Johnson', 45, 'Other', '789 Oak Rd, Somewhere', '555-9876', 'Hypertension'),
('Emily White', 42, 'Female', '567 Pine St, Somewhere', '555-2468', 'Asthma'),
('Michael Brown', 55, 'Male', '890 Cedar Ave, Anytown', '555-3698', 'Type 2 Diabetes'),
('Sarah Green', 30, 'Female', '123 Maple Rd, Othertown', '555-1597', 'Migraine'),
('David Lee', 60, 'Male', '456 Oak Ln, Somewhere', '555-7531', 'Hypertension, High Cholesterol'),
('Lisa Chen', 25, 'Female', '789 Birch Dr, Anytown', '555-9512', 'No significant medical history');

-- Insert data into Doctors table
INSERT INTO Doctors (Name, Specialty, YearsOfExperience, ContactInformation)
VALUES 
('Dr. Emily Brown', 'Cardiology', 10, 'dr.brown@hospital.com'),
('Dr. Michael Lee', 'Pediatrics', 8, 'dr.lee@hospital.com'),
('Dr. Sarah Wilson', 'Orthopedics', 15, 'dr.wilson@hospital.com'),
('Dr. Robert Johnson', 'Neurology', 12, 'dr.johnson@hospital.com'),
('Dr. Amanda Taylor', 'Dermatology', 7, 'dr.taylor@hospital.com'),
('Dr. Christopher Davis', 'Oncology', 20, 'dr.davis@hospital.com'),
('Dr. Elizabeth Martinez', 'Gynecology', 9, 'dr.martinez@hospital.com');

-- Insert data into Departments table
INSERT INTO Departments (DepartmentName)
VALUES 
('Cardiology'),
('Pediatrics'),
('Orthopedics'),
('Emergency'),
('Neurology'),
('Dermatology'),
('Oncology'),
('Gynecology');

-- Insert data into DoctorDepartments table
INSERT INTO DoctorDepartments (DoctorID, DepartmentID)
VALUES 
(1, 1),
(2, 2),
(3, 3),
(4, 5),
(5, 6),
(6, 7),
(7, 8);

-- Insert data into Appointments table
INSERT INTO Appointments (PatientID, DoctorID, Date, Time, Status)
VALUES 
(1, 1, '2024-10-20', '10:00:00', 'Scheduled'),
(2, 2, '2024-10-21', '14:30:00', 'Scheduled'),
(3, 3, '2024-10-22', '11:15:00', 'Scheduled'),
(4, 4, '2024-10-23', '09:00:00', 'Scheduled'),
(5, 5, '2024-10-23', '11:30:00', 'Scheduled'),
(6, 6, '2024-10-24', '14:00:00', 'Scheduled'),
(7, 7, '2024-10-24', '16:30:00', 'Scheduled'),
(8, 1, '2024-10-25', '10:00:00', 'Scheduled'),
(1, 2, '2024-10-25', '13:30:00', 'Scheduled'),
(2, 3, '2024-10-26', '11:00:00', 'Scheduled'),
(3, 4, '2024-10-26', '15:30:00', 'Scheduled');

-- Insert data into Services table
INSERT INTO Services (ServiceName, Cost)
VALUES 
('General Consultation', 100.00),
('X-Ray', 150.00),
('Blood Test', 50.00),
('MRI Scan', 500.00),
('CT Scan', 350.00),
('Ultrasound', 200.00),
('Biopsy', 250.00);

-- Insert data into AppointmentServices table
INSERT INTO AppointmentServices (AppointmentID, ServiceID)
VALUES 
(1, 1),
(1, 3),
(2, 1),
(3, 1),
(3, 2),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 1),
(8, 3),
(9, 2),
(10, 4);

-- Insert data into Bills table
INSERT INTO Bills (AppointmentID, TotalAmount, DateOfPayment, PaymentStatus)
VALUES 
(1, 150.00, '2024-10-20', 'Paid'),
(2, 100.00, '2024-10-21', 'Unpaid'),
(3, 250.00, '2024-10-22', 'Paid'),
(4, 500.00, '2024-10-23', 'Paid'),
(5, 350.00, '2024-10-23', 'Unpaid'),
(6, 250.00, '2024-10-24', 'Paid'),
(7, 200.00, '2024-10-24', 'Unpaid');

-- Insert data into Staff table
INSERT INTO Staff (Name, Role, DepartmentID, ContactInformation)
VALUES 
('Alice Johnson', 'Nurse', 1, 'alice.j@hospital.com'),
('Bob Williams', 'Receptionist', 4, 'bob.w@hospital.com'),
('Carol Davis', 'Lab Technician', 2, 'carol.d@hospital.com'),
('Mark Wilson', 'Nurse', 5, 'mark.w@hospital.com'),
('Jennifer Lopez', 'Lab Technician', 6, 'jennifer.l@hospital.com'),
('Thomas Brown', 'Receptionist', 7, 'thomas.b@hospital.com'),
('Sophia Kim', 'Nurse', 8, 'sophia.k@hospital.com');

-- Insert data into Rooms table
INSERT INTO Rooms (RoomNumber, RoomType, AvailabilityStatus)
VALUES 
('101', 'General', 'Available'),
('102', 'ICU', 'Occupied'),
('201', 'Operation Theater', 'Available'),
('301', 'General', 'Available'),
('302', 'ICU', 'Available'),
('401', 'Operation Theater', 'Occupied'),
('402', 'General', 'Occupied');

-- Insert data into PatientAdmissions table
INSERT INTO PatientAdmissions (PatientID, RoomID, AdmissionDate, DischargeDate)
VALUES 
(1, 1, '2024-10-20', '2024-10-25'),
(2, 2, '2024-10-21', NULL),
(3, 3, '2024-10-22', '2024-10-23'),
(4, 4, '2024-10-23', NULL),
(5, 5, '2024-10-24', '2024-10-27'),
(6, 6, '2024-10-25', NULL),
(7, 7, '2024-10-26', '2024-10-28');

-- SCHEDULE APPOINTMENT PROCEDURE
DELIMITER //
CREATE PROCEDURE ScheduleAppointment(
    IN p_PatientID INT,
    IN p_DoctorID INT,
    IN p_Date DATE,
    IN p_Time TIME
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- Rollback transaction in case of error
        ROLLBACK;
    END;

    START TRANSACTION;

    INSERT INTO Appointments (PatientID, DoctorID, Date, Time, Status)
    VALUES (p_PatientID, p_DoctorID, p_Date, p_Time, 'Scheduled');

    COMMIT;
END //
DELIMITER ;

-- UPDATE PATIENT INFO
DELIMITER //
CREATE PROCEDURE UpdatePatientInfo(
    IN p_PatientID INT,
    IN p_Name VARCHAR(100),
    IN p_Age INT,
    IN p_Gender ENUM('Male', 'Female', 'Other'),
    IN p_Address VARCHAR(255),
    IN p_ContactNumber VARCHAR(15),
    IN p_MedicalHistory TEXT
)
BEGIN
    UPDATE Patients
    SET Name = p_Name, Age = p_Age, Gender = p_Gender, Address = p_Address, ContactNumber = p_ContactNumber, MedicalHistory = p_MedicalHistory
    WHERE PatientID = p_PatientID;
END //
DELIMITER ;

-- GENERATE BILL
DELIMITER //
CREATE PROCEDURE GenerateBill(
    IN p_AppointmentID INT,
    IN p_TotalAmount DECIMAL(10, 2)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- Rollback transaction in case of error
        ROLLBACK;
    END;

    START TRANSACTION;

    INSERT INTO Bills (AppointmentID, TotalAmount, DateOfPayment, PaymentStatus)
    VALUES (p_AppointmentID, p_TotalAmount, CURDATE(), 'Unpaid');

    COMMIT;
END //
DELIMITER ;

-- GENERATE BILL TRIGGER
DELIMITER //
CREATE TRIGGER GenerateBillAfterCompletion
AFTER UPDATE ON Appointments
FOR EACH ROW
BEGIN
    IF NEW.Status = 'Completed' THEN
        INSERT INTO Bills (AppointmentID, TotalAmount, DateOfPayment, PaymentStatus)
        VALUES (NEW.AppointmentID, 100.00, CURDATE(), 'Unpaid');
    END IF;
END //
DELIMITER ;

-- VIEWS
-- ALL APPOINTMENTS FOR A SPECIFIC DOCTOR
CREATE VIEW DoctorAppointments AS 
SELECT 
	a.AppointmentID, 
    d.Name as DoctorName, 
    p.Name as PatientName, 
    a.Date, 
    a.Time, 
    a.Status
FROM appointments a 
JOIN doctors d ON a.DoctorID = d.DoctorID
JOIN patients p ON a.PatientID = p.PatientID
WHERE d.DoctorID = 2;

-- ALL APPOINTMENTS FOR A SPECIFIC PATIENT
CREATE VIEW GetPatientAppointments AS
SELECT 
	a.AppointmentID,
	p.Name as PatientName,
    d.Name as DoctorName,
    a.Date,
    a.Time,
    a.Status
FROM appointments a
JOIN patients p ON a.patientID = p.patientID
JOIN doctors d ON a.DoctorID = d.DoctorID
WHERE p.patientID = 1;

-- COMPLEX QUERIES 

-- 1. List of all appointments scheduled for a specific doctor on a given day
-- SELECT a.AppointmentID, p.Name AS PatientName, a.Time, a.Status
-- FROM Appointments a
-- JOIN Patients p ON a.PatientID = p.PatientID
-- WHERE a.DoctorID = ? AND a.Date = ?;

-- 2. Total revenue generated by a specific doctor during a given month
-- SELECT d.Name AS DoctorName, SUM(b.TotalAmount) AS TotalRevenue
-- FROM Doctors d
-- JOIN Appointments a ON d.DoctorID = a.DoctorID
-- JOIN Bills b ON a.AppointmentID = b.AppointmentID
-- WHERE d.DoctorID = ? AND MONTH(a.Date) = ? AND YEAR(a.Date) = ?
-- GROUP BY d.DoctorID;

-- 3. Patients who have not paid their bills within 30 days
SELECT p.PatientID, p.Name, b.BillID, b.TotalAmount, b.DateOfPayment
FROM Patients p
JOIN Appointments a ON p.PatientID = a.PatientID
JOIN Bills b ON a.AppointmentID = b.AppointmentID
WHERE b.PaymentStatus = 'Unpaid' AND DATEDIFF(CURDATE(), b.DateOfPayment) >= 30;

-- 4. List of patients who have had more than 3 appointments in the past 6 months
SELECT p.PatientID, p.Name, COUNT(a.AppointmentID) AS AppointmentCount
FROM Patients p
JOIN Appointments a ON p.PatientID = a.PatientID
WHERE a.Date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
GROUP BY p.PatientID
HAVING AppointmentCount > 3;

-- 5. Most popular doctor specialties based on appointment volume
SELECT d.Specialty, COUNT(a.AppointmentID) AS AppointmentCount
FROM Doctors d
JOIN Appointments a ON d.DoctorID = a.DoctorID
GROUP BY d.Specialty
ORDER BY AppointmentCount DESC
LIMIT 5;


