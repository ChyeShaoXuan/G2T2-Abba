'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"

interface Client {
  clientId: number,
  name: string,
  phoneNumber: string,
  email: string,
  adminId: number,
  packageId: number,
  workerId: number
}

export default function ViewClients() {
  const [clients, setClients] = useState<Client[]>([])
  const [newClient, setNewClient] = useState<Omit<Client, 'clientId'>>({
    name: '',
    phoneNumber: '',
    email: '',
    adminId: 0,
    packageId: 0,
    workerId: 0
  })
  const [editingClient, setEditingClient] = useState<Client | null>(null)
  const [adminIds, setAdminIds] = useState<number[]>([])
  const [workerIds, setWorkerIds] = useState<number[]>([])
  const [packageIds, setPackageIds] = useState<number[]>([])
  const [errors, setErrors] = useState<{ [key: string]: string }>({})

  useEffect(() => {
    // Fetch clients from the backend
    const fetchClients = async () => {
      try {
        const clientsResponse = await axios.get(`http://localhost:8080/admin/clients`)
        setClients(clientsResponse.data)
      } catch (error) {
        console.error('Error fetching clients:', error)
      }
    }

    // Fetch unique admin IDs
    const fetchAdminIds = async () => {
      try {
        const adminIdsResponse = await axios.get(`http://localhost:8080/admin/unique_admin_ids`)
        setAdminIds(adminIdsResponse.data)
      } catch (error) {
        console.error('Error fetching admin IDs:', error)
      }
    }

    // Fetch unique worker IDs
    const fetchWorkerIds = async () => {
      try {
        const workerIdsResponse = await axios.get(`http://localhost:8080/admin/unique_worker_ids`)
        setWorkerIds(workerIdsResponse.data)
      } catch (error) {
        console.error('Error fetching worker IDs:', error)
      }
    }

    // Fetch unique package IDs
    const fetchPackageIds = async () => {
      try {
        const packageIdsResponse = await axios.get(`http://localhost:8080/admin/unique_package_ids`)
        setPackageIds(packageIdsResponse.data)
      } catch (error) {
        console.error('Error fetching package IDs:', error)
      }
    }

    fetchClients()
    fetchAdminIds()
    fetchWorkerIds()
    fetchPackageIds()
  }, [])

  const deleteClient = async (client: Client) => {
    try {
      if (client.adminId === undefined || client.adminId === null) {
        console.error('Error: adminId is undefined or null')
        return
      }
      await axios.delete(`http://localhost:8080/admin/${client.adminId}/clients/${client.clientId}`)
      setClients(prevClients => prevClients.filter(w => w.clientId !== client.clientId))
    } catch (error) {
      console.error('Error deleting client:', error)
    }
  }

  const updateClient = async (client: Client) => {
    try {
      const response = await axios.put(`http://localhost:8080/admin/clients/${client.clientId}`, client)
      setClients(prevClients => prevClients.map(w => (w.clientId === client.clientId ? response.data : w)))
      setEditingClient(null)
      window.location.reload()
    } catch (error) {
      console.error('Error updating client:', error)
    }
  }

  const validateInputs = () => {
    const newErrors: { [key: string]: string } = {}
    if (!newClient.name) newErrors.name = 'Name is required'
    if (!newClient.phoneNumber) newErrors.phoneNumber = 'Phone number is required'
    if (!newClient.email) newErrors.email = 'Email is required'
    if (!newClient.adminId) newErrors.adminId = 'Admin ID is required'
    if (!newClient.packageId) newErrors.packageId = 'Package ID is required'
    if (!newClient.workerId) newErrors.workerId = 'Worker ID is required'
    return newErrors
  }

  const addClient = async () => {
    const newErrors = validateInputs()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    try {
      if (newClient.adminId === 0) {
        console.error('Error: adminId is not set')
        return
      }
      const response = await axios.post(`http://localhost:8080/admin/${newClient.adminId}/clients`, newClient)
      setClients(prevClients => [...prevClients, response.data])
      setNewClient({
        name: '',
        phoneNumber: '',
        email: '',
        adminId: 0,
        packageId: 0,
        workerId: 0
      })
      setErrors({})
      window.location.reload()
    } catch (error) {
      console.error('Error adding client:', error)
    }
  }

  const handleEditClick = (client: Client) => {
    setEditingClient(client)
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    if (editingClient) {
      setEditingClient({ ...editingClient, [name]: value })
    } else {
      setNewClient({ ...newClient, [name]: value })
    }
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Client Records</h1>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Email</TableHead>
            <TableHead>Name</TableHead>
            <TableHead>Phone Number</TableHead>
            <TableHead>Admin ID</TableHead>
            <TableHead>Preferred Package</TableHead>
            <TableHead>Preferred Worker</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {clients.map(client => (
            <TableRow key={client.clientId}>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <input
                    type="text"
                    name="email"
                    value={editingClient.email}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  client.email
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <input
                    type="text"
                    name="name"
                    value={editingClient.name}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  client.name
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <input
                    type="text"
                    name="phoneNumber"
                    value={editingClient.phoneNumber}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  client.phoneNumber
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <select
                    name="adminId"
                    value={editingClient.adminId}
                    onChange={handleInputChange}
                    className="border p-2"
                  >
                    <option value="">Select Admin ID</option>
                    {adminIds.map(id => (
                      <option key={id} value={id}>{id}</option>
                    ))}
                  </select>
                ) : (
                  client.adminId
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <select
                    name="packageId"
                    value={editingClient.packageId}
                    onChange={handleInputChange}
                    className="border p-2"
                  >
                    <option value="">Select Package ID</option>
                    {packageIds.map(id => (
                      <option key={id} value={id}>{id}</option>
                    ))}
                  </select>
                ) : (
                  client.packageId
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <select
                    name="workerId"
                    value={editingClient.workerId}
                    onChange={handleInputChange}
                    className="border p-2"
                  >
                    <option value="">Select Worker ID</option>
                    {workerIds.map(id => (
                      <option key={id} value={id}>{id}</option>
                    ))}
                  </select>
                ) : (
                  client.workerId
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <Button className="bg-blue-700 hover:bg-blue-900 mr-3" onClick={() => updateClient(editingClient)}>Save</Button>
                ) : (
                  <Button className="bg-blue-700 hover:bg-blue-900 mr-3" onClick={() => handleEditClick(client)}>Edit</Button>
                )}
                <Button className="bg-blue-700 hover:bg-blue-900 ml-2" onClick={() => deleteClient(client)}>Remove</Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <div className="mt-8">
        <Card className="w-full max-w-2xl mx-auto">
          <CardHeader>
            <CardTitle className="text-2xl font-bold">Add New Client</CardTitle>
          </CardHeader>
          <CardContent>
            <form className="grid gap-4 sm:grid-cols-2">
              <div className="space-y-2">
                <label htmlFor="name" className="text-sm font-medium">Name</label>
                <input
                  id="name"
                  type="text"
                  name="name"
                  value={newClient.name}
                  onChange={handleInputChange}
                  placeholder="Full Name"
                  className="w-full p-2 border rounded"
                />
                {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
              </div>
              <div className="space-y-2">
                <label htmlFor="phoneNumber" className="text-sm font-medium">Phone Number</label>
                <input
                  id="phoneNumber"
                  type="text"
                  name="phoneNumber"
                  value={newClient.phoneNumber}
                  onChange={handleInputChange}
                  placeholder="Phone Number"
                  className="w-full p-2 border rounded"
                />
                {errors.phoneNumber && <p className="text-red-500 text-sm">{errors.phoneNumber}</p>}
              </div>
              <div className="space-y-2 sm:col-span-2">
                <label htmlFor="email" className="text-sm font-medium">Email</label>
                <input
                  id="email"
                  type="email"
                  name="email"
                  value={newClient.email}
                  onChange={handleInputChange}
                  placeholder="Email"
                  className="w-full p-2 border rounded"
                />
                {errors.email && <p className="text-red-500 text-sm">{errors.email}</p>}
              </div>
              <div className="space-y-2">
                <label htmlFor="adminId" className="text-sm font-medium">Admin ID</label>
                <select
                  id="adminId"
                  name="adminId"
                  value={newClient.adminId}
                  onChange={handleInputChange}
                  className="w-full p-2 border rounded"
                >
                  <option value="">Select Admin ID</option>
                  {adminIds.map(id => (
                    <option key={id} value={id}>{id}</option>
                  ))}
                </select>
                {errors.adminId && <p className="text-red-500 text-sm">{errors.adminId}</p>}
              </div>
              <div className="space-y-2">
                <label htmlFor="packageId" className="text-sm font-medium">Package ID</label>
                <select
                  id="packageId"
                  name="packageId"
                  value={newClient.packageId}
                  onChange={handleInputChange}
                  className="w-full p-2 border rounded"
                >
                  <option value="">Select Package ID</option>
                  {packageIds.map(id => (
                    <option key={id} value={id}>{id}</option>
                  ))}
                </select>
                {errors.packageId && <p className="text-red-500 text-sm">{errors.packageId}</p>}
              </div>
              <div className="space-y-2">
                <label htmlFor="workerId" className="text-sm font-medium">Worker ID</label>
                <select
                  id="workerId"
                  name="workerId"
                  value={newClient.workerId}
                  onChange={handleInputChange}
                  className="w-full p-2 border rounded"
                >
                  <option value="">Select Worker ID</option>
                  {workerIds.map(id => (
                    <option key={id} value={id}>{id}</option>
                  ))}
                </select>
                {errors.workerId && <p className="text-red-500 text-sm">{errors.workerId}</p>}
              </div>
              <Button className="sm:col-span-2 bg-blue-700 hover:bg-blue-900" onClick={addClient}>
                Add Client
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}