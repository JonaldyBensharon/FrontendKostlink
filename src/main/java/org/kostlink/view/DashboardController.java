package org.kostlink.view;

import org.kostlink.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

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

        // 5. Menu Sidebar: Pengaturan
        view.getBtnPengaturan().setOnAction(e -> tampilkanHalamanPengaturan());

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
        // Ambil status terbaru dari Main.getIsSudahBayar() agar selalu sinkron
        TagihanPage tagihanPage = new TagihanPage(view.getNoKamar(), Main.getIsSudahBayar());

        // AKSI JALUR CADANGAN: Hubungkan juga tombol bayar jika pengguna membukanya lewat menu sidebar tagihan
        if (tagihanPage.getBtnBayarSekarang() != null) {
            tagihanPage.getBtnBayarSekarang().setOnAction(e -> eksekusiAlurBayar());
        }

        // Klik Tombol Detail Pembayaran pada Riwayat Tagihan (Hanya tampil jika sudah lunas)
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

    private void tampilkanHalamanPengaturan() {
        PengaturanPage pengaturanPage = new PengaturanPage();
        pengaturanPage.getBtnSimpanSesi().setOnAction(e -> {
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Pengaturan berhasil diperbarui!");
            success.show();
        });

        VBox contentArea = view.getContentArea();
        contentArea.getChildren().setAll(pengaturanPage.getLayout());
    }

    // --- PROSES POP-UP PEMBAYARAN KUSTOM YANG SUDAH DIPERBAIKI ---
    private void eksekusiAlurBayar() {
        TagihanPage.tampilkanPopUpPembayaran(() -> {
            // 1. Ubah status data di Main menjadi true (Lunas)
            Main.setIsSudahBayar(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Terima kasih! Pembayaran Anda berhasil divalidasi. 🎉");
            alert.showAndWait();

            // 2. DETEKSI REFRESH PINTAR:
            VBox contentArea = view.getContentArea();

            // Kita cek apakah di dalam contentArea ada komponen Separator.
            // TagihanPage meletakkan Separator di komponen kedua (index 1) setelah judul.
            boolean isSedangBukaHalamanTagihan = false;
            for (javafx.scene.Node node : contentArea.getChildren()) {
                if (node instanceof Separator) {
                    isSedangBukaHalamanTagihan = true;
                    break;
                }
            }

            if (isSedangBukaHalamanTagihan) {
                // Jika user mengklik "Saya Sudah Bayar" dari dalam halaman riwayat tagihan,
                // jalankan fungsi ini agar halaman tagihannya langsung berubah jadi hijau di tempat!
                tampilkanHalamanTagihan();
            } else {
                // Jika user mengkliknya dari halaman utama (Dashboard huni),
                // refresh struktur dashboard utamanya!
                Main.showDashboard();
            }
        });
    }
}