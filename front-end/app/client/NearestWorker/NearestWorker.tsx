'use client'

import { useState, useEffect } from 'react'
import NearestWorker from '@/components/client/NearestWorker/NearestWorker';
import NavigationBar from "@/components/ui/clientpagesnavbar"
import Loading from "@/components/ui/loading"

export default function NearestWorkerPage() {
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false)
    }, 2000)

    return () => clearTimeout(timer)
  }, [])

  const clientId = 2; // Example client ID; replace with dynamic ID if needed

  return (
      <div>
          {loading && <Loading />}
          <NavigationBar/>
          <NearestWorker/>
      </div>
  );
};