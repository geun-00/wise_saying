package org.example.controller;

import org.example.entity.Wise;
import org.example.service.WiseService;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class WiseController {

    private static final int MAX_PAGE_ITEMS = 5;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private final Scanner sc;
    private final WiseService wiseService;

    public WiseController(Scanner sc, WiseService wiseService) {
        this.sc = sc;
        this.wiseService = wiseService;
    }

    public void register() {
        System.out.print("명언 : ");
        String content = sc.nextLine();

        System.out.print("작가 : ");
        String author = sc.nextLine();

        int registeredId = wiseService.register(author, content);
        System.out.printf("%d번 명언이 등록되었습니다.\n", registeredId);
    }

    public void printList(String command) {
        String[] queryString = command.split("\\?");

        if (queryString.length == 1) {
            printWiseList(wiseService.findAll(), DEFAULT_PAGE_NUMBER);
            return;
        }

        if (queryString[1].startsWith("page")) {
            int pageNum = Integer.parseInt(queryString[1].split("=")[1]);
            printWiseList(wiseService.findAll(), pageNum);
            return;
        }

        String[] params = queryString[1].split("&");

        String keywordType = params[0].split("=")[1];

        if (!validate(keywordType)) return;

        String keyword = params[1].split("=")[1];

        System.out.println("----------------------");
        System.out.println("검색타입 : " + keywordType);
        System.out.println("검색어 : " + keyword);
        System.out.println("----------------------");

        printWiseList(wiseService.findByType(keywordType, keyword), DEFAULT_PAGE_NUMBER);
    }

    private static boolean validate(String keywordType) {
        if (!"author".equalsIgnoreCase(keywordType) && !"content".equalsIgnoreCase(keywordType)) {
            System.out.println("keywordType을 다시 입력해 주세요. (author, content 중 1)");
            return false;
        }
        return true;
    }

    public void remove(String command) {
        Integer targetId = getTargetId(command, "삭제");
        if (targetId == null) return;

        itemUpdateProcess(
                targetId,
                _ -> {
                    wiseService.remove(targetId);
                    System.out.printf("%d번 명언이 삭제되었습니다.\n", targetId);
                }
        );
    }

    public void modify(String command) {
        Integer targetId = getTargetId(command, "수정");
        if (targetId == null) return;

        itemUpdateProcess(
                targetId,
                wise -> {
                    modify(wise, targetId);
                    System.out.printf("%d번 명언이 수정되었습니다.\n", targetId);
                }
        );
    }

    private Integer getTargetId(String command, String str) {
        String[] queryString = command.split("\\?");

        if (queryString.length == 1) {
            System.out.printf("%s?id=1 형식으로 입력해 주세요.\n", str);
            return null;
        }

        return Integer.parseInt(queryString[1].split("=")[1]);
    }

    private void modify(Wise wise, Integer targetId) {
        System.out.printf("명언(기존) : %s\n", wise.getContent());
        System.out.print("명언(수정) : ");
        String newContent = sc.nextLine().trim();

        System.out.printf("작가(기존) : %s\n", wise.getAuthor());
        System.out.print("작가(수정) : ");
        String newAuthor = sc.nextLine().trim();

        wiseService.modify(targetId, newContent, newAuthor);

        System.out.printf("%d번 명언이 수정되었습니다.\n", targetId);
    }

    private void itemUpdateProcess(Integer targetId, Consumer<Wise> consumer) {
        wiseService.findAll().stream()
                  .filter(wise -> wise.getId() == targetId)
                  .findFirst()
                  .ifPresentOrElse(
                          consumer,
                          () -> System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId)
                  );
    }

    private void printWiseList(List<Wise> wises, int pageNum) {
        int totalItems = wises.size();
        int totalPages = (int) Math.ceil((double) totalItems / MAX_PAGE_ITEMS);

        if (pageNum < 1 || pageNum > totalPages) {
            System.out.printf("존재하지 않는 페이지입니다. 총 %d 페이지\n", totalPages);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("번호 / 작가 / 명언\n")
          .append("----------------------\n");

        int from = (pageNum - 1) * MAX_PAGE_ITEMS;
        int to = Math.min(from + MAX_PAGE_ITEMS, totalItems);

        wises.subList(from, to)
             .forEach(wise -> sb
                     .append(wise.getId()).append(" / ")
                     .append(wise.getAuthor()).append(" / ")
                     .append(wise.getContent()).append("\n")
             );

        sb.append("----------------------\n")
          .append("페이지 : ");

        for (int i = 1; i <= totalPages; i++) {
            if (i == pageNum) sb.append("[").append(i).append("]");
            else sb.append(i);

            if (i < totalPages) {
                sb.append(" / ");
            }
        }

        System.out.println(sb);
    }

    public void build() {
        wiseService.build();
    }
}
