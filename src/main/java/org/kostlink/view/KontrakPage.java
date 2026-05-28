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
import java.time.format.TextStyle;
import java.util.Locale;

public class KontrakPage extends BasePage {
    private String noKamar;

    public KontrakPage(String noKamar) {
        this.noKamar = noKamar;
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
        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        Label headerIcon = new Label("📄");
        headerIcon.setFont(Font.font(28));
        VBox headerTexts = new VBox(2);
        Label lblTitle = new Label("Kontrak Sewa Kamar");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        lblTitle.setStyle("-fx-text-fill: #1F2937;");
        Label lblSubTitle = new Label("Detail informasi kontrak dan masa sewa Anda");
        lblSubTitle.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 13;");
        headerTexts.getChildren().addAll(lblTitle, lblSubTitle);
        headerRow.getChildren().addAll(headerIcon, headerTexts);

        // Main Card
        VBox boxKontrak = new VBox(18);
        boxKontrak.setPadding(new Insets(28));
        boxKontrak.setMaxWidth(720);
        boxKontrak.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 15, 0, 0, 4);"
        );

        // Room badge
        HBox roomBadge = new HBox(10);
        roomBadge.setAlignment(Pos.CENTER_LEFT);
        roomBadge.setPadding(new Insets(12, 18, 12, 18));
        roomBadge.setStyle(
            "-fx-background-color: #F5F3FF;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #E9D5FF;" +
            "-fx-border-radius: 12;"
        );
        Label roomIcon = new Label("🏠");
        roomIcon.setFont(Font.font(20));
        Label lblKamar = new Label("Kamar " + noKamar);
        lblKamar.setFont(Font.font("System", FontWeight.BOLD, 17));
        lblKamar.setStyle("-fx-text-fill: #5B21B6;");
        roomBadge.getChildren().addAll(roomIcon, lblKamar);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #F3F4F6;");

        // =====================================================================
        // 🔥 LOGIKA TANGGAL DINAMIS (MEMBONGKAR HARDCODED)
        // =====================================================================
        int tanggalMasuk = Main.getTanggalSiklusKost();
        LocalDate hariIni = LocalDate.now();
        String namaBulan = hariIni.getMonth().getDisplayName(TextStyle.FULL, new Locale("id", "ID"));
        int tahunIni = hariIni.getYear();
        String tanggalDinamis = tanggalMasuk + " " + namaBulan + " " + tahunIni;
        // =====================================================================

        // Info grid — styled rows
        VBox infoRows = new VBox(0);
        infoRows.getChildren().addAll(
            createInfoRow("📋", "Jenis Kontrak", "Bulanan", false),
            createInfoRow("💰", "Harga Sewa", "Rp 1.200.000 / bulan", false),
            createInfoRow("📅", "Tanggal Mulai", tanggalDinamis, false),
            createInfoRow("✅", "Status Kontrak", "Aktif Berjalan", true)
        );

        boxKontrak.getChildren().addAll(roomBadge, sep, infoRows);

        // Footer note
        VBox noteBox = new VBox(8);
        noteBox.setPadding(new Insets(14, 18, 14, 18));
        noteBox.setMaxWidth(720);
        noteBox.setStyle(
            "-fx-background-color: #EFF6FF;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #BFDBFE;" +
            "-fx-border-radius: 12;"
        );
        HBox noteContent = new HBox(10);
        noteContent.setAlignment(Pos.CENTER_LEFT);
        Label noteIcon = new Label("💡");
        noteIcon.setFont(Font.font(16));
        Label noteText = new Label("Kontrak akan diperpanjang otomatis setiap bulan selama status penghuni masih aktif.");
        noteText.setStyle("-fx-text-fill: #1E40AF; -fx-font-size: 12;");
        noteText.setWrapText(true);
        noteContent.getChildren().addAll(noteIcon, noteText);
        noteBox.getChildren().add(noteContent);

        this.layout.getChildren().addAll(headerRow, boxKontrak, noteBox);
    }

    private HBox createInfoRow(String icon, String label, String value, boolean isStatus) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 16, 14, 16));
        row.setStyle("-fx-border-color: transparent transparent #F3F4F6 transparent;");

        Label lblIcon = new Label(icon);
        lblIcon.setFont(Font.font(16));
        lblIcon.setMinWidth(28);

        Label lblLabel = new Label(label);
        lblLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 13;");
        lblLabel.setMinWidth(140);

        Label lblValue = new Label(value);
        lblValue.setFont(Font.font("System", FontWeight.BOLD, 14));
        if (isStatus) {
            lblValue.setStyle("-fx-text-fill: #059669; -fx-font-weight: bold;");
        } else {
            lblValue.setStyle("-fx-text-fill: #1F2937; -fx-font-weight: bold;");
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        row.getChildren().addAll(lblIcon, lblLabel, spacer, lblValue);
        return row;
    }
}