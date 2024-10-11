-- Import Clients Data
LOAD DATA INFILE '/path/to/your/csv/client.csv'
INTO TABLE clients
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;  -- To skip the header line

-- Import Workers Data
LOAD DATA INFILE '/path/to/your/csv/worker.csv'
INTO TABLE workers
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Packages Data
LOAD DATA INFILE '/path/to/your/csv/package.csv'
INTO TABLE packages
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Properties Data
LOAD DATA INFILE '/path/to/your/csv/property.csv'
INTO TABLE properties
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Worker Hours Data
LOAD DATA INFILE '/path/to/your/csv/workerhours.csv'
INTO TABLE worker_hours_stats
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Leave Stats Data
LOAD DATA INFILE '/path/to/your/csv/leavestats.csv'
INTO TABLE leave_stats
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Job Stats Data
LOAD DATA INFILE '/path/to/your/csv/jobstats.csv'
INTO TABLE job_stats
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Feedback Data
LOAD DATA INFILE '/path/to/your/csv/feedback.csv'
INTO TABLE feedback
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Leave Applications Data
LOAD DATA INFILE '/path/to/your/csv/leaveapplication.csv'
INTO TABLE leave_applications
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Cleaning Tasks Data
LOAD DATA INFILE '/path/to/your/csv/cleaningtask.csv'
INTO TABLE cleaning_tasks
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Import Admins Data
LOAD DATA INFILE '/path/to/your/csv/admin.csv'
INTO TABLE admins
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;
