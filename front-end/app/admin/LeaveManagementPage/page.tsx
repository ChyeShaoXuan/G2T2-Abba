'use client'

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import LeaveRequestReview from "@/components/admin/LeaveRequestReview/LeaveRequestReview";
import { useGlobalState } from "@/context/StateContext";

export default function LeaveManagementPage() {
  const router = useRouter();
  const { userType } = useGlobalState();

  useEffect(() => {
    // Check if user is not logged in or is not an admin
    if (!userType || userType !== 'admin') {
      router.push('/login');
    }
  }, [userType, router]);

  // If not admin, don't render the page content
  if (!userType || userType !== 'admin') {
    return null;
  }

  return (
    <div className="min-h-screen bg-background">
      <header className="bg-primary text-primary-foreground py-4">
        <div className="container mx-auto">
          <h1 className="text-2xl font-bold">Leave Management</h1>
        </div>
      </header>
      <main className="container mx-auto py-6">
        <LeaveRequestReview />
      </main>
    </div>
  )
}