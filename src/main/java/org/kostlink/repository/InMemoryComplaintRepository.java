package org.kostlink.repository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryComplaintRepository
        implements ComplaintRepository {

    private final List<String> keluhanList =
            new ArrayList<>();

    @Override
    public void save(String keluhan) {
        if (keluhan != null && !keluhan.trim().isEmpty()) {
            keluhanList.add(keluhan.trim());
        }
    }

    @Override
    public List<String> findAll() {
        return new ArrayList<>(keluhanList);
    }

    @Override
    public void clear() {
        keluhanList.clear();
    }
}