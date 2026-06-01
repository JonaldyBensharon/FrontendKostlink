package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

        // Header
        VBox headerTexts = new VBox(2);
        Label lblTitle = new Label("Laporan & Keluhan Fasilitas");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        lblTitle.setStyle("-fx-text-fill: #1F2937;");
        Label lblSubTitle = new Label("Laporkan masalah atau keluhan kepada pengelola kost");
        lblSubTitle.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 13;");
        headerTexts.getChildren().addAll(lblTitle, lblSubTitle);

        // Form Card
        VBox boxForm = new VBox(18);
        boxForm.setPadding(new Insets(28));
        boxForm.setMaxWidth(720);
        boxForm.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 15, 0, 0, 4);"
        );

        // Info banner
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

        // Label
        Label lblKet = new Label("DETAIL KELUHAN");
        lblKet.setStyle("-fx-text-fill: #374151; -fx-font-weight: bold; -fx-font-size: 12;");

        // Text area
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

        // Submit button
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
        this.layout.getChildren().addAll(headerTexts, boxForm);
    }

    public Button getBtnKirimLaporan() { return btnKirimLaporan; }
    public TextArea getTxtKeluhan() { return txtKeluhan; }
}