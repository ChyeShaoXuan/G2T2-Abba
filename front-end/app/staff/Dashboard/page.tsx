// front-end/app/staff/Dashboard/page.tsx

"use client";

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useGlobalState } from '@/context/StateContext';
import Navbar from '@/components/ui/workernavbar';
import DashboardInfo from '@/components/staff/DashboardInfo/DashboardInfo';

const WorkerDashboard = () => {
    const router = useRouter();
    const { userId, setUserId } = useGlobalState();

    useEffect(() => {
        // Check if we have a userId in localStorage
        const storedUserId = localStorage.getItem('userId');
        
        if (!userId && !storedUserId) {
            // No userId in global state or localStorage, redirect to auth
            router.push('/auth');
        } else if (!userId && storedUserId) {
            // No userId in global state but found in localStorage, restore it
            setUserId(storedUserId);
        }
    }, [userId, setUserId, router]);

    if (!userId) {
        return null; // Or a loading spinner
    }

    return (
        <div>
            <Navbar/>
            <p className='ml-4'>Welcome to your Dashboard. Worker ID: {userId}</p>
            <DashboardInfo workerId={userId}/>
        </div>
    );
};

export default WorkerDashboard;