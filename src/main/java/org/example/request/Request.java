package org.example.request;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Getter
public class Request {

    private final String path;
    private final Map<String, String> queryParams = new HashMap<>();

    public Request(Scanner sc) {
        String requestLine = sc.nextLine();

        String[] pathParts = requestLine.split("\\?");
        this.path = pathParts[0];

        if (pathParts.length > 1) {
            parseQueryParams(pathParts[1]);
        }
    }

    private void parseQueryParams(String queryString) {
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=", 2);

            if (keyValue.length < 2) continue;

            String key = keyValue[0];
            String value = keyValue[1];
            queryParams.put(key, value);
        }
    }

    public String getParameter(String name, String defaultValue) {
        return queryParams.getOrDefault(name, defaultValue);
    }

    public int getParamAsInt(String paramName, int defaultValue) {
        String value = queryParams.get(paramName);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
