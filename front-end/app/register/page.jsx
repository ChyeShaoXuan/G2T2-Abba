"use client";

import React, { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';

import styles from './Register.module.css'; // Import the CSS module

const REGISTER_URL = 'http://localhost:8080/authentication/register';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [role, setRole] = useState('');
  const [isBlank, setIsBlank] = useState(true);
  const [errorMessage, setErrorMessage] = useState('');
  const [isSubmitted, setIsSubmitted] = useState(false);

  const router = useRouter();

  const register = async (e) => {
    e.preventDefault();
    setIsSubmitted(true);

    if (name === '' || email === '' || password === '' || phoneNumber === '' || role === '') {
      setIsBlank(true);
      return;
    } else {
      setIsBlank(false);
    }

    try {
      const res = await axios.post(REGISTER_URL, {
        name,
        email,
        password,
        phoneNumber,
        role
      });

      if (res.status === 200) {
        console.log(res.data);
        router.push('/auth/verify-success');
      }
    } catch (error) {
      if (error.response && error.response.data) {
        const errorMessage = error.response.data.message;
        if (errorMessage === 'Verification email failed to send') {
          router.push('/auth/verify-failure');
        } else {
          setErrorMessage(errorMessage);
        }
      } else {
        setErrorMessage('An account is tied to this email address. Use another email.');
      }
    }
  };

  return (
    <form onSubmit={register}>
      <div className={styles['input-container']}>
        <div className={`${styles['form-container']} ${styles.name}`}>
          <input
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Name"
            className={`form-control ${styles['input-outline']}`}
          />
          <label htmlFor="name" className={styles['form-label']}>
            Name:
          </label>
        </div>

        <div className={`${styles['form-container']} ${styles.email}`}>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
            className={`form-control ${styles['input-outline']}`}
          />
          <label htmlFor="email" className={styles['form-label']}>
            Email address:
          </label>
        </div>

        <div className={`${styles['form-container']} ${styles.password}`}>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
            className={`form-control ${styles['input-outline']}`}
          />
          <label htmlFor="password" className={styles['form-label']}>
            Password:
          </label>
        </div>

        <div className={`${styles['form-container']} ${styles.phoneNumber}`}>
          <input
            type="text"
            id="phoneNumber"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            placeholder="Phone Number"
            className={`form-control ${styles['input-outline']}`}
          />
          <label htmlFor="phoneNumber" className={styles['form-label']}>
            Phone Number:
          </label>
        </div>

        <div className={`${styles['form-container']} ${styles.role}`}>
          <label htmlFor="role" className={styles['form-label']}>
            Role:
          </label>
          <select
            id="role"
            value={role}
            onChange={(e) => setRole(e.target.value)}
            className={`form-control ${styles['input-outline']}`}
          >
            <option value="">Select Role</option>
            <option value="Client">Client</option>
            <option value="Worker">Worker</option>
          </select>
        </div>

        {isBlank && isSubmitted && (
          <div style={{ marginBottom: '15px', color: 'red' }}>All fields are required.</div>
        )}
        {errorMessage && (
          <div style={{ marginBottom: '15px', color: 'red' }}>{errorMessage}</div>
        )}

        <div className={styles['button-container']}>
          <button className={`btn ${styles['btn-outline']}`} onClick={() => router.push('/login')}>
            Login
          </button>
          <button className={`btn ${styles['btn-outline']}`} type="submit">
            Submit
          </button>
        </div>
      </div>
    </form>
  );
};

export default Register;