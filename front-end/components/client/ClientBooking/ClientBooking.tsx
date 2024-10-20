'use client'

import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { format, addDays, addHours, isBefore, isAfter, setHours, setMinutes } from 'date-fns'
import { Calendar as CalendarIcon, Clock } from 'lucide-react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { cn } from "@/lib/utils"

const packageTypes = ['Weekly', 'Bi-weekly'] as const
const propertyTypes = ['HDB', 'Condominium', 'Landed'] as const

const formSchema = z.object({
  packageType: z.enum(packageTypes, {
    required_error: "Please select a package type.",
  }),
  propertyType: z.enum(propertyTypes, {
    required_error: "Please select a property type.",
  }),
  numberOfRooms: z.number().int().positive().max(5, "For 5 bedrooms or more, please contact the sales team."),
  date: z.date().refine((date) => {
    const tomorrow = addDays(new Date(), 1)
    return isAfter(date, tomorrow)
  }, "Booking must be at least 24 hours in advance."),
  time: z.string().refine((time) => {
    const [hours, minutes] = time.split(':').map(Number)
    return (hours >= 8 && hours < 22) && !(hours >= 12 && hours < 13) && !(hours >= 17 && hours < 18)
  }, "Please select a time between 8:00 AM and 10:00 PM, avoiding lunch (12-1 PM) and dinner (5-6 PM) hours."),
  preferredWorker: z.string().optional(),
})

export default function ClientBooking() {
  const [availableWorkers, setAvailableWorkers] = useState(['Worker 1', 'Worker 2', 'Worker 3']) // This would be fetched from an API in a real application

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      packageType: undefined,
      propertyType: undefined,
      numberOfRooms: 1,
      preferredWorker: undefined,
    },
  })

  function onSubmit(values: z.infer<typeof formSchema>) {
    // Here you would typically send the booking data to your backend
    console.log(values)
    // You could also show a success message to the user
    alert('Booking submitted successfully!')
  }

  return (
    <Card className="w-full max-w-2xl mx-auto">
      <CardHeader>
        <CardTitle>Book a Cleaning Service</CardTitle>
        <CardDescription>Fill in the details to book your cleaning service.</CardDescription>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <FormField
              control={form.control}
              name="packageType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Package Type</FormLabel>
                  <Select onValueChange={field.onChange} defaultValue={field.value}>
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a package type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {packageTypes.map((type) => (
                        <SelectItem key={type} value={type}>{type}</SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="propertyType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Property Type</FormLabel>
                  <Select onValueChange={field.onChange} defaultValue={field.value}>
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a property type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {propertyTypes.map((type) => (
                        <SelectItem key={type} value={type}>{type}</SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="numberOfRooms"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Number of Rooms</FormLabel>
                  <FormControl>
                    <Input type="number" {...field} onChange={e => field.onChange(parseInt(e.target.value))} />
                  </FormControl>
                  <FormDescription>
                    For 5 bedrooms or more, please contact our sales team.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="date"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Date</FormLabel>
                  <Popover>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant={"outline"}
                          className={cn(
                            "w-full pl-3 text-left font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value ? (
                            format(field.value, "PPP")
                          ) : (
                            <span>Pick a date</span>
                          )}
                          <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-auto p-0" align="start">
                      <Calendar
                        mode="single"
                        selected={field.value}
                        onSelect={field.onChange}
                        disabled={(date) =>
                          isBefore(date, addDays(new Date(), 1))
                        }
                        initialFocus
                      />
                    </PopoverContent>
                  </Popover>
                  <FormDescription>
                    Bookings must be made at least 24 hours in advance.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="time"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Time</FormLabel>
                  <FormControl>
                    <div className="flex items-center">
                      <Input
                        type="time"
                        {...field}
                        min="08:00"
                        max="22:00"
                        step="3600"
                        className="w-full"
                      />
                      <Clock className="ml-2 h-4 w-4 opacity-50" />
                    </div>
                  </FormControl>
                  <FormDescription>
                    Please select a time between 8:00 AM and 10:00 PM, avoiding lunch (12-1 PM) and dinner (5-6 PM) hours.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="preferredWorker"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Preferred Worker (Optional)</FormLabel>
                  <Select onValueChange={field.onChange} defaultValue={field.value}>
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a preferred worker (optional)" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {availableWorkers.map((worker) => (
                        <SelectItem key={worker} value={worker}>{worker}</SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button type="submit">Book Cleaning Service</Button>
          </form>
        </Form>
      </CardContent>
    </Card>
  )
}