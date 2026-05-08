package org.kostlink.view;

import org.kostlink.Main;

public class DashboardController {
    private DashboardPage view;

    public DashboardController(DashboardPage view) {
        this.view = view;
        initEvents();
    }

    private void initEvents() {
        // Tombol Sidebar
        view.getBtnDashboard().setOnAction(e -> {
            System.out.println("Klik: Dashboard");
        });

        view.getBtnKontrak().setOnAction(e -> {
            System.out.println("Klik: Kontrak Saya");
        });

        view.getBtnTagihan().setOnAction(e -> {
            System.out.println("Klik: Riwayat Tagihan");
        });

        // Event Tombol Laporan Baru
        view.getBtnLaporan().setOnAction(e -> {
            System.out.println("Klik: Halaman Laporan/Keluhan");
            // Main.showLaporan(); // Panggil method di Main jika sudah dibuat
        });

        view.getBtnPengaturan().setOnAction(e -> {
            System.out.println("Klik: Pengaturan");
        });

        // Tombol Logout
        view.getBtnLogout().setOnAction(e -> {
            Main.backToLogin();
        });

        // Tombol Lengkapi Data (jika ada)
        if (view.getBtnLengkapiData() != null) {
            view.getBtnLengkapiData().setOnAction(e -> {
                Main.goToFormulir();
            });
        }
    }
}