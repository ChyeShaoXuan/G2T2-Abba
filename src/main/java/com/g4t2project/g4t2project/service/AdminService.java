package com.g4t2project.g4t2project.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.g4t2project.g4t2project.DTO.*;
import com.g4t2project.g4t2project.repository.*;
import com.g4t2project.g4t2project.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;
@Service
public class AdminService {
    @Autowired
    private PropertyRepository propertyRepository;
  
    @Autowired
    private WorkerHoursRepository workerHoursRepository;
    @Autowired
    private JobStatsRepository jobStatsRepository;

    @Autowired
    private LeaveStatsRepository leaveStatsRepository;

    @Autowired
    private CleaningPackageRepository cleaningPackageRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;


    @Transactional
    public Worker addWorkerUnderAdmin(Long adminId, Worker worker) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        worker.setAdmin(admin); // Ensure the worker is associated with the admin
        return workerRepository.save(worker); // Save the worker
    }

    
    public Admin removeWorkerUnderAdmin(Long adminId, Long workerId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        Worker worker = workerRepository.findById(workerId).orElseThrow(() -> new RuntimeException("Worker not found"));
        workerHoursRepository.deleteWorkerHourByWorkerId(workerId);
        leaveApplicationRepository.deletePropertyByWorkerId(workerId);
        cleaningTaskRepository.deleteTaskByWorkerId(workerId);
        admin.removeWorker(worker);
        return adminRepository.save(admin);
    }
   
    @Transactional
    public Worker updateWorker(Long workerId, Worker updatedWorker) {
        Worker existingWorker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        existingWorker.setName(updatedWorker.getName());
        existingWorker.setPhoneNumber(updatedWorker.getPhoneNumber());
        existingWorker.setShortBio(updatedWorker.getShortBio());
        existingWorker.setDeployed(updatedWorker.getDeployed());
        existingWorker.setTele_Id(updatedWorker.getTele_Id());
        existingWorker.setCurPropertyId(updatedWorker.getCurPropertyId());
        existingWorker.setAvailable(updatedWorker.isAvailable());

        // Ensure the worker is associated with the correct admin
        if (updatedWorker.getAdmin() != null) {
            Admin admin = adminRepository.findById(updatedWorker.getAdmin().getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            existingWorker.setAdmin(admin);
        } else {
            // Keep the existing admin if not provided in the update
            existingWorker.setAdmin(existingWorker.getAdmin());
        }

        return workerRepository.save(existingWorker);
    }

    public void updateLeaveApplicationStatus(int leaveApplicationId, LeaveApplication.Status status) {
        Optional<LeaveApplication> leaveApplicationOpt = leaveApplicationRepository.findById(leaveApplicationId);
        if (leaveApplicationOpt.isPresent()) {
            LeaveApplication leaveApplication = leaveApplicationOpt.get();
            leaveApplication.setStatus(status);
            leaveApplicationRepository.save(leaveApplication);
        } else {
            throw new RuntimeException("Leave Application not found");
        }
    }



    public Client addClientUnderAdmin(Long adminId, ClientDTO clientDTO) {
        Admin admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        // Map fields from ClientDTO to Client entity
        Client client = new Client(
            admin,
            clientDTO.getPackageId() != 0 ? cleaningPackageRepository.findById(clientDTO.getPackageId()).orElse(null) : null,
            clientDTO.getWorkerId() != null ? workerRepository.findById(clientDTO.getWorkerId()).orElse(null) : null,
            clientDTO.getName(),
            clientDTO.getPhoneNumber(),
            clientDTO.getEmail()
        );
    
        // Set the admin to ensure association
        client.setAdmin(admin);
    
        // Save the client entity
        return clientRepository.save(client);
    }

    @Transactional
    public Admin removeClientUnderAdmin(Long adminId, Long clientId) {
        Admin admin = adminRepository.findById(adminId)
          .orElseThrow(() -> new RuntimeException("Admin not found"));
        Client client = clientRepository.findById(clientId)
          .orElseThrow(() -> new RuntimeException("Client not found"));

        admin.removeClient(client);  // remove association
        clientRepository.delete(client);  // cascade delete will handle properties and tasks

        return adminRepository.save(admin);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }
    
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientDTO> clientDTOs = new ArrayList<>();
    
        for (Client client : clients) {
            ClientDTO dto = new ClientDTO(
                client.getClientId(),
                client.getEmail(),
                client.getName(),
                client.getPhoneNumber(),
                client.getAdmin() != null ? client.getAdmin().getAdminId() : null,
                client.getPreferredPackage() != null ? client.getPreferredPackage().getPackageId() : 0,
                client.getPreferredWorker() != null ? client.getPreferredWorker().getWorkerId() : null
            );
            clientDTOs.add(dto);
        }
    
        return clientDTOs;
    }
    

    

    public CleaningTask assignTaskToWorker(int taskId, Long workerId) {
       
        CleaningTask task = cleaningTaskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        Worker worker = workerRepository.findById(workerId).orElseThrow(() -> new RuntimeException("Worker not found"));

        task.setWorker(worker);
        task.setStatus(CleaningTask.Status.Assigned);

        return cleaningTaskRepository.save(task);
    }


    @Transactional
    public Client updateClient(Long clientId, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        existingClient.setName(clientDTO.getName());
        existingClient.setPhoneNumber(clientDTO.getPhoneNumber());
        existingClient.setEmail(clientDTO.getEmail());

        if (clientDTO.getPackageId() != 0) {
            CleaningPackage cleaningPackage = cleaningPackageRepository.findById(clientDTO.getPackageId())
                    .orElseThrow(() -> new RuntimeException("Package not found"));
            existingClient.setPreferredPackage(cleaningPackage);
        }

        if (clientDTO.getWorkerId() != null) {
            Worker worker = workerRepository.findById(clientDTO.getWorkerId())
                    .orElseThrow(() -> new RuntimeException("Worker not found"));
            existingClient.setPreferredWorker(worker);
        }

        if (clientDTO.getAdminId() != null) {
            Admin admin = adminRepository.findById(clientDTO.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            existingClient.setAdmin(admin);
        }

        return clientRepository.save(existingClient);
    }
    
    
    public Map<String, StatsDTO> getAllStats() {
        List<JobStats> jobStatsList = jobStatsRepository.findAll();
        List<LeaveStats> leaveStatsList = leaveStatsRepository.findAll();

        Map<String, StatsDTO> statsMap = new HashMap<>();

        for (JobStats jobStats : jobStatsList) {
            StatsDTO statsDTO = new StatsDTO();
            statsDTO.setMonthYear(jobStats.getMonthYear());
            statsDTO.setTotalHours(jobStats.getTotalHours());
            statsDTO.setTotalCleaningTasks(jobStats.getTotalCleaningTasks());
            statsDTO.setTotalClients(jobStats.getTotalClients());
            statsDTO.setTotalProperties(jobStats.getTotalProperties());
            statsDTO.setTotalWorkers(jobStats.getTotalWorkers());
            statsDTO.setTotalPackages(jobStats.getTotalPackages());
            statsMap.put(jobStats.getMonthYear(), statsDTO);
        }

        for (LeaveStats leaveStats : leaveStatsList) {
            StatsDTO statsDTO = statsMap.getOrDefault(leaveStats.getMonthYear(), new StatsDTO());
            statsDTO.setMonthYear(leaveStats.getMonthYear());
            statsDTO.setAlCount(leaveStats.getAlCount());
            statsDTO.setMcCount(leaveStats.getMcCount());
            statsDTO.setHlCount(leaveStats.getHlCount());
            statsDTO.setElCount(leaveStats.getElCount());
            statsMap.put(leaveStats.getMonthYear(), statsDTO);
        }

        return statsMap;
    }

    public List<Long> getAllWorkerIds() {
        return workerRepository.findAllWorkerIds();
    }

    public List<WorkerHours> getWorkerHoursByWorkerId(Long workerId) {
        return workerHoursRepository.findByWorker_WorkerId(workerId);
    }





    
}
