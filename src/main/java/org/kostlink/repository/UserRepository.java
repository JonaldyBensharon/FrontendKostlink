package org.kostlink.repository;

import org.kostlink.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    boolean existsByUsername(String username);

    void delete(String username);
}