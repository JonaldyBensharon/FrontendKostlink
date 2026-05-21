package org.kostlink.view;

import org.kostlink.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminDashboardController {
    private AdminDashboardPage view;

    public AdminDashboardController(AdminDashboardPage view) {
        this.view = view;
        initEvents();
        tampilkanDataPenghuni(); ;
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

        int totalPenghuni = Main.getStatusAktif() ? 1 : 0;
        int sudahBayar = (Main.getStatusAktif() && Main.getIsSudahBayar()) ? 1 : 0;
        int belumBayar = (Main.getStatusAktif() && !Main.getIsSudahBayar()) ? 1 : 0;
        int jumlahKeluhan = Main.getListKeluhan().size();

        summaryRow.getChildren().addAll(
                createCardMini("Total Penghuni", totalPenghuni + " Orang", "#3B82F6"),
                createCardMini("Sudah Lunas", sudahBayar + " Kamar", "#10B981"),
                createCardMini("Belum Bayar", belumBayar + " Kamar", "#EF4444"),
                createCardMini("Laporan Masuk", jumlahKeluhan + " Keluhan", "#F59E0B")
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

            // Hitung perkiraan jatuh tempo berdasarkan tanggal pendaftaran
            LocalDate hariIni = LocalDate.now();
            LocalDate jatuhTempo = LocalDate.of(hariIni.getYear(), hariIni.getMonth(), Main.getTanggalSiklusKost());
            if (Main.getIsSudahBayar()) {
                jatuhTempo = jatuhTempo.plusMonths(1);
            }
            String tglFormat = jatuhTempo.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

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
            Label lblFinansial = new Label(Main.getIsSudahBayar() ? ": LUNAS" : ": MENUNGGU PEMBAYARAN");
            lblFinansial.setTextFill(Main.getIsSudahBayar() ? Color.GREEN : Color.RED);
            lblFinansial.setStyle("-fx-font-weight: bold;");
            grid.add(lblFinansial, 1, 4);

            detailCard.getChildren().add(grid);
        } else {
            Label empty = new Label("Belum ada data penghuni kost aktif di sistem.");
            empty.setTextFill(Color.GRAY);
            detailCard.getChildren().add(empty);
        }

        area.getChildren().addAll(title, new Separator(), summaryRow, detailCard);
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

        if (Main.getStatusAktif()) {
            HBox rowData = new HBox(20);
            rowData.setAlignment(Pos.CENTER_LEFT);
            rowData.setPadding(new Insets(15));
            rowData.setStyle("-fx-background-radius: 8; -fx-border-radius: 8;");

            VBox info = new VBox(5);
            Label lblUser = new Label("Kamar " + Main.getNomorKamarPenghuni() + " - " + Main.getNamaLengkapPenghuni());
            lblUser.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");

            Label lblStatus = new Label();
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            if (Main.getIsSudahBayar()) {
                rowData.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: #BBF7D0;");
                lblStatus.setText("STATUS: TERVERIFIKASI LUNAS 🎉");
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setStyle("-fx-font-weight: bold;");
                info.getChildren().addAll(lblUser, lblStatus);

                Button btnReset = new Button("Reset Jadi Belum Bayar 🔄");
                btnReset.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                btnReset.setOnAction(e -> {
                    Main.setIsSudahBayar(false);
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Tagihan berhasil di-reset ke Belum Bayar untuk periode berikutnya!");
                    a.showAndWait();
                    tampilkanValidasiPembayaran();
                });
                rowData.getChildren().addAll(info, spacer, btnReset);
            } else {
                rowData.setStyle("-fx-background-color: #FFF5F5; -fx-border-color: #FFCCCC;");
                lblStatus.setText("STATUS: BELUM BAYAR / MENUNGGU TRANSFER 💰");
                lblStatus.setTextFill(Color.RED);
                lblStatus.setStyle("-fx-font-weight: bold;");
                info.getChildren().addAll(lblUser, lblStatus);

                Button btnPaksaLunas = new Button("Konfirmasi Lunas Manual (Admin) ✅");
                btnPaksaLunas.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                btnPaksaLunas.setOnAction(e -> {
                    Main.setIsSudahBayar(true);
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Pembayaran penghuni berhasil divalidasi langsung oleh Ibu Kost!");
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