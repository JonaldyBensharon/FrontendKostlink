package org.kostlink.service;

import org.kostlink.model.Penghuni;
import org.kostlink.model.User;
import org.kostlink.model.PemilikKos;
import org.kostlink.repository.JPAUserRepository;
import org.kostlink.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final UserService instance = new UserService();

    private final UserRepository userRepository;

    private UserService() {
        this.userRepository = new JPAUserRepository();
        seedDefaultUsers();
    }

    public static UserService getInstance() {
        return instance;
    }

    // =========================
    // DEFAULT DATA
    // =========================

    private void seedDefaultUsers() {

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new PemilikKos("admin", "admin123"));
        }

        if (!userRepository.existsByUsername("zaskiah")) {
            userRepository.save(new PemilikKos("zaskiah", "123"));
        }

        if (!userRepository.existsByUsername("user")) {
            Penghuni defaultPenghuni = new Penghuni("user", "123");
            defaultPenghuni.setNamaLengkap("Penghuni Demo");
            defaultPenghuni.setNomorKamar("A01");
            defaultPenghuni.setStatusAktif(true);

            userRepository.save(defaultPenghuni);
        }
    }

    // =========================
    // LOGIN
    // =========================

    public User login(String username, String password) {

        if (username == null || password == null) {
            return null;
        }

        return userRepository.findByUsername(username.trim())
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    // =========================
    // REGISTER
    // =========================
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

    // =========================
    // LOOKUP
    // =========================

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // =========================
    // DEBUG / TRANSITION SUPPORT
    // =========================

    public Map<String, User> getAllUsers() {
        Map<String, User> users = new HashMap<>();

        for (User user : userRepository.findAll()) {
            users.put(user.getUsername(), user);
        }

        return users;
    }
}