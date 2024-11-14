import FinishedJobs from "@/components/staff/FinishedJobs/FinishedJobs";
import UpcomingJobs from "@/components/staff/UpcomingJobs/UpcomingJobs";
import Navbar from "@/components/ui/workernavbar";

export default function JobNotifications() {
  return (
    <div className="min-h-screen bg-background">
    
      <main className="container mx-auto">
        <Navbar/>
        <UpcomingJobs/>
        <FinishedJobs/>
      </main>
    </div>
  )
}