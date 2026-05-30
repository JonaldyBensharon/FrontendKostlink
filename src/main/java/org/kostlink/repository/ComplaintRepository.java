package org.kostlink.repository;

import java.util.List;

public interface ComplaintRepository {

    void save(String keluhan);

    List<String> findAll();

    void clear();
}