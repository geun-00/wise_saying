package org.example.controller;

import java.util.Scanner;

public class SystemController {

    private final Scanner sc;

    public SystemController(Scanner sc) {
        this.sc = sc;
    }

    public void exit() {
        System.out.println("명언 앱을 종료합니다.");
        sc.close();
    }
}