package org.example.service;

import org.example.entity.Wise;
import org.example.repository.WiseRepository;

import java.util.List;

public class WiseService {

    private final WiseRepository repository;

    public WiseService(WiseRepository repository) {
        this.repository = repository;
    }

    public int register(String author, String content) {
        return repository.register(author, content);
    }

    public List<Wise> findAll() {
        return repository.findAll();
    }

    public List<Wise> findByType(String keywordType, String keyword) {
        return repository.findByType(keywordType, keyword);
    }

    public void remove(Integer targetId) {
        repository.remove(targetId);
    }

    public void modify(Integer targetId, String newContent, String newAuthor) {
        repository.modify(targetId, newContent, newAuthor);
    }

    public void build() {
        repository.build();
    }
}
