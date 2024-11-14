'use client'

import { useState, useEffect } from 'react'
import ViewAdmins from "@/components/admin/ViewAdmins/ViewAdmins"
import Navbar from "@/components/ui/adminpagesnavbar"

export default function ViewAdminsPage() {

  return (
    <div>
      <Navbar />
      <ViewAdmins />
    </div>
  )
}