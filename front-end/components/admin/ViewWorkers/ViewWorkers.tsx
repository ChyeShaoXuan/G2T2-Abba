'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Button } from "@/components/ui/button"
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
  supervisorId: number
}

export default function ViewWorkers() {
  const [workers, setWorkers] = useState<Worker[]>([])

  useEffect(() => {
    // Fetch workers from the backend
    const fetchWorkers = async () => {
      try {
        const workersResponse = await axios.get('http://localhost:8080/admin/workers')
        setWorkers(workersResponse.data)
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    fetchWorkers()
  }, [])

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Worker Records</h1>
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
              <TableCell>{worker.supervisorId}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}