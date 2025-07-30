package org.example.app;

import org.example.controller.SystemController;
import org.example.controller.WiseController;
import org.example.repository.WiseRepository;

import java.util.Scanner;

public class App {

    private final WiseController wiseController;
    private final SystemController systemController;
    private final WiseRepository repository;
    private final Scanner sc;

    private App(WiseController wiseController, SystemController systemController, WiseRepository repository, Scanner sc) {
        this.wiseController = wiseController;
        this.systemController = systemController;
        this.repository = repository;
        this.sc = sc;
    }

    // 일반 실행용
    public static App defaultApp(WiseController wiseController, SystemController systemController, WiseRepository repository) {
        return new App(wiseController, systemController, repository, new Scanner(System.in));
    }

    // 테스트용
    public static App testApp(WiseController wiseController, SystemController systemController, WiseRepository repository, Scanner sc) {
        return new App(wiseController, systemController, repository, sc);
    }

    private static final int SAMPLE_ITEM_COUNT = 10;

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            if (repository.findAll().isEmpty()) {
//                initSample();
            }

            System.out.print("명령) ");

            String command = sc.nextLine().trim();
            String mainCommand = command.substring(0, 2);

            switch (mainCommand) {
                case "종료" -> {
                    systemController.exit();
                    return;
                }
                case "등록" -> wiseController.register();
                case "목록" -> wiseController.printList(command);
                case "삭제" -> wiseController.remove(command);
                case "수정" -> wiseController.modify(command);
                case "빌드" -> wiseController.build();
                default -> System.out.println("알 수 없는 명령입니다. 다시 입력해 주세요.");
            }
        }
    }

    private void initSample() {
        for (int i = 1; i <= SAMPLE_ITEM_COUNT; i++) {
            repository.register(
                    "작자미상" + i,
                    "명언" + i
            );
        }
    }
}