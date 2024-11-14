"use client";

import React from 'react';
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
        <div className="text-white text-lg font-bold">ABBA Client Console</div>
        <div className="flex space-x-4">
          <Link href="/client/Dashboard" className="text-white hover:text-gray-400">Dashboard</Link>
          <Link href="/client/Booking" className="text-white hover:text-gray-400">Booking</Link>
          <button onClick={handleLogout} className="text-white hover:text-gray-400">Logout</button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;