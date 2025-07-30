package org.example.app;

public class Main {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();

        App app = App.defaultApp(
                appConfig.wiseController(),
                appConfig.systemController(),
                appConfig.wiseRepository()
        );

        app.run();
    }
}
