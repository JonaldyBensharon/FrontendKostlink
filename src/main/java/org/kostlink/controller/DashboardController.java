package org.kostlink.controller;

import org.kostlink.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.kostlink.view.DashboardPage;
import org.kostlink.view.KontrakPage;
import org.kostlink.view.LaporanPage;
import org.kostlink.view.TagihanPage;

public class DashboardController {
    private DashboardPage view;

    public DashboardController(DashboardPage view) {
        this.view = view;
        initEvents();
    }

    private void initEvents() {
        // 1. Menu Sidebar: Dashboard (Refresh Halaman Beranda Utama)
        view.getBtnDashboard().setOnAction(e -> Main.showDashboard());

        // 2. Menu Sidebar: Kontrak Saya
        view.getBtnKontrak().setOnAction(e -> tampilkanHalamanKontrak());

        // 3. Menu Sidebar: Riwayat Tagihan
        view.getBtnTagihan().setOnAction(e -> tampilkanHalamanTagihan());

        // 4. Menu Sidebar: Laporan/Keluhan
        view.getBtnLaporan().setOnAction(e -> tampilkanHalamanLaporan());

        // Tombol Logout
        view.getBtnLogout().setOnAction(e -> Main.backToLogin());

        // Proteksi Tombol Lengkapi Data di Beranda Utama
        if (view.getBtnLengkapiData() != null) {
            view.getBtnLengkapiData().setOnAction(e -> Main.goToFormulir());
        }

        // AKSI JALUR UTAMA: Hubungkan tombol bayar jika ia sedang tampil di halaman depan (Dashboard)
        if (view.getBtnBayarSekarang() != null) {
            view.getBtnBayarSekarang().setOnAction(e -> eksekusiAlurBayar());
        }
    }

    // --- LOGIKA PERPINDAHAN HALAMAN (CONTENT AREA SWITCHER) ---

    private void tampilkanHalamanKontrak() {
        KontrakPage kontrakPage = new KontrakPage(view.getNoKamar());
        VBox contentArea = view.getContentArea();
        contentArea.getChildren().setAll(kontrakPage.getLayout());
    }

    private void tampilkanHalamanTagihan() {
        // Buat TagihanPage dengan status pembayaran terbaru
        TagihanPage tagihanPage = new TagihanPage(view.getNoKamar(), Main.getStatusPembayaran());

        // Hubungkan tombol bayar jika ada (status BELUM_BAYAR)
        if (tagihanPage.getBtnBayarSekarang() != null) {
            tagihanPage.getBtnBayarSekarang().setOnAction(e -> eksekusiAlurBayar());
        }

        // Klik Tombol Detail Pembayaran (Hanya tampil jika sudah LUNAS)
        if (tagihanPage.getBtnDetailBayar() != null) {
            tagihanPage.getBtnDetailBayar().setOnAction(e -> {
                TagihanPage.tampilkanPopUpDetailInvoice(view.getNoKamar());
            });
        }

        VBox contentArea = view.getContentArea();
        contentArea.getChildren().setAll(tagihanPage.getLayout());
    }

    private void tampilkanHalamanLaporan() {
        LaporanPage laporanPage = new LaporanPage();

        laporanPage.getBtnKirimLaporan().setOnAction(e -> {
            String teksKeluhan = laporanPage.getTxtKeluhan().getText().trim();
            if (!teksKeluhan.isEmpty()) {
                // HUBUNGKAN KE MAIN AGAR MASUK KE DATABASE SEMENTARA IBU KOST
                Main.tambahKeluhan("Kamar " + view.getNoKamar() + " (" + Main.getNamaLengkapPenghuni() + "): " + teksKeluhan);

                Alert success = new Alert(Alert.AlertType.INFORMATION, "Laporan Anda berhasil dikirim ke pengelola kost! 🚀");
                success.showAndWait();
                laporanPage.getTxtKeluhan().clear();
            } else {
                Alert warning = new Alert(Alert.AlertType.WARNING, "Harap isi keluhan Anda terlebih dahulu!");
                warning.show();
            }
        });

        VBox contentArea = view.getContentArea();
        contentArea.getChildren().setAll(laporanPage.getLayout());
    }

    // =========================================================================
    // PROSES PEMBAYARAN: User kirim bukti → status MENUNGGU_VERIFIKASI
    // Status TIDAK langsung berubah jadi LUNAS, harus dikonfirmasi admin
    // =========================================================================
    private void eksekusiAlurBayar() {
        TagihanPage.tampilkanPopUpPembayaran((buktiPath) -> {
            // 1. Ubah status menjadi MENUNGGU_VERIFIKASI + simpan path bukti
            Main.kirimBuktiPembayaran(buktiPath);

            // 2. Deteksi halaman saat ini SEBELUM Platform.runLater
            VBox contentArea = view.getContentArea();
            boolean isSedangBukaHalamanTagihan = false;
            for (javafx.scene.Node node : contentArea.getChildren()) {
                if (node instanceof Separator) {
                    isSedangBukaHalamanTagihan = true;
                    break;
                }
            }
            final boolean refreshKeTagihan = isSedangBukaHalamanTagihan;

            // 3. Tampilkan alert & refresh SETELAH popup modal benar-benar tertutup
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Bukti pembayaran Anda berhasil dikirim! 📤\n\n" +
                        "Status tagihan akan berubah menjadi LUNAS setelah\n" +
                        "Ibu Kost memverifikasi pembayaran Anda.");
                alert.setHeaderText("Menunggu Verifikasi Admin");
                alert.showAndWait();

                // 4. Refresh tampilan
                if (refreshKeTagihan) {
                    tampilkanHalamanTagihan();
                } else {
                    Main.showDashboard();
                }
            });
        });
    }
}