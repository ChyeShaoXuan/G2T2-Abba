'use client'

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Button } from "@/components/ui/button"


// Mock data - replace with actual data fetching in a real application
const monthlyStats = [
  { month: 'Jan', jobs: 120, clients: 80, newJobs: 20, newClients: 10, terminatedClients: 5, cancellations: 8, reschedules: 15 },
  { month: 'Feb', jobs: 135, clients: 85, newJobs: 25, newClients: 12, terminatedClients: 3, cancellations: 10, reschedules: 18 },
  { month: 'Mar', jobs: 150, clients: 90, newJobs: 30, newClients: 15, terminatedClients: 4, cancellations: 12, reschedules: 20 },
]

const workerHours = [
  { id: 'w1', name: 'John Doe', weeklyHours: 40, monthlyHours: 160, annualHours: 1920, overtimeHours: 10 },
  { id: 'w2', name: 'Jane Smith', weeklyHours: 35, monthlyHours: 140, annualHours: 1680, overtimeHours: 5 },
  { id: 'w3', name: 'Bob Johnson', weeklyHours: 38, monthlyHours: 152, annualHours: 1824, overtimeHours: 8 },
]


export default function JobStatisticsDashboard() {
  const [selectedMonth, setSelectedMonth] = useState('Mar')

  const exportToCSV = () => {
    // Implementation for exporting data to CSV
    console.log('Exporting data to CSV...')
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Admin Statistics Dashboard</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
        <Card>
          <CardHeader>
            <CardTitle>Monthly Overview</CardTitle>
          </CardHeader>
          <CardContent>
            <Select onValueChange={setSelectedMonth} defaultValue={selectedMonth}>
              <SelectTrigger>
                <SelectValue placeholder="Select month" />
              </SelectTrigger>
              <SelectContent>
                {monthlyStats.map(stat => (
                  <SelectItem key={stat.month} value={stat.month}>{stat.month}</SelectItem>
                ))}
              </SelectContent>
            </Select>
            <div className="mt-4">
              {monthlyStats.find(stat => stat.month === selectedMonth) && (
                <ul>
                  <li>Jobs: {monthlyStats.find(stat => stat.month === selectedMonth)?.jobs}</li>
                  <li>Clients: {monthlyStats.find(stat => stat.month === selectedMonth)?.clients}</li>
                  <li>New Jobs: {monthlyStats.find(stat => stat.month === selectedMonth)?.newJobs}</li>
                  <li>New Clients: {monthlyStats.find(stat => stat.month === selectedMonth)?.newClients}</li>
                  <li>Terminated Clients: {monthlyStats.find(stat => stat.month === selectedMonth)?.terminatedClients}</li>
                  <li>Cancellations: {monthlyStats.find(stat => stat.month === selectedMonth)?.cancellations}</li>
                  <li>Reschedules: {monthlyStats.find(stat => stat.month === selectedMonth)?.reschedules}</li>
                </ul>
              )}
            </div>
          </CardContent>
        </Card>


        <Card>
          <CardHeader>
            <CardTitle>Worker Hours</CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Name</TableHead>
                  <TableHead>Weekly</TableHead>
                  <TableHead>Monthly</TableHead>
                  <TableHead>Annual</TableHead>
                  <TableHead>Overtime</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {workerHours.map(worker => (
                  <TableRow key={worker.id}>
                    <TableCell>{worker.name}</TableCell>
                    <TableCell>{worker.weeklyHours}</TableCell>
                    <TableCell>{worker.monthlyHours}</TableCell>
                    <TableCell>{worker.annualHours}</TableCell>
                    <TableCell>{worker.overtimeHours}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>

      <Button onClick={exportToCSV}>Export to CSV</Button>
    </div>
  )
}