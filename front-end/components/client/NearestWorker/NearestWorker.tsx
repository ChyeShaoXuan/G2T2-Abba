"use client";

import { useEffect, useState } from 'react';
import { getNearestWorker, Worker } from '@/utils/apiClient';
import { useRouter } from 'next/router';

export default function NearestWorkerPage() {
    const [worker, setWorker] = useState<Worker | null>(null);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    // Assuming propertyId, shift, and date are passed as query params
    const { propertyId, shift, date } = router.query;

    useEffect(() => {
        if (propertyId && shift && date) {
            getNearestWorker(Number(propertyId), String(shift), String(date))
                .then((workerData) => {
                    setWorker(workerData);
                    setLoading(false);
                })
                .catch(() => setLoading(false));
        }
    }, [propertyId, shift, date]);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1>Nearest Worker Details</h1>
            {worker ? (
                <div>
                    <p><strong>Name:</strong> {worker.name}</p>
                    <p><strong>Phone:</strong> {worker.phoneNumber}</p>
                    <p><strong>Short Bio:</strong> {worker.shortBio}</p>
                    <p><strong>Deployed:</strong> {worker.deployed ? "Yes" : "No"}</p>
                    <p><strong>Telegram ID:</strong> {worker.tele_Id}</p>
                    <p><strong>Current Property ID:</strong> {worker.curPropertyId}</p>
                    <p><strong>Available:</strong> {worker.available ? "Yes" : "No"}</p>
                    <p><strong>Admin ID:</strong> {worker.adminId}</p>
                    <p><strong>Weekly Hours:</strong> {worker.worker_hours_in_week}</p>
                </div>
            ) : (
                <p>No worker found for the specified criteria.</p>
            )}
        </div>
    );
}
