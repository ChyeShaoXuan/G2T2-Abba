'use client'

import { useState, useEffect } from 'react'
import ViewWorkers from "@/components/admin/ViewWorkers/ViewWorkers"
import Navbar from "@/components/ui/adminpagesnavbar"
import Loading from "@/components/ui/loading"

export default function ViewWorkersPage() {
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Simulate a loading delay
    const timer = setTimeout(() => {
      setLoading(false)
    }, 2000)

    return () => clearTimeout(timer)
  }, [])

  return (
    <div>
      {loading && <Loading />}
      <Navbar />
      <ViewWorkers />
    </div>
  )
}