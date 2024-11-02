"use client";

import React from 'react';
import { useRouter } from 'next/navigation';
import styles from './failure.module.css';

const VerifyFailure = () => {
  const router = useRouter();

  return (
    <div className="container mx-auto p-4 flex flex-col items-center space-y-4">
      <h1 className="text-2xl font-bold">Verification Failed</h1>
      <p>There was an error verifying your email. Please try again.</p>
      <div className={styles['button-container']}>
        <button onClick={() => router.push('/auth/register')} className={styles['btn-outline']}>
          Go to Register
        </button>
      </div>
    </div>
  );
};

export default VerifyFailure;