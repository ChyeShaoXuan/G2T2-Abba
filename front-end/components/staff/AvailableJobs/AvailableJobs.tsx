'use client'

import { useState } from 'react'
import { format, addHours } from 'date-fns'
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { Badge } from "@/components/ui/badge"
import { Clock, MapPin, DollarSign, AlertTriangle, Calendar } from 'lucide-react'

interface JobDetails {
  id: string
  clientName: string
  address: string
  startTime: Date
  duration: number
  payRate: number
}

const jobs: JobDetails[] = [
  {
    id: '12345',
    clientName: 'ABC Corporation',
    address: '123 Main St, Cityville, State 12345',
    startTime: new Date('2023-06-15T14:30:00'), // 2:30 PM
    duration: 3, // 3 hours
    payRate: 15.50
  },
  {
    id: '67890',
    clientName: 'XYZ Inc.',
    address: '456 Elm St, Townsburg, State 67890',
    startTime: new Date('2023-06-16T10:00:00'), // 10:00 AM
    duration: 2, // 2 hours
    payRate: 16.75
  },
  {
    id: '24680',
    clientName: 'LMN Enterprises',
    address: '789 Oak Ave, Villageton, State 24680',
    startTime: new Date('2023-06-16T13:30:00'), // 1:30 PM
    duration: 4, // 4 hours
    payRate: 17.25
  }
]

function AvailableJobCard({ job, status, onAccept, onDecline }: { job: JobDetails, status: 'pending' | 'accepted' | 'declined', onAccept: () => void, onDecline: () => void }) {
  const endTime = addHours(job.startTime, job.duration)
  const isLunchTime = job.startTime.getHours() === 12 || (endTime.getHours() === 13 && endTime.getMinutes() > 0)

  return (
    <Card className="w-full max-w-sm">
      <CardHeader>
        <CardTitle className="flex justify-between items-center">
          <span className="text-lg">{job.clientName}</span>
          <Badge variant={status === 'pending' ? 'outline' : status === 'accepted' ? 'default' : 'destructive'}>
            {status === 'pending' ? 'Available' : status === 'accepted' ? 'Accepted' : 'Declined'}
          </Badge>
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <p className="text-sm text-muted-foreground flex items-center">
          <MapPin className="w-4 h-4 mr-1" /> {job.address}
        </p>
        <div className="flex items-center">
          <Calendar className="w-4 h-4 mr-1" />
          <span className="text-sm">{format(job.startTime, 'MMMM d, yyyy')}</span>
        </div>
        <div className="flex justify-between items-center">
          <div className="flex items-center">
            <Clock className="w-4 h-4 mr-1" />
            <span className="text-sm">{format(job.startTime, 'h:mm a')} - {format(endTime, 'h:mm a')}</span>
          </div>
          <span className="text-sm">{job.duration} hour{job.duration > 1 ? 's' : ''}</span>
        </div>
        <div className="flex items-center">
          <DollarSign className="w-4 h-4 mr-1" />
          <span className="text-sm">${job.payRate.toFixed(2)} per hour</span>
        </div>
        {isLunchTime && (
          <Alert className="mt-2">
            <AlertTriangle className="h-4 w-4" />
            <AlertTitle>Lunch Break Overlap</AlertTitle>
            <AlertDescription>
              This job overlaps with the standard lunch break (12 PM - 1 PM).
            </AlertDescription>
          </Alert>
        )}
      </CardContent>
      <CardFooter className="flex justify-between">
        {status === 'pending' && (
          <>
            <Button onClick={onAccept} className="w-[45%]">Accept</Button>
            <Button onClick={onDecline} variant="outline" className="w-[45%]">Decline</Button>
          </>
        )}
        {status === 'accepted' && (
          <Alert>
            <AlertDescription>
              You have accepted this job. Please arrive at the location by {format(job.startTime, 'h:mm a')} on {format(job.startTime, 'MMMM d, yyyy')}.
            </AlertDescription>
          </Alert>
        )}
        {status === 'declined' && (
          <Alert variant="destructive">
            <AlertDescription>
              You have declined this job. This may affect your salary.
            </AlertDescription>
          </Alert>
        )}
      </CardFooter>
    </Card>
  )
}

export default function AvailableJobs() {
  const [jobStatuses, setJobStatuses] = useState<{ [key: string]: 'pending' | 'accepted' | 'declined' }>({})

  const handleAccept = (jobId: string) => {
    setJobStatuses(prev => ({ ...prev, [jobId]: 'accepted' }))
    console.log(`Job ${jobId} accepted`)
  }

  const handleDecline = (jobId: string) => {
    setJobStatuses(prev => ({ ...prev, [jobId]: 'declined' }))
    console.log(`Job ${jobId} declined`)
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Available Jobs</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {jobs.map(job => (
          <AvailableJobCard
            key={job.id}
            job={job}
            status={jobStatuses[job.id] || 'pending'}
            onAccept={() => handleAccept(job.id)}
            onDecline={() => handleDecline(job.id)}
          />
        ))}
      </div>
    </div>
  )
}