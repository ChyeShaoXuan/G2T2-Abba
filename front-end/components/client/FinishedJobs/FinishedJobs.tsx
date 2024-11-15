"use client";

import { useState, useEffect } from "react";
import { format } from "date-fns";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
import { MapPin, Clock, Calendar } from "lucide-react";
import { Button } from "@/components/ui/button";
import { getCompletedTasksByClient, CompletedTaskDTO, submitFeedback } from "@/utils/apiClient";
import FeedbackModal from "@/components/FeedbackModal";


// interface Job {
//   id: number;
//   date: string;
//   shift: string;
//   status: string;
//   propertyId: number;
//   workerId: number;
//   clientName: string;
//   address: string;
// }

export default function FinishedJobs() {
  const [myJobs, setMyJobs] = useState<CompletedTaskDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedJob, setSelectedJob] = useState<CompletedTaskDTO | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const clientId = 2; 
  const [successMessage, setSuccessMessage] = useState(false);

  useEffect(() => {
    async function fetchCompletedTasks() {
      try {
        const completedTasks = await getCompletedTasksByClient(clientId);

        const jobs = completedTasks
          .filter((task) => task.status === "Completed")  
          .map((task) => ({
            taskId: task.taskId,
            date: task.date,
            shift: task.shift,
            status: task.status,
            propertyId: task.propertyId,
            workerId: task.workerId,
            acknowledged: task.acknowledged ?? false, // Dummy or default value if not available
            feedbackId: task.feedbackId ?? null, // Dummy or default value if not available
          }));

        // console.log("Transformed jobs:", jobs);
        setMyJobs(jobs);
      } catch (error) {
        console.error("Failed to load completed tasks:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchCompletedTasks();
  }, [clientId]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (myJobs.length === 0) {
    return <div>No completed tasks found yet.</div>;
  }

  const handleOpenModal = (job: CompletedTaskDTO) => {
    setSelectedJob(job);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
      setIsModalOpen(false);
      setSelectedJob(null);
  };

  const handleFeedbackSubmit = async (rating: number, comment: string) => {
    if (!rating || rating < 1 || rating > 5) {
      window.alert("Please provide a rating between 1 and 5.");
      return;
    }
    if (!comment || comment.trim() === "") {
      window.alert("Please provide a comment.");
      return;
    }
    if (selectedJob) {
        await submitFeedback(selectedJob.taskId, rating, comment);
        console.log("Feedback submitted for task ID:", selectedJob.taskId);
        window.alert("Feedback submitted successfully!");

        handleCloseModal();
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Finished Jobs</h1>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {myJobs.map((job) => (
          <Card key={job.taskId} className="cursor-pointer">
            <CardHeader>
              <CardTitle className="flex justify-between items-center">
                <span>{job.workerId}</span>
                <Badge variant={job.status === "Completed" ? "secondary" : "outline"}>
                  {job.status}
                </Badge>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <p className="flex items-center">
                <Calendar className="mr-2" /> {format(new Date(job.date), "MMMM d, yyyy")}
              </p>
              <p className="flex items-center">
                <MapPin className="mr-2" /> {job.propertyId}
              </p>
              <p className="flex items-center">
                <Clock className="mr-2" /> Shift: {job.shift}
              </p>
            </CardContent>
            <CardFooter>
                <Button onClick={() => handleOpenModal(job)}>Leave Feedback</Button>
            </CardFooter>
          </Card>
        ))}
      </div>
      {selectedJob && (
        <FeedbackModal
            isOpen={isModalOpen}
            onClose={handleCloseModal}
            onSubmit={handleFeedbackSubmit}
        />
      )}
    </div>
  );
}
