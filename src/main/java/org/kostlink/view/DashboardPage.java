package org.kostlink.view;

import org.kostlink.Main;
import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DashboardPage extends BasePage {

    private String username, namaAsli, noKamar;
    private boolean isAktif;
    private Label lblUser;
    private Button btnLogout, btnLengkapiData;
    private Button btnDashboard, btnKontrak, btnTagihan, btnLaporan, btnPengaturan;
    private Button btnBayarSekarang;
    private VBox contentArea;
    private int tanggalSiklusKost;

    public DashboardPage(String username, String namaAsli, String noKamar, boolean isAktif, int tanggalSiklusKost) {
        this.username = (username == null || username.isEmpty()) ? "User" : username;
        this.namaAsli = (namaAsli == null) ? "" : namaAsli;
        this.noKamar = (noKamar == null || noKamar.isEmpty()) ? "-" : noKamar;
        this.isAktif = isAktif;
        this.tanggalSiklusKost = tanggalSiklusKost;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear();
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setStyle("-fx-background-color: #1a0025;");

        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setPrefSize(2000, 2000);

        // ===== SIDEBAR PREMIUM =====
        VBox sidebar = new VBox(8);
        sidebar.setMinWidth(270);
        sidebar.setMaxWidth(270);
        sidebar.setPrefHeight(Double.MAX_VALUE);
        sidebar.setPadding(new Insets(30, 18, 30, 18));
        sidebar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1a0025, #2D033B, #3b0764);"
        );

        // Logo area
        VBox logoArea = new VBox(4);
        logoArea.setPadding(new Insets(10, 0, 20, 5));
        Label lblIcon = new Label("🏠");
        lblIcon.setFont(Font.font(28));
        Label lblLogo = new Label("KOSTLINK");
        lblLogo.setTextFill(Color.WHITE);
        lblLogo.setFont(Font.font("System", FontWeight.BOLD, 22));
        Label lblSub = new Label("Sistem Manajemen Kost");
        lblSub.setTextFill(Color.rgb(180, 160, 210));
        lblSub.setFont(Font.font(11));
        logoArea.getChildren().addAll(lblIcon, lblLogo, lblSub);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.1);");

        Label lblMenu = new Label("MENU UTAMA");
        lblMenu.setTextFill(Color.rgb(160, 140, 190));
        lblMenu.setFont(Font.font("System", FontWeight.BOLD, 10));
        lblMenu.setPadding(new Insets(15, 0, 8, 8));

        VBox menuBox = new VBox(4);
        btnDashboard = createMenuButton("🏠  Dashboard", true);
        btnKontrak = createMenuButton("📄  Kontrak Saya", false);
        btnTagihan = createMenuButton("💰  Riwayat Tagihan", false);
        btnLaporan = createMenuButton("⚠️  Laporan/Keluhan", false);
        btnPengaturan = createMenuButton("⚙️  Pengaturan", false);

        if (!isAktif) {
            btnKontrak.setDisable(true);
            btnTagihan.setDisable(true);
            btnLaporan.setDisable(true);
            btnPengaturan.setDisable(true);
        }

        menuBox.getChildren().addAll(btnDashboard, btnKontrak, btnTagihan, btnLaporan, btnPengaturan);
        sidebar.getChildren().addAll(logoArea, sep, lblMenu, menuBox);

        // ===== CONTENT AREA =====
        VBox rightSide = new VBox();
        HBox.setHgrow(rightSide, Priority.ALWAYS);
        rightSide.setStyle("-fx-background-color: #F3F4F6;");

        // ScrollPane wrapper for content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: #F3F4F6; -fx-background-color: #F3F4F6; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Top Bar
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(16, 35, 16, 35));
        topBar.setStyle(
            "-fx-background-color: white;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);"
        );

        String displayName = (namaAsli.isEmpty()) ? username : namaAsli;

        // User badge
        HBox userBadge = new HBox(8);
        userBadge.setAlignment(Pos.CENTER);
        userBadge.setPadding(new Insets(6, 14, 6, 10));
        userBadge.setStyle("-fx-background-color: #F5F3FF; -fx-background-radius: 20; -fx-cursor: hand;");
        Label avatarSmall = new Label(displayName.substring(0, 1).toUpperCase());
        avatarSmall.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #7C3AED, #5B21B6);" +
            "-fx-background-radius: 50; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-min-width: 32; -fx-min-height: 32; -fx-alignment: center; -fx-font-size: 13;"
        );
        lblUser = new Label(displayName);
        lblUser.setFont(Font.font("System", FontWeight.BOLD, 13));
        lblUser.setStyle("-fx-text-fill: #2D033B; -fx-cursor: hand;");
        userBadge.getChildren().addAll(avatarSmall, lblUser);

        btnLogout = new Button("Logout");
        String logoutBase = "-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 8 18; -fx-background-radius: 10; -fx-font-size: 12;";
        String logoutHover = "-fx-background-color: #DC2626; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 8 18; -fx-background-radius: 10; -fx-font-size: 12;";
        btnLogout.setStyle(logoutBase);
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle(logoutHover));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle(logoutBase));
        topBar.getChildren().addAll(userBadge, btnLogout);

        contentArea = new VBox(24);
        contentArea.setPadding(new Insets(30, 35, 35, 35));
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        if (!isAktif) {
            // ===== WELCOME SCREEN =====
            contentArea.setAlignment(Pos.CENTER);
            VBox welcomeCard = new VBox(20);
            welcomeCard.setAlignment(Pos.CENTER);
            welcomeCard.setPadding(new Insets(60, 50, 60, 50));
            welcomeCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 30, 0, 0, 10);"
            );
            welcomeCard.setMaxWidth(650);

            Label welcomeIcon = new Label("👋");
            welcomeIcon.setFont(Font.font(52));

            Label lblWelcome = new Label("Selamat Datang!");
            lblWelcome.setFont(Font.font("System", FontWeight.BOLD, 32));
            lblWelcome.setStyle("-fx-text-fill: #1F2937;");

            Label lblDesc = new Label("Lengkapi data profil dan informasi kamar Anda\nuntuk mengakses dashboard penuh.");
            lblDesc.setStyle("-fx-text-alignment: center; -fx-text-fill: #6B7280;");
            lblDesc.setFont(Font.font(15));

            btnLengkapiData = new Button("LENGKAPI DATA SEKARANG  →");
            String ldBase = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 15 40;" +
                "-fx-background-radius: 14; -fx-cursor: hand; -fx-font-size: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.35), 15, 0, 0, 5);";
            String ldHover = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 15 40;" +
                "-fx-background-radius: 14; -fx-cursor: hand; -fx-font-size: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.5), 20, 0, 0, 7);";
            btnLengkapiData.setStyle(ldBase);
            btnLengkapiData.setOnMouseEntered(e -> btnLengkapiData.setStyle(ldHover));
            btnLengkapiData.setOnMouseExited(e -> btnLengkapiData.setStyle(ldBase));

            welcomeCard.getChildren().addAll(welcomeIcon, lblWelcome, lblDesc, btnLengkapiData);
            contentArea.getChildren().add(welcomeCard);
        } else {
            // ===== ACTIVE DASHBOARD =====
            contentArea.setAlignment(Pos.TOP_LEFT);

            // Greeting
            Label lblGreeting = new Label("Halo, " + displayName + "! 👋");
            lblGreeting.setFont(Font.font("System", FontWeight.BOLD, 26));
            lblGreeting.setStyle("-fx-text-fill: #1F2937;");
            Label lblSubGreeting = new Label("Berikut ringkasan informasi kost Anda hari ini.");
            lblSubGreeting.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14;");

            // ===== STATUS PEMBAYARAN =====
            String statusPembayaran = Main.getStatusPembayaran();
            LocalDate hariIni = LocalDate.now();

            // Cek siklus baru
            LocalDate jatuhTempoBerikutnya = Main.getJatuhTempoBerikutnya();
            if ("LUNAS".equals(statusPembayaran) && jatuhTempoBerikutnya != null) {
                if (hariIni.isAfter(jatuhTempoBerikutnya) || hariIni.isEqual(jatuhTempoBerikutnya)) {
                    Main.resetPembayaran();
                    statusPembayaran = "BELUM_BAYAR";
                }
            }

            // Hitung jatuh tempo
            LocalDate jatuhTempoTampil;
            if ("LUNAS".equals(statusPembayaran) && jatuhTempoBerikutnya != null) {
                jatuhTempoTampil = jatuhTempoBerikutnya;
            } else {
                int maxHari = hariIni.lengthOfMonth();
                int tanggalSiklusValid = Math.min(this.tanggalSiklusKost, maxHari);
                jatuhTempoTampil = LocalDate.of(hariIni.getYear(), hariIni.getMonth(), tanggalSiklusValid);
                if (jatuhTempoTampil.isBefore(hariIni)) {
                    jatuhTempoTampil = jatuhTempoTampil.plusMonths(1);
                }
            }

            String txtStatusSewa;
            String statusColor;
            switch (statusPembayaran) {
                case "MENUNGGU_VERIFIKASI":
                    txtStatusSewa = "Menunggu Verifikasi";
                    statusColor = "#D97706";
                    break;
                case "LUNAS":
                    txtStatusSewa = "Aktif (Lunas) ✅";
                    statusColor = "#059669";
                    break;
                default:
                    txtStatusSewa = "Belum Bayar";
                    statusColor = "#DC2626";
                    break;
            }

            String formattedDate = jatuhTempoTampil.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

            // ===== STAT CARDS =====
            HBox statsRow = new HBox(18);
            statsRow.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(statsRow, Priority.ALWAYS);
            statsRow.getChildren().addAll(
                createStatCard("Kamar Anda", "Kamar " + noKamar, "🏠", "#7C3AED", "#F5F3FF"),
                createStatCard("Status Sewa", txtStatusSewa, "💳", statusColor, "LUNAS".equals(statusPembayaran) ? "#ECFDF5" : "MENUNGGU_VERIFIKASI".equals(statusPembayaran) ? "#FFFBEB" : "#FEF2F2"),
                createStatCard("Jatuh Tempo", formattedDate, "📅", "#2563EB", "#EFF6FF")
            );

            // ===== NOTIFIKASI JATUH TEMPO =====
            VBox notifikasiArea = null;
            if ("BELUM_BAYAR".equals(statusPembayaran)) {
                long selisihHari = ChronoUnit.DAYS.between(hariIni, jatuhTempoTampil);
                String pesanNotifikasi = null;
                String notifBg, notifBorder, notifText, notifIcon;

                if (selisihHari < 0) {
                    pesanNotifikasi = "Jatuh tempo sudah lewat " + Math.abs(selisihHari) + " hari yang lalu! Segera lakukan pembayaran.";
                    notifBg = "#FEF2F2"; notifBorder = "#FECACA"; notifText = "#991B1B"; notifIcon = "🚨";
                } else if (selisihHari == 0) {
                    pesanNotifikasi = "HARI INI adalah jatuh tempo pembayaran kost Anda!";
                    notifBg = "#FEF2F2"; notifBorder = "#FCA5A5"; notifText = "#B91C1C"; notifIcon = "🔴";
                } else if (selisihHari <= 3) {
                    pesanNotifikasi = selisihHari + " hari lagi akan jatuh tempo pembayaran kost Anda!";
                    notifBg = "#FFFBEB"; notifBorder = "#FDE68A"; notifText = "#92400E"; notifIcon = "⏰";
                } else if (selisihHari <= 7) {
                    pesanNotifikasi = "Jatuh tempo pembayaran kost " + selisihHari + " hari lagi (" + formattedDate + ")";
                    notifBg = "#EFF6FF"; notifBorder = "#BFDBFE"; notifText = "#1E40AF"; notifIcon = "📢";
                } else {
                    notifBg = ""; notifBorder = ""; notifText = ""; notifIcon = "";
                }

                if (pesanNotifikasi != null) {
                    notifikasiArea = new VBox(8);
                    notifikasiArea.setMaxWidth(Double.MAX_VALUE);
                    notifikasiArea.setPadding(new Insets(14, 18, 14, 18));
                    notifikasiArea.setStyle(
                        "-fx-background-color: " + notifBg + ";" +
                        "-fx-border-color: " + notifBorder + ";" +
                        "-fx-background-radius: 12; -fx-border-radius: 12;"
                    );

                    HBox notifContent = new HBox(10);
                    notifContent.setAlignment(Pos.CENTER_LEFT);
                    Label iconLabel = new Label(notifIcon);
                    iconLabel.setFont(Font.font(18));
                    Label lblNotif = new Label(pesanNotifikasi);
                    lblNotif.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: " + notifText + ";");
                    lblNotif.setWrapText(true);
                    notifContent.getChildren().addAll(iconLabel, lblNotif);
                    notifikasiArea.getChildren().add(notifContent);
                }
            }

            // ===== TAGIHAN AREA =====
            VBox tableArea = new VBox(16);
            tableArea.setMaxWidth(Double.MAX_VALUE);
            tableArea.setPadding(new Insets(25));
            tableArea.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 4);"
            );

            Label lblTable = new Label("📋  Tagihan & Riwayat Pembayaran");
            lblTable.setFont(Font.font("System", FontWeight.BOLD, 17));
            lblTable.setStyle("-fx-text-fill: #1F2937;");

            Separator tableSep = new Separator();
            tableSep.setStyle("-fx-background-color: #F3F4F6;");
            tableArea.getChildren().addAll(lblTable, tableSep);

            if ("BELUM_BAYAR".equals(statusPembayaran)) {
                HBox tagihanBox = new HBox(16);
                tagihanBox.setAlignment(Pos.CENTER_LEFT);
                tagihanBox.setPadding(new Insets(16));
                tagihanBox.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FECACA; -fx-background-radius: 12; -fx-border-radius: 12;");

                Label statusIcon = new Label("🔴");
                statusIcon.setFont(Font.font(24));

                VBox infoTagihan = new VBox(4);
                Label lblBulan = new Label("Tagihan Bulan Ini — Kamar " + noKamar);
                lblBulan.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #1F2937;");
                Label lblStatus = new Label("BELUM BAYAR");
                lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #DC2626; -fx-font-size: 12;");
                infoTagihan.getChildren().addAll(lblBulan, lblStatus);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                btnBayarSekarang = new Button("Bayar Sekarang 💰");
                String payBase = "-fx-background-color: linear-gradient(to right, #DC2626, #EF4444); -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 22; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(220,38,38,0.3), 10, 0, 0, 3);";
                String payHover = "-fx-background-color: linear-gradient(to right, #B91C1C, #DC2626); -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 22; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(220,38,38,0.5), 14, 0, 0, 5);";
                btnBayarSekarang.setStyle(payBase);
                btnBayarSekarang.setOnMouseEntered(e -> btnBayarSekarang.setStyle(payHover));
                btnBayarSekarang.setOnMouseExited(e -> btnBayarSekarang.setStyle(payBase));

                tagihanBox.getChildren().addAll(statusIcon, infoTagihan, spacer, btnBayarSekarang);
                tableArea.getChildren().add(tagihanBox);

            } else if ("MENUNGGU_VERIFIKASI".equals(statusPembayaran)) {
                HBox tagihanBox = new HBox(16);
                tagihanBox.setAlignment(Pos.CENTER_LEFT);
                tagihanBox.setPadding(new Insets(16));
                tagihanBox.setStyle("-fx-background-color: #FFFBEB; -fx-border-color: #FDE68A; -fx-background-radius: 12; -fx-border-radius: 12;");

                Label statusIcon = new Label("⏳");
                statusIcon.setFont(Font.font(24));

                VBox infoTagihan = new VBox(4);
                Label lblBulan = new Label("Tagihan Bulan Ini — Kamar " + noKamar);
                lblBulan.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #1F2937;");
                Label lblStatus = new Label("MENUNGGU VERIFIKASI ADMIN");
                lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #D97706; -fx-font-size: 12;");

                LocalDate tglKirim = Main.getTanggalKirimBukti();
                String tglKirimStr = (tglKirim != null)
                        ? tglKirim.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                        : hariIni.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                Label lblDetail = new Label("Bukti dikirim " + tglKirimStr + " • Menunggu konfirmasi Ibu Kost");
                lblDetail.setStyle("-fx-font-size: 11; -fx-text-fill: #92400E;");
                lblDetail.setWrapText(true);

                infoTagihan.getChildren().addAll(lblBulan, lblStatus, lblDetail);
                tagihanBox.getChildren().addAll(statusIcon, infoTagihan);
                tableArea.getChildren().add(tagihanBox);

            } else {
                HBox tagihanBox = new HBox(16);
                tagihanBox.setAlignment(Pos.CENTER_LEFT);
                tagihanBox.setPadding(new Insets(16));
                tagihanBox.setStyle("-fx-background-color: #ECFDF5; -fx-border-color: #A7F3D0; -fx-background-radius: 12; -fx-border-radius: 12;");

                Label statusIcon = new Label("✅");
                statusIcon.setFont(Font.font(24));

                VBox infoTagihan = new VBox(4);
                Label lblBulan = new Label("Tagihan Bulan Ini — Kamar " + noKamar);
                lblBulan.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #065F46;");
                Label lblStatus = new Label("LUNAS — TERVERIFIKASI");
                lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #059669; -fx-font-size: 12;");

                LocalDate tglKonfirmasi = Main.getTanggalKonfirmasiAdmin();
                String tglStr = (tglKonfirmasi != null)
                        ? tglKonfirmasi.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                        : hariIni.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                Label lblDetailSesi = new Label("Dikonfirmasi " + tglStr + " • Berikutnya: " + formattedDate);
                lblDetailSesi.setStyle("-fx-font-size: 11; -fx-text-fill: #065F46;");

                infoTagihan.getChildren().addAll(lblBulan, lblStatus, lblDetailSesi);
                tagihanBox.getChildren().addAll(statusIcon, infoTagihan);
                tableArea.getChildren().add(tagihanBox);
            }

            // ===== HERO BANNER KOST =====
            StackPane heroBanner = createHeroBanner();

            // ===== FASILITAS KOST =====
            VBox fasilitasSection = createFasilitasKost();

            // Susun konten
            contentArea.getChildren().addAll(lblGreeting, lblSubGreeting);
            if (notifikasiArea != null) contentArea.getChildren().add(notifikasiArea);
            contentArea.getChildren().addAll(statsRow, heroBanner, tableArea, fasilitasSection);
        }

        scrollPane.setContent(contentArea);
        rightSide.getChildren().addAll(topBar, scrollPane);
        mainContainer.getChildren().addAll(sidebar, rightSide);
        this.layout.getChildren().setAll(mainContainer);
    }

    private Button createMenuButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(11, 14, 11, 14));

        String baseStyle = "-fx-background-color: transparent; -fx-text-fill: rgba(200,180,230,0.8); -fx-font-size: 13; -fx-cursor: hand; -fx-background-radius: 10;";
        String hoverStyle = "-fx-background-color: rgba(124,58,237,0.2); -fx-text-fill: white; -fx-font-size: 13; -fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 10;";
        String activeStyle = "-fx-background-color: rgba(124,58,237,0.25); -fx-text-fill: white; -fx-font-size: 13; -fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: rgba(124,58,237,0.5); -fx-border-radius: 10; -fx-border-width: 0 0 0 3;";

        btn.setStyle(isActive ? activeStyle : baseStyle);
        if (!isActive) {
            btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        }
        return btn;
    }

    private VBox createStatCard(String title, String value, String icon, String accentColor, String bgColor) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(20));
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + bgColor + ";" +
            "-fx-border-radius: 14;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 8, 0, 0, 2);"
        );

        Label lblIcon = new Label(icon);
        lblIcon.setFont(Font.font(22));
        Label lblT = new Label(title);
        lblT.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");
        Label lblV = new Label(value);
        lblV.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblV.setStyle("-fx-text-fill: " + accentColor + ";");
        card.getChildren().addAll(lblIcon, lblT, lblV);

        // Hover effect
        String cardBaseStyle = card.getStyle();
        String cardHoverStyle = "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + accentColor + ";" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1.5;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 14, 0, 0, 4);";
        card.setOnMouseEntered(e -> card.setStyle(cardHoverStyle));
        card.setOnMouseExited(e -> card.setStyle(cardBaseStyle));

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
    // =========================================================================
    // VISUAL SECTIONS: Hero Banner, Galeri Kamar, Fasilitas
    // =========================================================================

    private StackPane createHeroBanner() {
        StackPane banner = new StackPane();
        banner.setMaxWidth(Double.MAX_VALUE);
        banner.setPrefHeight(200);
        banner.setMinHeight(200);
        banner.setMaxHeight(200);
        banner.setClip(createRoundedClip(banner, 18));
        banner.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2, #f093fb);" +
            "-fx-background-radius: 18;"
        );

        // Decorative illustration canvas (right side only, positioned absolute)
        Canvas canvas = new Canvas(350, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Decorative clouds
        gc.setFill(Color.rgb(255, 255, 255, 0.12));
        gc.fillOval(20, 20, 80, 28);
        gc.fillOval(45, 12, 55, 24);
        gc.fillOval(220, 15, 70, 22);

        // Building silhouette (main kost building)
        gc.setFill(Color.rgb(255, 255, 255, 0.18));
        gc.fillRect(80, 55, 110, 145);
        gc.fillRect(200, 80, 85, 120);
        gc.fillRect(55, 75, 50, 125);

        // Roof triangles
        gc.setFill(Color.rgb(255, 255, 255, 0.22));
        double[] roofX1 = {70, 135, 200};
        double[] roofY1 = {55, 30, 55};
        gc.fillPolygon(roofX1, roofY1, 3);
        double[] roofX2 = {195, 242, 290};
        double[] roofY2 = {80, 58, 80};
        gc.fillPolygon(roofX2, roofY2, 3);

        // Windows on buildings
        gc.setFill(Color.rgb(255, 255, 200, 0.45));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                gc.fillRoundRect(95 + col * 40, 70 + row * 38, 22, 26, 4, 4);
            }
        }
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                gc.fillRoundRect(210 + col * 32, 95 + row * 38, 18, 22, 4, 4);
            }
        }

        // Door
        gc.setFill(Color.rgb(255, 255, 255, 0.3));
        gc.fillRoundRect(123, 162, 24, 38, 4, 4);

        // Trees
        gc.setFill(Color.rgb(255, 255, 255, 0.1));
        gc.fillOval(295, 110, 40, 60);
        gc.fillOval(305, 95, 30, 45);
        gc.fillRect(315, 158, 6, 42);

        gc.fillOval(15, 130, 35, 50);
        gc.fillOval(22, 118, 28, 38);
        gc.fillRect(32, 170, 5, 30);

        // Small stars
        gc.setFill(Color.rgb(255, 255, 255, 0.25));
        gc.fillOval(160, 18, 4, 4);
        gc.fillOval(260, 35, 3, 3);
        gc.fillOval(110, 40, 5, 5);

        // Text overlay — left side, padded well within bounds
        VBox textOverlay = new VBox(6);
        textOverlay.setAlignment(Pos.CENTER_LEFT);
        textOverlay.setPadding(new Insets(25, 20, 25, 35));
        textOverlay.setMaxHeight(200);

        Label lblPromo = new Label("✨ SELAMAT DATANG DI KOSTLINK");
        lblPromo.setFont(Font.font("System", FontWeight.BOLD, 10));
        lblPromo.setTextFill(Color.rgb(255, 255, 255, 0.9));
        lblPromo.setPadding(new Insets(3, 10, 3, 10));
        lblPromo.setStyle(
            "-fx-background-color: rgba(255,255,255,0.18);" +
            "-fx-background-radius: 16;"
        );

        Label lblBannerTitle = new Label("Kost Nyaman, Aman &\nStrategis di Pusat Kota");
        lblBannerTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        lblBannerTitle.setTextFill(Color.WHITE);
        lblBannerTitle.setWrapText(true);
        lblBannerTitle.setMaxWidth(420);
        lblBannerTitle.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 2);");

        Label lblBannerDesc = new Label("WiFi cepat \u2022 AC \u2022 Kamar mandi dalam \u2022 Parkir luas \u2022 Keamanan 24 jam");
        lblBannerDesc.setFont(Font.font(12));
        lblBannerDesc.setTextFill(Color.rgb(255, 255, 255, 0.85));
        lblBannerDesc.setWrapText(true);
        lblBannerDesc.setMaxWidth(400);

        HBox tagRow = new HBox(6);
        tagRow.getChildren().addAll(
            createBannerTag("\ud83c\udfe0 Fully Furnished"),
            createBannerTag("\ud83d\udcf6 WiFi Gratis"),
            createBannerTag("\ud83c\udd7f\ufe0f Free Parking")
        );

        textOverlay.getChildren().addAll(lblPromo, lblBannerTitle, lblBannerDesc, tagRow);

        // Layout: text left, illustration right
        HBox bannerContent = new HBox();
        bannerContent.setMaxWidth(Double.MAX_VALUE);
        bannerContent.setMaxHeight(200);
        HBox.setHgrow(textOverlay, Priority.ALWAYS);
        bannerContent.getChildren().addAll(textOverlay, canvas);
        bannerContent.setAlignment(Pos.CENTER_LEFT);

        banner.getChildren().add(bannerContent);

        // Shadow effect on entire banner
        banner.setEffect(new DropShadow(15, 0, 4, Color.rgb(0, 0, 0, 0.12)));

        return banner;
    }

    private Rectangle createRoundedClip(StackPane pane, double radius) {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(radius * 2);
        clip.setArcHeight(radius * 2);
        clip.widthProperty().bind(pane.widthProperty());
        clip.heightProperty().bind(pane.heightProperty());
        return clip;
    }

    private Label createBannerTag(String text) {
        Label tag = new Label(text);
        tag.setFont(Font.font("System", FontWeight.BOLD, 10));
        tag.setTextFill(Color.WHITE);
        tag.setPadding(new Insets(4, 10, 4, 10));
        tag.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2);" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255,255,255,0.3);" +
            "-fx-border-radius: 14;"
        );
        return tag;
    }


    private VBox createFasilitasKost() {
        VBox section = new VBox(16);
        section.setMaxWidth(Double.MAX_VALUE);
        section.setPadding(new Insets(0, 0, 20, 0));

        // Section header
        HBox headerRow = new HBox(10);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        Label headerIcon = new Label("🏢");
        headerIcon.setFont(Font.font(22));
        VBox headerTexts = new VBox(2);
        Label lblSection = new Label("Fasilitas Unggulan");
        lblSection.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblSection.setStyle("-fx-text-fill: #1F2937;");
        Label lblSubSection = new Label("Nikmati berbagai fasilitas premium yang kami sediakan");
        lblSubSection.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");
        headerTexts.getChildren().addAll(lblSection, lblSubSection);
        headerRow.getChildren().addAll(headerIcon, headerTexts);

        // Facilities grid (2 rows x 3 cols)
        HBox row1 = new HBox(14);
        row1.setMaxWidth(Double.MAX_VALUE);
        row1.getChildren().addAll(
            createFasilitasCard("📶", "WiFi Super Cepat", "Internet fiber optic hingga 100 Mbps", "#3B82F6", "#EFF6FF"),
            createFasilitasCard("❄️", "AC Setiap Kamar", "Pendingin ruangan untuk kenyamanan", "#06B6D4", "#ECFEFF"),
            createFasilitasCard("🔒", "Keamanan 24 Jam", "CCTV & satpam jaga sepanjang hari", "#8B5CF6", "#F5F3FF")
        );

        HBox row2 = new HBox(14);
        row2.setMaxWidth(Double.MAX_VALUE);
        row2.getChildren().addAll(
            createFasilitasCard("🅿️", "Parkir Luas", "Area parkir motor & mobil memadai", "#F59E0B", "#FFFBEB"),
            createFasilitasCard("🧹", "Cleaning Service", "Kebersihan area umum setiap hari", "#10B981", "#ECFDF5"),
            createFasilitasCard("🍽️", "Dapur Bersama", "Fasilitas memasak lengkap & bersih", "#EF4444", "#FEF2F2")
        );

        section.getChildren().addAll(headerRow, row1, row2);
        return section;
    }

    private VBox createFasilitasCard(String icon, String title, String desc,
                                      String accentColor, String bgColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        String cardBase = "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 14;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 8, 0, 0, 2);";
        card.setStyle(cardBase);

        // Icon circle
        Label lblIcon = new Label(icon);
        lblIcon.setFont(Font.font(28));
        lblIcon.setAlignment(Pos.CENTER);
        lblIcon.setPrefSize(52, 52);
        lblIcon.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 50;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
        );

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblTitle.setStyle("-fx-text-fill: #1F2937;");

        Label lblDesc = new Label(desc);
        lblDesc.setFont(Font.font(11));
        lblDesc.setStyle("-fx-text-fill: #6B7280;");
        lblDesc.setWrapText(true);

        card.getChildren().addAll(lblIcon, lblTitle, lblDesc);

        // Hover
        String cardHover = "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + accentColor + ";" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1.5;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 14, 0, 0, 4);";
        card.setOnMouseEntered(e -> {
            card.setStyle(cardHover);
            card.setTranslateY(-3);
        });
        card.setOnMouseExited(e -> {
            card.setStyle(cardBase);
            card.setTranslateY(0);
        });

        return card;
    }
}