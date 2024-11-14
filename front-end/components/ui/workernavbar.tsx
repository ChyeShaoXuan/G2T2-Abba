"use client";

import Link from 'next/link';
// import { useAuth } from '../../app/context/useAuth'; // Adjust the path as necessary
import { useGlobalState } from '@/context/StateContext';
import { useRouter } from 'next/navigation';

const Navbar = () => {
  const { logout } = useGlobalState();
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  return (
    <nav className="bg-gray-800 p-4">
      <div className="container mx-auto flex justify-between items-center">
        <div className="text-white text-lg font-bold">ABBA Worker Console</div>
        <div className="flex space-x-4">
          <Link href="/staff/JobsDisplay" className="text-white hover:text-gray-400">Jobs Display</Link>
          <Link href="/staff/LeaveApplication" className="text-white hover:text-gray-400">Leave Application</Link>
          <button onClick={handleLogout} className="text-white hover:text-gray-400">Logout</button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;