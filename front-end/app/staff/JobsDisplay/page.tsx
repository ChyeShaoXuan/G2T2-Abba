import JobReallocationDisplay from "@/components/staff/AvailableJobs/AvailableJobs";
import UpcomingJobs from "@/components/staff/UpcomingJobs/UpcomingJobs";

export default function JobNotifications() {
  return (
    <div className="min-h-screen bg-background">
      <header className="bg-primary text-primary-foreground py-4">
        <div className="container mx-auto">
          <h1 className="text-2xl font-bold">Job Displays</h1>
        </div>
      </header>
      <main className="container mx-auto py-6">
        <JobReallocationDisplay />
        <UpcomingJobs/>
      </main>
    </div>
  )
}