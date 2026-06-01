package org.kostlink.service;

import org.kostlink.repository.ComplaintRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository repository;

    public ComplaintService(ComplaintRepository repository) {
        this.repository = repository;
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