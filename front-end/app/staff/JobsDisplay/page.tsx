"use client";
import FinishedJobs from "@/components/staff/FinishedJobs/FinishedJobs";
import UpcomingJobs from "@/components/staff/UpcomingJobs/UpcomingJobs";
import Navbar from "@/components/ui/workernavbar";
import { useRouter } from 'next/navigation';
import { useGlobalState } from '@/context/StateContext';
import { useEffect } from 'react';

export default function JobNotifications() {
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
    <div className="min-h-screen bg-background">
    
      <main className="container mx-auto">
      <p className='ml-4'>Welcome to your Dashboard. Worker ID: {userId}</p>
        <Navbar/>
        <UpcomingJobs workerId={userId}/>
        <FinishedJobs workerId={userId}/>
      </main>
    </div>
  )
}