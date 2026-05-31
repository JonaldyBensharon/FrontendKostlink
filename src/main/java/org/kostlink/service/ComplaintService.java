package org.kostlink.service;

import org.kostlink.repository.ComplaintRepository;
import org.kostlink.repository.InMemoryComplaintRepository;

import java.util.List;

public class ComplaintService {

    private static final ComplaintService instance =
            new ComplaintService();

    private final ComplaintRepository repository;

    private ComplaintService() {
        this.repository =
                new InMemoryComplaintRepository();
    }

    public static ComplaintService getInstance() {
        return instance;
    }

    public List<String> getKeluhanList() {
        return repository.findAll();
    }

    public void tambahKeluhan(String keluhan) {
        repository.save(keluhan);
    }

    public void clearKeluhan() {
        repository.clear();
    }
}