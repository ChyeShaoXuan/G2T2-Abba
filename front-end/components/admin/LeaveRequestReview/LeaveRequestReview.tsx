'use client'

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useGlobalState } from '@/context/StateContext';

interface LeaveApplicationDTO {
    leaveApplicationId: number;
    workerName: string;
    leaveType: string;
    startDate: string;
    endDate: string;
    status: string;
}

const LeaveRequestReview = () => {
    const [leaveApplications, setLeaveApplications] = useState<LeaveApplicationDTO[]>([]);
    const [selectedLeave, setSelectedLeave] = useState<LeaveApplicationDTO | null>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchLeaveApplications();
    }, []);

    const fetchLeaveApplications = async () => {
        try {
            const response = await axios.get('http://localhost:8080/leave/all', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
                }
            });
            if (Array.isArray(response.data)) {
                setLeaveApplications(response.data);
            } else {
                setError('Unexpected response format');
            }
        } catch (error) {
            console.error('Error fetching leave applications:', error);
            setError('Failed to fetch leave applications');
        }
    };

    const handleApprove = async (leaveId: number) => {
        try {
            await axios.put(`http://localhost:8080/leave/approve/${leaveId}`, null, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
                }
            });
            fetchLeaveApplications();
        } catch (error) {
            console.error('Error approving leave application:', error);
            setError('Failed to approve leave application');
        }
    };

    const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>, leaveId: number) => {
        const file = event.target.files?.[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('mcDocument', file);
        formData.append('leaveId', leaveId.toString());

        try {
            await axios.post('http://localhost:8080/leave/upload-mc', formData, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('jwtToken')}`,
                    'Content-Type': 'multipart/form-data'
                }
            });
            fetchLeaveApplications();
        } catch (error) {
            console.error('Error uploading MC document:', error);
            setError('Failed to upload MC document');
        }
    };

    return (
        <div>
            <h1>Admin Leave Management</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <table>
                <thead>
                    <tr>
                        <th>Worker Name</th>
                        <th>Leave Type</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {leaveApplications.map((leave) => (
                        <tr key={leave.leaveApplicationId}>
                            <td>{leave.workerName}</td>
                            <td>{leave.leaveType}</td>
                            <td>{leave.startDate}</td>
                            <td>{leave.endDate}</td>
                            <td>{leave.status}</td>
                            <td>
                                <button onClick={() => handleApprove(leave.leaveApplicationId)}>Approve</button>
                                <input type="file" onChange={(event) => handleFileUpload(event, leave.leaveApplicationId)} />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default LeaveRequestReview;