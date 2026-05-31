package org.kostlink.repository;

import org.kostlink.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        Optional<User> existing = findByUsername(user.getUsername());

        if (existing.isPresent()) {
            users.remove(existing.get());
        }

        users.add(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public void delete(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
    }
}