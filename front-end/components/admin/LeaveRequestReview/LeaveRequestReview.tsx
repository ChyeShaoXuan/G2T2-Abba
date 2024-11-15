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
            await axios.post(`http://localhost:8080/leave/approve/${leaveId}`, null, {
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

    const getStatusColor = (status: string | null | undefined) => {
        if (!status) return 'bg-gray-100 text-gray-800';
        
        switch (status.toLowerCase()) {
            case 'approved':
                return 'bg-green-100 text-green-800';
            case 'rejected':
                return 'bg-red-100 text-red-800';
            case 'pending':
                return 'bg-yellow-100 text-yellow-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-gray-900">Leave Applications</h1>
                {error && (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-2 rounded">
                        {error}
                    </div>
                )}
            </div>
            
            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Worker Name</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Leave Type</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Start Date</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">End Date</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                            {leaveApplications.map((leave) => (
                                <tr key={leave.leaveApplicationId} className="hover:bg-gray-50">
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                        {leave.workerName}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {leave.leaveType}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {new Date(leave.startDate).toLocaleDateString()}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {new Date(leave.endDate).toLocaleDateString()}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(leave.status)}`}>
                                            {leave.status}
                                        </span>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        {leave.status === 'Pending' && (
                                            <button
                                                onClick={() => handleApprove(leave.leaveApplicationId)}
                                                className="bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded text-sm"
                                            >
                                                Approve
                                            </button>
                                        )}
                                        {leave.leaveType === 'MC' && (
                                            <div className="inline-block">
                                                <input
                                                    type="file"
                                                    onChange={(e) => handleFileUpload(e, leave.leaveApplicationId)}
                                                    className="hidden"
                                                    id={`mc-upload-${leave.leaveApplicationId}`}
                                                    accept=".pdf,.jpg,.jpeg,.png"
                                                />
                                                <label
                                                    htmlFor={`mc-upload-${leave.leaveApplicationId}`}
                                                    className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded text-sm cursor-pointer"
                                                >
                                                    Upload MC
                                                </label>
                                            </div>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default LeaveRequestReview;