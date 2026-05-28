package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomePenghuniPage extends BasePage {

    private String nama, username, noKamar;
    private Button btnBack;

    public HomePenghuniPage(String nama, String username, String noKamar) {
        this.nama = (nama == null || nama.isEmpty()) ? "Penghuni" : nama;
        this.username = username;
        this.noKamar = noKamar;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.setAlignment(Pos.CENTER);
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a0025, #2D033B, #3b0764);");

        VBox container = new VBox(24);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(45, 50, 40, 50));
        container.setMaxWidth(520);
        container.setStyle(
            "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 28;" +
            "-fx-border-color: rgba(255,255,255,0.12);" +
            "-fx-border-radius: 28;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 40, 0, 0, 15);"
        );

        // Avatar
        Label avatar = new Label(nama.substring(0, 1).toUpperCase());
        avatar.setFont(Font.font("System", FontWeight.BOLD, 46));
        avatar.setTextFill(Color.WHITE);
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(110, 110);
        avatar.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #7C3AED, #A855F7);" +
            "-fx-background-radius: 100;" +
            "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.45), 20, 0, 0, 8);"
        );

        // Name
        Label lblNama = new Label(nama);
        lblNama.setFont(Font.font("System", FontWeight.BOLD, 26));
        lblNama.setTextFill(Color.WHITE);

        Label lblRole = new Label("Penghuni Aktif");
        lblRole.setTextFill(Color.rgb(168, 85, 247));
        lblRole.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblRole.setPadding(new Insets(4, 14, 4, 14));
        lblRole.setStyle(
            "-fx-background-color: rgba(124,58,237,0.15);" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: rgba(124,58,237,0.3);" +
            "-fx-border-radius: 20;"
        );

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.1);");

        // Info Grid
        VBox infoBox = new VBox(12);
        infoBox.setMaxWidth(400);
        infoBox.getChildren().addAll(
            createInfoRow("👤", "Username", username),
            createInfoRow("🏠", "Nomor Kamar", "Kamar " + noKamar),
            createInfoRow("✅", "Status Akun", "Terverifikasi")
        );

        // Back button
        btnBack = new Button("← KEMBALI KE DASHBOARD");
        btnBack.setMaxWidth(Double.MAX_VALUE);
        btnBack.setPrefHeight(46);
        String btnBase = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.4), 15, 0, 0, 4);";
        String btnHover = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.6), 20, 0, 0, 6);";
        btnBack.setStyle(btnBase);
        btnBack.setOnMouseEntered(e -> btnBack.setStyle(btnHover));
        btnBack.setOnMouseExited(e -> btnBack.setStyle(btnBase));

        container.getChildren().addAll(avatar, lblNama, lblRole, sep, infoBox, btnBack);
        this.layout.getChildren().add(container);
    }

    private HBox createInfoRow(String icon, String label, String value) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 18, 12, 18));
        row.setStyle(
            "-fx-background-color: rgba(255,255,255,0.05);" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: rgba(255,255,255,0.08);" +
            "-fx-border-radius: 12;"
        );

        Label lblIcon = new Label(icon);
        lblIcon.setFont(Font.font(18));

        VBox textArea = new VBox(2);
        Label lblLabel = new Label(label);
        lblLabel.setTextFill(Color.rgb(160, 150, 200));
        lblLabel.setFont(Font.font(11));
        Label lblValue = new Label(value);
        lblValue.setTextFill(Color.WHITE);
        lblValue.setFont(Font.font("System", FontWeight.BOLD, 14));
        textArea.getChildren().addAll(lblLabel, lblValue);

        row.getChildren().addAll(lblIcon, textArea);
        HBox.setHgrow(row, Priority.ALWAYS);
        return row;
    }

    public Button getBtnBack() { return btnBack; }
}