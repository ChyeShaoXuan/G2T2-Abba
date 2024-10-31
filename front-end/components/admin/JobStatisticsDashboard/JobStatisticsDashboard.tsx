'use client'

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { useState, useEffect } from 'react'
import { Button } from "@/components/ui/button"
import axios from 'axios'
import Papa from 'papaparse'

interface Worker {
  workerId: number
  name: string
}

interface WorkerHours {
  workerHoursId: number
  worker: Worker
  monthYear: string
  totalHoursWorked: number
  overtimeHours: number
}

interface StatsDTO {
  monthYear: string
  totalHours: number
  totalCleaningTasks: number
  totalClients: number
  totalProperties: number
  totalWorkers: number
  totalPackages: number
  alCount: number
  mcCount: number
  hlCount: number
  elCount: number
}

export default function JobStatisticsDashboard() {
  const [selectedMonth, setSelectedMonth] = useState<string>('')
  const [monthlyStats, setMonthlyStats] = useState<StatsDTO[]>([])
  const [workers, setWorkers] = useState<Worker[]>([])
  const [workerHours, setWorkerHours] = useState<WorkerHours[]>([])
  const [selectedWorkerId, setSelectedWorkerId] = useState<number | null>(null)
  const [viewMode, setViewMode] = useState<'monthly' | 'annual'>('monthly')

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const response = await axios.get('http://localhost:8080/admin/stats')
        const statsData = Object.values(response.data) as StatsDTO[]
        setMonthlyStats(statsData)
        if (statsData.length > 0) {
          setSelectedMonth(statsData[0].monthYear)
        }
      } catch (error) {
        console.error('Error fetching stats:', error)
      }
    }

    const fetchWorkers = async () => {
      try {
        const response = await axios.get('http://localhost:8080/admin/workers')
        setWorkers(response.data)
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    fetchStats()
    fetchWorkers()
  }, [])

  useEffect(() => {
    const fetchWorkerHours = async () => {
      if (selectedWorkerId !== null) {
        try {
          const response = await axios.get(`http://localhost:8080/admin/${selectedWorkerId}/hours`)
          setWorkerHours(response.data)
        } catch (error) {
          console.error('Error fetching worker hours:', error)
        }
      }
    }

    fetchWorkerHours()
  }, [selectedWorkerId])

  const exportMonthlyStatsToCSV = () => {
    const csvData = monthlyStats.map(stat => ({
      MonthYear: stat.monthYear,
      TotalHours: stat.totalHours,
      TotalCleaningTasks: stat.totalCleaningTasks,
      TotalClients: stat.totalClients,
      TotalProperties: stat.totalProperties,
      TotalWorkers: stat.totalWorkers,
      TotalPackages: stat.totalPackages,
      AnnualLeaveCount: stat.alCount,
      MedicalLeaveCount: stat.mcCount,
      HolidayLeaveCount: stat.hlCount,
      EmergencyLeaveCount: stat.elCount
    }))

    const csv = Papa.unparse(csvData)
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    const url = URL.createObjectURL(blob)
    link.setAttribute('href', url)
    link.setAttribute('download', 'monthly_stats.csv')
    link.style.visibility = 'hidden'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }

  const exportWorkerHoursToCSV = () => {
    let csvData = []

    if (viewMode === 'monthly') {
      csvData = workerHours.map(wh => ({
        WorkerName: wh.worker.name,
        MonthYear: wh.monthYear,
        TotalHoursWorked: wh.totalHoursWorked,
        OvertimeHours: wh.overtimeHours
      }))
    } else if (viewMode === 'annual' && selectedWorkerId !== null) {
      const annualHours = getAnnualHours(selectedWorkerId)
      const annualOvertimeHours = workerHours
        .filter(wh => wh.worker.workerId === selectedWorkerId)
        .reduce((total, wh) => total + wh.overtimeHours, 0)

      csvData = [{
        WorkerName: getWorkerNameById(selectedWorkerId),
        Year: 'Annual',
        TotalHoursWorked: annualHours,
        OvertimeHours: annualOvertimeHours
      }]
    }

    const csv = Papa.unparse(csvData)
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    const url = URL.createObjectURL(blob)
    link.setAttribute('href', url)
    link.setAttribute('download', viewMode === 'monthly' ? 'worker_hours_monthly.csv' : 'worker_hours_annual.csv')
    link.style.visibility = 'hidden'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }

  const getWorkerNameById = (workerId: number) => {
    const worker = workers.find(w => w.workerId === workerId)
    return worker ? worker.name : ''
  }

  const getAnnualHours = (workerId: number) => {
    return workerHours
      .filter(wh => wh.worker.workerId === workerId)
      .reduce((total, wh) => total + wh.totalHoursWorked, 0)
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
                  <SelectItem key={stat.monthYear} value={stat.monthYear}>{stat.monthYear}</SelectItem>
                ))}
              </SelectContent>
            </Select>
            <div className="mt-4">
              {monthlyStats.find(stat => stat.monthYear === selectedMonth) && (
                <ul>
                  <li>Jobs: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.totalCleaningTasks}</li>
                  <li>Clients: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.totalClients}</li>
                  <li>Total Hours: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.totalHours}</li>
                  <li>Total Properties: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.totalProperties}</li>
                  <li>Total Workers: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.totalWorkers}</li>
                  <li>Total Packages: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.totalPackages}</li>
                  <li>Annual Leave Count: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.alCount}</li>
                  <li>Medical Leave Count: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.mcCount}</li>
                  <li>Holiday Leave Count: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.hlCount}</li>
                  <li>Emergency Leave Count: {monthlyStats.find(stat => stat.monthYear === selectedMonth)?.elCount}</li>
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
            <Select onValueChange={(value) => setSelectedWorkerId(Number(value))} defaultValue="">
              <SelectTrigger>
                <SelectValue placeholder="Select worker" />
              </SelectTrigger>
              <SelectContent>
                {workers.map(worker => (
                  <SelectItem key={worker.workerId} value={worker.workerId.toString()}>{worker.name}</SelectItem>
                ))}
              </SelectContent>
            </Select>
            <div className="mt-4">
              <Button onClick={() => setViewMode('monthly')} className={`mr-2 ${viewMode === 'monthly' ? 'bg-blue-500' : 'bg-gray-500'}`}>Monthly</Button>
              <Button onClick={() => setViewMode('annual')} className={`${viewMode === 'annual' ? 'bg-blue-500' : 'bg-gray-500'}`}>Annual</Button>
            </div>
            <Table className="mt-4">
              <TableHeader>
                <TableRow>
                  <TableHead>Month/Year</TableHead>
                  <TableHead>Total Hours</TableHead>
                  <TableHead>Overtime Hours</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {viewMode === 'monthly' && workerHours.filter(wh => wh.worker.workerId === selectedWorkerId).map(wh => (
                  <TableRow key={wh.workerHoursId}>
                    <TableCell>{wh.monthYear}</TableCell>
                    <TableCell>{wh.totalHoursWorked}</TableCell>
                    <TableCell>{wh.overtimeHours}</TableCell>
                  </TableRow>
                ))}
                {viewMode === 'annual' && selectedWorkerId !== null && (
                  <TableRow>
                    <TableCell>Annual</TableCell>
                    <TableCell>{getAnnualHours(selectedWorkerId)}</TableCell>
                    <TableCell>{workerHours.filter(wh => wh.worker.workerId === selectedWorkerId).reduce((total, wh) => total + wh.overtimeHours, 0)}</TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>

      <Button className="ml-4 bg-green-600" onClick={exportMonthlyStatsToCSV}>Export Monthly Stats to CSV</Button>
      <Button className="ml-4 bg-green-600" onClick={exportWorkerHoursToCSV}>Export Worker Hours to CSV</Button>
    </div>
  )
}