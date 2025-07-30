import org.example.controller.SystemController;
import org.example.controller.WiseController;
import org.example.repository.MemoryWiseRepository;
import org.example.repository.WiseRepository;
import org.example.service.WiseService;

import java.util.Scanner;

public class TestConfig {
    private final Scanner sc;

    public TestConfig(Scanner sc) {
        this.sc = sc;
    }

    public WiseController wiseController(WiseRepository wiseRepository) {
        return new WiseController(sc, wiseService(wiseRepository));
    }

    public SystemController systemController() {
        return new SystemController(sc);
    }

    public WiseService wiseService(WiseRepository wiseRepository) {
        return new WiseService(wiseRepository);
    }

    public WiseRepository wiseRepository() {
        return new MemoryWiseRepository();
    }
}
