'use client'

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { useState, useEffect } from 'react'
import { Button } from "@/components/ui/button"
import axios from 'axios'
import Papa from 'papaparse'
import { BarChart, Bar, XAxis, YAxis, Tooltip, Legend, LineChart, Line, ResponsiveContainer } from 'recharts'

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

interface WorkerHoursCSV {
  WorkerName: string
  MonthYear: string
  TotalHoursWorked: number
  OvertimeHours: number
}

interface AnnualWorkerHoursCSV {
  WorkerName: string
  Year: string
  TotalHoursWorked: number
  OvertimeHours: number
}

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042']

export default function JobStatisticsDashboard() {
  const [selectedMonth, setSelectedMonth] = useState<string>('')
  const [monthlyStats, setMonthlyStats] = useState<StatsDTO[]>([])
  const [workers, setWorkers] = useState<Worker[]>([])
  const [workerHours, setWorkerHours] = useState<WorkerHours[]>([])
  const [selectedWorkerId, setSelectedWorkerId] = useState<number | null>(null)
  const [viewMode, setViewMode] = useState<'monthly' | 'annual'>('monthly')
  const [isClient, setIsClient] = useState(false)

  useEffect(() => {
    setIsClient(true)
  }, [])

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
    if (viewMode === 'monthly') {
      const csvData: WorkerHoursCSV[] = workerHours.map(wh => ({
        WorkerName: wh.worker.name,
        MonthYear: wh.monthYear,
        TotalHoursWorked: wh.totalHoursWorked,
        OvertimeHours: wh.overtimeHours
      }))
      const csv = Papa.unparse(csvData)
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      const url = URL.createObjectURL(blob)
      link.setAttribute('href', url)
      link.setAttribute('download', 'worker_hours_monthly.csv')
      link.style.visibility = 'hidden'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    } else if (viewMode === 'annual' && selectedWorkerId !== null) {
      const annualHours = getAnnualHours(selectedWorkerId)
      const annualOvertimeHours = workerHours
        .filter(wh => wh.worker.workerId === selectedWorkerId)
        .reduce((total, wh) => total + wh.overtimeHours, 0)

      const csvData: AnnualWorkerHoursCSV[] = [{
        WorkerName: getWorkerNameById(selectedWorkerId),
        Year: 'Annual',
        TotalHoursWorked: annualHours,
        OvertimeHours: annualOvertimeHours
      }]
      const csv = Papa.unparse(csvData)
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      const url = URL.createObjectURL(blob)
      link.setAttribute('href', url)
      link.setAttribute('download', 'worker_hours_annual.csv')
      link.style.visibility = 'hidden'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    }
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

  const selectedMonthStats = monthlyStats.find(stat => stat.monthYear === selectedMonth)
  const filteredWorkerHours = workerHours.filter(wh => wh.worker.workerId === selectedWorkerId)

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
            <div className="mt-4 mr-2">
              {isClient && (
                <ResponsiveContainer width="100%" height={300}>
                  <BarChart data={[selectedMonthStats]} layout="vertical">
                    <defs>
                      <linearGradient id="colorTotalHours" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#8884d8" stopOpacity={0}/>
                      </linearGradient>
                      <linearGradient id="colorTotalCleaningTasks" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#82ca9d" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#82ca9d" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <XAxis type="number" label={{ value: 'Count', position: 'insideBottomRight', offset: -5 }} />
                    <YAxis type="category" dataKey="monthYear" label={{ angle: -90, position: 'insideLeft' }} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="totalHours" name="Total Hours" fill="url(#colorTotalHours)" barSize={30} />
                    <Bar dataKey="totalCleaningTasks" name="Total Cleaning Tasks" fill="url(#colorTotalCleaningTasks)" barSize={30} />
                  </BarChart>
                </ResponsiveContainer>
              )}
            </div>
            <div className="mt-2 mr-2">
              {isClient && (
                <ResponsiveContainer width="100%" height={300}>
                  <BarChart data={[selectedMonthStats]} layout="vertical">
                    <defs>
                      <linearGradient id="colorTotalProperties" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#8884d8" stopOpacity={0}/>
                      </linearGradient>
                      <linearGradient id="colorTotalPackages" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#82ca9d" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#82ca9d" stopOpacity={0}/>
                      </linearGradient>
                      <linearGradient id="colorTotalClients" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#FFBB28" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#FFBB28" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <XAxis type="number" label={{ value: 'Count', position: 'insideBottomRight', offset: -5 }} />
                    <YAxis type="category" dataKey="monthYear" label={{  angle: -90, position: 'insideLeft' }} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="totalProperties" name="Total Properties" fill="url(#colorTotalProperties)" barSize={30} />
                    <Bar dataKey="totalPackages" name="Total Packages" fill="url(#colorTotalPackages)" barSize={30} />
                    <Bar dataKey="totalClients" name="Total Clients" fill="url(#colorTotalClients)" barSize={30} />
                  </BarChart>
                </ResponsiveContainer>
              )}
            </div>
            <div className="mt-2 mr-2">
              {isClient && (
                <ResponsiveContainer width="100%" height={300}>
                  <BarChart data={[selectedMonthStats]} layout="vertical">
                    <defs>
                      <linearGradient id="colorALCount" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#8884d8" stopOpacity={0}/>
                      </linearGradient>
                      <linearGradient id="colorMCCount" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#82ca9d" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#82ca9d" stopOpacity={0}/>
                      </linearGradient>
                      <linearGradient id="colorHLCount" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#FFBB28" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#FFBB28" stopOpacity={0}/>
                      </linearGradient>
                      <linearGradient id="colorELCount" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="5%" stopColor="#FF8042" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#FF8042" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <XAxis type="number" label={{ value: 'Count', position: 'insideBottomRight', offset: -5 }} />
                    <YAxis type="category" dataKey="monthYear" label={{ value: 'Month', angle: -90, position: 'insideLeft' }} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="alCount" name="Annual Leave Count" fill="url(#colorALCount)" barSize={30} />
                    <Bar dataKey="mcCount" name="Medical Leave Count" fill="url(#colorMCCount)" barSize={30} />
                    <Bar dataKey="hlCount" name="Holiday Leave Count" fill="url(#colorHLCount)" barSize={30} />
                    <Bar dataKey="elCount" name="Emergency Leave Count" fill="url(#colorELCount)" barSize={30} />
                  </BarChart>
                </ResponsiveContainer>
              )}
            </div>
            <Button className="ml-4 bg-green-600" onClick={exportMonthlyStatsToCSV}>Export Monthly Stats to CSV</Button>
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
                {viewMode === 'monthly' && filteredWorkerHours.map(wh => (
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
                    <TableCell>{filteredWorkerHours.reduce((total, wh) => total + wh.overtimeHours, 0)}</TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
            <div className="mt-4">
              {isClient && (
                <ResponsiveContainer width="100%" height={400}>
                  <LineChart data={filteredWorkerHours}>
                    <XAxis dataKey="monthYear" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Line type="monotone" dataKey="totalHoursWorked" name="Total Hours Worked" stroke="#8884d8" />
                    <Line type="monotone" dataKey="overtimeHours" name="Overtime Hours" stroke="#82ca9d" />
                  </LineChart>
                </ResponsiveContainer>
              )}
            </div>
            <Button className="mt-4 ml-4 bg-green-600" onClick={exportWorkerHoursToCSV}>Export Worker Hours to CSV</Button>
          </CardContent>
        </Card>
      </div>

    </div>
  )
}