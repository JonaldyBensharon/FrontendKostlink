package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PengaturanPage extends BasePage {
    private Button btnSimpanSesi;
    private CheckBox chkDarkMode;
    private CheckBox chkNotif;

    public PengaturanPage() {
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
        Label headerIcon = new Label("⚙️");
        headerIcon.setFont(Font.font(28));
        VBox headerTexts = new VBox(2);
        Label lblTitle = new Label("Pengaturan Aplikasi");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        lblTitle.setStyle("-fx-text-fill: #1F2937;");
        Label lblSubTitle = new Label("Sesuaikan preferensi aplikasi sesuai kebutuhan Anda");
        lblSubTitle.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 13;");
        headerTexts.getChildren().addAll(lblTitle, lblSubTitle);
        headerRow.getChildren().addAll(headerIcon, headerTexts);

        // Settings Card
        VBox boxSetting = new VBox(0);
        boxSetting.setPadding(new Insets(0));
        boxSetting.setMaxWidth(720);
        boxSetting.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 15, 0, 0, 4);"
        );

        // Section: Notifikasi
        VBox sectionNotif = new VBox(8);
        sectionNotif.setPadding(new Insets(24, 28, 20, 28));
        sectionNotif.setStyle("-fx-border-color: transparent transparent #F3F4F6 transparent;");

        Label lblNotifSection = new Label("🔔  NOTIFIKASI");
        lblNotifSection.setStyle("-fx-text-fill: #6B7280; -fx-font-weight: bold; -fx-font-size: 11;");

        HBox notifRow = createSettingRow(
            "Pengingat WhatsApp Otomatis",
            "Kirim notifikasi WhatsApp saat mendekati jatuh tempo pembayaran"
        );
        chkNotif = new CheckBox();
        chkNotif.setSelected(true);
        chkNotif.setStyle("-fx-cursor: hand;");
        Region spacerNotif = new Region();
        HBox.setHgrow(spacerNotif, Priority.ALWAYS);
        notifRow.getChildren().addAll(spacerNotif, chkNotif);

        sectionNotif.getChildren().addAll(lblNotifSection, notifRow);

        // Section: Tampilan
        VBox sectionTampilan = new VBox(8);
        sectionTampilan.setPadding(new Insets(20, 28, 20, 28));
        sectionTampilan.setStyle("-fx-border-color: transparent transparent #F3F4F6 transparent;");

        Label lblTampilanSection = new Label("🎨  TAMPILAN");
        lblTampilanSection.setStyle("-fx-text-fill: #6B7280; -fx-font-weight: bold; -fx-font-size: 11;");

        HBox darkRow = createSettingRow(
            "Mode Gelap (Dark Mode)",
            "Ubah tampilan aplikasi menjadi tema gelap untuk kenyamanan mata"
        );
        chkDarkMode = new CheckBox();
        chkDarkMode.setStyle("-fx-cursor: hand;");
        Region spacerDark = new Region();
        HBox.setHgrow(spacerDark, Priority.ALWAYS);
        darkRow.getChildren().addAll(spacerDark, chkDarkMode);

        sectionTampilan.getChildren().addAll(lblTampilanSection, darkRow);

        // Save Button Area
        VBox saveArea = new VBox();
        saveArea.setPadding(new Insets(20, 28, 24, 28));

        btnSimpanSesi = new Button("💾  Simpan Pengaturan");
        btnSimpanSesi.setMaxWidth(Double.MAX_VALUE);
        btnSimpanSesi.setPrefHeight(46);
        String btnBase = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.35), 12, 0, 0, 3);";
        String btnHover = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.5), 16, 0, 0, 5);";
        btnSimpanSesi.setStyle(btnBase);
        btnSimpanSesi.setOnMouseEntered(e -> btnSimpanSesi.setStyle(btnHover));
        btnSimpanSesi.setOnMouseExited(e -> btnSimpanSesi.setStyle(btnBase));

        saveArea.getChildren().add(btnSimpanSesi);

        boxSetting.getChildren().addAll(sectionNotif, sectionTampilan, saveArea);
        this.layout.getChildren().addAll(headerRow, boxSetting);
    }

    private HBox createSettingRow(String title, String description) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 16, 14, 16));
        row.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-background-radius: 12;"
        );

        VBox textArea = new VBox(3);
        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblTitle.setStyle("-fx-text-fill: #1F2937;");
        Label lblDesc = new Label(description);
        lblDesc.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 11;");
        lblDesc.setWrapText(true);
        textArea.getChildren().addAll(lblTitle, lblDesc);
        HBox.setHgrow(textArea, Priority.ALWAYS);

        row.getChildren().add(textArea);
        return row;
    }

    // --- GETTERS ---
    public Button getBtnSimpanSesi() {
        return btnSimpanSesi;
    }

    // 3. TAMBAHKAN FUNGSI GETTER INI AGAR CONTROLLER BISA MEMBACA STATUS CENTANGNYA
    public CheckBox getChkDarkMode() {
        return chkDarkMode;
    }

    public CheckBox getChkNotif() {
        return chkNotif;
    }
}