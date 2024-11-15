"use client";

import React, { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';
import { useGlobalState } from '@/context/StateContext';

import styles from '../register/Register.module.css';

const LOGIN_URL = 'http://localhost:8080/authentication/login';

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isBlank, setIsBlank] = useState(false);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [loggedIn, setLoggedIn] = useState(false);
    const { login } = useGlobalState();
    const router = useRouter();

    const handleLogin = async (e) => {
        e.preventDefault();

        setIsSubmitted(true);

        if (!username || !password) {
            setIsBlank(true);
            setLoggedIn(false);
            return;
        }

        setIsBlank(false);

        try {
            const response = await axios.post(LOGIN_URL, {
                username,
                password
            });

            if (response.status === 200) {
                const userId = await fetchUserId(response.data.username, response.data.role);
                
                // Set role to 'admin' if user has ROLE_ADMIN
                const role = response.data.roles.includes('ROLE_ADMIN') ? 'admin' : response.data.role;
                
                // Call login only once with all data
                login({
                    ...response.data,
                    userId,
                    role
                });

                // Store userId in localStorage
                localStorage.setItem('userId', userId);

                // Set loggedIn state
                setLoggedIn(true);

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
            } else {
                setLoggedIn(false);
            }
        } catch (error) {
            console.error('Error logging in:', error);
            setLoggedIn(false);
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
            <div className={styles['input-container']}>
                <div className={`${styles['form-container']} ${styles.username}`}>
                    <input
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Username"
                    />
                    <label htmlFor="username" className={styles['form-label']}>
                        Username:
                    </label>
                </div>

                <div className={`${styles['form-container']} ${styles.password}`}>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Password"
                    />
                    <label htmlFor="password" className={styles['form-label']}>
                        Password:
                    </label>
                </div>

                {isBlank && isSubmitted && (
                    <div style={{ marginBottom: '15px' }}>Something is missing...</div>
                )}
                {!isBlank && isSubmitted && loggedIn && (
                    <div style={{ marginBottom: '15px' }}>Successfully Logged In!</div>
                )}
                {!isBlank && isSubmitted && !loggedIn && (
                    <div style={{ marginBottom: '15px' }}>Username or password is incorrect.</div>
                )}
                <div className={styles['button-container']}>
                  <button className={`btn ${styles['btn-outline']}`} type="submit">
                    Login
                  </button>
                </div>
            </div>
            
        </form>
    );
};

export default LoginPage;