"use client";

import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import NearestWorker from '@/components/client/NearestWorker/NearestWorker';
import Loading from '@/components/ui/loading';

interface WorkerDetails {
    name: string;
    phoneNumber: string;
    shortBio: string;
    available: string;
}

export default function NearestWorkerPage() {
    const [worker, setWorker] = useState<WorkerDetails | null>(null);
    const searchParams = useSearchParams();

    useEffect(() => {
        // Extract query params from the URL
        const name = searchParams.get("name");
        const phoneNumber = searchParams.get("phoneNumber");
        const shortBio = searchParams.get("shortBio");
        const available = searchParams.get("available");

        // Set worker details if all necessary params are present
        if (name && phoneNumber && shortBio && available) {
            setWorker({
                name,
                phoneNumber,
                shortBio,
                available,
            });
        }
    }, [searchParams]);

    if (!worker) {
        return <Loading />;
    }

    return <NearestWorker worker={worker} />;
}
