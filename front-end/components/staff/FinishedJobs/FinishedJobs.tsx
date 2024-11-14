'use client'

import { useState, useEffect } from 'react'
import { format } from 'date-fns'
import axios from 'axios'
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { MapPin, Clock, Calendar } from 'lucide-react'


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

export default function FinishedJobs() {
  const [myJobs, setMyJobs] = useState<Job[]>([])
  const [isLoading, setIsLoading] = useState(false)

  const fetchCompletedTasks = async () => {
    try {
      const workerId = 5 // Example worker ID, should come from the logged-in user's details
      const tasksResponse = await axios.get(`http://localhost:8080/cleaningTasks/tasks/${workerId}`, { params: { status: "Completed" } })

      console.log(tasksResponse.data)
      setMyJobs(tasksResponse.data)

    } catch (error) {
      console.error('Error fetching tasks:', error)
    }
  }

  useEffect(() => {
    fetchCompletedTasks()

    // const interval = setInterval(fetchCompletedTasks, 2000)
    // return () => clearInterval(interval)
  }, [])

  return (
    <div className="container mx-auto p-4">
      <div className="flex items-center mb-4">
        <h1 className="text-2xl font-bold">Finished Jobs</h1>
        <div className="ml-4">
          <Button onClick={fetchCompletedTasks} disabled={isLoading}>
            {isLoading ? 'Refreshing...' : 'Refresh'}
          </Button>
        </div>

      </div>


      {/* Show message if no finished jobs */}
      {myJobs.length === 0 ? (
        <div className="text-center p-4">
          <p>No Finished Tasks</p>
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {myJobs.map((task) => (
            <Card key={task.taskId} className="cursor-pointer">
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
                {task.status === 'Assigned' && !task.acknowledged && (
                  <Button onClick={() => { /* Open dialog to confirm arrival */ }}>Confirm Arrival</Button>
                )}
                {task.status === 'in progress' && !task.acknowledged && (
                  <Button onClick={() => { /* Open dialog to complete job */ }}>Complete Job</Button>
                )}
              </CardFooter>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
