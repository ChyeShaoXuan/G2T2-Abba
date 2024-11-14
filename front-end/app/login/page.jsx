// File: frontend/app/Login/page.jsx
"use client";

import React, { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';
import { useGlobalState } from '@/context/StateContext';
import styles from '../register/Register.module.css';

const LOGIN_URL = 'http://localhost:8080/authentication/login';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState([]);
    const [isBlank, setIsBlank] = useState(true);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [userId, setUserId] = useState('');
    
    const router = useRouter();
    const { login } = useGlobalState();

    const fetchUserId = async (name, userType) => {
      try {
          let response;
          switch (userType) {
              case 'Worker':
                  response = await axios.get(`http://localhost:8080/worker/workerId/${name}`);
                  break;
              case 'Admin':
                  response = await axios.get(`http://localhost:8080/admin/adminId/${name}`);
                  break;
              case 'Client':
                  response = await axios.get(`http://localhost:8080/clients/clientId/${name}`);
                  break;
              default:
                  throw new Error('Invalid user type');
          }
          return response.data;
      } catch (error) {
          console.error('Error fetching user ID:', error);
          throw error;
      }
  };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitted(true);

        if (username === '' || password === '') {
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

                // Route based on role
                if (response.data.username === 'root' || response.data.roles.includes('ROLE_ADMIN')) {
                    router.push('/admin/JobStatisticsDashboard');
                } else if (response.data.role === 'Worker') {
                    router.push('/staff/Dashboard');
                } else if (response.data.role === 'Client') {
                    router.push('/client/Dashboard');
                }
            }
        } catch (error) {
            console.error('Login error:', error);
            setErrors(['Invalid credentials']);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
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
            {errors.length > 0 && (
              <div style={{ marginBottom: '15px', color: 'red' }}>
                {errors.map((error, index) => (
                  <div key={index}>{error}</div>
                ))}
              </div>
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

export default Login;