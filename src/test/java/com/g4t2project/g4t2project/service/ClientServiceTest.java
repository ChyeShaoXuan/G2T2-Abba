package com.g4t2project.g4t2project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private CleaningPackageRepository cleaningPackageRepository;

    @Mock
    private CleaningTaskRepository cleaningTaskRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private WorkerRepository workerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
