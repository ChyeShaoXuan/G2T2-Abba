'use client'

import { useState, useEffect } from 'react'
import ViewWorkers from "@/components/admin/ViewWorkers/ViewWorkers"
import Navbar from "@/components/ui/adminpagesnavbar"
import Loading from "@/components/ui/loading"

export default function ViewWorkersPage() {
  
  return (
    <div>
      <Navbar />
      <ViewWorkers />
    </div>
  )
}