package org.example.app;

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
//        return new FileWiseRepository();
//        return new DBWiseRepository();
    }

    public Scanner scanner() {
        return sc;
    }
}
