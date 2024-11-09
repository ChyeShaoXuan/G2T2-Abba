"use client";

import React, { useState } from 'react';
import { placeOrder, PlaceOrderRequestDTO, CleaningTaskDTO } from '@/utils/apiClient';

interface ClientBookingProps {
    clientId: number;
}

const ClientBooking: React.FC<ClientBookingProps> = ({ clientId }) => {
    const [formData, setFormData] = useState<PlaceOrderRequestDTO>({
        packageType: '',
        propertyType: '',
        numberOfRooms: undefined,
        shift: '',
        date: '',
        preferredWorkerId: undefined,
    });

    const [result, setResult] = useState<CleaningTaskDTO | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: name === 'numberOfRooms' ? parseInt(value) : value,
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await placeOrder(clientId, formData);
            setResult(response);
            setError(null);
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
                {/* <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Package Type:
                    <input type="text" name="packageType" value={formData.packageType || ''} onChange={handleChange} required />
                </label>
                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Property Type:
                    <input type="text" name="propertyType" value={formData.propertyType} onChange={handleChange} required />
                </label> */}
                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Package Type:
                    <select name="packageType" value={formData.packageType} onChange={handleChange} required>
                        <option value="Weekly">Weekly</option>
                        <option value="BiWeekly">BiWeekly</option>
                    </select>
                </label>

                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Property Type:
                <select name="propertyType" value={formData.propertyType} onChange={handleChange} required>
                    <option value="Hdb">HDB</option>
                    <option value="Condominium">Condominium</option>
                    <option value="Landed">Landed</option>
                </select>
                </label>

                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Number of Rooms:
                    <input type="number" name="numberOfRooms" value={formData.numberOfRooms || ''} onChange={handleChange} required />
                </label>
                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Shift:
                    <select name="shift" value={formData.shift} onChange={handleChange} required>
                        <option value="Morning">Morning</option>
                        <option value="Afternoon">Afternoon</option>
                        <option value="Evening">Evening</option>
                    </select>
                </label>
                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Date:
                    <input type="date" name="date" value={formData.date} onChange={handleChange} required />
                </label>
                <label style={{ display: 'flex', flexDirection: 'column' }}>
                    Worker ID (optional):
                    <input type="number" name="preferredWorkerId" value={formData.preferredWorkerId || ''} onChange={handleChange} />
                </label>
                <button type="submit" style={{ padding: '10px', backgroundColor: '#007bff', color: '#fff', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
                    Place Order
                </button>
            </form>

            {result && (
                <div style={{ marginTop: '20px', textAlign: 'center' }}>
                    <h3>Order Placed Successfully</h3>
                    <p>Task ID: {result.taskId}</p>
                    <p>Property ID: {result.propertyId}</p>
                    <p>Shift: {result.shift}</p>
                    <p>Date: {result.date}</p>
                    <p>Acknowledged: {result.acknowledged ? 'Yes' : 'No'}</p>
                </div>
            )}

            {error && <p style={{ color: 'red', marginTop: '10px' }}>Error: {error}</p>}
        </div>
    );
};

export default ClientBooking;
