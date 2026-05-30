package org.kostlink.service;

import org.kostlink.model.Penghuni;
import org.kostlink.model.User;

import java.util.ArrayList;
import java.util.List;

public class AppStateService {

    private static final AppStateService instance = new AppStateService();

    private User currentUser;
    private Penghuni currentPenghuni;
    private final List<String> keluhanList;

    private AppStateService() {
        this.keluhanList = new ArrayList<>();
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
    // KELUHAN
    // =========================

    public List<String> getKeluhanList() {
        return keluhanList;
    }

    public void tambahKeluhan(String keluhan) {
        if (keluhan != null && !keluhan.trim().isEmpty()) {
            keluhanList.add(keluhan);
        }
    }

    public void clearKeluhan() {
        keluhanList.clear();
    }

    // =========================
    // RESET SESSION
    // =========================

    public void resetSession() {
        currentUser = null;
        currentPenghuni = null;
        keluhanList.clear();
    }
}