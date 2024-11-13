"use client";

import React, { useState } from 'react';
import Navbar from '@/components/ui/workernavbar';
import DashboardInfo from '@/components/staff/DashboardInfo/DashboardInfo';
import FinishedJobs from '@/components/staff/FinishedJobs/FinishedJobs';


const WorkerDashboard = () => {
    return (
        <div>
            <Navbar/>
            <p className='ml-4'>Welcome to your Dashboard. Here you can view your profile, tasks, and manage your work schedule.</p>
            <DashboardInfo/>
        </div>
    );
};

export default WorkerDashboard;