'use client';

import { useState, useEffect } from 'react';
import Navbar from "@/components/ui/adminpagesnavbar";
import Loading from "@/components/ui/loading";
import JobStatisticsDashboard from '@/components/admin/JobStatisticsDashboard/JobStatisticsDashboard'; 

function JobStatisticsPage() {

  return (
    <div>
      {/* <Navbar /> */}
      <JobStatisticsDashboard />
    </div>
  );
}

export default JobStatisticsPage;