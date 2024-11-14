'use client'

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import React from 'react';

export default function DashboardInfo() {
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
    worker_hours_in_week: number
  }
  const [workers, setWorkers] = useState<Worker[]>([])
  const username = localStorage.getItem('username')

  useEffect(() => {
    // Fetch workers from the backend
    const fetchWorkers = async () => {
      try {
        const workersResponse = await axios.get(`http://localhost:8080/admin/workers`)
        const worker = workersResponse.data.find((worker: Worker) => worker.name === username)
        if (worker) {
          setWorkers([worker]) // Set only the worker with workerId = 1
        }
        console.log(workersResponse.data) 
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    fetchWorkers()
  }, [username])

  return (
    <div className="bg-gray-100 py-5">
      <div className="container mx-auto">
        <div className="mb-4 p-3 bg-white rounded-lg shadow-md">
          <p className="text-lg font-semibold mb-1">Worker Profile</p>
        </div>

        <div className="flex flex-wrap -mx-4">
          <div className="w-full lg:w-1/3 px-4 mb-4">
            <div className="bg-white rounded-lg shadow-md p-4 text-center">
              <img
                src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp"
                alt="avatar"
                className="rounded-full w-32 mb-4 mx-auto"
              />
              <h5 className="text-xl font-semibold mb-2">{workers[0]?.name}</h5>
              <p className="text-gray-600 mb-4">{workers[0]?.shortBio}</p>
              <p className="text-gray-600 mb-4">{workers[0]?.phoneNumber}</p>
              <p className="text-gray-600 mb-4">{workers[0]?.tele_Id}</p>
              <p className="text-gray-600 mb-4">{workers[0]?.curPropertyId}</p>
              <p className="text-gray-600 mb-4">{workers[0]?.available ? 'Available' : 'Not Available'}</p>
              <p className="text-gray-600 mb-4">{workers[0]?.worker_hours_in_week} hours/week</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}