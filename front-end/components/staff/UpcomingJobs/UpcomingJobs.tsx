'use client'

import { useState, useEffect } from 'react'
import { format, addMinutes, isBefore } from 'date-fns'
import axios from 'axios'
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { MapPin, Clock, Calendar, AlertTriangle } from 'lucide-react'


interface Job {
  taskId: number
  acknowledged: boolean
  date: string
  shift: string
  status: string
  feedbackId: number | null
  propertyId: number
  workerId: number | null
  address: string
  latitude: number
  longitude: number
  numberOfRooms: number
}

// interface Worker {
//   workerId: number
//   name: string
//   phoneNumber: string
//   shortBio: string
//   deployed: boolean
//   tele_Id: string
//   curPropertyId: number
//   available: boolean
//   adminId: number
//   worker_hours_in_week: number
// }

export default function UpcomingJobs() {
  const username = localStorage.getItem('username')
  const [editingJob, setEditingJob] = useState<Job | null>(null)
  const [myJobs, setMyJobs] = useState<Job[]>([])
  const [selectedJob, setSelectedJob] = useState<Job | null>(null)
  const [isArrivalDialogOpen, setIsArrivalDialogOpen] = useState(false)
  const [isCompletionDialogOpen, setIsCompletionDialogOpen] = useState(false)
  const [arrivalPhoto, setArrivalPhoto] = useState<File | null>(null)
  const [completionPhoto, setCompletionPhoto] = useState<File | null>(null)
  const [arrivalPhotoUrl, setArrivalPhotoUrl] = useState<string | null>(null)
  const [workers, setWorkers] = useState<Worker[]>([])
  const [workerId, setWorkerId] = useState<number | null>(null)

  // useEffect(() => {
  //   // Fetch workers from the backend
  //   const fetchWorkers = async () => {
  //     try {
  //       const workersResponse = await axios.get(`http://localhost:8080/admin/workers`)
  //       const worker = workersResponse.data.find((worker: Worker) => worker.name === username)
  //       if (worker) {
  //         setWorkers([worker]) 
  //         setWorkerId(worker.workerId)
  //       }
  //       console.log(workersResponse.data) 
  //     } catch (error) {
  //       console.error('Error fetching workers:', error)
  //     }
  //   }

  //   fetchWorkers()
  // }, [username])

useEffect(() => {
    // Fetch tasks from the backend
    const fetchTasks = async () => {
      try {
        const workerId = 1 // Example worker ID, should come from the logged-in user's details
        const tasksResponse = await axios.get(`http://localhost:8080/cleaningTasks/tasks/${workerId}`)

        console.log(tasksResponse.data)
        setMyJobs(tasksResponse.data.filter((task: Job) => task.status !== 'Completed'))

      } catch (error) {
        console.error('Error fetching tasks:', error)
      }
    }

    fetchTasks()
  }, [])

  const handleArrivalConfirmation = async () => {
    if (selectedJob && arrivalPhoto) {
      const formData = new FormData()
      formData.append('photo', arrivalPhoto)

      try {
        const response = await axios.post(`http://localhost:8080/cleaningTasks/${selectedJob.taskId}/confirmArrival`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })

        if (response.status === 200) {
          setMyJobs(prevJobs =>
            prevJobs.map(job =>
              job.taskId === selectedJob.taskId
                ? { ...job, arrivalConfirmed: true, status: 'in progress' }
                : job
            )
          )
          setIsArrivalDialogOpen(false)
          setArrivalPhoto(null)
          console.log(`Arrival photo uploaded for job ${selectedJob.taskId}`)
          fetchArrivalPhoto(selectedJob.taskId)
        } else {
          console.error('Arrival confirmation failed:', response.data)
        }
      } catch (error) {
        console.error('Error confirming arrival:', error)
      }
    }
  }

  const fetchArrivalPhoto = async (taskId: number) => {
    try {
      const response = await axios.get(`http://localhost:8080/cleaningTasks/${taskId}/arrivalPhoto`, {
        responseType: 'arraybuffer'
      })
      const base64 = btoa(
        new Uint8Array(response.data).reduce((data, byte) => data + String.fromCharCode(byte), '')
      )
      setArrivalPhotoUrl(`data:image/jpeg;base64,${base64}`)
    } catch (error) {
      console.error('Error fetching arrival photo:', error)
    }
  }

  const handleCompletionConfirmation = async () => {
    if (selectedJob && completionPhoto) {
      const formData = new FormData()
      formData.append('photo', completionPhoto)

      try {
        const response = await axios.post(`http://localhost:8080/cleaningTasks/${selectedJob.taskId}/confirmCompletion`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })

        if (response.status === 200) {
          setMyJobs(prevJobs =>
            prevJobs.map(job =>
              job.taskId === selectedJob.taskId
                ? { ...job, completionConfirmed: true, status: 'Completed' }
                : job
            )
            .filter(job => job.status !== 'Completed')
          )
          setIsCompletionDialogOpen(false)
          setCompletionPhoto(null)
          console.log(`Completion photo uploaded for job ${selectedJob.taskId}`)
        } else {
          console.error('Completion confirmation failed:', response.data)
        }
      } catch (error) {
        console.error('Error confirming completion:', error)
      }
    }
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Upcoming Jobs</h1>
      {/* Show message if no finished jobs */}
      {myJobs.length === 0 ? (
        <div className="text-center p-4">
          <p>No Upcoming Tasks</p>
        </div>
      ) : (
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {myJobs.map((task) => (
          <Card key={task.taskId} className="cursor-pointer" onClick={() => setSelectedJob(task)}>
            <CardHeader>
              <CardTitle className="flex justify-between items-center">
                <span>{`Property ID: ${task.propertyId}`}</span>
                <Badge variant={task.status === 'upcoming' ? 'outline' : task.status === 'in progress' ? 'default' : 'secondary'}>
                  {task.status}
                </Badge>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <p className="flex items-center">
                <Calendar className="mr-2" /> {task.date ? format(new Date(task.date), 'MMMM d, yyyy') : 'Invalid Date'}
              </p>
              <p className="flex items-center">
                <MapPin className="mr-2" /> {task.propertyAddress}
              </p>
              <p className="flex items-center">
                <Clock className="mr-2" /> {task.shift}
              </p>
              
            </CardContent>

            <CardFooter>
              {task.status === 'Assigned' && !task.arrivalConfirmed && (
                <Button onClick={() => setIsArrivalDialogOpen(true)}>Confirm Arrival</Button>
              )}
              {task.status === 'in progress' && !task.completionConfirmed && (
                <Button onClick={() => setIsCompletionDialogOpen(true)}>Complete Job</Button>
              )}
            </CardFooter>
          </Card>
        ))}
      </div>
      )}

      {/* Arrival Confirmation Dialog */}
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

      {/* Completion Confirmation Dialog */}
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
    </div>
  )
}

