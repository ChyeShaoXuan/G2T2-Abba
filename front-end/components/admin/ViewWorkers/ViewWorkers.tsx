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
  adminId: number
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
    adminId: 0
  })
  const [editingWorkerId, setEditingWorkerId] = useState<number | null>(null)
  const [editingWorker, setEditingWorker] = useState<Worker | null>(null)

  useEffect(() => {
    // Fetch workers from the backend
    const fetchWorkers = async () => {
      try {
        const workersResponse = await axios.get(`http://localhost:8080/admin/workers`)
        setWorkers(workersResponse.data)
        console.log(workersResponse.data)
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    fetchWorkers()
  }, [])

  const deleteWorker = async (worker: Worker) => {
    try {
      if (worker.adminId === undefined || worker.adminId === null) {
        console.error('Error: adminId is undefined or null')
        return
      }
      await axios.delete(`http://localhost:8080/admin/${worker.adminId}/workers/${worker.workerId}`)
      setWorkers(prevWorkers => prevWorkers.filter(w => w.workerId !== worker.workerId))
    } catch (error) {
      console.error('Error deleting worker:', error)
    }
  }

  const updateWorker = async (worker: Worker) => {
    try {
      const response = await axios.put(`http://localhost:8080/admin/workers/${worker.workerId}`, worker)
      setWorkers(prevWorkers => prevWorkers.map(w => (w.workerId === worker.workerId ? response.data : w)))
      setEditingWorkerId(null)
      setEditingWorker(null)
    } catch (error) {
      console.error('Error updating worker:', error)
    }
  }

  const addWorker = async () => {
    try {
      if (newWorker.adminId === 0) {
        console.error('Error: adminId is not set')
        return
      }
      const response = await axios.post(
        `http://localhost:8080/admin/${newWorker.adminId}/workers`,
        newWorker,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      )
      setWorkers(prevWorkers => [...prevWorkers, response.data])
      setNewWorker({
        name: '',
        phoneNumber: '',
        shortBio: '',
        deployed: false,
        tele_Id: '',
        curPropertyId: 0,
        available: false,
        adminId: 0
      })
    } catch (error) {
      console.error('Error adding worker:', error)
    }
  }

  const handleEditClick = (worker: Worker) => {
    setEditingWorkerId(worker.workerId)
    setEditingWorker(worker)
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (editingWorker) {
      const { name, value } = e.target
      setEditingWorker({ ...editingWorker, [name]: value })
    } else {
      const { name, value } = e.target
      setNewWorker({ ...newWorker, [name]: value })
    }
  }

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (editingWorker) {
      const { name, checked } = e.target
      setEditingWorker({ ...editingWorker, [name]: checked })
    } else {
      const { name, checked } = e.target
      setNewWorker({ ...newWorker, [name]: checked })
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
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {workers.map(worker => (
            <TableRow key={worker.workerId}>
              <TableCell>
                {editingWorkerId === worker.workerId ? (
                  <input
                    type="text"
                    name="name"
                    value={editingWorker?.name || ''}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.name
                )}
              </TableCell>
              <TableCell>
                {editingWorkerId === worker.workerId ? (
                  <input
                    type="text"
                    name="phoneNumber"
                    value={editingWorker?.phoneNumber || ''}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.phoneNumber
                )}
              </TableCell>
              <TableCell>
                {editingWorkerId === worker.workerId ? (
                  <input
                    type="text"
                    name="shortBio"
                    value={editingWorker?.shortBio || ''}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.shortBio
                )}
              </TableCell>
              <TableCell>
                {editingWorkerId === worker.workerId ? (
                  <input
                    type="checkbox"
                    name="deployed"
                    checked={editingWorker?.deployed || false}
                    onChange={handleCheckboxChange}
                  />
                ) : (
                  worker.deployed ? 'Yes' : 'No'
                )}
              </TableCell>
              <TableCell>
                {editingWorkerId === worker.workerId ? (
                  <input
                    type="text"
                    name="tele_Id"
                    value={editingWorker?.tele_Id || ''}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.tele_Id
                )}
              </TableCell>
              <TableCell>
                {editingWorkerId === worker.workerId ? (
                  <input
                    type="number"
                    name="curPropertyId"
                    value={editingWorker?.curPropertyId || 0}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.curPropertyId
                )}
              </TableCell>
              <TableCell>
                {editingWorkerId === worker.workerId ? (
                  <input
                    type="checkbox"
                    name="available"
                    checked={editingWorker?.available || false}
                    onChange={handleCheckboxChange}
                  />
                ) : (
                  worker.available ? 'Yes' : 'No'
                )}
              </TableCell>
              <TableCell>{worker.adminId}</TableCell>
              <TableCell>
                {editingWorkerId === worker.workerId ? (
                  <Button className="bg-blue-700 hover:bg-blue-900 ml-2" onClick={() => editingWorker && updateWorker(editingWorker)}>Save</Button>
                ) : (
                  <Button className="bg-blue-700 hover:bg-blue-900 ml-2" onClick={() => handleEditClick(worker)}>Edit</Button>
                )}
                <Button className="bg-blue-700 hover:bg-blue-900 ml-2" onClick={() => deleteWorker(worker)}>Remove</Button>
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
          name="name"
          value={newWorker.name}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Phone Number"
          name="phoneNumber"
          value={newWorker.phoneNumber}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Short Bio"
          name="shortBio"
          value={newWorker.shortBio}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Telegram ID"
          name="tele_Id"
          value={newWorker.tele_Id}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="number"
          placeholder="Current Property ID"
          name="curPropertyId"
          value={newWorker.curPropertyId}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="number"
          placeholder="Supervisor ID"
          name="adminId"
          value={newWorker.adminId}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <div className="flex items-center mb-2">
          <label className="mr-2">Deployed</label>
          <input
            type="checkbox"
            name="deployed"
            checked={newWorker.deployed}
            onChange={handleCheckboxChange}
          />
        </div>
        <div className="flex items-center mb-2">
          <label className="mr-2">Available</label>
          <input
            type="checkbox"
            name="available"
            checked={newWorker.available}
            onChange={handleCheckboxChange}
          />
        </div>
        <Button className="bg-blue-700 hover:bg-blue-900" onClick={addWorker}>Add Worker</Button>
      </div>
    </div>
  )
}