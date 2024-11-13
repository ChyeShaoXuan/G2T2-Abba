"use client";

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Loading from "@/components/ui/loading";

interface WorkerDetails {
    name: string;
    phoneNumber: string;
    shortBio: string;
    available: string;
}

interface NearestWorkerProps {
    worker: WorkerDetails;
}

const NearestWorker: React.FC<NearestWorkerProps> = ({ worker }) => {
    return (
        <div>
            <h1>Nearest Worker Details</h1>
            {worker ? (
                <div style={{ border: '1px solid #ddd', padding: '20px', borderRadius: '8px', maxWidth: '400px', margin: '0 auto' }}>
                    <p><strong>Name:</strong> {worker.name}</p>
                    <p><strong>Phone:</strong> {worker.phoneNumber}</p>
                    <p><strong>Short Bio:</strong> {worker.shortBio}</p>
                    <p><strong>Available:</strong> {worker.available === "true" ? "Yes" : "No"}</p>
                </div>
            ) : (
                <p>No worker found for the specified criteria.</p>
            )}
        </div>
    );
};

export default NearestWorker;
