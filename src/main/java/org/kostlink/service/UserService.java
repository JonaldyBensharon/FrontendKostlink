package org.kostlink.service;

import org.kostlink.model.Penghuni;
import org.kostlink.model.User;
import org.kostlink.model.PemilikKos;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final UserService instance = new UserService();

    private final Map<String, User> userDatabase;

    private UserService() {
        this.userDatabase = new HashMap<>();
        seedDefaultUsers();
    }

    public static UserService getInstance() {
        return instance;
    }

    // =========================
    // DEFAULT DATA
    // =========================

    private void seedDefaultUsers() {
        userDatabase.put("admin", new PemilikKos("admin", "admin123"));
        userDatabase.put("zaskiah", new PemilikKos("zaskiah", "123"));

        Penghuni defaultPenghuni = new Penghuni("user", "123");

        defaultPenghuni.setNamaLengkap("Penghuni Demo");
        defaultPenghuni.setNomorKamar("A01");
        defaultPenghuni.setStatusAktif(true);

        userDatabase.put(defaultPenghuni.getUsername(), defaultPenghuni);
    }

    // =========================
    // LOGIN
    // =========================

    public User login(String username, String password) {

        if (username == null || password == null) {
            return null;
        }

        User user = userDatabase.get(username.trim());

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    // =========================
    // REGISTER
    // =========================

    public boolean usernameExists(String username) {
        return userDatabase.containsKey(username);
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
        userDatabase.put(username, penghuniBaru);

        return penghuniBaru;
    }

    // =========================
    // LOOKUP
    // =========================

    public User findByUsername(String username) {
        return userDatabase.get(username);
    }

    // =========================
    // DEBUG / TRANSITION SUPPORT
    // =========================

    public Map<String, User> getAllUsers() {
        return new HashMap<>(userDatabase);
    }
}