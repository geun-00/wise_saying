package org.example.app;

public class Main {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();

        App app = new App(
                appConfig.wiseController(),
                appConfig.systemController(),
                appConfig.wiseRepository(),
                appConfig.scanner()
        );

        app.run();
    }
}
