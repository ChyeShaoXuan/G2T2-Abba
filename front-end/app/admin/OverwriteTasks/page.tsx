'use client'

import { useState, useEffect } from 'react'
import OverwriteTasks from "@/components/admin/OverwriteTasks/OverwriteTasks"
import Navbar from "@/components/ui/adminpagesnavbar"
import Loading from "@/components/ui/loading"

export default function OverwriteTasksPage() {

  return (
    <div>
      <Navbar />
      <OverwriteTasks />
    </div>
  )
}