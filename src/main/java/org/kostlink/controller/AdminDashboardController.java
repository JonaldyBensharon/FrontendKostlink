package org.kostlink.controller;

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
import org.kostlink.service.PenghuniService;
import org.kostlink.view.AdminDashboardPage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class AdminDashboardController {
    private AdminDashboardPage view;
    private final PenghuniService penghuniService;

    public AdminDashboardController(AdminDashboardPage view, PenghuniService penghuniService) {
        this.view = view;
        this.penghuniService = penghuniService;
        initEvents();
        tampilkanDataPenghuni();
    }

    private void initEvents() {
        view.getBtnDataPenghuni().setOnAction(e -> {
            view.setActiveButton(view.getBtnDataPenghuni());
            tampilkanDataPenghuni();
        });
        view.getBtnValidasiBayar().setOnAction(e -> {
            view.setActiveButton(view.getBtnValidasiBayar());
            tampilkanValidasiPembayaran();
        });
        view.getBtnKeluhanPenghuni().setOnAction(e -> {
            view.setActiveButton(view.getBtnKeluhanPenghuni());
            tampilkanKeluhanPenghuni();
        });
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

        // Ambil semua penghuni dari database
        List<Penghuni> semuaPenghuni = penghuniService.findAllAktifPenghuni();

        int totalPenghuni = semuaPenghuni.size();
        int sudahBayar = 0, menungguVerif = 0, belumBayar = 0;

        for (Penghuni p : semuaPenghuni) {
            String status = p.getStatusPembayaran();
            if ("LUNAS".equals(status)) sudahBayar++;
            else if ("MENUNGGU_VERIFIKASI".equals(status)) menungguVerif++;
            else belumBayar++;
        }

        int jumlahKeluhan = Main.getListKeluhan().size();

        HBox summaryRow = new HBox(20);
        summaryRow.setMaxWidth(Double.MAX_VALUE);
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

        if (semuaPenghuni.isEmpty()) {
            Label empty = new Label("Belum ada data penghuni kost aktif di sistem.");
            empty.setTextFill(Color.GRAY);
            detailCard.getChildren().add(empty);
        } else {
            for (Penghuni p : semuaPenghuni) {
                VBox penghuniCard = buildPenghuniDetailCard(p);
                detailCard.getChildren().add(penghuniCard);
            }
        }

        area.getChildren().addAll(title, new Separator(), summaryRow, detailCard);
    }

    private VBox buildPenghuniDetailCard(Penghuni p) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 10; -fx-border-color: #E5E7EB; -fx-border-radius: 10;");

        LocalDate hariIni = LocalDate.now();
        String statusPembayaran = p.getStatusPembayaran();

        // Hitung jatuh tempo
        LocalDate jatuhTempoTampil;
        LocalDate jatuhTempoBerikutnya = (p.getTanggalKonfirmasiAdmin() != null)
                ? p.getTanggalKonfirmasiAdmin().plusDays(30) : null;

        if ("LUNAS".equals(statusPembayaran) && jatuhTempoBerikutnya != null) {
            jatuhTempoTampil = jatuhTempoBerikutnya;
        } else {
            int tanggalSiklus = p.getTanggalSiklusKost();
            int maxHari = hariIni.lengthOfMonth();
            int tanggalSiklusValid = Math.min(tanggalSiklus, maxHari);
            jatuhTempoTampil = LocalDate.of(hariIni.getYear(), hariIni.getMonth(), tanggalSiklusValid);
            if (jatuhTempoTampil.isBefore(hariIni)) {
                jatuhTempoTampil = jatuhTempoTampil.plusMonths(1);
            }
        }
        String tglFormat = jatuhTempoTampil.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(8);

        grid.add(new Label("Nama Lengkap"), 0, 0);
        grid.add(createBoldLabel(": " + p.getNamaLengkap()), 1, 0);
        grid.add(new Label("Alokasi Kamar"), 0, 1);
        grid.add(createBoldLabel(": Kamar " + p.getNomorKamar()), 1, 1);
        grid.add(new Label("Tanggal Siklus"), 0, 2);
        grid.add(new Label(": Tiap Tanggal " + p.getTanggalSiklusKost()), 1, 2);
        grid.add(new Label("Batas Jatuh Tempo"), 0, 3);
        Label lblTgl = new Label(": " + tglFormat);
        lblTgl.setStyle("-fx-text-fill: #1E3A8A; -fx-font-weight: bold;");
        grid.add(lblTgl, 1, 3);

        grid.add(new Label("Status Keuangan"), 0, 4);
        Label lblFinansial;
        switch (statusPembayaran) {
            case "MENUNGGU_VERIFIKASI":
                lblFinansial = new Label(": MENUNGGU VERIFIKASI");
                lblFinansial.setStyle("-fx-font-weight: bold; -fx-text-fill: #92400E;");
                break;
            case "LUNAS":
                lblFinansial = new Label(": LUNAS");
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

        if (p.getTanggalKonfirmasiAdmin() != null) {
            grid.add(new Label("Terakhir Dikonfirmasi"), 0, 5);
            Label lblK = new Label(": " + p.getTanggalKonfirmasiAdmin().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            lblK.setStyle("-fx-font-weight: bold; -fx-text-fill: #166534;");
            grid.add(lblK, 1, 5);
        }

        card.getChildren().add(grid);

        // Notifikasi jatuh tempo
        if ("BELUM_BAYAR".equals(statusPembayaran)) {
            long selisihHari = ChronoUnit.DAYS.between(hariIni, jatuhTempoTampil);
            String pesanAdmin = null, bgColor, borderColor, textColor;

            if (selisihHari < 0) {
                pesanAdmin = "Penghuni sudah melewati jatuh tempo " + Math.abs(selisihHari) + " hari!";
                bgColor = "#FEF2F2"; borderColor = "#FECACA"; textColor = "#991B1B";
            } else if (selisihHari == 0) {
                pesanAdmin = "Hari ini adalah jatuh tempo penghuni!";
                bgColor = "#FEF2F2"; borderColor = "#FCA5A5"; textColor = "#B91C1C";
            } else if (selisihHari <= 3) {
                pesanAdmin = selisihHari + " hari lagi jatuh tempo penghuni kamar " + p.getNomorKamar();
                bgColor = "#FFFBEB"; borderColor = "#FDE68A"; textColor = "#92400E";
            } else {
                bgColor = ""; borderColor = ""; textColor = "";
            }

            if (pesanAdmin != null) {
                Label lblNotif = new Label(pesanAdmin);
                lblNotif.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: " + textColor + ";");
                lblNotif.setWrapText(true);
                VBox notifBox = new VBox(lblNotif);
                notifBox.setPadding(new Insets(10));
                notifBox.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: " + borderColor + "; -fx-background-radius: 8; -fx-border-radius: 8;");
                card.getChildren().add(notifBox);
            }
        }

        if ("MENUNGGU_VERIFIKASI".equals(statusPembayaran)) {
            LocalDate tglKirim = p.getTanggalKirimBukti();
            String tglKirimStr = (tglKirim != null) ? tglKirim.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "-";
            Label lblPending = new Label("Ada bukti pembayaran masuk pada " + tglKirimStr + " yang belum diverifikasi.");
            lblPending.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: #92400E;");
            lblPending.setWrapText(true);
            VBox notifPending = new VBox(lblPending);
            notifPending.setPadding(new Insets(10));
            notifPending.setStyle("-fx-background-color: #FFFBEB; -fx-border-color: #FDE68A; -fx-background-radius: 8; -fx-border-radius: 8;");
            card.getChildren().add(notifPending);
        }

        return card;
    }

    // =========================================================================
    // 2. MENU: VALIDASI RIWAYAT PEMBAYARAN
    // =========================================================================
    private void tampilkanValidasiPembayaran() {
        VBox area = view.getContentArea();
        area.getChildren().clear();

        Label title = new Label("Manajemen Riwayat & Validasi Pembayaran");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));

        VBox containerBox = new VBox(15);
        containerBox.setPadding(new Insets(20));
        containerBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0;");

        // Ambil semua penghuni aktif dari database
        List<Penghuni> semuaPenghuni = penghuniService.findAllAktifPenghuni();

        if (semuaPenghuni.isEmpty()) {
            containerBox.getChildren().add(new Label("Tidak ada data transaksi pembayaran berjalan."));
        } else {
            for (Penghuni p : semuaPenghuni) {
                HBox rowData = buildPaymentRow(p);
                containerBox.getChildren().add(rowData);
            }
        }

        area.getChildren().addAll(title, new Separator(), containerBox);
    }

    private HBox buildPaymentRow(Penghuni p) {
        String statusPembayaran = p.getStatusPembayaran();

        HBox rowData = new HBox(20);
        rowData.setAlignment(Pos.CENTER_LEFT);
        rowData.setPadding(new Insets(15));

        VBox info = new VBox(5);
        Label lblUser = new Label("Kamar " + p.getNomorKamar() + " - " + p.getNamaLengkap());
        lblUser.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");

        Label lblStatus = new Label();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if ("MENUNGGU_VERIFIKASI".equals(statusPembayaran)) {
            rowData.setStyle("-fx-background-color: #FFFBEB; -fx-border-color: #FDE68A; -fx-background-radius: 8; -fx-border-radius: 8;");
            lblStatus.setText("STATUS: MENUNGGU VERIFIKASI");
            lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #92400E;");

            LocalDate tglKirim = p.getTanggalKirimBukti();
            String tglKirimStr = (tglKirim != null) ? tglKirim.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "-";
            Label lblTgl = new Label("Bukti dikirim pada: " + tglKirimStr);
            lblTgl.setStyle("-fx-font-size: 12; -fx-text-fill: #78350F;");
            info.getChildren().addAll(lblUser, lblStatus, lblTgl);

            String buktiPath = p.getBuktiPembayaranPath();
            if (buktiPath != null) {
                Label lblBukti = new Label("Bukti pembayaran telah di-upload");
                lblBukti.setStyle("-fx-font-size: 12; -fx-text-fill: #5B21B6; -fx-font-weight: bold;");
                info.getChildren().add(lblBukti);
            }

            Button btnLihatBukti = new Button("Lihat Bukti");
            btnLihatBukti.setStyle("-fx-background-color: #7C3AED; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
            btnLihatBukti.setDisable(buktiPath == null);
            btnLihatBukti.setOnAction(e -> { if (buktiPath != null) tampilkanPopUpBuktiPembayaran(buktiPath, p); });

            Button btnKonfirmasi = new Button("Konfirmasi LUNAS");
            btnKonfirmasi.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
            btnKonfirmasi.setOnAction(e -> {
                penghuniService.konfirmasiPembayaranByAdmin(p);
                LocalDate jt = p.getTanggalKonfirmasiAdmin() != null ? p.getTanggalKonfirmasiAdmin().plusDays(30) : null;
                String tglJT = (jt != null) ? jt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "-";
                Alert a = new Alert(Alert.AlertType.INFORMATION,
                        "Pembayaran berhasil diverifikasi!\n\nStatus: LUNAS\nJatuh tempo berikutnya: " + tglJT);
                a.setHeaderText("Konfirmasi Berhasil");
                a.showAndWait();
                tampilkanValidasiPembayaran();
            });

            Button btnTolak = new Button("Tolak Pembayaran");
            btnTolak.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
            btnTolak.setOnAction(e -> {
                penghuniService.tolakPembayaranByAdmin(p);
                Alert a = new Alert(Alert.AlertType.WARNING, "Bukti pembayaran ditolak!\nStatus dikembalikan ke BELUM BAYAR.");
                a.setHeaderText("Pembayaran Ditolak");
                a.showAndWait();
                tampilkanValidasiPembayaran();
            });

            HBox tombolArea = new HBox(10);
            tombolArea.setAlignment(Pos.CENTER);
            tombolArea.getChildren().addAll(btnLihatBukti, btnKonfirmasi, btnTolak);
            rowData.getChildren().addAll(info, spacer, tombolArea);

        } else if ("LUNAS".equals(statusPembayaran)) {
            rowData.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: #BBF7D0; -fx-background-radius: 8; -fx-border-radius: 8;");
            lblStatus.setText("STATUS: TERVERIFIKASI LUNAS");
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setStyle("-fx-font-weight: bold;");

            LocalDate tglKonfirmasi = p.getTanggalKonfirmasiAdmin();
            if (tglKonfirmasi != null) {
                Label lblTglK = new Label("Dikonfirmasi pada: " + tglKonfirmasi.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                lblTglK.setStyle("-fx-font-size: 12; -fx-text-fill: #166534;");
                info.getChildren().addAll(lblUser, lblStatus, lblTglK);
            } else {
                info.getChildren().addAll(lblUser, lblStatus);
            }

            LocalDate jt = (tglKonfirmasi != null) ? tglKonfirmasi.plusDays(30) : null;
            if (jt != null) {
                Label lblJT = new Label("Jatuh tempo berikutnya: " + jt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                lblJT.setStyle("-fx-font-size: 12; -fx-text-fill: #1E3A8A; -fx-font-weight: bold;");
                info.getChildren().add(lblJT);
            }

            Button btnReset = new Button("Reset Jadi Belum Bayar");
            btnReset.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
            btnReset.setOnAction(e -> {
                penghuniService.resetPembayaranByAdmin(p);
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Tagihan berhasil di-reset ke Belum Bayar!");
                a.showAndWait();
                tampilkanValidasiPembayaran();
            });
            rowData.getChildren().addAll(info, spacer, btnReset);

        } else {
            rowData.setStyle("-fx-background-color: #FFF5F5; -fx-border-color: #FFCCCC; -fx-background-radius: 8; -fx-border-radius: 8;");
            lblStatus.setText("STATUS: BELUM BAYAR / MENUNGGU TRANSFER");
            lblStatus.setTextFill(Color.RED);
            lblStatus.setStyle("-fx-font-weight: bold;");
            info.getChildren().addAll(lblUser, lblStatus);

            Button btnPaksaLunas = new Button("Konfirmasi Lunas Manual (Admin)");
            btnPaksaLunas.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");
            btnPaksaLunas.setOnAction(e -> {
                penghuniService.konfirmasiPembayaranByAdmin(p);
                LocalDate jt = p.getTanggalKonfirmasiAdmin() != null ? p.getTanggalKonfirmasiAdmin().plusDays(30) : null;
                String tglJT = (jt != null) ? jt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "-";
                Alert a = new Alert(Alert.AlertType.INFORMATION,
                        "Pembayaran berhasil divalidasi langsung!\n\nJatuh tempo berikutnya: " + tglJT);
                a.setHeaderText("Konfirmasi Manual Berhasil");
                a.showAndWait();
                tampilkanValidasiPembayaran();
            });
            rowData.getChildren().addAll(info, spacer, btnPaksaLunas);
        }

        return rowData;
    }

    // =========================================================================
    // 3. MENU: KOTAK MASUK KELUHAN
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
            Label empty = new Label("Bersih! Tidak ada laporan keluhan masuk dari penghuni saat ini.");
            empty.setTextFill(Color.GRAY);
            listContainer.getChildren().add(empty);
        } else {
            for (String keluhan : Main.getListKeluhan()) {
                HBox card = new HBox(10);
                card.setPadding(new Insets(15));
                card.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-background-radius: 8;");
                Label lblIsi = new Label(keluhan);
                lblIsi.setStyle("-fx-font-size: 14;");
                card.getChildren().add(lblIsi);
                listContainer.getChildren().add(card);
            }
        }

        area.getChildren().addAll(title, new Separator(), listContainer);
    }

    // =========================================================================
    // POPUP: LIHAT BUKTI PEMBAYARAN
    // =========================================================================
    private void tampilkanPopUpBuktiPembayaran(String filePath, Penghuni p) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("KOSTLINK - Bukti Pembayaran");

        VBox root = new VBox(16);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white;");

        Label lblTitle = new Label("Bukti Pembayaran");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: #1F2937;");

        Label lblPenghuni = new Label("Penghuni: " + p.getNamaLengkap() + " — Kamar " + p.getNomorKamar());
        lblPenghuni.setStyle("-fx-font-size: 13; -fx-text-fill: #4B5563;");

        if (filePath != null) {
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                Image img = new Image(imgFile.toURI().toString(), 420, 400, true, true);
                ImageView imageView = new ImageView(img);

                VBox imgContainer = new VBox(8);
                imgContainer.setAlignment(Pos.CENTER);
                imgContainer.setPadding(new Insets(12));
                imgContainer.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-background-radius: 12; -fx-border-radius: 12;");

                Label lblFile = new Label(imgFile.getName());
                lblFile.setStyle("-fx-font-size: 11; -fx-text-fill: #6B7280;");
                imgContainer.getChildren().addAll(imageView, lblFile);
                root.getChildren().addAll(lblTitle, lblPenghuni, new Separator(), imgContainer);
            } else {
                Label lblError = new Label("File tidak ditemukan: " + filePath);
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

    // --- HELPER COMPONENTS ---
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