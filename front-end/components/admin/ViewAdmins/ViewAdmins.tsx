'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"

interface User {
  userId: number,
  name: string,
  email: string,
  role: string
}

export default function ViewAdmins() {
  const [users, setUsers] = useState<User[]>([])
  const [editingUser, setEditingUser] = useState<User | null>(null)
  const [errors, setErrors] = useState<{ [key: string]: string }>({})

  useEffect(() => {
    fetchUsers()
  }, [])

  const fetchUsers = async () => {
    try {
      const usersResponse = await axios.get('http://localhost:8080/admin/users')
      setUsers(usersResponse.data)
    } catch (error) {
      console.error('Error fetching users:', error)
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    if (editingUser) {
      setEditingUser({ ...editingUser, [name]: value })
    }
  }

  const updateUserRole = async (user: User) => {
    try {
      await axios.put(`http://localhost:8080/admin/users/${user.userId}/role`, { role: user.role }, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      setEditingUser(null)
      fetchUsers() // Re-fetch users to get the updated data
    } catch (error) {
      console.error('Error updating user role:', error)
    }
  }

  const handleEditClick = (user: User) => {
    setEditingUser(user)
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Admin Records</h1>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Email</TableHead>
            <TableHead>Name</TableHead>
            <TableHead>Role</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {users.map(user => (
            <TableRow key={user.userId}>
              <TableCell>
                {editingUser && editingUser.userId === user.userId ? (
                  <input
                    type="text"
                    name="email"
                    value={editingUser.email}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  user.email
                )}
              </TableCell>
              <TableCell>
                {editingUser && editingUser.userId === user.userId ? (
                  <input
                    type="text"
                    name="name"
                    value={editingUser.name}
                    onChange={handleInputChange}
                    className="border p-2"
                  />
                ) : (
                  user.name
                )}
              </TableCell>
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