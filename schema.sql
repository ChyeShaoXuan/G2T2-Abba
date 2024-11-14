-- Create tables

CREATE TABLE IF NOT EXISTS worker (
    workerId INT PRIMARY KEY,
    adminId INT,
    name VARCHAR(255),
    phoneNumber VARCHAR(50),
    shortBio VARCHAR(255),
    deployed BOOLEAN,
    tele_Id VARCHAR(50),
    curPropertyId INT,
    available BOOLEAN,
    userId INT,
    FOREIGN KEY (userId) REFERENCES user(userId),
    FOREIGN KEY (adminId) REFERENCES admin(adminId)
);

CREATE TABLE IF NOT EXISTS admin (
    adminId INT PRIMARY KEY,
    name VARCHAR(255),
    isRoot BOOLEAN,
    userId INT,
    FOREIGN KEY (userId) REFERENCES user(userId)
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
    workerId INT,
    propertyId INT,
    adminId INT,
    packageId INT PRIMARY KEY,
    userId INT,
    FOREIGN KEY (userId) REFERENCES user(userId),
    FOREIGN KEY (workerId) REFERENCES worker(workerId),
    FOREIGN KEY (adminId) REFERENCES admin(adminId),
    FOREIGN KEY (packageId) REFERENCES cleaning_package(packageId)
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
    propertyType VARCHAR(50),
    price DECIMAL(10, 2),
    hours DECIMAL(10, 2),
    hourly_rate DECIMAL(10, 2),
    property_details VARCHAR(255),
    pax INT,
    manualBookingRequired BOOLEAN
);


CREATE TABLE IF NOT EXISTS property (
    propertyId INT PRIMARY KEY,
    clientId INT,
    packageId INT,
    address VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    numberOfRooms INT
);

CREATE TABLE IF NOT EXISTS workerhours (
    workerHoursId INT PRIMARY KEY,
    workerId INT,
    monthYear VARCHAR(7),  -- Format YYYY-MM
    totalHoursWorked INT,
    overtimeHours INT
);

CREATE TABLE IF NOT EXISTS user (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL, 
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(50) NOT NULL,
    role ENUM('Admin', 'Worker', 'Client') NOT NULL,
    twoFactorToken VARCHAR(255),
    tokenExpiryTime DATETIME,
    isVerified BOOLEAN
)

CREATE TABLE IF NOT EXISTS role (
    roleId INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(userId),
    FOREIGN KEY (role_id) REFERENCES role(roleId)
);