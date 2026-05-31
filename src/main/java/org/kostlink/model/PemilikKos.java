package org.kostlink.model;

import org.kostlink.Main;

public class PemilikKos extends User {

    public PemilikKos(String username, String password) {
        super(username, password, Role.PEMILIK_KOST);
    }

    @Override
    public void bukaDashboard() {
        Main.jalankanDashboardAdmin(this);
    }
}