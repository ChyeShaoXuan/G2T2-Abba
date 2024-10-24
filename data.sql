-- Insert data

INSERT INTO admin (adminId, name, isRoot) VALUES
(1, 'John Doe', true),
(2, 'Jane Smith', false),
(3, 'Michael Brown', false),
(4, 'Alice Johnson', false);

INSERT INTO worker (worker_id, adminId, name, phoneNumber, shortBio, deployed, tele_Id, curPropertyId, available) VALUES
(1, 1, 'John Smith', '+6512345678', 'Experienced cleaner, pet-friendly', true, '@johnsmith', 101, True),
(2, 1, 'Mary Tan', '+6598765432', 'Specializes in disinfection treatment', false, '@marytan', 0, True),
(3, 1, 'Ahmed Ali', '+6581234567', 'No pets due to allergies', true, '@ahmedali', 103, False),
(4, 1, 'Susan Lee', '+6587654321', 'Background in carpet shampooing', false, '@susanlee', 0, False);

INSERT INTO client (clientId, name, phoneNumber, email, workerId) VALUES
(1, 'John Doe', '123-456-7890', 'johndoe@example.com', 1),
(2, 'Jane Smith', '987-654-3210', 'janesmith@example.com', 2),
(3, 'Bob Johnson', '555-0123', 'bobjohnson@example.com', 1),
(4, 'Alice Williams', '444-5678', 'alicewilliams@example.com', 3);

INSERT INTO cleaningpackage (packageId, packageType, price, hours, hourly_rate, property_details) VALUES
(1, 'Weekly', 200, 5, 40, '2-bedroom apartment'),
(2, 'BiWeekly', 350, 10, 35, '3-bedroom house with garden'),
(3, 'Weekly', 150, 3, 50, 'Studio apartment'),
(4, 'BiWeekly', 500, 15, 33, '4-bedroom family home');

INSERT INTO Property (propertyId, clientId, packageId, address, latitude, longitude) VALUES
(101, 1, 1, '123 Street', 1.12345, 103.12345),
(102, 2, 2, '456 Avenue', 1.23456, 103.23456),
(103, 3, 3, '789 Road', 1.34567, 103.34567),
(104, 4, 4, '101 Boulevard', 1.45678, 103.45678);


INSERT INTO cleaningtask (taskId, propertyId, workerId, feedbackId, shift, status, date, Acknowledged) VALUES
(1, 101, 1, NULL, 'Morning', 'Scheduled', '2024-10-15', false),
(2, 102, 2, NULL, 'Afternoon', 'InProgress', '2024-10-16', true),
(3, 103, 3, NULL, 'Evening', 'Cancelled', '2024-10-17', false),
(4, 104, 1, NULL, 'Morning', 'Completed', '2024-10-18', true);

INSERT INTO feedback (feedbackId, rating, comment) VALUES
(1, 5, 'Great job, very satisfied!'),
(2, 4, 'Good service but could improve on timekeeping.'),
(3, 3, 'Average service, not what I expected.'),
(4, 5, 'Excellent work, will hire again!');

INSERT INTO jobstats (monthYear, totalHours, totalCleaningTasks, totalClients, totalProperties, totalWorkers, totalPackages) VALUES
('10-2024', 160, 80, 20, 15, 10, 5),
('09-2024', 150, 75, 18, 14, 9, 6),
('08-2024', 170, 85, 22, 16, 12, 8),
('07-2024', 180, 90, 25, 20, 15, 10);

INSERT INTO leaveapplication (leaveApplicationId, workerId, adminId, startDate, endDate, leaveType, status, submissionDateTime, mcDocumentSubmitted) 
VALUES 
(1, 1, 1, '2024-10-01', '2024-10-05', 'MC', 'Pending', '2024-09-30T12:00:00', TRUE),
(2, 2, 1, '2024-10-10', '2024-10-12', 'AL', 'Approved', '2024-10-01T14:30:00', FALSE),
(3, 3, 2, '2024-10-15', '2024-10-20', 'HL', 'Rejected', '2024-10-02T09:00:00', FALSE),
(4, 1, 2, '2024-10-22', '2024-10-24', 'EL', 'Pending', '2024-10-15T10:15:00', FALSE);


INSERT INTO leave_stats (monthYear, alCount, mcCount, hlCount, elCount, workerId) VALUES
('2024-11', 2, 1, 0, 1, 1),
('2024-10', 3, 0, 0, 0, 1),
('2024-09', 1, 2, 1, 0, 2),
('2024-08', 2, 0, 1, 1, 2);


INSERT INTO worker_hours_stats (workerHoursId, worker_id, monthYear, totalHoursWorked, overtimeHours) VALUES
(1, 1, '2024-01', 160, 10),
(2, 2, '2024-01', 150, 5),
(3, 1, '2024-02', 170, 15),
(4, 3, '2024-01', 180, 20),
(5, 2, '2024-02', 160, 0);
