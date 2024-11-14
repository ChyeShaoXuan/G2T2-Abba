'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"

interface Worker {
  workerId: number
  name: string
  phoneNumber: string
  shortBio: string
  deployed: boolean
  curPropertyId: number
  available: boolean
  emailID: string
  adminId: number
}

export default function ViewWorkers() {
  const [workers, setWorkers] = useState<Worker[]>([])
  const [newWorker, setNewWorker] = useState<Omit<Worker, 'workerId'>>({
    name: '',
    phoneNumber: '',
    shortBio: '',
    deployed: false,
    curPropertyId: 0,
    available: true,
    emailID: '',
    adminId: 0
  })
  const [editingWorker, setEditingWorker] = useState<Worker | null>(null)
  const [adminIds, setAdminIds] = useState<number[]>([])
  const [propertyIds, setPropertyIds] = useState<number[]>([])

  useEffect(() => {
    const fetchWorkers = async () => {
      try {
        const workersResponse = await axios.get(`http://localhost:8080/admin/workers_admin`)
        setWorkers(workersResponse.data)
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    const fetchAdminIds = async () => {
      try {
        const adminIdsResponse = await axios.get(`http://localhost:8080/admin/unique_admin_ids`)
        setAdminIds(adminIdsResponse.data)
      } catch (error) {
        console.error('Error fetching admin IDs:', error)
      }
    }

    const fetchPropertyIds = async () => {
      try {
        const propertyIdsResponse = await axios.get(`http://localhost:8080/admin/unique_property_ids`)
        setPropertyIds(propertyIdsResponse.data)
      } catch (error) {
        console.error('Error fetching property IDs:', error)
      }
    }

    fetchWorkers()
    fetchAdminIds()
    fetchPropertyIds()
  }, [])

  const deleteWorker = async (worker: Worker) => {
    try {
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
      setEditingWorker(null)
    } catch (error) {
      console.error('Error updating worker:', error)
    }
  }

  const addWorker = async () => {
    try {
      const response = await axios.post(`http://localhost:8080/admin/${newWorker.adminId}/workers`, newWorker)
      setWorkers(prevWorkers => [...prevWorkers, response.data])
      setNewWorker({
        name: '',
        phoneNumber: '',
        shortBio: '',
        deployed: false,
        emailID: '',
        curPropertyId: 0,
        available: true,
        adminId: 0
      })
    } catch (error) {
      console.error('Error adding worker:', error)
    }
  }

  const handleEditClick = (worker: Worker) => {
    setEditingWorker(worker)
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type, checked } = e.target as HTMLInputElement;
    const newValue = type === 'checkbox' ? checked : value;
    if (editingWorker) {
      setEditingWorker({ ...editingWorker, [name]: newValue });
    } else {
      setNewWorker({ ...newWorker, [name]: newValue });
    }
  };

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
            <TableHead>Email</TableHead>
            <TableHead>Current Property ID</TableHead>
            <TableHead>Available</TableHead>
            <TableHead>Admin ID</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {workers.map(worker => (
            <TableRow key={worker.workerId}>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <input
                    type="text"
                    name="name"
                    value={editingWorker.name}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.name
                )}
              </TableCell>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <input
                    type="text"
                    name="phoneNumber"
                    value={editingWorker.phoneNumber}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.phoneNumber
                )}
              </TableCell>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <input
                    type="text"
                    name="shortBio"
                    value={editingWorker.shortBio}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.shortBio
                )}
              </TableCell>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <input
                    type="checkbox"
                    name="deployed"
                    checked={editingWorker.deployed}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.deployed ? 'Yes' : 'No'
                )}
              </TableCell>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <input
                    type="text"
                    name="emailID"
                    value={editingWorker.emailID}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.emailID
                )}
              </TableCell>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <select
                    name="curPropertyId"
                    value={editingWorker.curPropertyId}
                    onChange={handleInputChange}
                    className="border p-2"
                  >
                    <option value="">Select Property ID</option>
                    {propertyIds.map(id => (
                      <option key={id} value={id}>{id}</option>
                    ))}
                  </select>
                ) : (
                  worker.curPropertyId
                )}
              </TableCell>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <input
                    type="checkbox"
                    name="available"
                    checked={editingWorker.available}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  worker.available ? 'Yes' : 'No'
                )}
              </TableCell>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <select
                    name="adminId"
                    value={editingWorker.adminId}
                    onChange={handleInputChange}
                    className="border p-2"
                  >
                    <option value="">Select Admin ID</option>
                    {adminIds.map(id => (
                      <option key={id} value={id}>{id}</option>
                    ))}
                  </select>
                ) : (
                  worker.adminId
                )}
              </TableCell>
              <TableCell>
                {editingWorker && editingWorker.workerId === worker.workerId ? (
                  <Button className="bg-blue-700 hover:bg-blue-900 mr-3" onClick={() => updateWorker(editingWorker)}>Save</Button>
                ) : (
                  <Button className="bg-blue-700 hover:bg-blue-900 mr-3" onClick={() => handleEditClick(worker)}>Edit</Button>
                )}
                <Button className="bg-blue-700 hover:bg-blue-900 ml-2" onClick={() => deleteWorker(worker)}>Remove</Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <div className="mt-8">
        <Card className="w-full max-w-2xl mx-auto">
          <CardHeader>
            <CardTitle className="text-2xl font-bold">Add New Worker</CardTitle>
          </CardHeader>
          <CardContent>
            <form className="grid gap-4 sm:grid-cols-2">
              <div className="space-y-2">
                <label htmlFor="name" className="text-sm font-medium">Name</label>
                <input
                  id="name"
                  type="text"
                  name="name"
                  value={newWorker.name}
                  onChange={handleInputChange}
                  placeholder="Full Name"
                  className="w-full p-2 border rounded"
                />
              </div>
              <div className="space-y-2">
                <label htmlFor="phoneNumber" className="text-sm font-medium">Phone Number</label>
                <input
                  id="phoneNumber"
                  type="text"
                  name="phoneNumber"
                  value={newWorker.phoneNumber}
                  onChange={handleInputChange}
                  placeholder="Phone Number"
                  className="w-full p-2 border rounded"
                />
              </div>
              <div className="space-y-2 sm:col-span-2">
                <label htmlFor="shortBio" className="text-sm font-medium">Short Bio</label>
                <input
                  id="shortBio"
                  type="text"
                  name="shortBio"
                  value={newWorker.shortBio}
                  onChange={handleInputChange}
                  placeholder="Short Bio"
                  className="w-full p-2 border rounded"
                />
              </div>
              <div className="space-y-2">
                <label htmlFor="emailID" className="text-sm font-medium">Email</label>
                <input
                  id="emailID"
                  type="text"
                  name="emailID"
                  value={newWorker.emailID}
                  onChange={handleInputChange}
                  placeholder="Email"
                  className="w-full p-2 border rounded"
                />
              </div>
              <div className="space-y-2">
                <label htmlFor="curPropertyId" className="text-sm font-medium">Property ID</label>
                <select
                  id="curPropertyId"
                  name="curPropertyId"
                  value={newWorker.curPropertyId}
                  onChange={handleInputChange}
                  className="w-full p-2 border rounded"
                >
                  <option value="">Select Property ID</option>
                  {propertyIds.map(id => (
                    <option key={id} value={id}>{id}</option>
                  ))}
                </select>
              </div>
              <div className="space-y-2">
                <label htmlFor="adminId" className="text-sm font-medium">Admin ID</label>
                <select
                  id="adminId"
                  name="adminId"
                  value={newWorker.adminId}
                  onChange={handleInputChange}
                  className="w-full p-2 border rounded"
                >
                  <option value="">Select Admin ID</option>
                  {adminIds.map(id => (
                    <option key={id} value={id}>{id}</option>
                  ))}
                </select>
              </div>
              <div className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  id="deployed"
                  name="deployed"
                  checked={newWorker.deployed}
                  onChange={handleInputChange}
                  className="border rounded"
                />
                <label htmlFor="deployed" className="text-sm font-medium">Deployed</label>
              </div>
              <div className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  id="available"
                  name="available"
                  checked={newWorker.available}
                  onChange={handleInputChange}
                  className="border rounded"
                />
                <label htmlFor="available" className="text-sm font-medium">Available</label>
              </div>
              <Button className="sm:col-span-2 bg-blue-700 hover:bg-blue-900" onClick={addWorker}>
                Add Worker
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}