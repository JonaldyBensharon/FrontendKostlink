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
        VBox formCard = new VBox(6);
        formCard.setPadding(new Insets(36, 42, 32, 42));
        formCard.setMaxWidth(480);
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
        VBox headerText = new VBox(2);
        headerText.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = new Label("Lengkapi Profil Anda");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 21));
        lblTitle.setTextFill(Color.WHITE);
        Label lblSub = new Label("Isi data berikut untuk mengakses dashboard");
        lblSub.setTextFill(Color.rgb(180, 170, 210, 0.8));
        lblSub.setFont(Font.font(12));
        headerText.getChildren().addAll(lblTitle, lblSub);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.1);");

        // Styles
        String lS = "-fx-text-fill: rgba(200,190,230,0.9); -fx-font-size: 11; -fx-font-weight: bold;";
        String iS =
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10 14;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(200,200,220,0.45);" +
            "-fx-border-color: rgba(255,255,255,0.13);" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13;";
        String iE =
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10 14;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(200,200,220,0.45);" +
            "-fx-border-color: #EF4444;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13;";

        // === Field Groups ===
        Label lblNIK = new Label("NIK"); lblNIK.setStyle(lS);
        txtNIK = new TextField(); txtNIK.setPromptText("Masukkan NIK Anda (16 digit)"); txtNIK.setStyle(iS); txtNIK.setPrefHeight(38);
        Label vNIK = mkVal();
        VBox gNIK = mkGrp(lblNIK, txtNIK, vNIK);

        Label lblNama = new Label("NAMA LENGKAP"); lblNama.setStyle(lS);
        txtNamaLengkap = new TextField(); txtNamaLengkap.setPromptText("Masukkan nama lengkap"); txtNamaLengkap.setStyle(iS); txtNamaLengkap.setPrefHeight(38);
        Label vNama = mkVal();
        VBox gNama = mkGrp(lblNama, txtNamaLengkap, vNama);

        Label lblHP = new Label("NOMOR HP"); lblHP.setStyle(lS);
        txtNoHP = new TextField(); txtNoHP.setPromptText("Contoh: 08123456789"); txtNoHP.setStyle(iS); txtNoHP.setPrefHeight(38);
        Label vHP = mkVal();
        VBox gHP = mkGrp(lblHP, txtNoHP, vHP);

        Label lblAlamat = new Label("ALAMAT ASAL"); lblAlamat.setStyle(lS);
        txtAlamat = new TextField(); txtAlamat.setPromptText("Masukkan alamat asal Anda"); txtAlamat.setStyle(iS); txtAlamat.setPrefHeight(38);
        Label vAlamat = mkVal();
        VBox gAlamat = mkGrp(lblAlamat, txtAlamat, vAlamat);

        Label lblKamar = new Label("NOMOR KAMAR"); lblKamar.setStyle(lS);
        txtNoKamar = new TextField(); txtNoKamar.setPromptText("Contoh: 5"); txtNoKamar.setStyle(iS); txtNoKamar.setPrefHeight(38);
        Label vKamar = mkVal();
        VBox gKamar = mkGrp(lblKamar, txtNoKamar, vKamar);

        // Spacer before buttons
        Region btnSpacer = new Region();
        btnSpacer.setPrefHeight(6);

        // Buttons
        btnKonfirmasi = new Button("SIMPAN & MASUK DASHBOARD  \u2192");
        btnKonfirmasi.setMaxWidth(Double.MAX_VALUE);
        btnKonfirmasi.setPrefHeight(42);
        String bB = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.4), 15, 0, 0, 4);";
        String bH = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.6), 20, 0, 0, 6);";
        String bD = "-fx-background-color: rgba(124,58,237,0.3);" +
                "-fx-text-fill: rgba(255,255,255,0.4); -fx-font-weight: bold; -fx-font-size: 13;" +
                "-fx-background-radius: 12; -fx-cursor: default;";
        btnKonfirmasi.setStyle(bB);
        btnKonfirmasi.setOnMouseEntered(e -> { if (!btnKonfirmasi.isDisabled()) btnKonfirmasi.setStyle(bH); });
        btnKonfirmasi.setOnMouseExited(e -> { if (!btnKonfirmasi.isDisabled()) btnKonfirmasi.setStyle(bB); });

        btnBatal = new Button("Batal");
        btnBatal.setMaxWidth(Double.MAX_VALUE);
        btnBatal.setPrefHeight(36);
        String cB = "-fx-background-color: rgba(239,68,68,0.15); -fx-text-fill: #FCA5A5;" +
                " -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-size: 12;";
        String cH = "-fx-background-color: #EF4444; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-size: 12;";
        btnBatal.setStyle(cB);
        btnBatal.setOnMouseEntered(e -> btnBatal.setStyle(cH));
        btnBatal.setOnMouseExited(e -> btnBatal.setStyle(cB));

        // === Validasi real-time ===
        Runnable validateAll = () -> {
            String nik = txtNIK.getText() == null ? "" : txtNIK.getText().trim();
            String nama = txtNamaLengkap.getText() == null ? "" : txtNamaLengkap.getText().trim();
            String hp = txtNoHP.getText() == null ? "" : txtNoHP.getText().trim();
            String alamat = txtAlamat.getText() == null ? "" : txtAlamat.getText().trim();
            String kamar = txtNoKamar.getText() == null ? "" : txtNoKamar.getText().trim();

            boolean nkOk = true, nmOk = true, hpOk = true, alOk = true, kmOk = true;

            if (!nik.isEmpty()) {
                if (!nik.matches("^[0-9]+$")) {
                    setErr(vNIK, "NIK hanya boleh berisi angka", txtNIK, iE); nkOk = false;
                } else if (nik.length() != 16) {
                    setErr(vNIK, "NIK harus 16 digit (" + nik.length() + "/16)", txtNIK, iE); nkOk = false;
                } else { clr(vNIK, txtNIK, iS); }
            } else { clr(vNIK, txtNIK, iS); nkOk = false; }

            if (!nama.isEmpty()) {
                if (!nama.matches("^[a-zA-Z\\s]+$")) {
                    setErr(vNama, "Nama hanya boleh berisi huruf dan spasi", txtNamaLengkap, iE); nmOk = false;
                } else if (nama.length() < 3) {
                    setErr(vNama, "Nama minimal 3 karakter (" + nama.length() + "/3)", txtNamaLengkap, iE); nmOk = false;
                } else if (nama.length() > 50) {
                    setErr(vNama, "Nama maksimal 50 karakter (" + nama.length() + "/50)", txtNamaLengkap, iE); nmOk = false;
                } else { clr(vNama, txtNamaLengkap, iS); }
            } else { clr(vNama, txtNamaLengkap, iS); nmOk = false; }

            if (!hp.isEmpty()) {
                if (!hp.matches("^(\\+62|0)[0-9]+$")) {
                    setErr(vHP, "Format nomor HP tidak valid (awali dengan 0 atau +62)", txtNoHP, iE); hpOk = false;
                } else {
                    String d = hp.replaceAll("[^0-9]", "");
                    if (d.length() < 10) { setErr(vHP, "Nomor HP minimal 10 digit", txtNoHP, iE); hpOk = false; }
                    else if (d.length() > 15) { setErr(vHP, "Nomor HP maksimal 15 digit", txtNoHP, iE); hpOk = false; }
                    else { clr(vHP, txtNoHP, iS); }
                }
            } else { clr(vHP, txtNoHP, iS); hpOk = false; }

            if (!alamat.isEmpty()) {
                if (alamat.length() < 5) {
                    setErr(vAlamat, "Alamat minimal 5 karakter (" + alamat.length() + "/5)", txtAlamat, iE); alOk = false;
                } else if (alamat.length() > 100) {
                    setErr(vAlamat, "Alamat maksimal 100 karakter (" + alamat.length() + "/100)", txtAlamat, iE); alOk = false;
                } else { clr(vAlamat, txtAlamat, iS); }
            } else { clr(vAlamat, txtAlamat, iS); alOk = false; }

            if (!kamar.isEmpty()) {
                if (!kamar.matches("^[0-9]+$")) {
                    setErr(vKamar, "Nomor kamar hanya boleh berisi angka", txtNoKamar, iE); kmOk = false;
                } else if (kamar.length() > 3) {
                    setErr(vKamar, "Nomor kamar maksimal 3 digit", txtNoKamar, iE); kmOk = false;
                } else { clr(vKamar, txtNoKamar, iS); }
            } else { clr(vKamar, txtNoKamar, iS); kmOk = false; }

            boolean ok = nkOk && nmOk && hpOk && alOk && kmOk;
            btnKonfirmasi.setDisable(!ok);
            btnKonfirmasi.setStyle(ok ? bB : bD);
        };

        txtNIK.textProperty().addListener((o, a, b) -> validateAll.run());
        txtNamaLengkap.textProperty().addListener((o, a, b) -> validateAll.run());
        txtNoHP.textProperty().addListener((o, a, b) -> validateAll.run());
        txtAlamat.textProperty().addListener((o, a, b) -> validateAll.run());
        txtNoKamar.textProperty().addListener((o, a, b) -> validateAll.run());

        btnKonfirmasi.setDisable(true);
        btnKonfirmasi.setStyle(bD);

        formCard.getChildren().addAll(
                headerText, sep,
                gNIK, gNama, gHP, gAlamat, gKamar,
                btnSpacer, btnKonfirmasi, btnBatal);

        this.layout.getChildren().setAll(formCard);
    }

    private VBox mkGrp(Label label, TextField input, Label val) {
        VBox g = new VBox(2);
        g.getChildren().addAll(label, input, val);
        return g;
    }

    private Label mkVal() {
        Label lbl = new Label();
        lbl.setFont(Font.font(10));
        lbl.setTextFill(Color.rgb(252, 165, 165));
        lbl.setWrapText(true);
        lbl.setMinHeight(13);
        lbl.setPrefHeight(13);
        lbl.setMaxHeight(13);
        lbl.setText("");
        return lbl;
    }

    private void setErr(Label lbl, String msg, TextField f, String es) {
        lbl.setText(msg);
        f.setStyle(es);
    }

    private void clr(Label lbl, TextField f, String ns) {
        lbl.setText("");
        f.setStyle(ns);
    }

    public String getNamaLengkap() { return txtNamaLengkap.getText(); }
    public String getNoKamar() { return txtNoKamar.getText(); }
    public Button getBtnKonfirmasi() { return btnKonfirmasi; }
    public Button getBtnBatal() { return btnBatal; }
}