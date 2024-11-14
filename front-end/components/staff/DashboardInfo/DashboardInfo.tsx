'use client'

import 'mdb-react-ui-kit/dist/css/mdb.min.css';

import { useState, useEffect } from 'react'
import axios from 'axios'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import React from 'react';
import { useGlobalState } from '@/context/StateContext';
import {
  MDBCol,
  MDBContainer,
  MDBRow,
  MDBCard,
  MDBCardText,
  MDBCardBody,
  MDBCardImage,
  MDBBtn,
  MDBBreadcrumb,
  MDBBreadcrumbItem,
  MDBProgress,
  MDBProgressBar,
  MDBIcon,
  MDBListGroup,
  MDBListGroupItem
} from 'mdb-react-ui-kit';

interface DashboardInfoProps {
  workerId: string;
}

export default function DashboardInfo({ workerId }: DashboardInfoProps) {
  const { setWorkerId } = useGlobalState();
  const [workers, setWorkers] = useState<Worker[]>([])
  interface Worker {
    workerId: number
    name: string
    phoneNumber: string
    shortBio: string
    deployed: boolean
    curPropertyId: number
    worker_hours_in_week: number
  }

  useEffect(() => {
    const fetchWorkers = async () => {
      try {
        // Use the workerId from props instead of username
        const workersResponse = await axios.get(`http://localhost:8080/admin/workers`)
        const worker = workersResponse.data.find((worker: Worker) => worker.workerId === setWorkerId)
        if (worker) {
          setWorkers([worker])
        }
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    if (workerId) {
      fetchWorkers()
    }
  }, [workerId, setWorkerId])
  
  const username = localStorage.getItem('username')
  console.log(localStorage)

  useEffect(() => {
    // Fetch workers from the backend
    const fetchWorkers = async () => {
      try {
        const workersResponse = await axios.get(`http://localhost:8080/admin/workers`)
        console.log(workersResponse)
        const worker = workersResponse.data.find((worker: Worker) => worker.name === username)
        if (worker) {
          setWorkers([worker]) // Set only the worker with workerId = 1
        }
        console.log(workersResponse.data) // Logs all workers (for debugging)
      } catch (error) {
        console.error('Error fetching workers:', error)
      }
    }

    fetchWorkers()
  }, [username])

  return (
    <>
      <section style={{ backgroundColor: '#eee' }}>
        <MDBContainer className="py-5">
          <MDBRow>
            <MDBCol>
              <MDBBreadcrumb className="bg-light rounded-3 p-3 mb-4">
                <p className="lead fw-normal mb-1 "><b>Worker Profile</b></p>
              </MDBBreadcrumb>
            </MDBCol>
          </MDBRow>

          <MDBRow>
            <MDBCol lg="4">
              <MDBCard className="mb-4">
                <MDBCardBody className="text-center d-flex flex-column align-items-center">
                  <MDBCardImage
                    src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp"
                    alt="avatar"
                    className="rounded-circle"
                    style={{ width: '150px' }}
                    fluid />
                  <p className="text-muted mt-4 mb-1">Worker</p>
                  <p className=" mb-4">Abba Maintenance Services</p>
                  <div className="d-flex justify-content-center mb-2">
                    <MDBBtn>Follow</MDBBtn>
                    <MDBBtn outline className="ms-1">Message</MDBBtn>
                  </div>
                </MDBCardBody>
              </MDBCard>
            </MDBCol>

            {workers.map(worker => (
              < MDBCol lg="8" key={worker.workerId}>
                <MDBCard className="mb-4">
                  <MDBCardBody>
                    <MDBRow>
                      <MDBCol sm="3">
                        <MDBCardText>Full Name</MDBCardText>
                      </MDBCol>
                      <MDBCol sm="9">
                        <MDBCardText className="text-muted">{worker.name}</MDBCardText>
                      </MDBCol>
                    </MDBRow>
                    <hr />
                    <MDBRow>
                      <MDBCol sm="3">
                        <MDBCardText>Email</MDBCardText>
                      </MDBCol>
                      <MDBCol sm="9">
                        <MDBCardText className="text-muted">example@example.com</MDBCardText>
                      </MDBCol>
                    </MDBRow>
                    <hr />
                    <MDBRow>
                      <MDBCol sm="3">
                        <MDBCardText>Phone</MDBCardText>
                      </MDBCol>
                      <MDBCol sm="9">
                        <MDBCardText className="text-muted">{worker.phoneNumber}</MDBCardText>
                      </MDBCol>
                    </MDBRow>
                    <hr />
                    <MDBRow>
                      <MDBCol sm="3">
                        <MDBCardText>Short Bio</MDBCardText>
                      </MDBCol>
                      <MDBCol sm="9">
                        <MDBCardText className="text-muted">{worker.shortBio}</MDBCardText>
                      </MDBCol>
                    </MDBRow>
                    <hr />
                    {/* <MDBRow>
                      <MDBCol sm="3">
                        <MDBCardText>Worker Hours in Week</MDBCardText>
                      </MDBCol>
                      <MDBCol sm="9">
                        <MDBCardText className="text-muted">30{worker.workerHoursInWeek}</MDBCardText>
                      </MDBCol>
                    </MDBRow>
                    <hr /> */}
                    <MDBRow className="justify-content-end">
                      <MDBCol sm="3" className="d-flex justify-content-end">
                        <MDBBtn outline color="dark" style={{ height: '36px', overflow: 'visible' }}>
                          Edit profile
                        </MDBBtn>
                      </MDBCol>
                    </MDBRow>
                  </MDBCardBody>
                </MDBCard>
              </MDBCol>
            ))}

          </MDBRow>
        </MDBContainer>
      </section >

    </>

  );


}