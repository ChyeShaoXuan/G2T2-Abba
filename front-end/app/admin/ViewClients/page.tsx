'use client'

import { useState, useEffect } from 'react'
import ViewClients from "@/components/admin/ViewClients/ViewClients"
import Navbar from "@/components/ui/adminpagesnavbar"
import Loading from "@/components/ui/loading"

export default function ViewClientsPage() {

  return (
    <div>
      <Navbar />
      <ViewClients />
    </div>
  )
}