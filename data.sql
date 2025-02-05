-- Insert data

INSERT INTO admin (adminId, name, isRoot, emailId) VALUES
(1, 'John Doe', true, "is442g4t2@gmail.com"),
(2, 'Jane Smith', false, "randomEmail"),
(3, 'Michael Brown', false, "randomEmail"),
(4, 'Alice Johnson', false, "randomEmail");

INSERT INTO worker (WorkerId, adminId, name, phoneNumber, shortBio, deployed, emailId, curPropertyId, available, worker_hours_in_week) VALUES
(1, 1, 'John Smith', '+6512345678', 'Experienced cleaner, pet-friendly', true, 'akannappan27@gmail.com', 100, True, 20),
(2, 1, 'Mary Tan', '+6598765432', 'Specializes in disinfection treatment', false, '@marytan', 100, True, 44),
(3, 1, 'Ahmed Ali', '+6581234567', 'No pets due to allergies', true, '@ahmedali', 100, False, 40),
(4, 1, 'Susan Lee', '+6587654321', 'Background in carpet shampooing', false, '@susanlee', 100, False, 30);


INSERT INTO cleaningpackage (packageId, packageType, price, hours, hourly_rate, property_details, pax, manualBookingRequired, propertyType) VALUES
(1, 'Weekly', 200, 5, 40, '2-bedroom apartment', 2, false, 'Hdb'),
(2, 'BiWeekly', 350, 10, 35, '3-bedroom house with garden', 3, false, 'Condominium'),
(3, 'Weekly', 150, 3, 50, 'Studio apartment', 1, false, 'Landed'),
(4, 'BiWeekly', 500, 15, 33, '4-bedroom family home', 6, true, 'Hdb');

-- To replace 0 with null for price and hourly_rate after changing entity
INSERT INTO cleaningpackage (packageId, packageType, propertyType, price, hours, hourly_rate, pax, manualBookingRequired, property_details) VALUES
-- Weekly
-- HDB Packages
(5, 'Weekly', 'Hdb', 0, 2, 0, 0, true, '2 Bedrooms and Below'),
(6, 'Weekly', 'Hdb', 276, 3, 23, 1, false, '3-Room'),
(7, 'Weekly', 'Hdb', 336, 4, 21, 1, false, '4-Room'),
(8, 'Weekly', 'Hdb', 0, 5, 0, 0, true, '5 Bedrooms or More'),

-- Condominium Packages
(9, 'Weekly', 'Condominium', 276, 3, 23, 1, false, '2 Bedroom and below'),
(10, 'Weekly', 'Condominium', 336, 4, 21, 1, false, '3 Bedroom'),
(11, 'Weekly', 'Condominium', 0, 5, 0, 0, true, '4 Bedrooms or More'),

-- Night Shift Packages for Condominium
(12, 'Weekly', 'Condominium', 0, 2.5, 19, 1, true, '2-3 Bedroom Night Shift (After 7 pm)'),
(13, 'Weekly', 'Condominium', 0, 3, 19, 1, true, '2-3 Bedroom Night Shift (After 6 pm)'),
(14, 'Weekly', 'Condominium', 0, 4, 18, 1, true, '2-3 Bedroom Night Shift (After 6 pm)');

-- Insert Bi-weekly packages into the cleaningpackage table
INSERT INTO cleaningpackage (packageId, packageType, price, hours, hourly_rate, property_details, pax, manualBookingRequired, propertyType) VALUES
(15, 'BiWeekly', 138.00, 3, 23, 'Promotion Bi-weekly - 3 hours', 1, false, 'Hdb'),
(16, 'BiWeekly', 168.00, 4, 21, 'Promotion Bi-weekly - 4 hours', 1, false, 'Hdb'),
(17, 'BiWeekly', 0, 5, 0, 'Promotion Bi-weekly - 5 hours, contact sales', 1, true, 'Hdb');

-- Insert Landed property information for manual booking
INSERT INTO cleaningpackage (packageId, packageType, price, hours, hourly_rate, property_details, pax, manualBookingRequired, propertyType) VALUES
(18, 'BiWeekly', 0, 0, 0, 'Landed property cleaning, contact sales', 0, true, 'Landed');



INSERT INTO client (clientId, name, phoneNumber, email, adminId, packageId, workerId) VALUES
(1, 'John Doe', '123-456-7890', 'johndoe@example.com', 1,1,1),
(2, 'Jane Smith', '987-654-3210', 'janesmith@example.com', 2,1,1),
(3, 'Bob Johnson', '555-0123', 'bobjohnson@example.com', 1,1,1),
(4, 'Alice Williams', '444-5678', 'alicewilliams@example.com', 3,1,1);


INSERT INTO Property (propertyId, clientId, packageId, address, latitude, longitude, numberOfRooms) VALUES
(100, 1, 1, "71 Ubi Rd 1, #10-42, Singapore 408732", 1.3323483, 103.8897642, 1),
(101, 1, 1, 'Pasir Ris Sports Centre, 120 Pasir Ris Central, Singapore 519640', 1.3741178512573242, 103.9515151977539,2),
(102, 2, 2, 'Boon Keng Ville, Block 17 Upper Boon Keng Rd, Boon Keng Ville, Singapore 380017', 1.3149851560592651, 103.87104797363281,3),
(103, 3, 1, 'Woodlands Civic Centre, Block 900 South Woodlands Dr, Woodlands Civic Centre, Singapore 730900', 1.4352025985717773, 103.78697204589844,4),
(104, 4, 2, '6 Holland Cl, Singapore', 1.3076527118682861, 103.79556274414062,5),
(105, 1, 1, '81 Victoria St, Singapore 188065', 1.2963, 103.8502,5),
(106, 2, 1, '13 Holland Dr, Singapore', 1.3093297481536865, 103.79297637939453,3);




INSERT INTO cleaningtask (taskId, propertyId, workerId, feedbackId, shift, status, date, Acknowledged) VALUES
(1, 101, 1, NULL, 'Morning', 'Scheduled', '2024-11-15', false),
(2, 102, 2, NULL, 'Afternoon', 'InProgress', '2024-11-16', true),
(3, 103, 3, NULL, 'Evening', 'Cancelled', '2024-11-15', false),
(4, 104, 1, NULL, 'Morning', 'Completed', '2024-11-15', true);

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


INSERT INTO worker_hours_stats (workerHoursId, workerId, monthYear, totalHoursWorked, overtimeHours) VALUES
(1, 1, '2024-01', 160, 10),
(2, 2, '2024-01', 150, 5),
(3, 1, '2024-02', 170, 15),
(4, 3, '2024-01', 180, 20),
(5, 2, '2024-02', 160, 0);