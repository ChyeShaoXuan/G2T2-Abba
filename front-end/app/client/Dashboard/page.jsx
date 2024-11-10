"use client";

import React from 'react';
import NavigationBar from "@/components/ui/clientpagesnavbar"

const ClientDashboard = () => {
    return (
        <div>
            <NavigationBar />
            <div style={{ marginTop: '50px' }}>
                <h1>Client Dashboard</h1>
                <p>Welcome to the Client Dashboard. Here you can view your orders, track shipments, and manage your account.</p>
            </div>
        </div>
    );
};

export default ClientDashboard;