'use client'

import { useState } from 'react'
import AdminAuth from '@/components/admin/Auth/Auth'
import ClientAuth from '@/components/client/Auth/Auth'
import StaffAuth from '@/components/staff/Auth/Auth'
import { Button } from "@/components/ui/button"

export default function AuthPage() {
  const [userType, setUserType] = useState<'admin' | 'customer' | 'worker' | null>(null)

  if (!userType) {
    return (
      <div className="container mx-auto p-4 flex flex-col items-center space-y-4">
        <h1 className="text-2xl font-bold">Choose User Type</h1>
        <Button onClick={() => setUserType('admin')}>Admin</Button>
        <Button onClick={() => setUserType('customer')}>Customer</Button>
        <Button onClick={() => setUserType('worker')}>Worker</Button>
      </div>
    )
  }

  return (
    <div  className="container mx-auto p-4">
      {userType === 'admin' && <AdminAuth />}
      {userType === 'customer' && <ClientAuth />}
      {userType === 'worker' && <StaffAuth />}
      <Button onClick={() => setUserType(null)} className="mt-4">Back to User Selection</Button>
    </div>
  )
}