"use client";

import React, { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';

import styles from '../register/Register.module.css'; // Import the CSS module

const LOGIN_URL = 'http://localhost:8080/authentication/login'; 


const Login = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    

    const [errors, setErrors] = useState([]);
    const [loggedIn, setLoggedIn] = useState(false);
    const [isBlank, setIsBlank] = useState(true);
    const [isSubmitted, setIsSubmitted] = useState(false);
    

    const router = useRouter();

    const login = async (e) => {
        e.preventDefault()
        setIsSubmitted(true)

        if (username === '' || password === '') {

            setIsBlank(true);
            return;

        } 

        else {

            setIsBlank(false);

        }

        try {

            const res = await axios.post(LOGIN_URL, {
                username,
                password
            })

            if (res.status === 200) {
                console.log(res.data)
                console.log('Logged in successfully')
                setLoggedIn(true)

                if (res.data.username === 'admin') {

                    router.push('/admin/Dashboard');

                }

                else if (res.data.role === 'Worker') {

                    router.push('/staff/Dashboard')
                
                }

                else if (res.data.role === 'Client') {
                    router.push('/client/Dashboard')
                }

                
                
            }

        }

        catch (error) {
            console.log(error)
            setErrors([error])
        }

    }

    return (
        <form onSubmit={login}>
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

}

export default Login;