'use client';

import { useState, useEffect } from 'react';
import Navbar from "@/components/ui/adminpagesnavbar";
import Loading from "@/components/ui/loading";
import JobStatisticsDashboard from '@/components/admin/JobStatisticsDashboard/JobStatisticsDashboard'; 

function JobStatisticsPage() {
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false);
    }, 2000);

    return () => clearTimeout(timer);
  }, []);

  return (
    <div>
      {loading && <Loading />}
      <Navbar />
      <JobStatisticsDashboard />
    </div>
  );
}

export default JobStatisticsPage;