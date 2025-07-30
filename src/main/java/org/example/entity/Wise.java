package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Wise {

    private int id;
    private String author;
    private String content;

    public void modifyContent(String newContent) {
        this.content = newContent;
    }

    public void modifyAuthor(String newAuthor) {
        this.author = newAuthor;
    }
}
