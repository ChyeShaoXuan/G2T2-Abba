'use client'

import { useState, useEffect } from 'react'
import JobStatisticsDashboard from "@/components/admin/JobStatisticsDashboard/JobStatisticsDashboard"
import Navbar from "@/components/ui/adminpagesnavbar"
import Loading from "@/components/ui/loading"

export default function JobStatisticsPage() {
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
      <JobStatisticsDashboard />
    </div>
  )
}