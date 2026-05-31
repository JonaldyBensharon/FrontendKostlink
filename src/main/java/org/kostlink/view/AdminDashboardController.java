package org.kostlink.view;

import org.kostlink.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kostlink.model.Penghuni;
import org.kostlink.model.User;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class AdminDashboardController {
    private AdminDashboardPage view;

    public AdminDashboardController(AdminDashboardPage view) {
        this.view = view;
        initEvents();
        tampilkanDataPenghuni();
    }

    private void initEvents() {
        view.getBtnDataPenghuni().setOnAction(e -> tampilkanDataPenghuni());
        view.getBtnValidasiBayar().setOnAction(e -> tampilkanValidasiPembayaran());
        view.getBtnKeluhanPenghuni().setOnAction(e -> tampilkanKeluhanPenghuni());
        view.getBtnLogout().setOnAction(e -> Main.backToLogin());
    }

    // =========================================================================
    // 1. MENU: MONITORING DATA PENGHUNI & STATUS JATUH TEMPO
    // =========================================================================
    private void tampilkanDataPenghuni() {
        VBox area = view.getContentArea();
        area.getChildren().clear();

        Label title = new Label("Dashboard Monitoring Ibu Kost");
        title.setFont(Font.font("System", FontWeight.BOLD, 26));

        HBox summaryRow = new HBox(20);
        summaryRow.setMaxWidth(Double.MAX_VALUE);

        String statusPembayaran = Main.getStatusPembayaran();

        // Cek apakah siklus baru sudah dimulai (jatuh tempo +30 hari sudah lewat)
        LocalDate hariIni = LocalDate.now();
        LocalDate jatuhTempoBerikutnya = Main.getJatuhTempoBerikutnya();

        if ("LUNAS".equals(statusPembayaran) && jatuhTempoBerikutnya != null) {
            boolean siklusExpired =
                    !hariIni.isBefore(jatuhTempoBerikutnya);

            if (siklusExpired) {
                Main.resetPembayaran();
                statusPembayaran = Main.getStatusPembayaran(); // sinkronisasi ulang
            }
        }

        // Pengambilan data dari database untuk keperluan penampilan

        Map<String, User> users = Main.getUserService().getAllUsers();

        int totalPenghuni = 0;
        int sudahBayar = 0;
        int menungguVerif = 0;
        int belumBayar = 0;

        for (User u : users.values()) {

            if (u instanceof Penghuni p && p.isStatusAktif()) {
                totalPenghuni++;

                String status = p.getStatusPembayaran();

                if ("LUNAS".equals(status)) {
                    sudahBayar++;
                } else if ("MENUNGGU_VERIFIKASI".equals(status)) {
                    menungguVerif++;
                } else {
                    belumBayar++;
                }
            }
        }

        int jumlahKeluhan = Main.getListKeluhan().size();

        summaryRow.getChildren().addAll(
                createCardMini("Total Penghuni", totalPenghuni + " Orang", "#3B82F6"),
                createCardMini("Sudah Lunas", sudahBayar + " Kamar", "#10B981"),
                createCardMini("Menunggu Verifikasi", menungguVerif + " Kamar", "#F59E0B"),
                createCardMini("Belum Bayar", belumBayar + " Kamar", "#EF4444")
        );

        // --- KARTU RINCIAN DATA DIRI ---
        VBox detailCard = new VBox(15);
        detailCard.setPadding(new Insets(25));
        detailCard.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0;");

        Label lblSection = new Label("Tabel Pemantauan Profil & Jatuh Tempo");
        lblSection.setFont(Font.font("System", FontWeight.BOLD, 18));
        detailCard.getChildren().addAll(lblSection, new Separator());

        if (Main.getStatusAktif()) {
            GridPane grid = new GridPane();
            grid.setHgap(30);
            grid.setVgap(12);

            // Hitung jatuh tempo untuk ditampilkan
            LocalDate jatuhTempoTampil;
            if ("LUNAS".equals(statusPembayaran) && jatuhTempoBerikutnya != null) {
                jatuhTempoTampil = jatuhTempoBerikutnya;
            } else {
                int tanggalSiklus = Main.getTanggalSiklusKost();
                int maxHari = hariIni.lengthOfMonth();
                int tanggalSiklusValid = Math.min(tanggalSiklus, maxHari);
                jatuhTempoTampil = LocalDate.of(hariIni.getYear(), hariIni.getMonth(), tanggalSiklusValid);
                if (jatuhTempoTampil.isBefore(hariIni)) {
                    jatuhTempoTampil = jatuhTempoTampil.plusMonths(1);
                }
            }
            String tglFormat = jatuhTempoTampil.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

            grid.add(new Label("Nama Lengkap"), 0, 0);
            grid.add(createBoldLabel(": " + Main.getNamaLengkapPenghuni()), 1, 0);

            grid.add(new Label("Alokasi Kamar"), 0, 1);
            grid.add(createBoldLabel(": Kamar " + Main.getNomorKamarPenghuni()), 1, 1);

            grid.add(new Label("Tanggal Siklus"), 0, 2);
            grid.add(new Label(": Tiap Tanggal " + Main.getTanggalSiklusKost()), 1, 2);

            grid.add(new Label("Batas Jatuh Tempo"), 0, 3);
            Label lblTgl = new Label(": " + tglFormat);
            lblTgl.setStyle("-fx-text-fill: #1E3A8A; -fx-font-weight: bold;");
            grid.add(lblTgl, 1, 3);

            grid.add(new Label("Status Keuangan"), 0, 4);
            Label lblFinansial;
            switch (statusPembayaran) {
                case "MENUNGGU_VERIFIKASI":
                    lblFinansial = new Label(": MENUNGGU VERIFIKASI ⏳");
                    lblFinansial.setStyle("-fx-font-weight: bold; -fx-text-fill: #92400E;");
                    break;
                case "LUNAS":
                    lblFinansial = new Label(": LUNAS ✅");
                    lblFinansial.setTextFill(Color.GREEN);
                    lblFinansial.setStyle("-fx-font-weight: bold;");
                    break;
                default:
                    lblFinansial = new Label(": MENUNGGU PEMBAYARAN");
                    lblFinansial.setTextFill(Color.RED);
                    lblFinansial.setStyle("-fx-font-weight: bold;");
                    break;
            }
            grid.add(lblFinansial, 1, 4);

            // Tampilkan tanggal konfirmasi admin jika ada
            LocalDate tglKonfirmasi = Main.getTanggalKonfirmasiAdmin();
            if (tglKonfirmasi != null) {
                grid.add(new Label("Terakhir Dikonfirmasi"), 0, 5);
                Label lblKonfirmasi = new Label(": " + tglKonfirmasi.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                lblKonfirmasi.setStyle("-fx-font-weight: bold; -fx-text-fill: #166534;");
                grid.add(lblKonfirmasi, 1, 5);
            }

            detailCard.getChildren().add(grid);

            // Notifikasi jatuh tempo di panel admin (hanya jika BELUM_BAYAR)
            if ("BELUM_BAYAR".equals(statusPembayaran)) {
                long selisihHari = ChronoUnit.DAYS.between(hariIni, jatuhTempoTampil);
                VBox notifAdmin = new VBox(5);
                notifAdmin.setPadding(new Insets(12, 15, 12, 15));

                String pesanAdmin, bgColor, borderColor, textColor;
                if (selisihHari < 0) {
                    pesanAdmin = "⚠️ Penghuni sudah melewati jatuh tempo " + Math.abs(selisihHari) + " hari!";
                    bgColor = "#FEF2F2"; borderColor = "#FECACA"; textColor = "#991B1B";
                } else if (selisihHari == 0) {
                    pesanAdmin = "🔴 Hari ini adalah jatuh tempo penghuni!";
                    bgColor = "#FEF2F2"; borderColor = "#FCA5A5"; textColor = "#B91C1C";
                } else if (selisihHari <= 3) {
                    pesanAdmin = "⏰ " + selisihHari + " hari lagi jatuh tempo penghuni kamar " + Main.getNomorKamarPenghuni();
                    bgColor = "#FFFBEB"; borderColor = "#FDE68A"; textColor = "#92400E";
                } else {
                    pesanAdmin = null; bgColor = ""; borderColor = ""; textColor = "";
                }

                if (pesanAdmin != null) {
                    notifAdmin.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: " + borderColor + "; -fx-background-radius: 8; -fx-border-radius: 8;");
                    Label lblNotif = new Label(pesanAdmin);
                    lblNotif.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: " + textColor + ";");
                    lblNotif.setWrapText(true);
                    notifAdmin.getChildren().add(lblNotif);
                    detailCard.getChildren().add(notifAdmin);
                }
            }

            // Notifikasi khusus: ada pembayaran yang menunggu verifikasi
            if ("MENUNGGU_VERIFIKASI".equals(statusPembayaran)) {
                VBox notifPending = new VBox(5);
                notifPending.setPadding(new Insets(12, 15, 12, 15));
                notifPending.setStyle("-fx-background-color: #FFFBEB; -fx-border-color: #FDE68A; -fx-background-radius: 8; -fx-border-radius: 8;");

                LocalDate tglKirim = Main.getTanggalKirimBukti();
                String tglKirimStr = (tglKirim != null) ? tglKirim.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "-";
                Label lblPending = new Label("📋 Ada bukti pembayaran masuk pada " + tglKirimStr + " yang belum diverifikasi. Silakan cek di menu Validasi Pembayaran.");
                lblPending.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: #92400E;");
                lblPending.setWrapText(true);
                notifPending.getChildren().add(lblPending);
                detailCard.getChildren().add(notifPending);
            }

        } else {
            Label empty = new Label("Belum ada data penghuni kost aktif di sistem.");
            empty.setTextFill(Color.GRAY);
            detailCard.getChildren().add(empty);
        }

        area.getChildren().addAll(title, new Separator(), summaryRow, detailCard);
    }

    // =========================================================================
    // 2. MENU: VALIDASI RIWAYAT PEMBAYARAN
    // Disini admin bisa: Konfirmasi (LUNAS) / Tolak / Reset
    // =========================================================================
    private void tampilkanValidasiPembayaran() {
        VBox area = view.getContentArea();
        area.getChildren().clear();

        Label title = new Label("Manajemen Riwayat & Validasi Pembayaran");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));

        VBox containerBox = new VBox(15);
        containerBox.setPadding(new Insets(20));
        containerBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0;");

        if (Main.getStatusAktif()) {
            String statusPembayaran = Main.getStatusPembayaran();

            HBox rowData = new HBox(20);
            rowData.setAlignment(Pos.CENTER_LEFT);
            rowData.setPadding(new Insets(15));

            VBox info = new VBox(5);
            Label lblUser = new Label("Kamar " + Main.getNomorKamarPenghuni() + " - " + Main.getNamaLengkapPenghuni());
            lblUser.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");

            Label lblStatus = new Label();
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            if ("MENUNGGU_VERIFIKASI".equals(statusPembayaran)) {
                // ===== MENUNGGU VERIFIKASI: Admin bisa Konfirmasi atau Tolak =====
                rowData.setStyle("-fx-background-color: #FFFBEB; -fx-border-color: #FDE68A; -fx-background-radius: 8; -fx-border-radius: 8;");
                lblStatus.setText("STATUS: MENUNGGU VERIFIKASI ⏳");
                lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #92400E;");

                LocalDate tglKirim = Main.getTanggalKirimBukti();
                String tglKirimStr = (tglKirim != null) ? tglKirim.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "-";
                Label lblTgl = new Label("Bukti dikirim pada: " + tglKirimStr);
                lblTgl.setStyle("-fx-font-size: 12; -fx-text-fill: #78350F;");

                info.getChildren().addAll(lblUser, lblStatus, lblTgl);

                // Tampilkan info bukti pembayaran jika ada
                String buktiPath = Main.getBuktiPembayaranPath();
                if (buktiPath != null) {
                    Label lblBukti = new Label("📎 Bukti pembayaran telah di-upload");
                    lblBukti.setStyle("-fx-font-size: 12; -fx-text-fill: #5B21B6; -fx-font-weight: bold;");
                    info.getChildren().add(lblBukti);
                }

                // Tombol Lihat Bukti
                Button btnLihatBukti = new Button("Lihat Bukti 🖼️");
                btnLihatBukti.setStyle("-fx-background-color: #7C3AED; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
                btnLihatBukti.setDisable(buktiPath == null);
                btnLihatBukti.setOnAction(e -> {
                    if (buktiPath != null) {
                        tampilkanPopUpBuktiPembayaran(buktiPath);
                    }
                });

                // Tombol Konfirmasi LUNAS
                Button btnKonfirmasi = new Button("Konfirmasi LUNAS ✅");
                btnKonfirmasi.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
                btnKonfirmasi.setOnAction(e -> {
                    Main.konfirmasiPembayaranAdmin();

                    LocalDate jatuhTempoBaru = Main.getJatuhTempoBerikutnya();
                    String tglJT = (jatuhTempoBaru != null)
                            ? jatuhTempoBaru.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                            : "-";

                    Alert a = new Alert(Alert.AlertType.INFORMATION,
                            "Pembayaran berhasil diverifikasi! ✅\n\n" +
                            "Status penghuni: LUNAS\n" +
                            "Jatuh tempo berikutnya: " + tglJT + "\n" +
                            "(30 hari setelah tanggal konfirmasi)");
                    a.setHeaderText("Konfirmasi Berhasil");
                    a.showAndWait();
                    tampilkanValidasiPembayaran();
                });

                // Tombol Tolak
                Button btnTolak = new Button("Tolak Pembayaran ❌");
                btnTolak.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
                btnTolak.setOnAction(e -> {
                    Main.tolakPembayaranAdmin();
                    Alert a = new Alert(Alert.AlertType.WARNING,
                            "Bukti pembayaran ditolak!\n" +
                            "Status dikembalikan ke BELUM BAYAR.\n" +
                            "Penghuni harus mengirim ulang bukti pembayaran.");
                    a.setHeaderText("Pembayaran Ditolak");
                    a.showAndWait();
                    tampilkanValidasiPembayaran();
                });

                HBox tombolArea = new HBox(10);
                tombolArea.setAlignment(Pos.CENTER);
                tombolArea.getChildren().addAll(btnLihatBukti, btnKonfirmasi, btnTolak);
                rowData.getChildren().addAll(info, spacer, tombolArea);

            } else if ("LUNAS".equals(statusPembayaran)) {
                // ===== SUDAH LUNAS =====
                rowData.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: #BBF7D0; -fx-background-radius: 8; -fx-border-radius: 8;");
                lblStatus.setText("STATUS: TERVERIFIKASI LUNAS 🎉");
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setStyle("-fx-font-weight: bold;");

                LocalDate tglKonfirmasi = Main.getTanggalKonfirmasiAdmin();
                Label lblTglKonfirmasi = new Label();
                if (tglKonfirmasi != null) {
                    lblTglKonfirmasi.setText("Dikonfirmasi pada: " + tglKonfirmasi.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                    lblTglKonfirmasi.setStyle("-fx-font-size: 12; -fx-text-fill: #166534;");
                }

                LocalDate jatuhTempoBaru = Main.getJatuhTempoBerikutnya();
                Label lblJT = new Label();
                if (jatuhTempoBaru != null) {
                    lblJT.setText("Jatuh tempo berikutnya: " + jatuhTempoBaru.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                    lblJT.setStyle("-fx-font-size: 12; -fx-text-fill: #1E3A8A; -fx-font-weight: bold;");
                }

                info.getChildren().addAll(lblUser, lblStatus);
                if (tglKonfirmasi != null) info.getChildren().add(lblTglKonfirmasi);
                if (jatuhTempoBaru != null) info.getChildren().add(lblJT);

                Button btnReset = new Button("Reset Jadi Belum Bayar 🔄");
                btnReset.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
                btnReset.setOnAction(e -> {
                    Main.resetPembayaran();
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Tagihan berhasil di-reset ke Belum Bayar!");
                    a.showAndWait();
                    tampilkanValidasiPembayaran();
                });
                rowData.getChildren().addAll(info, spacer, btnReset);

            } else {
                // ===== BELUM BAYAR =====
                rowData.setStyle("-fx-background-color: #FFF5F5; -fx-border-color: #FFCCCC; -fx-background-radius: 8; -fx-border-radius: 8;");
                lblStatus.setText("STATUS: BELUM BAYAR / MENUNGGU TRANSFER 💰");
                lblStatus.setTextFill(Color.RED);
                lblStatus.setStyle("-fx-font-weight: bold;");
                info.getChildren().addAll(lblUser, lblStatus);

                Button btnPaksaLunas = new Button("Konfirmasi Lunas Manual (Admin) ✅");
                btnPaksaLunas.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
                btnPaksaLunas.setOnAction(e -> {
                    Main.konfirmasiPembayaranAdmin();

                    LocalDate jatuhTempoBaru = Main.getJatuhTempoBerikutnya();
                    String tglJT = (jatuhTempoBaru != null)
                            ? jatuhTempoBaru.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                            : "-";

                    Alert a = new Alert(Alert.AlertType.INFORMATION,
                            "Pembayaran berhasil divalidasi langsung oleh Ibu Kost!\n\n" +
                            "Jatuh tempo berikutnya: " + tglJT);
                    a.setHeaderText("Konfirmasi Manual Berhasil");
                    a.showAndWait();
                    tampilkanValidasiPembayaran();
                });

                rowData.getChildren().addAll(info, spacer, btnPaksaLunas);
            }
            containerBox.getChildren().add(rowData);
        } else {
            containerBox.getChildren().add(new Label("Tidak ada data transaksi pembayaran berjalan."));
        }

        area.getChildren().addAll(title, new Separator(), containerBox);
    }

    // =========================================================================
    // 3. MENU: KOTAK MASUK KELUHAN NYATA DARI PENGHUNI
    // =========================================================================
    private void tampilkanKeluhanPenghuni() {
        VBox area = view.getContentArea();
        area.getChildren().clear();

        Label title = new Label("Kotak Masuk Laporan Keluhan Penghuni");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));

        VBox listContainer = new VBox(15);
        listContainer.setPadding(new Insets(20));
        listContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0;");

        if (Main.getListKeluhan().isEmpty()) {
            Label empty = new Label("Bersih! Tidak ada laporan keluhan masuk dari penghuni saat ini. ✨");
            empty.setTextFill(Color.GRAY);
            listContainer.getChildren().add(empty);
        } else {
            // Looping isi daftar keluhan yang dikirim dari penghuni secara real-time
            for (String keluhan : Main.getListKeluhan()) {
                HBox card = new HBox(10);
                card.setPadding(new Insets(15));
                card.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-background-radius: 8;");

                Label lblIcon = new Label("📩 ");
                Label lblIsi = new Label(keluhan);
                lblIsi.setStyle("-fx-font-size: 14;");

                card.getChildren().addAll(lblIcon, lblIsi);
                listContainer.getChildren().add(card);
            }
        }

        area.getChildren().addAll(title, new Separator(), listContainer);
    }

    // =========================================================================
    // POPUP: LIHAT BUKTI PEMBAYARAN YANG DI-UPLOAD USER
    // =========================================================================
    private void tampilkanPopUpBuktiPembayaran(String filePath) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("KOSTLINK - Bukti Pembayaran");

        VBox root = new VBox(16);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white;");

        Label lblTitle = new Label("🖼️ Bukti Pembayaran");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: #1F2937;");

        Label lblPenghuni = new Label("Penghuni: " + Main.getNamaLengkapPenghuni() + " — Kamar " + Main.getNomorKamarPenghuni());
        lblPenghuni.setStyle("-fx-font-size: 13; -fx-text-fill: #4B5563;");

        if (filePath != null) {
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                Image img = new Image(imgFile.toURI().toString(), 420, 400, true, true);
                ImageView imageView = new ImageView(img);
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);");

                VBox imgContainer = new VBox(8);
                imgContainer.setAlignment(Pos.CENTER);
                imgContainer.setPadding(new Insets(12));
                imgContainer.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-background-radius: 12; -fx-border-radius: 12;");

                Label lblFile = new Label("📁 " + imgFile.getName());
                lblFile.setStyle("-fx-font-size: 11; -fx-text-fill: #6B7280;");
                imgContainer.getChildren().addAll(imageView, lblFile);
                root.getChildren().addAll(lblTitle, lblPenghuni, new Separator(), imgContainer);
            } else {
                Label lblError = new Label("⚠️ File tidak ditemukan: " + filePath);
                lblError.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 13;");
                lblError.setWrapText(true);
                root.getChildren().addAll(lblTitle, lblPenghuni, lblError);
            }
        } else {
            Label lblNone = new Label("Tidak ada bukti pembayaran yang di-upload.");
            lblNone.setStyle("-fx-text-fill: #6B7280;");
            root.getChildren().addAll(lblTitle, lblPenghuni, lblNone);
        }

        Button btnTutup = new Button("Tutup");
        btnTutup.setStyle("-fx-background-color: #E5E7EB; -fx-text-fill: #374151; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 8; -fx-cursor: hand;");
        btnTutup.setOnAction(e -> window.close());
        root.getChildren().add(btnTutup);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: white;");
        window.setScene(new Scene(sp, 500, 550));
        window.showAndWait();
    }

    // --- HELPER COMPONENT DESIGN ---
    private VBox createCardMini(String title, String value, String hexColor) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: " + hexColor + "; -fx-border-width: 0 0 0 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 10, 0, 0, 5);");

        Label lblT = new Label(title);
        lblT.setTextFill(Color.GRAY);
        lblT.setFont(Font.font(13));

        Label lblV = new Label(value);
        lblV.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblV.setStyle("-fx-text-fill: " + hexColor + ";");

        card.getChildren().addAll(lblT, lblV);
        return card;
    }

    private Label createBoldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-weight: bold;");
        return l;
    }
}