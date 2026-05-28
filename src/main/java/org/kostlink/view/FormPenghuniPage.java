package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FormPenghuniPage extends BasePage {

    private TextField txtNamaLengkap, txtNoKamar, txtNoHP, txtNIK, txtAlamat;
    private Button btnKonfirmasi, btnBatal;

    @Override
    public void setupComponents() {
        this.layout.setSpacing(0);
        this.layout.setPadding(new Insets(0));
        this.layout.setAlignment(Pos.CENTER);
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a0025, #2D033B, #3b0764);");

        // Form Card — premium glassmorphism
        VBox formCard = new VBox(16);
        formCard.setPadding(new Insets(40, 45, 35, 45));
        formCard.setMaxWidth(520);
        formCard.setAlignment(Pos.CENTER_LEFT);
        formCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 24;" +
            "-fx-border-color: rgba(255,255,255,0.12);" +
            "-fx-border-radius: 24;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 40, 0, 0, 15);"
        );

        // Header
        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        Label headerIcon = new Label("📋");
        headerIcon.setFont(Font.font(32));
        VBox headerText = new VBox(2);
        Label lblTitle = new Label("Lengkapi Profil Anda");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        lblTitle.setTextFill(Color.WHITE);
        Label lblSub = new Label("Isi data berikut untuk mengakses dashboard");
        lblSub.setTextFill(Color.rgb(180, 170, 210, 0.8));
        lblSub.setFont(Font.font(12));
        headerText.getChildren().addAll(lblTitle, lblSub);
        headerRow.getChildren().addAll(headerIcon, headerText);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.1);");

        // Label style
        String labelStyle = "-fx-text-fill: rgba(200,190,230,0.9); -fx-font-size: 12; -fx-font-weight: bold;";

        // Input style
        String inputStyle =
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 11 16;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(200,200,220,0.45);" +
            "-fx-border-color: rgba(255,255,255,0.13);" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13;";

        // Fields
        Label lblNIK = new Label("NIK"); lblNIK.setStyle(labelStyle);
        txtNIK = new TextField(); txtNIK.setPromptText("Masukkan NIK Anda"); txtNIK.setStyle(inputStyle);

        Label lblNama = new Label("NAMA LENGKAP"); lblNama.setStyle(labelStyle);
        txtNamaLengkap = new TextField(); txtNamaLengkap.setPromptText("Masukkan nama lengkap"); txtNamaLengkap.setStyle(inputStyle);

        Label lblHP = new Label("NOMOR HP"); lblHP.setStyle(labelStyle);
        txtNoHP = new TextField(); txtNoHP.setPromptText("Contoh: 08123456789"); txtNoHP.setStyle(inputStyle);

        Label lblAlamat = new Label("ALAMAT ASAL"); lblAlamat.setStyle(labelStyle);
        txtAlamat = new TextField(); txtAlamat.setPromptText("Masukkan alamat asal Anda"); txtAlamat.setStyle(inputStyle);

        Label lblKamar = new Label("NOMOR KAMAR"); lblKamar.setStyle(labelStyle);
        txtNoKamar = new TextField(); txtNoKamar.setPromptText("Contoh: 5"); txtNoKamar.setStyle(inputStyle);

        // Buttons
        btnKonfirmasi = new Button("SIMPAN & MASUK DASHBOARD  →");
        btnKonfirmasi.setMaxWidth(Double.MAX_VALUE);
        btnKonfirmasi.setPrefHeight(46);
        String btnBase = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.4), 15, 0, 0, 4);";
        String btnHover = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.6), 20, 0, 0, 6);";
        btnKonfirmasi.setStyle(btnBase);
        btnKonfirmasi.setOnMouseEntered(e -> btnKonfirmasi.setStyle(btnHover));
        btnKonfirmasi.setOnMouseExited(e -> btnKonfirmasi.setStyle(btnBase));

        btnBatal = new Button("Batal");
        btnBatal.setMaxWidth(Double.MAX_VALUE);
        btnBatal.setPrefHeight(40);
        String cancelBase = "-fx-background-color: rgba(239,68,68,0.15); -fx-text-fill: #FCA5A5;" +
                " -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-size: 13;";
        String cancelHover = "-fx-background-color: #EF4444; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-size: 13;";
        btnBatal.setStyle(cancelBase);
        btnBatal.setOnMouseEntered(e -> btnBatal.setStyle(cancelHover));
        btnBatal.setOnMouseExited(e -> btnBatal.setStyle(cancelBase));

        formCard.getChildren().addAll(
                headerRow, sep,
                lblNIK, txtNIK,
                lblNama, txtNamaLengkap,
                lblHP, txtNoHP,
                lblAlamat, txtAlamat,
                lblKamar, txtNoKamar,
                btnKonfirmasi, btnBatal);

        this.layout.getChildren().setAll(formCard);
    }

    public String getNamaLengkap() { return txtNamaLengkap.getText(); }
    public String getNoKamar() { return txtNoKamar.getText(); }
    public Button getBtnKonfirmasi() { return btnKonfirmasi; }
    public Button getBtnBatal() { return btnBatal; }
}