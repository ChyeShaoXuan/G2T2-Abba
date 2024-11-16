'use client'

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import LeaveRequestReview from "@/components/admin/LeaveRequestReview/LeaveRequestReview";
import { useGlobalState } from "@/context/StateContext";
import Navbar from "@/components/ui/adminpagesnavbar";

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
    <div>
      <Navbar />
      <LeaveRequestReview />
    </div>
  )
}