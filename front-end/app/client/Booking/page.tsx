import ClientBooking from '@/components/client/ClientBooking/ClientBooking';
// import Navbar from "@/components/ui/adminpagesnavbar";

export default function PlaceOrderPage() {
  const clientId = 1; // Example client ID; replace with dynamic ID if needed

  return (
      <div>
          {/* <Navbar/> */}
          <h1>Place Cleaning Order</h1>
          <ClientBooking clientId={clientId} />
      </div>
  );
};