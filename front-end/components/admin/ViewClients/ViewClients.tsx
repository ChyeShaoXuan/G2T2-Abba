'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

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
  const [Clients, setClients] = useState<Client[]>([])
  const [newClient, setNewClient] = useState<Omit<Client, 'clientId'>>({
    name: '',
    phoneNumber: '',
    email: '',
    adminId: 0,
    packageId: 0,
    workerId: 0
  })
  const [editingClient, setEditingClient] = useState<Client | null>(null)

  useEffect(() => {
    // Fetch Clients from the backend
    const fetchClients = async () => {
      try {
        const clientsResponse = await axios.get(`http://localhost:8080/admin/clients`)
        
        setClients(clientsResponse.data)
      } catch (error) {
        console.error('Error fetching clients:', error)
      }
    }

    fetchClients()
  }, [])

  const deleteClient = async (client: Client) => {
    try {
      if (client.adminId === undefined || client.adminId === null) {
        console.error('Error: supervisorId is undefined or null')
        return
      }
      await axios.delete(`http://localhost:8080/admin/${client.adminId}/clients/${client.clientId}`)
      setClients(prevClients => prevClients.filter(w => w.clientId !== client.clientId))
    } catch (error) {
      console.error('Error deleting Client:', error)
    }
  }

  const updateClient = async (Client: Client) => {
    try {
      const response = await axios.put(`http://localhost:8080/admin/clients/${Client.clientId}`, Client)
      setClients(prevClients => prevClients.map(w => (w.clientId === Client.clientId ? response.data : w)))
      setEditingClient(null)
      window.location.reload()
    } catch (error) {
      console.error('Error updating Client:', error)
    }
  }

  const addClient = async () => {
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
      window.location.reload()
    } catch (error) {
      console.error('Error adding client:', error)
    }
  }

  const handleEditClick = (client: Client) => {
    setEditingClient(client)
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (editingClient) {
      const { name, value } = e.target
      setEditingClient({ ...editingClient, [name]: value })
    } else {
      const { name, value } = e.target
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
          {Clients.map(client => (
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
                  <input
                    type="number"
                    name="adminId"
                    value={editingClient.adminId}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  client.adminId
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <input
                    type="number"
                    name="packageId"
                    value={editingClient.packageId}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  client.packageId
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <input
                    type="number"
                    name="workerId"
                    value={editingClient.workerId}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  client.workerId
                )}
              </TableCell>
              <TableCell>
                {editingClient && editingClient.clientId === client.clientId ? (
                  <Button onClick={() => updateClient(editingClient)}>Save</Button>
                ) : (
                  <Button onClick={() => handleEditClick(client)}>Edit</Button>
                )}
                <Button onClick={() => deleteClient(client)}>Remove</Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <div className="mt-4">
        <h2 className="text-xl font-bold mb-2">Add New Client</h2>
        <input
          type="text"
          placeholder="Name"
          name="name"
          value={newClient.name}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Phone Number"
          name="phoneNumber"
          value={newClient.phoneNumber}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="text"
          placeholder="Email"
          name="email"
          value={newClient.email}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="number"
          placeholder="Admin ID"
          name="adminId"
          value={newClient.adminId}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="number"
          placeholder="Preferred Package(ID)"
          name="packageId"
          value={newClient.packageId}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <input
          type="number"
          placeholder="Preferred Worker(ID)"
          name="workerId"
          value={newClient.workerId}
          onChange={handleInputChange}
          className="border p-2 mb-2"
        />
        <Button onClick={addClient}>Add Client</Button>
      </div>
    </div>
  )
}