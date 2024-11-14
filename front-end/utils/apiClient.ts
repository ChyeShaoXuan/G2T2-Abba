import axios from 'axios'

export interface PlaceOrderRequestDTO {
    packageType: string;
    propertyType: string;
    numberOfRooms: number;
    shift: string;
    date: string;
    preferredWorkerId?: number;
}

export interface CleaningTaskDTO {
    taskId: number;
    propertyId: number;
    shift: string;
    date: string;
    acknowledged: boolean;
    worker: Worker;
    // packageDetails: string | null; 
}

export interface Worker {
    workerId: number
    name: string
    phoneNumber: string
    shortBio: string
    // deployed: boolean
    // tele_Id: string
    // curPropertyId: number
    available: boolean
    // adminId: number
    // worker_hours_in_week: number
}

export interface CompletedTaskDTO {
    taskId: number;
    acknowledged: boolean;
    date: string;
    shift: string;
    status: string;
    feedbackId: number;
    propertyId: number;
    workerId: number;
}

export async function placeOrder(clientId: number, orderRequest: PlaceOrderRequestDTO): Promise<CleaningTaskDTO> {
    try {
        const response = await axios.post<CleaningTaskDTO>(`http://localhost:8080/clients/${clientId}/placeOrder`, orderRequest, {
            headers: {
                'Content-Type': 'application/json',
            },
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(`Error: ${error.response?.status} - ${error.response?.statusText}`);
        }
        throw new Error("An unknown error occurred");
    }
}

export async function getCompletedTasksByClient(clientId: number): Promise<CompletedTaskDTO[]> {
    try {
        console.log("Fetching completed tasks for client", clientId);
        const response = await axios.get<CompletedTaskDTO[]>(`http://localhost:8080/cleaningTasks/completed-tasks-by-client`, {
            params: { clientId }
        });
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error("Failed to fetch completed tasks:", error);
        throw error;
    }
}

export async function submitFeedback(taskId: number, rating: number, comment: string): Promise<void> {
    try {
        await axios.post(`http://localhost:8080/cleaningTasks/${taskId}/feedback`, {
            rating,
            comment,
        });
        console.log("Feedback submitted successfully");
    } catch (error) {
        console.error("Failed to submit feedback:", error);
        throw error;
    }
}

export async function getAvailableWorkers(): Promise<{ workerId: number; name: string }[]> {
    const response = await axios.get(`http://localhost:8080/worker/available`);
    return response.data.map((worker: any) => ({
        workerId: worker.workerId,
        name: worker.name,
    }));
}

