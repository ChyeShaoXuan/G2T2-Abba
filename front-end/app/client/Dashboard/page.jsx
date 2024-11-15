"use client";

import React from 'react';
import NavigationBar from "@/components/ui/clientpagesnavbar";

const ClientDashboard = () => {
    return (
        <div className="min-h-screen bg-gray-100">
            <NavigationBar />
            <div className="container mx-auto mt-12 p-4">
                <h1 className="text-3xl font-bold mb-4">Client Dashboard</h1>
                <p className="text-lg mb-6">Welcome to the Client Dashboard. Here you can make bookings and give feedback on completed jobs.</p>
                
            </div>
        </div>
    );
};

export default ClientDashboard;