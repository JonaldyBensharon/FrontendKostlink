package org.kostlink.service;

import org.kostlink.model.User;

public class AppStateService {

    private static final AppStateService instance = new AppStateService();

    private User currentUser;

    private AppStateService() {

    }

    public static AppStateService getInstance() {
        return instance;
    }

    // =========================
    // USER SESSION
    // =========================

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // =========================
    // RESET SESSION
    // =========================

    public void resetSession() {
        currentUser = null;
    }
}