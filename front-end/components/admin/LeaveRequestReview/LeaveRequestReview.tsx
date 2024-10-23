'use client'

import { useState } from 'react'
import { format } from 'date-fns'
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { AlertCircle, CheckCircle, XCircle } from 'lucide-react'
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"

interface LeaveRequest {
  id: string
  employeeName: string
  leaveType: 'Annual' | 'Medical' | 'Emergency'
  startDate: Date
  endDate: Date
  reason: string
  status: 'Pending' | 'Approved' | 'Rejected'
}

const initialLeaveRequests: LeaveRequest[] = [
  {
    id: '1',
    employeeName: 'John Doe',
    leaveType: 'Annual',
    startDate: new Date('2023-07-01'),
    endDate: new Date('2023-07-05'),
    reason: 'Family vacation',
    status: 'Pending'
  },
  {
    id: '2',
    employeeName: 'Jane Smith',
    leaveType: 'Medical',
    startDate: new Date('2023-06-15'),
    endDate: new Date('2023-06-17'),
    reason: 'Dental surgery',
    status: 'Pending'
  },
  {
    id: '3',
    employeeName: 'Bob Johnson',
    leaveType: 'Emergency',
    startDate: new Date('2023-06-10'),
    endDate: new Date('2023-06-10'),
    reason: 'Family emergency',
    status: 'Pending'
  }
]

export default function LeaveRequestReview() {
  const [leaveRequests, setLeaveRequests] = useState<LeaveRequest[]>(initialLeaveRequests)
  const [selectedRequest, setSelectedRequest] = useState<LeaveRequest | null>(null)

  const handleApprove = (id: string) => {
    setLeaveRequests(prevRequests =>
      prevRequests.map(request =>
        request.id === id ? { ...request, status: 'Approved' } : request
      )
    )
    setSelectedRequest(null)
  }

  const handleReject = (id: string) => {
    setLeaveRequests(prevRequests =>
      prevRequests.map(request =>
        request.id === id ? { ...request, status: 'Rejected' } : request
      )
    )
    setSelectedRequest(null)
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Leave Request Review</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Card>
          <CardHeader>
            <CardTitle>Pending Leave Requests</CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Employee</TableHead>
                  <TableHead>Type</TableHead>
                  <TableHead>Start Date</TableHead>
                  <TableHead>End Date</TableHead>
                  <TableHead>Status</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {leaveRequests.map((request) => (
                  <TableRow key={request.id} onClick={() => setSelectedRequest(request)} className="cursor-pointer hover:bg-muted">
                    <TableCell>{request.employeeName}</TableCell>
                    <TableCell>
                      <Badge variant={request.leaveType === 'Annual' ? 'default' : request.leaveType === 'Medical' ? 'destructive' : 'secondary'}>
                        {request.leaveType}
                      </Badge>
                    </TableCell>
                    <TableCell>{format(request.startDate, 'MMM dd, yyyy')}</TableCell>
                    <TableCell>{format(request.endDate, 'MMM dd, yyyy')}</TableCell>
                    <TableCell>
                      <Badge variant={request.status === 'Pending' ? 'outline' : request.status === 'Approved' ? 'default' : 'destructive'}>
                        {request.status}
                      </Badge>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Request Details</CardTitle>
          </CardHeader>
          <CardContent>
            {selectedRequest ? (
              <div className="space-y-4">
                <div>
                  <h3 className="font-semibold">{selectedRequest.employeeName}</h3>
                  <Badge variant={selectedRequest.leaveType === 'Annual' ? 'default' : selectedRequest.leaveType === 'Medical' ? 'destructive' : 'secondary'}>
                    {selectedRequest.leaveType} Leave
                  </Badge>
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">From: {format(selectedRequest.startDate, 'MMMM dd, yyyy')}</p>
                  <p className="text-sm text-muted-foreground">To: {format(selectedRequest.endDate, 'MMMM dd, yyyy')}</p>
                </div>
                <div>
                  <h4 className="font-semibold">Reason:</h4>
                  <p>{selectedRequest.reason}</p>
                </div>
                <Alert>
                  <AlertCircle className="h-4 w-4" />
                  <AlertTitle>Action Required</AlertTitle>
                  <AlertDescription>
                    Please review this leave request and take appropriate action.
                  </AlertDescription>
                </Alert>
              </div>
            ) : (
              <p className="text-center text-muted-foreground">Select a request to view details</p>
            )}
          </CardContent>
          <CardFooter className="flex justify-end space-x-2">
            {selectedRequest && selectedRequest.status === 'Pending' && (
              <>
                <Button onClick={() => handleApprove(selectedRequest.id)} className="bg-green-500 hover:bg-green-600">
                  <CheckCircle className="mr-2 h-4 w-4" /> Approve
                </Button>
                <Button onClick={() => handleReject(selectedRequest.id)} variant="destructive">
                  <XCircle className="mr-2 h-4 w-4" /> Reject
                </Button>
              </>
            )}
          </CardFooter>
        </Card>
      </div>
    </div>
  )
}