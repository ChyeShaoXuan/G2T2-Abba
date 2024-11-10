// DashboardInfo.tsx


'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

interface Worker {
  workerId: number
  name: string
  phoneNumber: string
  shortBio: string
  deployed: boolean
  tele_Id: string
  curPropertyId: number
  available: boolean
  adminId: number
  worker_hours_in_week: number
}

export default function DashboardInfo() {
  const [workers, setWorkers] = useState<Worker[]>([])

  useEffect(() => {
    // Fetch workers from the backend
    const fetchWorkers = async () => {
      try {
        const workersResponse = await axios.get(`http://localhost:8080/admin/workers`)
        const worker = workersResponse.data.find((worker: Worker) => worker.workerId === 1)
        if (worker) {
          setWorkers([worker]) // Set only the worker with workerId = 1
        }
        console.log(workersResponse.data) // Logs all workers (for debugging)
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    fetchWorkers()
  }, [])

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Worker Information</h1>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Name</TableHead>
            <TableHead>Phone Number</TableHead>
            <TableHead>Short Bio</TableHead>
            <TableHead>Deployed</TableHead>
            <TableHead>Telegram ID</TableHead>
            <TableHead>Current Property ID</TableHead>
            <TableHead>Available</TableHead>
            <TableHead>Supervisor ID</TableHead>
            <TableHead>Worked Hours in Week</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {workers.map(worker => (
            <TableRow key={worker.workerId}>
              <TableCell>{worker.name}</TableCell>
              <TableCell>{worker.phoneNumber}</TableCell>
              <TableCell>{worker.shortBio}</TableCell>
              <TableCell>{worker.deployed ? 'Yes' : 'No'}</TableCell>
              <TableCell>{worker.tele_Id}</TableCell>
              <TableCell>{worker.curPropertyId}</TableCell>
              <TableCell>{worker.available ? 'Yes' : 'No'}</TableCell>
              <TableCell>{worker.adminId}</TableCell>
              <TableCell>{worker.worker_hours_in_week}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
