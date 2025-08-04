package org.example.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.controller.SystemController;
import org.example.controller.WiseController;
import org.example.repository.MemoryWiseRepository;
import org.example.repository.WiseRepository;
import org.example.service.WiseService;

import java.util.Scanner;

public class AppConfig {

    private final Scanner sc = new Scanner(System.in);

    public WiseController wiseController() {
        return new WiseController(sc, wiseService());
    }

    public SystemController systemController() {
        return new SystemController(sc);
    }

    public WiseService wiseService() {
        return new WiseService(wiseRepository());
    }

    public WiseRepository wiseRepository() {
        return new MemoryWiseRepository();
//        return new FileWiseRepository(objectMapper());
//        return new DBWiseRepository();
    }

    public Scanner scanner() {
        return sc;
    }

    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
