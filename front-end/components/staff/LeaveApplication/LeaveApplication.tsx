'use client'

import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { format } from 'date-fns'
import { Calendar as CalendarIcon, Upload } from 'lucide-react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Textarea } from "@/components/ui/textarea"
import { cn } from "@/lib/utils"

import axios from 'axios'

const formSchema = z.object({
  workerName: z.string().min(2, {
    message: "Worker name must be at least 2 characters.",
  }),
  workerID: z.string().min(1, {
    message: "Worker ID is required.",
  }),
  leaveType: z.enum(["AL", "HL", "MC", "EL"], {
    required_error: "Please select a leave type.",
  }),
  startDate: z.date({
    required_error: "Start date is required.",
  }),
  endDate: z.date({
    required_error: "End date is required.",
  }),
  reason: z.string().min(1, {
    message: "Reason for leave is required.",
  }),
  mcFile: z.instanceof(File).optional().refine((file) => !file || file.size <= 5000000, {
    message: 'Max file size is 5MB.',
  }),
})



export default function LeaveApplicationForm() {
  const [file, setFile] = useState<File | null>(null)
  const [submitStatus, setSubmitStatus] = useState<'idle' | 'success' | 'error'>('idle')

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      workerName: "",
      workerID: "",
      leaveType: undefined,
      reason: "",
    },
  })

  const watchLeaveType = form.watch("leaveType")

  async function onSubmit(values: z.infer<typeof formSchema>) {
    // Create a FormData object to handle file uploads
    const formData = new FormData();

    // Append all the fields from the form
    formData.append('workerName', values.workerName);
    formData.append('workerID', values.workerID);
    formData.append('leaveType', values.leaveType);
    formData.append('startDate', format(values.startDate, 'yyyy-MM-dd'));
    formData.append('endDate', format(values.endDate, 'yyyy-MM-dd'));
    formData.append('reason', values.reason);

    // Fetch the admin ID dynamically based on worker ID
    const response = await axios.get(`http://localhost:8080/worker/${values.workerID}/admin`);
    const adminId = response.data;  // Directly use the fetched adminId

    const leaveApplication = {
      worker: {
        workerId: values.workerID,
      },
      admin: {
        adminId: adminId, // Hardcoded for now
      },
      startDate: values.startDate,
      endDate: values.endDate, 
      leaveType: values.leaveType,
      reason: values.reason,
      submissionDateTime: new Date().toISOString(),
    };

    try {
      const response = await axios.post('http://localhost:8080/leave/apply', leaveApplication);

      // Capture the leaveId from the response if provided
      const result = response.data;
      const leaveId = result.leaveId;
      console.log(result);
      console.log(leaveId);
      // Step 2: Upload MC document if leave type is "mc"
      if (values.leaveType === "MC" && file && leaveId) {
        const mcData = new FormData();
        mcData.append('leaveId', leaveId); // Use leaveId from the previous step
        mcData.append('mcDocument', file);

        await axios.post('http://localhost:8080/leave/upload-mc', mcData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
      }

      setSubmitStatus('success');
      form.reset();
      setFile(null); // Clear the file state

    } catch (error) {
      console.error(error);
      setSubmitStatus('error');
    }
  }

  return (
    <Card className="w-full max-w-2xl mx-auto">
      <CardHeader>
        <CardTitle>Leave Application</CardTitle>
        <CardDescription>Apply for leave or submit your MC.</CardDescription>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <FormField
              control={form.control}
              name="workerName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Worker Name</FormLabel>
                  <FormControl>
                    <Input placeholder="John Doe" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="workerID"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Worker ID</FormLabel>
                  <FormControl>
                    <Input placeholder="W12345" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="leaveType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Leave Type</FormLabel>
                  <Select onValueChange={field.onChange} defaultValue={field.value}>
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a leave type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="AL">Annual Leave</SelectItem>
                      <SelectItem value="MC">Medical Certificate (MC)</SelectItem>
                      <SelectItem value="HL">Hospitalization Leave</SelectItem>
                      <SelectItem value="EL">Emergency Leave</SelectItem>
                      <SelectItem value="other">Other</SelectItem>
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="startDate"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Start Date</FormLabel>
                  <FormControl>
                    <Calendar
                      mode="single"
                      selected={field.value}
                      onSelect={field.onChange}
                      disabled={(date) =>
                        date < new Date() || date < new Date("1900-01-01")
                      }
                      className="rounded-md border"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="endDate"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>End Date</FormLabel>
                  <FormControl>
                    <Calendar
                      mode="single"
                      selected={field.value}
                      onSelect={field.onChange}
                      disabled={(date) =>
                        date < new Date() || date < new Date("1900-01-01")
                      }
                      className="rounded-md border"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            </div>
            <FormField
              control={form.control}
              name="reason"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Reason for Leave</FormLabel>
                  <FormControl>
                    <Textarea 
                      placeholder="Please provide a reason for your leave application"
                      className="resize-none"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            {watchLeaveType === "MC" && (
              <FormField
                control={form.control}
                name="mcFile"
                render={({ field: { onChange, value, ...rest } }) => (
                  <FormItem>
                    <FormLabel>Upload e-MC</FormLabel>
                    <FormControl>
                      <div className="flex items-center space-x-2">
                        <Input
                          type="file"
                          accept=".pdf,.jpg,.jpeg,.png"
                          onChange={(e) => {
                            const file = e.target.files?.[0]
                            if (file) {
                              setFile(file)
                              onChange(file)
                            }
                          }}
                          {...rest}
                        />
                        <Button type="button" variant="outline" size="icon">
                          <Upload className="h-4 w-4" />
                        </Button>
                      </div>
                    </FormControl>
                    <FormDescription>
                      Upload the e-MC file (PDF, JPG, PNG, max 5MB)
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
            )}
            <Button type="submit">Submit Leave Application</Button>
          </form>
        </Form>
      </CardContent>
      <CardFooter>
        {submitStatus === 'success' && (
          <Alert className="w-full">
            <AlertTitle>Success</AlertTitle>
            <AlertDescription>
              Your leave application has been successfully submitted.
            </AlertDescription>
          </Alert>
        )}
        {submitStatus === 'error' && (
          <Alert variant="destructive" className="w-full">
            <AlertTitle>Error</AlertTitle>
            <AlertDescription>
              There was a problem submitting your leave application. Please try again.
            </AlertDescription>
          </Alert>
        )}
      </CardFooter>
    </Card>
  )
}