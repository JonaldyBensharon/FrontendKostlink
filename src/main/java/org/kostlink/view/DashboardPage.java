package org.kostlink.view;

import org.kostlink.Main;
import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardPage extends BasePage {

    private String username, namaAsli, noKamar;
    private boolean isAktif;
    private Label lblUser;
    private Button btnLogout, btnLengkapiData;

    // Tombol Sidebar Asli
    private Button btnDashboard, btnKontrak, btnTagihan, btnLaporan, btnPengaturan;

    // Komponen Konten Dinamis
    private Button btnBayarSekarang;
    private VBox contentArea;
    private boolean isSudahBayar;

    // Atribut untuk menampung tanggal siklus dinamis dari database/pengguna
    private int tanggalSiklusKost;

    // KONSTRUKTOR: Menerima parameter int tanggalSiklusKost di bagian paling akhir
    public DashboardPage(String username, String namaAsli, String noKamar, boolean isAktif, int tanggalSiklusKost) {
        this.username = (username == null || username.isEmpty()) ? "User" : username;
        this.namaAsli = (namaAsli == null) ? "" : namaAsli;
        this.noKamar = (noKamar == null || noKamar.isEmpty()) ? "-" : noKamar;
        this.isAktif = isAktif;
        this.isSudahBayar = Main.getIsSudahBayar();
        this.tanggalSiklusKost = tanggalSiklusKost;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear(); // Memastikan layout bersih saat re-render
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setStyle("-fx-background-color: #2D033B;");

        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setPrefSize(2000, 2000);

        // --- SIDEBAR ---
        VBox sidebar = new VBox(25);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);
        sidebar.setPrefHeight(Double.MAX_VALUE);
        sidebar.setPadding(new Insets(40, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: #2D033B;");

        Label lblLogo = new Label("KOSTLINK");
        lblLogo.setTextFill(Color.WHITE);
        lblLogo.setFont(Font.font("System", FontWeight.BOLD, 24));

        VBox menuBox = new VBox(10);
        btnDashboard = createMenuButton("🏠  Dashboard");
        btnKontrak = createMenuButton("📄  Kontrak Saya");
        btnTagihan = createMenuButton("💰  Riwayat Tagihan");
        btnLaporan = createMenuButton("⚠️  Laporan/Keluhan");
        btnPengaturan = createMenuButton("⚙️  Pengaturan");

        menuBox.getChildren().addAll(btnDashboard, btnKontrak, btnTagihan, btnLaporan, btnPengaturan);
        sidebar.getChildren().addAll(lblLogo, new Separator(), menuBox);

        // --- CONTENT AREA ---
        VBox rightSide = new VBox();
        HBox.setHgrow(rightSide, Priority.ALWAYS);
        rightSide.setStyle("-fx-background-color: #F8F9FA;");

        // Top Bar
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15, 40, 15, 40));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #EEEEEE; -fx-border-width: 0 0 1 0;");

        String displayName = (namaAsli.isEmpty()) ? username : namaAsli;
        lblUser = new Label(displayName + " 👤");
        lblUser.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblUser.setStyle("-fx-cursor: hand; -fx-text-fill: #2D033B;");

        btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #FF4B4B; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 8;");
        topBar.getChildren().addAll(lblUser, btnLogout);

        contentArea = new VBox(30);
        contentArea.setPadding(new Insets(40));
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        if (!isAktif) {
            contentArea.setAlignment(Pos.CENTER);
            VBox welcomeCard = new VBox(25);
            welcomeCard.setAlignment(Pos.CENTER);
            welcomeCard.setPadding(new Insets(60));
            welcomeCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 20, 0, 0, 10);");
            welcomeCard.setMaxWidth(800);

            Label lblWelcome = new Label("Selamat Datang!");
            lblWelcome.setFont(Font.font("System", FontWeight.BOLD, 36));

            Label lblDesc = new Label("Lengkapi data profil dan informasi kamar Anda\nuntuk mengakses dashboard penuh.");
            lblDesc.setStyle("-fx-text-alignment: center;");
            lblDesc.setFont(Font.font(18));
            lblDesc.setTextFill(Color.GRAY);

            btnLengkapiData = new Button("LENGKAPI DATA SEKARANG  →");
            btnLengkapiData.setStyle("-fx-background-color: #5D3FD3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 15 45; -fx-background-radius: 12; -fx-cursor: hand; -fx-font-size: 16;");

            welcomeCard.getChildren().addAll(lblWelcome, lblDesc, btnLengkapiData);
            contentArea.getChildren().add(welcomeCard);
        } else {
            contentArea.setAlignment(Pos.TOP_LEFT);
            Label lblTitle = new Label("Halaman Penghuni");
            lblTitle.setFont(Font.font("System", FontWeight.BOLD, 28));

            HBox statsRow = new HBox(25);
            statsRow.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(statsRow, Priority.ALWAYS);

            // =========================================================================
            // DIPERBAIKI: LOGIKA DINAMIS SIKLUS BULANAN (SINKRON DATA)
            // =========================================================================
            LocalDate hariIni = LocalDate.now();
            LocalDate jatuhTempoDate = LocalDate.of(hariIni.getYear(), hariIni.getMonth(), this.tanggalSiklusKost);

            // 1. Jika hari ini sudah melewati hari jatuh tempo bulan ini
            if (hariIni.isAfter(jatuhTempoDate)) {
                if (Main.getIsSudahBayar()) {
                    Main.setIsSudahBayar(false);
                    this.isSudahBayar = false;
                }
                jatuhTempoDate = jatuhTempoDate.plusMonths(1);
            }
            // 2. Jika hari ini tepat pada tanggal pendaftaran/jatuh tempo
            else if (hariIni.isEqual(jatuhTempoDate)) {
                if (this.isSudahBayar) {
                    jatuhTempoDate = jatuhTempoDate.plusMonths(1); // Geser tampilan jatuh tempo sebulan setelahnya
                }
            }
            // 3. Jika hari ini belum memasuki batas akhir tanggal jatuh tempo
            else {
                if (this.isSudahBayar) {
                    jatuhTempoDate = jatuhTempoDate.plusMonths(1);
                }
            }

            String formattedDate = jatuhTempoDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            String txtStatusSewa = isSudahBayar ? "Aktif (Lunas)" : "Menunggu Pembayaran";

            statsRow.getChildren().addAll(
                    createStatCard("Kamar Anda", "Kamar " + noKamar, "🏠"),
                    createStatCard("Status Sewa", txtStatusSewa, "💳"),
                    createStatCard("Jatuh Tempo", formattedDate, "📅")
            );

            VBox tableArea = new VBox(15);
            tableArea.setMaxWidth(Double.MAX_VALUE);
            tableArea.setPadding(new Insets(30));
            tableArea.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #E0E0E0;");

            Label lblTable = new Label("Tagihan Terakhir & Riwayat Pembayaran");
            lblTable.setFont(Font.font("System", FontWeight.BOLD, 18));
            tableArea.getChildren().addAll(lblTable, new Separator());

            if (!isSudahBayar) {
                HBox tagihanBox = new HBox(20);
                tagihanBox.setAlignment(Pos.CENTER_LEFT);
                tagihanBox.setPadding(new Insets(15));
                tagihanBox.setStyle("-fx-background-color: #FFF5F5; -fx-border-color: #FFCCCC; -fx-background-radius: 10; -fx-border-radius: 10;");

                VBox infoTagihan = new VBox(5);
                Label lblBulan = new Label("Tagihan Bulan Ini (Kamar " + noKamar + ")");
                lblBulan.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                Label lblStatus = new Label("Status: BELUM BAYAR");
                lblStatus.setTextFill(Color.RED);
                lblStatus.setStyle("-fx-font-weight: bold;");
                infoTagihan.getChildren().addAll(lblBulan, lblStatus);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                btnBayarSekarang = new Button("Bayar Sekarang 💰");
                btnBayarSekarang.setStyle("-fx-background-color: #FF4B4B; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20; -fx-background-radius: 8;");

                tagihanBox.getChildren().addAll(infoTagihan, spacer, btnBayarSekarang);
                tableArea.getChildren().add(tagihanBox);
            } else {
                HBox tagihanBox = new HBox(20);
                tagihanBox.setAlignment(Pos.CENTER_LEFT);
                tagihanBox.setPadding(new Insets(15));
                tagihanBox.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: #BBF7D0; -fx-background-radius: 10; -fx-border-radius: 10;");

                VBox infoTagihan = new VBox(5);
                Label lblBulan = new Label("Tagihan Bulan Ini (Kamar " + noKamar + ")");
                lblBulan.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #14532D;");
                Label lblStatus = new Label("Status: LUNAS / TERVERIFIKASI");
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setStyle("-fx-font-weight: bold;");

                String tanggalSekarang = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                Label lblDetailSesi = new Label("Dibayarkan via Virtual Account Mandiri pada " + tanggalSekarang);
                lblDetailSesi.setStyle("-fx-font-size: 12; -fx-text-fill: #166534;");

                infoTagihan.getChildren().addAll(lblBulan, lblStatus, lblDetailSesi);
                tagihanBox.getChildren().add(infoTagihan);
                tableArea.getChildren().add(tagihanBox);
            }

            contentArea.getChildren().addAll(lblTitle, statsRow, tableArea);
        }

        rightSide.getChildren().addAll(topBar, contentArea);
        mainContainer.getChildren().addAll(sidebar, rightSide);
        this.layout.getChildren().setAll(mainContainer);
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 15, 12, 15));

        String baseStyle = "-fx-background-color: transparent; -fx-text-fill: #D1D1D1; -fx-font-size: 14; -fx-cursor: hand; -fx-font-family: 'System';";
        String hoverStyle = "-fx-background-color: #43065A; -fx-text-fill: white; -fx-font-size: 14; -fx-cursor: hand; -fx-font-family: 'System'; -fx-font-weight: bold;";

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    private VBox createStatCard(String title, String value, String icon) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(25));
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0;");
        Label lblIcon = new Label(icon);
        Label lblT = new Label(title);
        lblT.setTextFill(Color.GRAY);
        Label lblV = new Label(value);
        lblV.setFont(Font.font("System", FontWeight.BOLD, 18));
        card.getChildren().addAll(lblIcon, lblT, lblV);
        return card;
    }

    // --- GETTERS ---
    public Button getBtnDashboard() { return btnDashboard; }
    public Button getBtnKontrak() { return btnKontrak; }
    public Button getBtnTagihan() { return btnTagihan; }
    public Button getBtnLaporan() { return btnLaporan; }
    public Button getBtnPengaturan() { return btnPengaturan; }
    public Button getBtnLogout() { return btnLogout; }
    public Button getBtnLengkapiData() { return btnLengkapiData; }
    public Label getLblUser() { return lblUser; }
    public VBox getContentArea() { return contentArea; }
    public String getNoKamar() { return noKamar; }
    public Button getBtnBayarSekarang() { return btnBayarSekarang; }
    public int getTanggalSiklusKost() { return tanggalSiklusKost; }
}