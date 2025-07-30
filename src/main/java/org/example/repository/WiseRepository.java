package org.example.repository;

import org.example.entity.Wise;

import java.util.List;

public interface WiseRepository {
    int register(String author, String content);

    List<Wise> findAll();
    List<Wise> findByType(String type, String keyword);

    void remove(int targetId);
    void modify(int id, String newContent, String newAuthor);

    void clear();

    default void build() {}
}
