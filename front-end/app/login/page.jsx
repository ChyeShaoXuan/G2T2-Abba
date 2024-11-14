"use client";

import React, { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';
import { useGlobalState } from '@/context/StateContext';

const LOGIN_URL = 'http://localhost:8080/authentication/login';

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isBlank, setIsBlank] = useState(false);
    const { login } = useGlobalState();
    const router = useRouter();

    const handleLogin = async (e) => {
        e.preventDefault();

        if (!username || !password) {
            setIsBlank(true);
            return;
        }

        setIsBlank(false);

        try {
            const response = await axios.post(LOGIN_URL, {
                username,
                password
            });

            if (response.status === 200) {
                // Store the complete login response in context
                login(response.data);
                const userId = await fetchUserId(response.data.username, response.data.role);
                login({ ...response.data, userId });

                // Store userId in localStorage
                localStorage.setItem('userId', userId);

                // Route based on role
                if (response.data.username === 'root' || response.data.roles.includes('ROLE_ADMIN')) {
                    router.push('/admin/JobStatisticsDashboard');
                } 
                else if (response.data.role === 'Client'){
                  router.push('/client/Dashboard');
                }
                else {
                    router.push('/staff/Dashboard');
                }
            }
        } catch (error) {
            console.error('Error logging in:', error);
        }
    };

    const fetchUserId = async (username, role) => {
        try {
            const response = await axios.get(`http://localhost:8080/worker/workerId/${username}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching user ID:', error);
            return null;
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Username"
            />
            <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
            />
            <button type="submit">Login</button>
            {isBlank && <p>Please fill in both fields.</p>}
        </form>
    );
};

export default LoginPage;