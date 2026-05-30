package org.kostlink.service;

import org.kostlink.model.Penghuni;
import org.kostlink.model.User;

public class AppStateService {

    private static final AppStateService instance = new AppStateService();

    private User currentUser;
    private Penghuni currentPenghuni;

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
    // PENGHUNI ACTIVE DATA
    // =========================

    public Penghuni getCurrentPenghuni() {
        return currentPenghuni;
    }

    public void setCurrentPenghuni(Penghuni currentPenghuni) {
        this.currentPenghuni = currentPenghuni;
    }

    // =========================
    // RESET SESSION
    // =========================

    public void resetSession() {
        currentUser = null;
        currentPenghuni = null;
    }
}