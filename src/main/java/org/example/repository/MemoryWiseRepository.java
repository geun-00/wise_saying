package org.example.repository;

import org.example.entity.Wise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryWiseRepository implements WiseRepository {

    private final List<Wise> wises = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public int register(String author, String content) {
        Wise registeredWise = new Wise(idGenerator.getAndIncrement(), author, content);
        wises.add(registeredWise);
        return registeredWise.getId();
    }

    @Override
    public List<Wise> findAll() {
        ArrayList<Wise> wiseList = new ArrayList<>(wises);
        wiseList.sort((origin, other) -> Integer.compare(other.getId(), origin.getId()));

        return Collections.unmodifiableList(wiseList);
    }

    @Override
    public void remove(int targetId) {
        wises.removeIf(wise -> wise.getId() == targetId);
    }

    @Override
    public void modify(int id, String newContent, String newAuthor) {
        wises.stream()
             .filter(wise -> wise.getId() == id)
             .findFirst()
             .ifPresent(
                     wise -> {
                         if (!newContent.isEmpty()) wise.modifyContent(newContent);
                         if (!newAuthor.isEmpty()) wise.modifyAuthor(newAuthor);
                     }
             );
    }

    @Override
    public List<Wise> findByType(String type, String keyword) {
        return wises.stream()
                    .filter(wise -> {
                        if ("author".equalsIgnoreCase(type)) {
                            return wise.getAuthor().contains(keyword);
                        }
                        if ("content".equalsIgnoreCase(type)) {
                            return wise.getContent().contains(keyword);
                        }
                        return false;
                    })
                    .toList();
    }

    @Override
    public void clear() {
        wises.clear();
    }
}
