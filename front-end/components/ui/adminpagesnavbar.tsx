"use client"

import Link from 'next/link';
import { useAuth } from '../../app/context/useAuth'; // Adjust the path as necessary
import { useRouter } from 'next/navigation';

const Navbar = () => {
  const { logout } = useAuth();
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  return (
    <nav className="bg-gray-800 p-4">
      <div className="container mx-auto flex justify-between items-center">
        <div className="text-white text-lg font-bold">ABBA Admin Console</div>
        <div className="flex space-x-4">
          <Link href="/admin/ViewClients" className="text-white hover:text-gray-400">View Clients</Link>
          <Link href="/admin/ViewWorkers" className="text-white hover:text-gray-400">View Workers</Link>
          <Link href="/admin/OverwriteTasks" className="text-white hover:text-gray-400">Overwrite Tasks</Link>
          <Link href="/admin/JobStatisticsDashboard" className="text-white hover:text-gray-400">Job Statistics</Link>
          <button onClick={handleLogout} className="text-white hover:text-gray-400">Logout</button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;