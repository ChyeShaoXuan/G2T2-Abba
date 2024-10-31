'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

interface Task {
  taskId: number
  acknowledged: boolean
  date: string
  shift: string
  status: string
  feedbackId: number | null
  propertyId: number
  workerId: number | null
}

export default function OverwriteTasks() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [editingTask, setEditingTask] = useState<Task | null>(null)

  useEffect(() => {
    // Fetch tasks from the backend
    const fetchTasks = async () => {
      try {
        const tasksResponse = await axios.get(`http://localhost:8080/cleaningTasks/tasks`)
        setTasks(tasksResponse.data)
      } catch (error) {
        console.error('Error fetching tasks:', error)
      }
    }

    fetchTasks()
  }, [])

  const updateTask = async (task: Task) => {
    try {
      const response = await axios.put(`http://localhost:8080/cleaningTasks/${task.taskId}`, task, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      setTasks(prevTasks => prevTasks.map(t => (t.taskId === task.taskId ? response.data : t)))
      setEditingTask(null)
    } catch (error) {
      console.error('Error updating task:', error)
    }
  }

  const handleEditClick = (task: Task) => {
    setEditingTask(task)
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (editingTask) {
      const { name, value } = e.target
      setEditingTask({ ...editingTask, [name]: value })
    }
  }

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (editingTask) {
      const { name, checked } = e.target
      setEditingTask({ ...editingTask, [name]: checked })
    }
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Overwrite Cleaning Tasks</h1>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Task ID</TableHead>
            <TableHead>Acknowledged</TableHead>
            <TableHead>Date</TableHead>
            <TableHead>Shift</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Feedback ID</TableHead>
            <TableHead>Property ID</TableHead>
            <TableHead>Worker ID</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {tasks.map(task => (
            <TableRow key={task.taskId}>
              <TableCell>{task.taskId}</TableCell>
              <TableCell>
                {editingTask && editingTask.taskId === task.taskId ? (
                  <input
                    type="checkbox"
                    name="acknowledged"
                    checked={editingTask.acknowledged}
                    onChange={handleCheckboxChange}
                  />
                ) : (
                  task.acknowledged ? 'Yes' : 'No'
                )}
              </TableCell>
              <TableCell>
                {editingTask && editingTask.taskId === task.taskId ? (
                  <input
                    type="date"
                    name="date"
                    value={editingTask.date}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  task.date
                )}
              </TableCell>
              <TableCell>
                {editingTask && editingTask.taskId === task.taskId ? (
                  <input
                    type="text"
                    name="shift"
                    value={editingTask.shift}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  task.shift
                )}
              </TableCell>
              <TableCell>
                {editingTask && editingTask.taskId === task.taskId ? (
                  <input
                    type="text"
                    name="status"
                    value={editingTask.status}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  task.status
                )}
              </TableCell>
              <TableCell>
                {editingTask && editingTask.taskId === task.taskId ? (
                  <input
                    type="number"
                    name="feedbackId"
                    value={editingTask.feedbackId || ''}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  task.feedbackId
                )}
              </TableCell>
              <TableCell>
                {editingTask && editingTask.taskId === task.taskId ? (
                  <input
                    type="number"
                    name="propertyId"
                    value={editingTask.propertyId}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  task.propertyId
                )}
              </TableCell>
              <TableCell>
                {editingTask && editingTask.taskId === task.taskId ? (
                  <input
                    type="number"
                    name="workerId"
                    value={editingTask.workerId || ''}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  task.workerId
                )}
              </TableCell>
              <TableCell>
                {editingTask && editingTask.taskId === task.taskId ? (
                  <Button className="bg-blue-500 hover:bg-blue-700" onClick={() => updateTask(editingTask)}>Confirm Overwrite</Button>
                ) : (
                  <Button className="bg-blue-500 hover:bg-blue-700" onClick={() => handleEditClick(task)}>Overwrite Record</Button>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}