import ClientBooking from '@/components/client/ClientBooking/ClientBooking'

export default function BookingPage() {
  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Book a Cleaning Service</h1>
      <ClientBooking />
    </div>
  )
}