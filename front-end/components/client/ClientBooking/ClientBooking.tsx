"use client";

import React, { useState, useEffect } from 'react';
import { placeOrder, PlaceOrderRequestDTO, CleaningTaskDTO, getAvailableWorkers } from '@/utils/apiClient';
import { useRouter } from 'next/navigation';
import styles from './ClientBooking.module.css';
import Loading from "@/components/ui/loading";
import { useGlobalState } from '@/context/StateContext';

interface ClientBookingProps {
    clientId: number;
}

const ClientBooking: React.FC<ClientBookingProps> = ({ clientId }) => {
    const [formData, setFormData] = useState<PlaceOrderRequestDTO>({
        packageType: '',
        propertyType: '',
        numberOfRooms: 0,
        shift: '',
        date: '',
        preferredWorkerId: 0 as number | undefined,
    });

    const [workers, setWorkers] = useState<{ workerId: number; name: string }[]>([]);
    const [result, setResult] = useState<CleaningTaskDTO | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState(false);
    const router = useRouter();
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        // Fetch available workers when the component mounts
        async function fetchWorkers() {
          try {
            const workerData = await getAvailableWorkers();
            setWorkers(workerData);
          } catch (err) {
            console.error("Failed to fetch workers:", err);
          }
        }
    
        fetchWorkers();
      }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: name === 'numberOfRooms' ? parseInt(value) : value,
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (formData.numberOfRooms < 0) {
            setError("Number of rooms cannot be negative.");
            return;
        }

        if (formData.preferredWorkerId !== undefined && formData.preferredWorkerId < 0) {
            setError("Preferred Worker ID cannot be negative.");
            return;
        }
  
    //   setLoading(true);
        try {
            const response = await placeOrder(clientId, formData);
            // if (response.packageDetails && response.packageDetails.manualBookingRequired) {
            //     setError("Please contact the sales team for manual booking assistance.");
            //     setLoading(false);
            //     return;
            //   }
            setResult(response);
            setError(null);
            setSuccessMessage(true);

            // Start loading animation after displaying the success message briefly
            setTimeout(() => {
                setLoading(true);
                setSuccessMessage(false);
                setTimeout(() => {
                    const worker = response.worker;
                    if (worker) {
                        console.log(worker);
                        const query = new URLSearchParams({
                            // workerId: worker.workerId.toString(),
                            name: worker.name,
                            phoneNumber: worker.phoneNumber,
                            shortBio: worker.shortBio,
                            // deployed: worker.deployed.toString(),
                            // tele_Id: worker.tele_Id,
                            // curPropertyId: worker.curPropertyId.toString(),
                            available: worker.available.toString(),
                            // adminId: worker.adminId.toString(),
                            // worker_hours_in_week: worker.worker_hours_in_week.toString(),
                        }).toString();
                        console.log(query);
                        router.push(`/client/NearestWorker?name=${worker.name}&phoneNumber=${worker.phoneNumber}&shortBio=${worker.shortBio}&available=${worker.available}`);
                    } else {
                        throw new Error("No worker assigned to this task");
                    }
                }, 1000); 
            }, 1000); // Show success message for 1 second
        } catch (err) {
            if (err instanceof Error) {
                setError(err.message);
            } else {
                setError('An unknown error occurred');
            }
            setResult(null);
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '20px' }}>
            <h1 style={{ marginBottom: '20px' }}>Place Cleaning Order</h1>
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '300px' }}>
                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Package Type:
                    <select name="packageType" value={formData.packageType} onChange={handleChange} required>
                        <option value="" disabled>Select Package Type</option>
                        <option value="Weekly">Weekly</option>
                        <option value="BiWeekly">BiWeekly</option>
                    </select>
                </label>

                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Property Type:
                <select name="propertyType" value={formData.propertyType} onChange={handleChange} required>
                    <option value="" disabled>Select Property Type</option>
                    <option value="Hdb">HDB</option>
                    <option value="Condominium">Condominium</option>
                    <option value="Landed">Landed</option>
                </select>
                </label>

                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Number of Rooms:
                    <input type="number" name="numberOfRooms" value={formData.numberOfRooms || ''} onChange={handleChange} min="0" required />
                </label>
                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Shift:
                    <select name="shift" value={formData.shift} onChange={handleChange} required>
                        <option value="" disabled>Select Shift</option>
                        <option value="Morning">Morning</option>
                        <option value="Afternoon">Afternoon</option>
                        <option value="Evening">Evening</option>
                    </select>
                </label>
                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Date:
                    <input type="date" name="date" value={formData.date} onChange={handleChange} required />
                </label>
                {/* <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Worker ID (optional):
                    <input type="number" name="preferredWorkerId" value={formData.preferredWorkerId || ''} onChange={handleChange} />
                </label> */}
                <label>
                Preferred Worker:
                <select
                    name="preferredWorkerId"
                    value={formData.preferredWorkerId}
                    onChange={handleChange}
                >
                    <option value="0">Select Preferred Worker (Optional)</option>
                    {workers.map((worker) => (
                    <option key={worker.workerId} value={worker.workerId}>
                        {worker.name}
                    </option>
                    ))}
                </select>
                </label>
                <button type="submit" style={{ padding: '10px', backgroundColor: '#007bff', color: '#fff', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
                    Place Order
                </button>
            </form>

            {successMessage && (
                <div className={styles.successBanner}>
                    Order placed successfully!
                </div>
            )}

            {loading && <Loading />} {/* Show loading animation before redirecting */}

            {error && <p style={{ color: 'red', marginTop: '10px' }}>Error: {error}</p>}
        </div>
    );
};

export default ClientBooking;
