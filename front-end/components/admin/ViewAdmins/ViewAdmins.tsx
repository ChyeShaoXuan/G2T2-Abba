'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

interface UserDTO {
  userId: number
  name: string
  email: string
  phoneNumber: string
  role: string
}

export default function ViewAdmins() {
  const [users, setUsers] = useState<UserDTO[]>([])
  const [editingUser, setEditingUser] = useState<UserDTO | null>(null)

  useEffect(() => {
    // Fetch users from the backend
    const fetchUsers = async () => {
      try {
        const usersResponse = await axios.get(`http://localhost:8080/admin/users`)
        setUsers(usersResponse.data)
      } catch (error) {
        console.error('Error fetching users:', error)
      }
    }

    fetchUsers()
  }, [])

  const updateUserRole = async (user: UserDTO) => {
    try {
      const response = await axios.put(`http://localhost:8080/admin/users/${user.userId}/role`, null, {
        params: { role: user.role }
      })
      setUsers(prevUsers => prevUsers.map(u => (u.userId === user.userId ? response.data : u)))
      setEditingUser(null)
    } catch (error) {
      console.error('Error updating user role:', error)
    }
  }

  const handleEditClick = (user: UserDTO) => {
    setEditingUser(user)
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const { name, value } = e.target
    if (editingUser) {
      setEditingUser({ ...editingUser, [name]: value })
    }
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Admin Management</h1>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Name</TableHead>
            <TableHead>Email</TableHead>
            <TableHead>Phone Number</TableHead>
            <TableHead>Role</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {users.map(user => (
            <TableRow key={user.userId}>
              <TableCell>{user.name}</TableCell>
              <TableCell>{user.email}</TableCell>
              <TableCell>{user.phoneNumber}</TableCell>
              <TableCell>
                {editingUser && editingUser.userId === user.userId ? (
                  <select
                    name="role"
                    value={editingUser.role}
                    onChange={handleInputChange}
                    className="border p-2"
                  >
                    <option value="Admin">Admin</option>
                    <option value="Worker">Worker</option>
                  </select>
                ) : (
                  user.role
                )}
              </TableCell>
              <TableCell>
                {editingUser && editingUser.userId === user.userId ? (
                  <Button className="bg-blue-700 hover:bg-blue-900 mr-3" onClick={() => updateUserRole(editingUser)}>Save</Button>
                ) : (
                  <Button className="bg-blue-700 hover:bg-blue-900 mr-3" onClick={() => handleEditClick(user)}>Edit</Button>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}