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
    worker: Worker
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

// export async function getNearestWorker(propertyId: number, shift: string, date: string): Promise<Worker | null> {
//     try {
//         const response = await axios.post<Worker>('http://localhost:8080/cleaningTasks/closestWorker', {
//             params: { propertyId, shift, date },
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error fetching nearest worker:", error);
//         return null;
//     }
// }
