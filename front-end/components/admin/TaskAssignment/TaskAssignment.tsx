'use client'

import { useState } from 'react'
import { format } from 'date-fns'
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

interface Task {
  id: string
  clientName: string
  address: string
  startTime: Date
  duration: number
}

interface Worker {
  id: string
  name: string
}

const initialTasks: Task[] = [
  {
    id: '1',
    clientName: 'ABC Corp',
    address: '123 Main St, Cityville',
    startTime: new Date('2023-06-20T09:00:00'),
    duration: 2
  },
  {
    id: '2',
    clientName: 'XYZ Inc',
    address: '456 Elm St, Townsburg',
    startTime: new Date('2023-06-21T14:00:00'),
    duration: 3
  },
  {
    id: '3',
    clientName: 'LMN Ltd',
    address: '789 Oak Ave, Villageton',
    startTime: new Date('2023-06-22T10:30:00'),
    duration: 4
  }
]

const workers: Worker[] = [
  { id: 'w1', name: 'John Doe' },
  { id: 'w2', name: 'Jane Smith' },
  { id: 'w3', name: 'Bob Johnson' },
  { id: 'w4', name: 'Alice Brown' }
]

export default function TaskAssignment() {
  const [tasks, setTasks] = useState<Task[]>(initialTasks)
  const [assignments, setAssignments] = useState<{ [taskId: string]: string }>({})

  const handleAssignment = (taskId: string, workerId: string) => {
    setAssignments(prev => ({ ...prev, [taskId]: workerId }))
  }

  const handleAssign = (taskId: string) => {
    const workerId = assignments[taskId]
    if (workerId) {
      // Here you would typically send the assignment to your backend
      console.log(`Task ${taskId} assigned to worker ${workerId}`)
      // Remove assigned task from the list
      setTasks(prev => prev.filter(task => task.id !== taskId))
      // Remove the assignment from the state
      setAssignments(prev => {
        const { [taskId]: _, ...rest } = prev
        return rest
      })
    } else {
      console.log("Assignment Failed: Please select a worker before assigning the task.")
    }
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Assign Pending Tasks</h1>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Client</TableHead>
            <TableHead>Address</TableHead>
            <TableHead>Date</TableHead>
            <TableHead>Time</TableHead>
            <TableHead>Duration</TableHead>
            <TableHead>Assign To</TableHead>
            <TableHead>Action</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {tasks.map(task => (
            <TableRow key={task.id}>
              <TableCell>{task.clientName}</TableCell>
              <TableCell>{task.address}</TableCell>
              <TableCell>{format(task.startTime, 'MMM d, yyyy')}</TableCell>
              <TableCell>{format(task.startTime, 'h:mm a')}</TableCell>
              <TableCell>{task.duration} hours</TableCell>
              <TableCell>
                <Select onValueChange={(value) => handleAssignment(task.id, value)}>
                  <SelectTrigger className="w-[180px]">
                    <SelectValue placeholder="Select a worker" />
                  </SelectTrigger>
                  <SelectContent>
                    {workers.map(worker => (
                      <SelectItem key={worker.id} value={worker.id}>
                        {worker.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </TableCell>
              <TableCell>
                <Button onClick={() => handleAssign(task.id)}>
                  Assign
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}