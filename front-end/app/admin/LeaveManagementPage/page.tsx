import LeaveRequestReview from "@/components/admin/LeaveRequestReview/LeaveRequestReview";

export default function LeaveManagementPage() {
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