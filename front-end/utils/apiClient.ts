import axios from 'axios'

export interface PlaceOrderRequestDTO {
    packageType: string;
    propertyType: string;
    numberOfRooms: number | undefined;
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
