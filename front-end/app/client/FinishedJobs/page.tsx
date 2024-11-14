'use client'

import { useState, useEffect } from 'react'
import FinishedJobs from '@/components/client/FinishedJobs/FinishedJobs';
import NavigationBar from "@/components/ui/clientpagesnavbar"
import Loading from "@/components/ui/loading"

export default function FinishedJobsPage() {
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Simulate a loading delay
    const timer = setTimeout(() => {
      setLoading(false)
    }, 2000)

    return () => clearTimeout(timer)
  }, [])

  const clientId = 2;
  
  return (
      <div>
          {loading && <Loading />}
          <NavigationBar/>
          <FinishedJobs/>
      </div>
  );
};