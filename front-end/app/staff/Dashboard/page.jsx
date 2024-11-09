"use client";

import React, { useState } from 'react';
import Navbar from '@/components/ui/workernavbar';
import DashboardInfo from '@/components/staff/DashboardInfo/DashboardInfo';


const WorkerDashboard = () => {
    return (
        <div>
            <Navbar/>
            <p class='ml-4'>Welcome to the Worker Dashboard. Here you can view your tasks, update your status, and manage your work schedule.</p>
            <DashboardInfo/>
        </div>
    );
};

export default WorkerDashboard;