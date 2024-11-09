'use client'

import { useState, useEffect } from 'react'
import { format, addMinutes, isBefore } from 'date-fns'
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { MapPin, Clock, Calendar, AlertTriangle } from 'lucide-react'

interface Job {
  id: string
  clientName: string
  address: string
  startTime: Date
  duration: number
  status: 'upcoming' | 'in progress' | 'finished'
  arrivalConfirmed: boolean
  completionConfirmed: boolean
  date: Date
}

const initialJobs: Job[] = [
  {
    id: '1',
    clientName: 'ABC Corp',
    address: '123 Main St, City',
    date: new Date('2023-06-15'),
    startTime: addMinutes(new Date(), 30),
    duration: 2,
    status: 'finished',
    arrivalConfirmed: false,
    completionConfirmed: false
  },
  {
    id: '2',
    clientName: 'XYZ Inc',
    address: '456 Elm St, Town',
    date: new Date('2023-06-16'),
    startTime: addMinutes(new Date(), 120),
    duration: 3,
    status: 'finished',
    arrivalConfirmed: false,
    completionConfirmed: false
  }
]

export default function FinishedJobs() {
  const [myJobs, setMyJobs] = useState<Job[]>(initialJobs)
  const [selectedJob, setSelectedJob] = useState<Job | null>(null)
  const [isArrivalDialogOpen, setIsArrivalDialogOpen] = useState(false)
  const [isCompletionDialogOpen, setIsCompletionDialogOpen] = useState(false)
  const [arrivalPhoto, setArrivalPhoto] = useState<File | null>(null)
  const [completionPhoto, setCompletionPhoto] = useState<File | null>(null)

  useEffect(() => {
    const timer = setInterval(() => {
      setMyJobs(prevJobs => 
        prevJobs.map(job => {
          if (job.status === 'upcoming' && !job.arrivalConfirmed && isBefore(job.startTime, new Date())) {
            // Alert logic would go here (e.g., send notification to supervisor)
            console.log(`Alert: Worker hasn't arrived for job ${job.id}`)
            return { ...job, status: 'in progress' }
          }
          return job
        })
      )
    }, 60000) // Check every minute

    return () => clearInterval(timer)
  }, [])

  const handleArrivalConfirmation = () => {
    if (selectedJob && arrivalPhoto) {
      setMyJobs(prevJobs => 
        prevJobs.map(job => 
          job.id === selectedJob.id 
            ? { ...job, arrivalConfirmed: true, status: 'in progress' } 
            : job
        )
      )
      setIsArrivalDialogOpen(false)
      setArrivalPhoto(null)
      // Here you would typically upload the photo to your backend
      console.log(`Arrival photo uploaded for job ${selectedJob.id}`)
    }
  }

  const handleCompletionConfirmation = () => {
    if (selectedJob && completionPhoto) {
      setMyJobs(prevJobs => 
        prevJobs.map(job => 
          job.id === selectedJob.id 
            ? { ...job, completionConfirmed: true, status: 'finished' } 
            : job
        )
      )
      setIsCompletionDialogOpen(false)
      setCompletionPhoto(null)
      // Here you would typically upload the photo to your backend
      console.log(`Completion photo uploaded for job ${selectedJob.id}`)
    }
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Finished Jobs</h1>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {myJobs.map(job => (
          <Card key={job.id} className="cursor-pointer" onClick={() => setSelectedJob(job)}>
            <CardHeader>
              <CardTitle className="flex justify-between items-center">
                <span>{job.clientName}</span>
                <Badge variant={job.status === 'upcoming' ? 'outline' : job.status === 'in progress' ? 'default' : 'secondary'}>
                  {job.status}
                </Badge>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <p className="flex items-center"><Calendar className="mr-2" /> {format(job.date, 'MMMM d, yyyy')}</p>
              <p className="flex items-center"><MapPin className="mr-2" /> {job.address}</p>
              <p className="flex items-center"><Clock className="mr-2" /> {format(job.startTime, 'h:mm a')} ({job.duration} hours)</p>
            </CardContent>
            <CardFooter>
              {job.status === 'upcoming' && !job.arrivalConfirmed && (
                <Button onClick={() => setIsArrivalDialogOpen(true)}>Confirm Arrival</Button>
              )}
              {job.status === 'in progress' && !job.completionConfirmed && (
                <Button onClick={() => setIsCompletionDialogOpen(true)}>Complete Job</Button>
              )}
            </CardFooter>
          </Card>
        ))}
      </div>

      <Dialog open={isArrivalDialogOpen} onOpenChange={setIsArrivalDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Confirm Arrival</DialogTitle>
            <DialogDescription>
              Please upload a photo to confirm your arrival at the job location.
            </DialogDescription>
          </DialogHeader>
          <div className="grid w-full max-w-sm items-center gap-1.5">
            <Label htmlFor="arrival-photo">Photo</Label>
            <Input 
              id="arrival-photo" 
              type="file" 
              accept="image/*"
              onChange={(e) => setArrivalPhoto(e.target.files?.[0] || null)}
            />
          </div>
          <DialogFooter>
            <Button onClick={handleArrivalConfirmation} disabled={!arrivalPhoto}>Confirm Arrival</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={isCompletionDialogOpen} onOpenChange={setIsCompletionDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Complete Job</DialogTitle>
            <DialogDescription>
              Please upload a photo to confirm the job completion.
            </DialogDescription>
          </DialogHeader>
          <div className="grid w-full max-w-sm items-center gap-1.5">
            <Label htmlFor="completion-photo">Photo</Label>
            <Input 
              id="completion-photo" 
              type="file" 
              accept="image/*"
              onChange={(e) => setCompletionPhoto(e.target.files?.[0] || null)}
            />
          </div>
          <DialogFooter>
            <Button onClick={handleCompletionConfirmation} disabled={!completionPhoto}>Complete Job</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {selectedJob && selectedJob.status === 'in progress' && !selectedJob.arrivalConfirmed && (
        <Alert className="mt-4">
          <AlertTriangle className="h-4 w-4" />
          <AlertTitle>Arrival Not Confirmed</AlertTitle>
          <AlertDescription>
            Please confirm your arrival for the job at {selectedJob.clientName}. Your supervisor will be alerted if you don't confirm within 5 minutes of the start time.
          </AlertDescription>
        </Alert>
      )}
    </div>
  )
}