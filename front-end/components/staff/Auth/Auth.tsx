'use client'

import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { AlertCircle } from 'lucide-react'

const workerAuthSchema = z.object({
  employeeId: z.string().min(1, "Employee ID is required"),
  password: z.string().min(8, "Password must be at least 8 characters"),
})

export default function StaffAuth() {
  const [error, setError] = useState<string | null>(null)
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  const form = useForm<z.infer<typeof workerAuthSchema>>({
    resolver: zodResolver(workerAuthSchema),
    defaultValues: {
      employeeId: "",
      password: "",
    },
  })

  function onSubmit(values: z.infer<typeof workerAuthSchema>) {
    // Here you would typically send a request to your backend to authenticate
    // For demonstration, we'll use a mock authentication
    if (values.employeeId === "W12345" && values.password === "password123") {
      setIsAuthenticated(true)
      setError(null)
    } else {
      setError("Invalid employee ID or password")
    }
  }

  if (isAuthenticated) {
    return (
      <Card className="w-full max-w-md mx-auto">
        <CardHeader>
          <CardTitle>Worker Dashboard</CardTitle>
          <CardDescription>You are logged in as a worker.</CardDescription>
        </CardHeader>
        <CardContent>
          <Button onClick={() => setIsAuthenticated(false)}>Logout</Button>
        </CardContent>
      </Card>
    )
  }

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
              name="employeeId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Employee ID</FormLabel>
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
  )
}