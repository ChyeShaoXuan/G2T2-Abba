package com.g4t2project.g4t2project.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.g4t2project.g4t2project.DTO.*;
import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.repository.*;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


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
    public WorkerDTO_Admin updateWorker(Long workerId, WorkerDTO_Admin workerDTO) {
        Worker existingWorker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        existingWorker.setName(workerDTO.getName());
        existingWorker.setPhoneNumber(workerDTO.getPhoneNumber());
        existingWorker.setShortBio(workerDTO.getShortBio());
        existingWorker.setemailID(workerDTO.getEmailID());
        existingWorker.setCurPropertyId(workerDTO.getCurPropertyId());
        existingWorker.setAvailable(workerDTO.isAvailable());
        existingWorker.setDeployed(workerDTO.isDeployed());

        // Ensure the worker is associated with the correct admin
        if (workerDTO.getAdminId() != null) {
            Admin admin = adminRepository.findById(workerDTO.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            existingWorker.setAdmin(admin);
        }

        Worker updatedWorker = workerRepository.save(existingWorker);
        return new WorkerDTO_Admin(
                updatedWorker.getWorkerId(),
                updatedWorker.getName(),
                updatedWorker.getPhoneNumber(),
                updatedWorker.getShortBio(),
                updatedWorker.isAvailable(),
                updatedWorker.getDeployed(),
                updatedWorker.getCurPropertyId(),
                updatedWorker.getEmailId(),
                updatedWorker.getAdminId()
        );
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
                clientDTO.getWorkerId() != null ? workerRepository.findById(clientDTO.getWorkerId().longValue()).orElse(null) : null,
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

   public List<WorkerDTO_Admin> getAllWorkersAdmin() {
        List<Worker> workers = workerRepository.findAll();
        return workers.stream()
                .map(worker -> new WorkerDTO_Admin(
                        worker.getWorkerId(),
                        worker.getName(),
                        worker.getPhoneNumber(),
                        worker.getShortBio(),
                        worker.isAvailable(),
                        worker.getDeployed(),
                        worker.getCurPropertyId(),
                        worker.getEmailId(),
                        worker.getAdminId()
                ))
                .collect(Collectors.toList());
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
                    client.getPreferredPackage() != null ? client.getPreferredPackage().getPackageId() : null,
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
            Worker worker = workerRepository.findById(clientDTO.getWorkerId().longValue())
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
    
    
    public List<Long> getAllUniqueAdminIds() {
        return adminRepository.findAll().stream()
                .map(Admin::getAdminId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Long> getAllUniquePropertyIds() {
        return propertyRepository.findAll().stream()
                .map(Property::getPropertyId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Long> getAllUniquePackageIds() {
        return cleaningPackageRepository.findAll().stream()
                .map(CleaningPackage::getPackageId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Integer> getAllUniqueWorkerIds() {
        return workerRepository.findAll().stream()
                .map(Worker::getWorkerId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> "Admin".equals(user.getRole()) || "Worker".equals(user.getRole()))
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getRole()
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    public User updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(roleName);

        // Update user_roles table
        Set<Role> roles = new HashSet<>();
        if ("Admin".equals(roleName)) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(adminRole);
        } else if ("Worker".equals(roleName)) {
            Role workerRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(workerRole);
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }
    

}
