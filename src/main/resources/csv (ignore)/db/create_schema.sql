-- Create Clients Table
CREATE TABLE clients (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Create Workers Table
CREATE TABLE workers (
    worker_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    -- Add other worker fields as needed
);

-- Create Packages Table
CREATE TABLE packages (
    package_id INT AUTO_INCREMENT PRIMARY KEY,
    package_type ENUM('Weekly', 'BiWeekly') NOT NULL,
    price INT NOT NULL,
    hours INT NOT NULL,
    hourly_rate INT NOT NULL,
    property_details TEXT NOT NULL
);

-- Create Properties Table
CREATE TABLE properties (
    property_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    package_id INT,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
    FOREIGN KEY (package_id) REFERENCES packages(package_id) ON DELETE CASCADE
);

-- Create WorkerHours Table
CREATE TABLE worker_hours_stats (
    worker_hours_id INT AUTO_INCREMENT PRIMARY KEY,
    worker_id INT,
    month_year VARCHAR(7) NOT NULL, -- Format YYYY-MM
    total_hours_worked INT NOT NULL,
    overtime_hours INT NOT NULL,
    UNIQUE(worker_id, month_year),
    FOREIGN KEY (worker_id) REFERENCES workers(worker_id) ON DELETE CASCADE
);

-- Create LeaveStats Table
CREATE TABLE leave_stats (
    leave_stats_id INT AUTO_INCREMENT PRIMARY KEY,
    month_year VARCHAR(7) NOT NULL, -- Format YYYY-MM
    al_count INT NOT NULL,
    mc_count INT NOT NULL,
    hl_count INT NOT NULL,
    el_count INT NOT NULL,
    worker_id INT,
    FOREIGN KEY (worker_id) REFERENCES workers(worker_id) ON DELETE CASCADE
);

-- Create JobStats Table
CREATE TABLE job_stats (
    job_stats_id INT AUTO_INCREMENT PRIMARY KEY,
    month_year VARCHAR(7) NOT NULL, -- Format YYYY-MM
    total_hours INT NOT NULL,
    total_cleaning_tasks INT NOT NULL,
    total_clients INT NOT NULL,
    total_properties INT NOT NULL,
    total_workers INT NOT NULL,
    total_packages INT NOT NULL
);

-- Create Feedback Table
CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    rating INT NOT NULL,
    comment TEXT,
    cleaning_task_id INT,
    FOREIGN KEY (cleaning_task_id) REFERENCES cleaning_tasks(cleaning_task_id) ON DELETE CASCADE
);

-- Create LeaveApplication Table
CREATE TABLE leave_applications (
    leave_application_id INT AUTO_INCREMENT PRIMARY KEY,
    worker_id INT,
    admin_id INT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    leave_type ENUM('AL', 'MC', 'HL', 'EL') NOT NULL,
    submission_datetime DATETIME NOT NULL,
    FOREIGN KEY (worker_id) REFERENCES workers(worker_id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id) ON DELETE CASCADE
);

-- Create CleaningTasks Table (assuming this is a relevant entity)
CREATE TABLE cleaning_tasks (
    cleaning_task_id INT AUTO_INCREMENT PRIMARY KEY,
    property_id INT,
    task_description TEXT NOT NULL,
    task_date DATE NOT NULL,
    FOREIGN KEY (property_id) REFERENCES properties(property_id) ON DELETE CASCADE
);

-- Create Admins Table
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    -- Add other admin fields as needed
);
