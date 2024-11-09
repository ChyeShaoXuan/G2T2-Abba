"use client";

import React from 'react';

import Link from 'next/link'

const Navbar = () => {
  return (
    <nav className="bg-gray-800 p-4">
      <div className="container mx-auto flex justify-between items-center">
        <div className="text-white text-lg font-bold">ABBA Client Console</div>
        <div className="flex space-x-4">
          <Link href="/client/Dashboard" className="text-white hover:text-gray-400">Dashboard</Link>
          <Link href="/client/Booking" className="text-white hover:text-gray-400">Booking</Link>
        </div>
      </div>
    </nav>
  )
}

export default Navbar