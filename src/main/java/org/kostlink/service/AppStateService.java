package org.kostlink.service;

import org.kostlink.model.User;

import org.springframework.stereotype.Component;

@Component
public class AppStateService {

    private User currentUser;

    public AppStateService() {
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