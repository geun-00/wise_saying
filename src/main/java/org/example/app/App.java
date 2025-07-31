package org.example.app;

import org.example.controller.SystemController;
import org.example.controller.WiseController;
import org.example.request.Request;

import java.util.Scanner;

public class App {

    private final WiseController wiseController;
    private final SystemController systemController;
    private final Scanner sc;

    public App(WiseController wiseController, SystemController systemController, Scanner sc) {
        this.wiseController = wiseController;
        this.systemController = systemController;
        this.sc = sc;
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            Request request = new Request(sc);

            switch (request.getPath()) {
                case "종료" -> {
                    systemController.exit();
                    return;
                }
                case "등록" -> wiseController.register();
                case "목록" -> wiseController.printList(request);
                case "삭제" -> wiseController.remove(request);
                case "수정" -> wiseController.modify(request);
                case "빌드" -> wiseController.build();
                default -> System.out.println("알 수 없는 명령입니다. 다시 입력해 주세요.");
            }
        }
    }
}