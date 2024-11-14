'use client'

import { useState, useEffect } from 'react'
import ClientBooking from '@/components/client/ClientBooking/ClientBooking';
import NavigationBar from "@/components/ui/clientpagesnavbar"
import Loading from "@/components/ui/loading"
import { useGlobalState } from '@/context/StateContext';

export default function PlaceOrderPage() {
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Simulate a loading delay
    const timer = setTimeout(() => {
      setLoading(false)
    }, 2000)

    return () => clearTimeout(timer)
  }, [])

  // const { clientId } = useGlobalState();  
  // console.log(clientId);
  const clientId = 2;

  return (
      <div>
          {loading && <Loading />}
          <NavigationBar/>
          <ClientBooking clientId={clientId} />
      </div>
  );
};