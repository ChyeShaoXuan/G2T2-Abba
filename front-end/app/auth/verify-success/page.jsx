"use client";

import React, { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

const VerifySuccess = () => {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [message, setMessage] = useState('Check your email for registration confirmation.');

  useEffect(() => {
    const status = searchParams.get('status');
    if (status) {
      if (status === 'success') {
        setMessage('Verification successful. Redirecting to login page...');
        setTimeout(() => {
          router.push('/login');
        }, 3000); // 3 seconds delay
      } else if (status === 'failed') {
        setMessage('Invalid or expired token.');
      } else if (status === 'error') {
        setMessage('An error occurred during verification.');
      }
    }
  }, [router, searchParams]);

  return (
    <div className="container mx-auto p-4 flex flex-col items-center space-y-4">
      <h1 className="text-2xl font-bold">Registration Verification</h1>
      <p>{message}</p>
    </div>
  );
};

export default VerifySuccess;