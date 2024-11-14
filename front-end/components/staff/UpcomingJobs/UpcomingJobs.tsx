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

  useEffect(() => {
    // Fetch tasks from the backend
    const fetchTasks = async () => {
      try {
        const workerId = 1 // Example worker ID, should come from the logged-in user's details
        const tasksResponse = await axios.get(`http://localhost:8080/cleaningTasks/tasks/myJobs/${workerId}`)

        console.log(tasksResponse.data)
        setMyJobs(tasksResponse.data)

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
                ? { ...job, completionConfirmed: true, status: 'finished' }
                : job
            )
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
                <MapPin className="mr-2" /> {task.address}
              </p>
              <p className="flex items-center">
                <Clock className="mr-2" /> {task.shift}
              </p>
              <p className="flex items-center">
                <strong>Number of Rooms:</strong> {task.numberOfRooms}
              </p>
              <p className="flex items-center">
                <strong>Package Type:</strong> {task.packageType}
              </p>
              <p className="flex items-center">
                <strong>Package Type:</strong> {task.propertyType}
              </p>
              <p className="flex items-center">
                <strong>Number of Rooms:</strong> {task.price}
              </p>
              <p className="flex items-center">
                <strong>Number of Rooms:</strong> {task.hours}
              </p>
              <p className="flex items-center">
                <strong>Number of Rooms:</strong> {task.hourlyRate}
              </p>
              <p className="flex items-center">
                <strong>Number of Rooms:</strong> {task.pax}
              </p>
            </CardContent>
            <CardFooter>
              {task.status === 'upcoming' && !task.arrivalConfirmed && (
                <Button onClick={() => setIsArrivalDialogOpen(true)}>Confirm Arrival</Button>
              )}
              {task.status === 'in progress' && !task.completionConfirmed && (
                <Button onClick={() => setIsCompletionDialogOpen(true)}>Complete Job</Button>
              )}
            </CardFooter>
          </Card>
        ))}
      </div>

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



// export default function UpcomingJobs() {
//   const [myJobs, setMyJobs] = useState<Job[]>(initialJobs)
//   const [selectedJob, setSelectedJob] = useState<Job | null>(null)
//   const [isArrivalDialogOpen, setIsArrivalDialogOpen] = useState(false)
//   const [isCompletionDialogOpen, setIsCompletionDialogOpen] = useState(false)
//   const [arrivalPhoto, setArrivalPhoto] = useState<File | null>(null)
//   const [completionPhoto, setCompletionPhoto] = useState<File | null>(null)
//   const [arrivalPhotoUrl, setArrivalPhotoUrl] = useState<string | null>(null)

//   useEffect(() => {
//     const timer = setInterval(() => {
//       setMyJobs(prevJobs =>
//         prevJobs.map(job => {
//           if (job.status === 'upcoming' && !job.arrivalConfirmed && isBefore(job.startTime, new Date())) {
//             console.log(`Alert: Worker hasn't arrived for job ${job.id}`)
//             return { ...job, status: 'in progress' }
//           }
//           return job
//         })
//       )
//     }, 60000) // Check every minute

//     return () => clearInterval(timer)
//   }, [])

//   const handleArrivalConfirmation = async () => {
//     if (selectedJob && arrivalPhoto) {
//       const formData = new FormData()
//       formData.append('photo', arrivalPhoto)

//       try {
//         const response = await axios.post(`http://localhost:8080/cleaningTasks/${selectedJob.id}/confirmArrival`, formData, {
//           headers: {
//             'Content-Type': 'multipart/form-data'
//           }
//         })

//         if (response.status === 200) {
//           setMyJobs(prevJobs =>
//             prevJobs.map(job =>
//               job.id === selectedJob.id
//                 ? { ...job, arrivalConfirmed: true, status: 'in progress' }
//                 : job
//             )
//           )
//           setIsArrivalDialogOpen(false)
//           setArrivalPhoto(null)
//           console.log(`Arrival photo uploaded for job ${selectedJob.id}`)
//           fetchArrivalPhoto(selectedJob.id)
//         } else {
//           console.error('Arrival confirmation failed:', response.data)
//         }
//       } catch (error) {
//         console.error('Error confirming arrival:', error)
//       }
//     }
//   }

//   const fetchArrivalPhoto = async (taskId: string) => {
//     try {
//       const response = await axios.get(`http://localhost:8080/cleaningTasks/${taskId}/arrivalPhoto`, {
//         responseType: 'arraybuffer'
//       })
//       const base64 = btoa(
//         new Uint8Array(response.data).reduce((data, byte) => data + String.fromCharCode(byte), '')
//       )
//       setArrivalPhotoUrl(`data:image/jpeg;base64,${base64}`)
//     } catch (error) {
//       console.error('Error fetching arrival photo:', error)
//     }
//   }

//   const handleCompletionConfirmation = async () => {
//     if (selectedJob && completionPhoto) {
//       const formData = new FormData()
//       formData.append('photo', completionPhoto)

//       try {
//         const response = await axios.post(`http://localhost:8080/cleaningTasks/${selectedJob.id}/confirmCompletion`, formData, {
//           headers: {
//             'Content-Type': 'multipart/form-data'
//           }
//         })

//         if (response.status === 200) {
//           setMyJobs(prevJobs =>
//             prevJobs.map(job =>
//               job.id === selectedJob.id
//                 ? { ...job, completionConfirmed: true, status: 'finished' }
//                 : job
//             )
//           )
//           setIsCompletionDialogOpen(false)
//           setCompletionPhoto(null)
//           console.log(`Completion photo uploaded for job ${selectedJob.id}`)
//         } else {
//           console.error('Completion confirmation failed:', response.data)
//         }
//       } catch (error) {
//         console.error('Error confirming completion:', error)
//       }
//     }
//   }

//   return (
//     <div className="container mx-auto p-4">
//       <h1 className="text-2xl font-bold mb-4">My Jobs</h1>
//       <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
//         {myJobs.map(job => (
//           <Card key={job.id} className="cursor-pointer" onClick={() => setSelectedJob(job)}>
//             <CardHeader>
//               <CardTitle className="flex justify-between items-center">
//                 <span>{job.clientName}</span>
//                 <Badge variant={job.status === 'upcoming' ? 'outline' : job.status === 'in progress' ? 'default' : 'secondary'}>
//                   {job.status}
//                 </Badge>
//               </CardTitle>
//             </CardHeader>
//             <CardContent>
//               <p className="flex items-center"><Calendar className="mr-2" /> {format(job.date, 'MMMM d, yyyy')}</p>
//               <p className="flex items-center"><MapPin className="mr-2" /> {job.address}</p>
//               <p className="flex items-center"><Clock className="mr-2" /> {format(job.startTime, 'h:mm a')} ({job.duration} hours)</p>
//               {job.arrivalConfirmed && arrivalPhotoUrl && (
//                 <img src={arrivalPhotoUrl} alt="Arrival Photo" className="mt-4" />
//               )}
//             </CardContent>
//             <CardFooter>
//               {job.status === 'upcoming' && !job.arrivalConfirmed && (
//                 <Button onClick={() => setIsArrivalDialogOpen(true)}>Confirm Arrival</Button>
//               )}
//               {job.status === 'in progress' && !job.completionConfirmed && (
//                 <Button onClick={() => setIsCompletionDialogOpen(true)}>Complete Job</Button>
//               )}
//             </CardFooter>
//           </Card>
//         ))}
//       </div>

//       <Dialog open={isArrivalDialogOpen} onOpenChange={setIsArrivalDialogOpen}>
//         <DialogContent>
//           <DialogHeader>
//             <DialogTitle>Confirm Arrival</DialogTitle>
//             <DialogDescription>
//               Please upload a photo to confirm your arrival at the job location.
//             </DialogDescription>
//           </DialogHeader>
//           <div className="grid w-full max-w-sm items-center gap-1.5">
//             <Label htmlFor="arrival-photo">Photo</Label>
//             <Input
//               id="arrival-photo"
//               type="file"
//               accept="image/*"
//               onChange={(e) => setArrivalPhoto(e.target.files?.[0] || null)}
//             />
//           </div>
//           <DialogFooter>
//             <Button onClick={handleArrivalConfirmation} disabled={!arrivalPhoto}>Confirm Arrival</Button>
//           </DialogFooter>
//         </DialogContent>
//       </Dialog>

//       <Dialog open={isCompletionDialogOpen} onOpenChange={setIsCompletionDialogOpen}>
//         <DialogContent>
//           <DialogHeader>
//             <DialogTitle>Complete Job</DialogTitle>
//             <DialogDescription>
//               Please upload a photo to confirm the job completion.
//             </DialogDescription>
//           </DialogHeader>
//           <div className="grid w-full max-w-sm items-center gap-1.5">
//             <Label htmlFor="completion-photo">Photo</Label>
//             <Input
//               id="completion-photo"
//               type="file"
//               accept="image/*"
//               onChange={(e) => setCompletionPhoto(e.target.files?.[0] || null)}
//             />
//           </div>
//           <DialogFooter>
//             <Button onClick={handleCompletionConfirmation} disabled={!completionPhoto}>Complete Job</Button>
//           </DialogFooter>
//         </DialogContent>
//       </Dialog>

//       {selectedJob && selectedJob.status === 'in progress' && !selectedJob.arrivalConfirmed && (
//         <Alert className="mt-4">
//           <AlertTriangle className="h-4 w-4" />
//           <AlertTitle>Arrival Not Confirmed</AlertTitle>
//           <AlertDescription>
//             Please confirm your arrival for the job at {selectedJob.clientName}. Your supervisor will be alerted if you don't confirm within 5 minutes of the start time.
//           </AlertDescription>
//         </Alert>
//       )}
//     </div>
//   )
// }