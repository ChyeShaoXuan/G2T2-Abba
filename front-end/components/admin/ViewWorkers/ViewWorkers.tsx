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
  const [newWorker, setNewWorker] = useState<Omit<Worker, 'workerId'>>({
    name: '',
    phoneNumber: '',
    shortBio: '',
    deployed: false,
    tele_Id: '',
    curPropertyId: 0,
    available: false,
    supervisorId: 0
  })
  const adminId = 1 // Replace with the actual adminId as needed

  useEffect(() => {
    // Fetch workers from the backend
    const fetchWorkers = async () => {
      try {
        const workersResponse = await axios.get(`http://localhost:8080/admin/workers`)
        setWorkers(workersResponse.data)
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    fetchWorkers()
  }, )

  const deleteWorker = async (workerId: number) => {
    try {
      await axios.delete(`http://localhost:8080/admin/${adminId}/workers/${workerId}`)
      setWorkers(prevWorkers => prevWorkers.filter(worker => worker.workerId !== workerId))
    } catch (error) {
      console.error('Error deleting worker:', error)
    }
  }

  const addWorker = async () => {
    try {
      const response = await axios.post(`http://localhost:8080/admin/${adminId}/workers`, newWorker)
      setWorkers(prevWorkers => [...prevWorkers, response.data])
      setNewWorker({
        name: '',
        phoneNumber: '',
        shortBio: '',
        deployed: false,
        tele_Id: '',
        curPropertyId: 0,
        available: false,
        supervisorId: 0
      })
    } catch (error) {
      console.error('Error adding worker:', error)
    }
  }

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
            <TableHead>Remove Worker</TableHead>
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
              <TableCell>
                <Button onClick={() => deleteWorker(worker.workerId)}>Remove</Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <div className="mt-4">
        <h2 className="text-xl font-bold mb-2">Add New Worker</h2>
        <input
          type="text"
          placeholder="Name"
          value={newWorker.name}
          onChange={(e) => setNewWorker({ ...newWorker, name: e.target.value })}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Phone Number"
          value={newWorker.phoneNumber}
          onChange={(e) => setNewWorker({ ...newWorker, phoneNumber: e.target.value })}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Short Bio"
          value={newWorker.shortBio}
          onChange={(e) => setNewWorker({ ...newWorker, shortBio: e.target.value })}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Telegram ID"
          value={newWorker.tele_Id}
          onChange={(e) => setNewWorker({ ...newWorker, tele_Id: e.target.value })}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Current Property ID"
          value={newWorker.curPropertyId}
          onChange={(e) => setNewWorker({ ...newWorker, curPropertyId: Number(e.target.value) })}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Supervisor ID"
          value={newWorker.supervisorId}
          onChange={(e) => setNewWorker({ ...newWorker, supervisorId: Number(e.target.value) })}
          className="border p-2 mb-2"
        />
        <div className="flex items-center mb-2">
          <label className="mr-2">Deployed</label>
          <input
            type="checkbox"
            checked={newWorker.deployed}
            onChange={(e) => setNewWorker({ ...newWorker, deployed: e.target.checked })}
          />
        </div>
        <div className="flex items-center mb-2">
          <label className="mr-2">Available</label>
          <input
            type="checkbox"
            checked={newWorker.available}
            onChange={(e) => setNewWorker({ ...newWorker, available: e.target.checked })}
          />
        </div>
        <Button onClick={addWorker}>Add Worker</Button>
      </div>
    </div>
  )
}