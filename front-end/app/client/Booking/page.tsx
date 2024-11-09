import ClientBooking from '@/components/client/ClientBooking/ClientBooking';
import NavigationBar from "@/components/ui/clientpagesnavbar"

export default function PlaceOrderPage() {
  const clientId = 1; // Example client ID; replace with dynamic ID if needed

  return (
      <div>
          <NavigationBar/>
          <h1>Place Cleaning Order</h1>
          <ClientBooking clientId={clientId} />
      </div>
  );
};