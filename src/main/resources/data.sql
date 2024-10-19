-- Create tables

CREATE TABLE IF NOT EXISTS worker (
    workerId INT PRIMARY KEY,
    supervisorId INT,
    name VARCHAR(255),
    phoneNumber VARCHAR(50),
    shortBio VARCHAR(255),
    deployed BOOLEAN,
    tele_Id VARCHAR(50),
    curPropertyId INT
);

CREATE TABLE IF NOT EXISTS admin (
    adminId INT PRIMARY KEY,
    name VARCHAR(255),
    isRoot BOOLEAN
);

CREATE TABLE IF NOT EXISTS cleaningtask (
    taskId INT PRIMARY KEY,
    propertyId INT,
    workerId INT,
    feedbackId INT,
    shift VARCHAR(50),
    status ENUM('Scheduled', 'InProgress', 'Cancelled', 'Completed'),
    date DATE,
    Acknowledged BOOLEAN
);

CREATE TABLE IF NOT EXISTS client (
    clientId INT PRIMARY KEY,
    name VARCHAR(255),
    phoneNumber VARCHAR(50),
    email VARCHAR(100),
    workerId INT
);

CREATE TABLE IF NOT EXISTS feedback (
    feedbackId INT PRIMARY KEY,
    cleaningTaskId INT,
    rating INT,
    comment TEXT
);

CREATE TABLE IF NOT EXISTS jobstats (
    monthYear VARCHAR(7) PRIMARY KEY,  -- Format MM-YYYY
    totalHours INT,
    totalCleaningTasks INT,
    totalClients INT,
    totalProperties INT,
    totalWorkers INT,
    totalPackages INT
);

CREATE TABLE IF NOT EXISTS leaveapplication (
    leaveApplicationId INT PRIMARY KEY,
    workerId INT,
    adminId INT,
    startDate DATE,
    endDate DATE,
    leaveType ENUM('MC', 'AL', 'HL', 'EL'),
    status ENUM('Pending', 'Approved', 'Rejected'),
    submissionDateTime DATETIME
);

CREATE TABLE IF NOT EXISTS leavestats (
    monthYear VARCHAR(7),  -- Format YYYY-MM
    alCount INT,
    mcCount INT,
    hlCount INT,
    elCount INT,
    workerId INT
);

CREATE TABLE IF NOT EXISTS cleaning_package (
    packageId INT PRIMARY KEY,
    packageType VARCHAR(50),
    price DECIMAL(10, 2),
    hours INT,
    hourly_rate DECIMAL(10, 2),
    property_details VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS property (
    propertyId INT PRIMARY KEY,
    clientId INT,
    packageId INT,
    address VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE
);

CREATE TABLE IF NOT EXISTS workerhours (
    workerHoursId INT PRIMARY KEY,
    workerId INT,
    monthYear VARCHAR(7),  -- Format YYYY-MM
    totalHoursWorked INT,
    overtimeHours INT
);

-- Insert data

INSERT INTO worker (workerId, supervisorId, name, phoneNumber, shortBio, deployed, tele_Id, curPropertyId) VALUES
(1, 1, 'John Smith', '+6512345678', 'Experienced cleaner, pet-friendly', true, '@johnsmith', 101),
(2, 1, 'Mary Tan', '+6598765432', 'Specializes in disinfection treatment', false, '@marytan', 0),
(3, 2, 'Ahmed Ali', '+6581234567', 'No pets due to allergies', true, '@ahmedali', 103),
(4, 2, 'Susan Lee', '+6587654321', 'Background in carpet shampooing', false, '@susanlee', 0);

INSERT INTO admin (adminId, name, isRoot) VALUES
(1, 'John Doe', true),
(2, 'Jane Smith', false),
(3, 'Michael Brown', false),
(4, 'Alice Johnson', false);

INSERT INTO cleaningtask (taskId, propertyId, workerId, feedbackId, shift, status, date, Acknowledged) VALUES
(1, 101, 1, NULL, 'Morning', 'Scheduled', '2024-10-15', false),
(2, 102, 2, NULL, 'Afternoon', 'InProgress', '2024-10-16', true),
(3, 103, 3, NULL, 'Evening', 'Cancelled', '2024-10-17', false),
(4, 104, 1, NULL, 'Morning', 'Completed', '2024-10-18', true);

INSERT INTO client (clientId, name, phoneNumber, email, workerId) VALUES
(1, 'John Doe', '123-456-7890', 'johndoe@example.com', 1),
(2, 'Jane Smith', '987-654-3210', 'janesmith@example.com', 2),
(3, 'Bob Johnson', '555-0123', 'bobjohnson@example.com', 1),
(4, 'Alice Williams', '444-5678', 'alicewilliams@example.com', 3);

INSERT INTO feedback (feedbackId, cleaningTaskId, rating, comment) VALUES
(1, 1, 5, 'Great job, very satisfied!'),
(2, 2, 4, 'Good service but could improve on timekeeping.'),
(3, 3, 3, 'Average service, not what I expected.'),
(4, 4, 5, 'Excellent work, will hire again!');

INSERT INTO jobstats (monthYear, totalHours, totalCleaningTasks, totalClients, totalProperties, totalWorkers, totalPackages) VALUES
('10-2024', 160, 80, 20, 15, 10, 5),
('09-2024', 150, 75, 18, 14, 9, 6),
('08-2024', 170, 85, 22, 16, 12, 8),
('07-2024', 180, 90, 25, 20, 15, 10);

INSERT INTO leaveapplication (leaveApplicationId, workerId, adminId, startDate, endDate, leaveType, status, submissionDateTime) VALUES
(1, 1, 1, '2024-10-01', '2024-10-05', 'MC', 'Pending', '2024-09-30T12:00:00'),
(2, 2, 1, '2024-10-10', '2024-10-12', 'AL', 'Approved', '2024-10-01T14:30:00'),
(3, 3, 2, '2024-10-15', '2024-10-20', 'HL', 'Rejected', '2024-10-02T09:00:00'),
(4, 1, 2, '2024-10-22', '2024-10-24', 'EL', 'Pending', '2024-10-15T10:15:00');

INSERT INTO leavestats (monthYear, alCount, mcCount, hlCount, elCount, workerId) VALUES
('2024-10', 2, 1, 0, 1, 1),
('2024-09', 3, 0, 0, 0, 1),
('2024-10', 1, 2, 1, 0, 2),
('2024-09', 2, 0, 1, 1, 2);

INSERT INTO cleaning_package (packageId, packageType, price, hours, hourly_rate, property_details) VALUES
(1, 'Weekly', 200, 5, 40, '2-bedroom apartment'),
(2, 'BiWeekly', 350, 10, 35, '3-bedroom house with garden'),
(3, 'Weekly', 150, 3, 50, 'Studio apartment'),
(4, 'BiWeekly', 500, 15, 33, '4-bedroom family home');

INSERT INTO property (propertyId, clientId, packageId, address, latitude, longitude) VALUES
(1, 1, 1, '123 Main St, Cityville', 40.7128, -74.0060),
(2, 2, 2, '456 Elm St, Townsville', 34.0522, -118.2437),
(3, 1, 1, '789 Maple Ave, Villageburg', 51.5074, -0.1278),
(4, 3, 2, '321 Oak Rd, Hamletton', 48.8566, 2.3522);

INSERT INTO workerhours (workerHoursId, workerId, monthYear, totalHoursWorked, overtimeHours) VALUES
(1, 1, '2024-01', 160, 10),
(2, 2, '2024-01', 150, 5),
(3, 1, '2024-02', 170, 15),
(4, 3, '2024-01', 180, 20),
(5, 2, '2024-02', 160, 0);
