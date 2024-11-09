"use client";

import React from 'react';
import Link from 'next/link';
import './ClientsPageNavigationBar.css';

const NavigationBar = () => {
    return (
        <nav className="navigation">
            <ul>
                <li>
                    <Link href="/client/Dashboard">Dashboard</Link>
                </li>
                <li>
                    <Link href="/client/Booking">Booking</Link>
                </li>
            </ul>
        </nav>
    );
};

export default NavigationBar;