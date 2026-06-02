package org.kostlink.service;

import org.kostlink.model.Penghuni;
import org.kostlink.model.User;
import org.kostlink.model.PemilikKos;
import org.kostlink.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        seedDefaultUsers();
    }

    // Data pemilik kos
    private void seedDefaultUsers() {

        if (!userRepository.existsByUsername("administrator")) {
            userRepository.save(new PemilikKos("administrator", "adminKostlink"));
        }
    }

    // Login
    public User login(String username, String password) {

        if (username == null || password == null) {
            return null;
        }

        User user = userRepository.findByUsername(username.trim())
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);

        if (user != null) {
            System.out.println("LOGIN SUCCESS: " + user.getUsername());
            System.out.println("ROLE: " + user.getRole());
            System.out.println("CLASS: " + user.getClass().getSimpleName());
        }

        return user;
    }

    // Registrasi
    public boolean usernameExists(String username) {
        if (username == null) {
            return false;
        }

        return userRepository.existsByUsername(username.trim());
    }

    public Penghuni registerPenghuni(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        username = username.trim();

        if (usernameExists(username)) {
            return null;
        }

        Penghuni penghuniBaru = new Penghuni(username, password);
        userRepository.save(penghuniBaru);

        return penghuniBaru;
    }

    // cari berdasarkan nama pengguna
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // DEBUG/TRANSITION SUPPORT
    public Map<String, User> getAllUsers() {
        Map<String, User> users = new HashMap<>();

        for (User user : userRepository.findAll()) {
            users.put(user.getUsername(), user);
        }

        return users;
    }
}