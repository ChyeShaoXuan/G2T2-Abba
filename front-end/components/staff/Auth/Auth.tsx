// File: front-end/components/staff/Auth/Auth.tsx
'use client'

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircle } from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useGlobalState } from '@/context/StateContext';
import axios from 'axios';

const workerAuthSchema = z.object({
  workerId: z.string().min(1, "Worker ID is required"),
  password: z.string().min(5, "Password must be at least 5 characters"),
});

export default function StaffAuth() {
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();
  const { login, logout, userType } = useGlobalState();

  // Log initial state
  console.log('Current userType:', userType);
  // console.log('localStorage data:', {
  //   userType: localStorage.getItem('userType'),
  //   username: localStorage.getItem('username'),
  //   token: localStorage.getItem('jwtToken')
  // });

  const form = useForm<z.infer<typeof workerAuthSchema>>({
    resolver: zodResolver(workerAuthSchema),
    defaultValues: {
      workerId: "",
      password: "",
    },
  });

  async function onSubmit(values: z.infer<typeof workerAuthSchema>) {
    console.log('Login attempt with values:', values);
    
    try {
      console.log('Making authentication request...');
      // const response = await axios.post('http://localhost:8080/worker/authenticate', values, {
      //   headers: { 'Content-Type': 'application/json' },
      // });

      // console.log('Server response:', response.data);

      // if (response.data.success) {
      //   console.log('Login successful, storing data in context...');
      //   login(response.data);
        
      //   console.log('Updated context state:', {
      //     userType: localStorage.getItem('userType'),
      //     username: localStorage.getItem('username'),
      //     token: localStorage.getItem('jwtToken')
      //   });

      //   console.log('Redirecting to dashboard...');
        router.push('/staff/Dashboard');
      // } else {
      //   console.log('Login failed:', response.data);
      //   setError("Invalid employee ID or password");
      // }
    } catch (error) {
      console.error('Login error:', error);
      setError("An error occurred during login.");
    }
  }

  // Log when component is determining what to render
  console.log('Rendering component with userType:', userType);

  if (userType === 'Worker') {
    console.log('Rendering worker dashboard view');
    return (
      <div className="relative w-full max-w-md mx-auto">
        <div className="text-white text-lg font-bold">Worker Dashboard
          <Link href="/staff/JobsDisplay">
            <Button className="text-white hover:text-gray-400 mt-5">Jobs Display</Button>
          </Link>
          <Link href="/staff/LeaveApplication">
            <Button className="text-white hover:text-gray-400 mt-5">Leave Application</Button>
          </Link>
        </div>

        <div>
          <Card className="mx-auto fixed top-4 right-4">
            <CardHeader>
              <CardDescription>You are logged in as a worker.</CardDescription>
            </CardHeader>
            <CardContent>
              <Button onClick={() => {
                console.log('Logout clicked');
                logout();
                console.log('State after logout:', {
                  userType: localStorage.getItem('userType'),
                  username: localStorage.getItem('username'),
                  token: localStorage.getItem('jwtToken')
                });
                router.push('/auth');
              }}>Logout</Button>
            </CardContent>
          </Card>
        </div>
      </div>
    );
  }

  console.log('Rendering login form view');
  return (
    <Card className="w-full max-w-md mx-auto">
      <CardHeader>
        <CardTitle>Worker Login</CardTitle>
        <CardDescription>Enter your employee ID and password to access your account.</CardDescription>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <FormField
              control={form.control}
              name="workerId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Worker ID</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input type="password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button type="submit" className="w-full">Login</Button>
          </form>
        </Form>
        {error && (
          <Alert variant="destructive" className="mt-4">
            <AlertCircle className="h-4 w-4" />
            <AlertTitle>Error</AlertTitle>
            <AlertDescription>{error}</AlertDescription>
          </Alert>
        )}
      </CardContent>
    </Card>
  );
}