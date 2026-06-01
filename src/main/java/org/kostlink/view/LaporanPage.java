package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// IMPORT TAMBAHAN UNTUK MEMBUKA LINK WHATSAPP
import java.awt.Desktop;
import java.net.URI;

public class LaporanPage extends BasePage {
    private Button btnKirimLaporan;
    private TextArea txtKeluhan;

    public LaporanPage() {
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear();
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setPadding(new Insets(10, 0, 0, 0));
        this.layout.setSpacing(20);
        this.layout.setStyle("-fx-background-color: transparent;");

        // ===== 1. HEADER UTAMA =====
        VBox headerTexts = new VBox(2);
        Label lblTitle = new Label("Laporan & Keluhan Fasilitas");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        lblTitle.setStyle("-fx-text-fill: #1F2937;");
        Label lblSubTitle = new Label("Laporkan masalah atau keluhan kepada pengelola kost");
        lblSubTitle.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 13;");
        headerTexts.getChildren().addAll(lblTitle, lblSubTitle);

        // ===== 2. KOTAK FORM (PUTIH) =====
        VBox boxForm = new VBox(18);
        boxForm.setPadding(new Insets(28));
        boxForm.setMaxWidth(720);
        boxForm.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 15, 0, 0, 4);"
        );

        // Info banner di dalam kotak
        HBox infoBanner = new HBox(10);
        infoBanner.setAlignment(Pos.CENTER_LEFT);
        infoBanner.setPadding(new Insets(12, 18, 12, 18));
        infoBanner.setStyle(
                "-fx-background-color: #FFFBEB;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #FDE68A;" +
                        "-fx-border-radius: 12;"
        );
        Label bannerIcon = new Label("💡");
        bannerIcon.setFont(Font.font(16));
        Label bannerText = new Label("Tuliskan keluhan Anda dengan jelas dan detail agar pengelola dapat menindaklanjuti dengan cepat.");
        bannerText.setStyle("-fx-text-fill: #92400E; -fx-font-size: 12;");
        bannerText.setWrapText(true);
        infoBanner.getChildren().addAll(bannerIcon, bannerText);

        // Label Detail Keluhan
        Label lblKet = new Label("DETAIL KELUHAN");
        lblKet.setStyle("-fx-text-fill: #374151; -fx-font-weight: bold; -fx-font-size: 12;");

        // Text area tempat mengetik
        txtKeluhan = new TextArea();
        txtKeluhan.setPromptText("Tuliskan detail keluhan Anda di sini...\n\nContoh: Lampu kamar mandi mati, AC bocor, WiFi lambat, dll.");
        txtKeluhan.setPrefHeight(160);
        txtKeluhan.setStyle(
                "-fx-background-color: #F9FAFB;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E5E7EB;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 14;" +
                        "-fx-font-size: 13;"
        );

        // Tombol Kirim Laporan
        btnKirimLaporan = new Button("📤  Kirim Laporan Keluhan");
        btnKirimLaporan.setMaxWidth(Double.MAX_VALUE);
        btnKirimLaporan.setPrefHeight(46);
        String btnBase = "-fx-background-color: linear-gradient(to right, #DC2626, #EF4444);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(220,38,38,0.3), 12, 0, 0, 3);";
        String btnHover = "-fx-background-color: linear-gradient(to right, #B91C1C, #DC2626);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(220,38,38,0.5), 16, 0, 0, 5);";
        btnKirimLaporan.setStyle(btnBase);
        btnKirimLaporan.setOnMouseEntered(e -> btnKirimLaporan.setStyle(btnHover));
        btnKirimLaporan.setOnMouseExited(e -> btnKirimLaporan.setStyle(btnBase));

        boxForm.getChildren().addAll(infoBanner, lblKet, txtKeluhan, btnKirimLaporan);

        // ===== 3. BAGIAN URGENT & DESKRIPSI (DI LUAR KOTAK PUTIH) =====
        VBox urgentHeader = new VBox(2);
        // Memberikan jarak atas agar tidak terlalu dekat dengan Kotak Putih di atasnya
        VBox.setMargin(urgentHeader, new Insets(10, 0, 0, 0));

        Label lblUrgent = new Label("Urgent?");
        lblUrgent.setFont(Font.font("System", FontWeight.BOLD, 24)); // Ukuran 24 tebal, sama dengan lblTitle
        lblUrgent.setStyle("-fx-text-fill: #1F2937;");

        // TAMBAHAN: Deskripsi singkat di bawah tulisan Urgent?
        Label lblUrgentSub = new Label("Hubungi kontak darurat pengelola kost jika terjadi masalah mendesak");
        lblUrgentSub.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 13;");

        urgentHeader.getChildren().addAll(lblUrgent, lblUrgentSub);

        // ===== 4. KOTAK HIJAU WHATSAPP (BISA DIKLIK) =====
        VBox waBox = new VBox(8);
        waBox.setAlignment(Pos.CENTER);
        waBox.setPadding(new Insets(14));
        waBox.setMaxWidth(720); // Sejajar rapi dengan boxForm

        // Style dasar Kotak WA & kursor tangan biar user tahu kalau ini bisa diklik
        String waBoxBase = "-fx-background-color: #F0FDF4; -fx-border-color: #22C55E; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";
        String waBoxHover = "-fx-background-color: #DCFCE7; -fx-border-color: #16A34A; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(34,197,94,0.15), 12, 0, 0, 3);";

        waBox.setStyle(waBoxBase);
        waBox.setOnMouseEntered(e -> waBox.setStyle(waBoxHover));
        waBox.setOnMouseExited(e -> waBox.setStyle(waBoxBase));

        // Event Handler: Ketika kotak diklik, langsung buka link WhatsApp ke nomor tujuan
        waBox.setOnMouseClicked(e -> {
            try {
                // Link resmi WhatsApp direct message tanpa perlu simpan kontak
                String urlWa = "https://wa.me/6281536259037?text=Halo%20Pengelola%20KostLink,%20saya%20butuh%20bantuan%20darurat.";
                Desktop.getDesktop().browse(new URI(urlWa));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Label lblHubungi = new Label("Hubungi kontak di bawah ini:");
        lblHubungi.setStyle("-fx-text-fill: #374151; -fx-font-weight: bold; -fx-font-size: 13;");

        HBox waContainer = new HBox(8);
        waContainer.setAlignment(Pos.CENTER);

        Label lblWaIcon = new Label("💬");
        lblWaIcon.setStyle("-fx-text-fill: #22C55E; -fx-font-weight: bold; -fx-font-size: 16;");

        // Menampilkan nomor handphone target yang kamu minta
        Label lblNoWa = new Label("0815-3625-9037");
        lblNoWa.setStyle("-fx-text-fill: #111827; -fx-font-weight: bold; -fx-font-size: 15;");

        waContainer.getChildren().addAll(lblWaIcon, lblNoWa);
        waBox.getChildren().addAll(lblHubungi, waContainer);

        // ===== 5. SUSUN UTAMA KE LAYOUT =====
        this.layout.getChildren().addAll(headerTexts, boxForm, urgentHeader, waBox);
    }

    public Button getBtnKirimLaporan() { return btnKirimLaporan; }
    public TextArea getTxtKeluhan() { return txtKeluhan; }
}