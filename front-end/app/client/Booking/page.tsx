'use client'

import { useState, useEffect } from 'react'
import ClientBooking from '@/components/client/ClientBooking/ClientBooking';
import NavigationBar from "@/components/ui/clientpagesnavbar"
import Loading from "@/components/ui/loading"
import { useGlobalState } from '@/context/StateContext';
import { useRouter } from 'next/navigation';

export default function PlaceOrderPage() {
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Simulate a loading delay
    const timer = setTimeout(() => {
      setLoading(false)
    }, 2000)

    return () => clearTimeout(timer)
  }, [])

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
          {loading && <Loading />}
          <p className='ml-4'>Welcome to your Dashboard. Client ID: {userId}</p>
          <NavigationBar/>
          <ClientBooking clientId={userId} />
      </div>
  );
};