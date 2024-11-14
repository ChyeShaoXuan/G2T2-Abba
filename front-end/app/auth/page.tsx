'use client';

import { useGlobalState } from '@/context/StateContext';
import AdminAuth from '@/components/admin/Auth/Auth';
import ClientAuth from '@/components/client/Auth/Auth';
import StaffAuth from '@/components/staff/Auth/Auth';
import { Button } from "@/components/ui/button";

export default function AuthPage() {
  const { userType, setUserType, userId, setUserId } = useGlobalState();

  // Handle user selection
  const handleUserSelection = (type: 'admin' | 'customer' | 'worker') => {
    setUserType(type);
    setUserId(generateUserId()); // Example: Generate a user ID or fetch it from a backend
  };

  // Example function to generate a user ID (replace with your actual logic)
  const generateUserId = () => Math.floor(Math.random() * 10000);

  if (!userType) {
    return (
      <div className="container mx-auto p-4 flex flex-col items-center space-y-4">
        <h1 className="text-2xl font-bold">Choose User Type</h1>
        <Button onClick={() => handleUserSelection('admin')}>Admin</Button>
        <Button onClick={() => handleUserSelection('customer')}>Customer</Button>
        <Button onClick={() => handleUserSelection('worker')}>Worker</Button>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4">
      <p>User Type: {userType}</p>
      <p>User ID: {userId}</p>
      {userType === 'admin' && <AdminAuth />}
      {userType === 'customer' && <ClientAuth />}
      {userType === 'worker' && <StaffAuth />}
      <Button onClick={() => setUserType(null)} className="mt-4">Back to User Selection</Button>
    </div>
  );
}
