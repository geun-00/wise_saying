package org.example.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Wise;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FileWiseRepository implements WiseRepository {

    private static final String DB_FOLDER_PATH = "src\\main\\java\\org\\example\\db\\wiseSaying\\";
    private static final String DATA_BUILD_PATH = DB_FOLDER_PATH + "data.json";
    private static final String LAST_ID_FILE_PATH = DB_FOLDER_PATH + "lastId.txt";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int register(String author, String content) {
        int id = getNextId();

        Path path = Path.of(DB_FOLDER_PATH, id + ".json");

        try {
            String json = objectMapper.writeValueAsString(new Wise(id, author, content));
            Files.writeString(path, json);
        } catch (IOException e) {
            System.err.println("명언 저장 중 오류가 발생했습니다");
            e.printStackTrace(System.err);
        }
        return id;
    }

    @Override
    public List<Wise> findAll() {
        List<Wise> wiseList = new ArrayList<>();
        try {
            File folder = new File(DB_FOLDER_PATH);
            File[] files = folder.listFiles((dir, name) -> !name.startsWith("data") && name.endsWith(".json"));

            if (files == null) return wiseList;

            for (File file : files) {
                String jsonWise = Files.readString(file.toPath());
                wiseList.add(parseJsonToWise(jsonWise));
            }
        } catch (IOException e) {
            System.err.println("파일을 읽어오는 중 오류가 발생했습니다");
            e.printStackTrace(System.err);
        }

        wiseList.sort((origin, other) -> Integer.compare(other.getId(), origin.getId()));
        return Collections.unmodifiableList(wiseList);
    }

    @Override
    public void remove(int targetId) {
        Path filePath = Path.of(DB_FOLDER_PATH, targetId + ".json");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.printf("%d번 명언 삭제 중 오류가 발생했습니다: \n", targetId);
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void build() {
        Path buildPath = Path.of(DATA_BUILD_PATH);

        try {
            List<Wise> wiseList = findAll();

            String json = objectMapper.writeValueAsString(wiseList);
            Files.writeString(buildPath, json);
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");

        } catch (IOException e) {
            System.err.println("build 파일 생성 중 오류가 발생했습니다.");
            e.printStackTrace(System.err);
        }
    }

    @Override
    public List<Wise> findByType(String type, String keyword) {
        List<Wise> result = new ArrayList<>();

        try {
            File folder = new File(DB_FOLDER_PATH);
            File[] files = folder.listFiles((dir, name) -> !name.startsWith("data") && name.endsWith(".json"));

            if (files == null) return result;

            for (File file : files) {
                String json = Files.readString(file.toPath());
                Wise wise = parseJsonToWise(json);

                if ("author".equalsIgnoreCase(type) && wise.getAuthor().contains(keyword)) {
                    result.add(wise);
                } else if ("content".equalsIgnoreCase(type) && wise.getContent().contains(keyword)) {
                    result.add(wise);
                }
            }
        } catch (IOException e) {
            System.err.println("파일 검색 중 오류가 발생했습니다");
            e.printStackTrace(System.err);
        }

        return result;
    }

    @Override
    public void modify(int id, String newContent, String newAuthor) {
        findById(id).ifPresentOrElse(
                wise -> {
                    try {
                        wise.modifyAuthor(newAuthor.isEmpty() ? wise.getAuthor() : newAuthor);
                        wise.modifyContent(newContent.isEmpty() ? wise.getContent() : newContent);

                        String updatedJson = objectMapper.writeValueAsString(wise);

                        Path filePath = Path.of(DB_FOLDER_PATH, id + ".json");

                        Files.writeString(filePath, updatedJson);
                    } catch (IOException e) {
                        System.err.printf("%d번 명언 수정 중 오류가 발생했습니다: %s\n", id, e.getMessage());
                    }
                },
                () -> System.out.printf("%d번 명언은 존재하지 않습니다.\n", id)
        );
    }

    @Override
    public void clear() {
        Path folderPath = Path.of(DB_FOLDER_PATH);

        if (!Files.exists(folderPath)) return;

        try {
            File folder = folderPath.toFile();
            File[] files = folder.listFiles();

            if (files == null || files.length == 0) return;

            for (File file : files) {
                if (file.isFile() && !file.getName().equals("lastId.txt")) {
                    Files.delete(file.toPath());
                }
            }
            System.out.println("모든 파일이 삭제되었습니다.");

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private Optional<Wise> findById(int id) {
        Path filePath = Path.of(DB_FOLDER_PATH, id + ".json");

        if (Files.exists(filePath)) {
            try {
                String json = Files.readString(filePath);
                return Optional.of(parseJsonToWise(json));
            } catch (IOException e) {
                System.err.printf("%d번 명언을 읽는 중 오류가 발생했습니다: %s\n", id, e.getMessage());
            }
        }

        return Optional.empty();
    }

    private int getNextId() {
        try {
            Path path = Path.of(LAST_ID_FILE_PATH);

            int lastId = Integer.parseInt(Files.readString(path).trim());
            int nextId = lastId + 1;

            Files.writeString(path, String.valueOf(nextId));
            return nextId;

        } catch (IOException e) {
            throw new RuntimeException("lastId.txt 파일 처리 중 오류가 발생했습니다.", e);
        }
    }

    private Wise parseJsonToWise(String json) {
        try {
            return objectMapper.readValue(json, Wise.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }
}