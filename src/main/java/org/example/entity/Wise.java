package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wise {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int id;
    private String author;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Wise createWise(int id, String author, String content) {
        LocalDateTime now = LocalDateTime.now();
        return new Wise(id, author, content, now, now);
    }

    public void modifyContent(String newContent) {
        this.content = newContent;
    }

    public void modifyAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    @JsonIgnore
    public String getFormattedCreatedAt() {
        return createdAt.format(DATE_TIME_FORMATTER);
    }

    @JsonIgnore
    public String getFormattedModifiedAt() {
        return modifiedAt.format(DATE_TIME_FORMATTER);
    }
}
