import JobReallocationDisplay from "@/components/staff/AvailableJobs/AvailableJobs";
import UpcomingJobs from "@/components/staff/UpcomingJobs/UpcomingJobs";
import Navbar from "@/components/ui/workernavbar";

export default function JobNotifications() {
  return (
      <main className="container mx-auto">
        <Navbar/>
        <JobReallocationDisplay />
        <UpcomingJobs/>
      </main>
   
  )
}