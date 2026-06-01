package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RegisterPage extends BasePage {
    private Label lblTitle;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private PasswordField txtConfirmPassword;
    private Button btnRegister;
    private Hyperlink linkBack;

    @Override
    public void setupComponents() {
        this.layout.setSpacing(0);
        this.layout.setAlignment(Pos.CENTER);
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a0025, #2D033B, #3b0764);");

        VBox registerCard = new VBox(12);
        registerCard.setAlignment(Pos.CENTER);
        registerCard.setPadding(new Insets(40, 45, 36, 45));
        registerCard.setMaxWidth(440);
        registerCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 24;" +
            "-fx-border-color: rgba(255,255,255,0.12);" +
            "-fx-border-radius: 24;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 40, 0, 0, 15);"
        );

        Label lblIcon = new Label("\ud83d\udcdd");
        lblIcon.setFont(Font.font(44));

        lblTitle = new Label("DAFTAR AKUN");
        lblTitle.setTextFill(Color.WHITE);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 30));

        Label lblSubtitle = new Label("Buat akun baru untuk mulai menggunakan KostLink");
        lblSubtitle.setTextFill(Color.rgb(200, 200, 220, 0.7));
        lblSubtitle.setFont(Font.font("System", 12));

        Region spacer1 = new Region();
        spacer1.setPrefHeight(2);

        String inputStyle =
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12 18;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(200,200,220,0.5);" +
            "-fx-border-color: rgba(255,255,255,0.15);" +
            "-fx-border-radius: 12;" +
            "-fx-font-size: 14;";

        String inputErrorStyle =
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12 18;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(200,200,220,0.5);" +
            "-fx-border-color: #EF4444;" +
            "-fx-border-radius: 12;" +
            "-fx-font-size: 14;";

        txtUsername = new TextField();
        txtUsername.setPromptText("Buat Username");
        txtUsername.setMaxWidth(350);
        txtUsername.setPrefHeight(44);
        txtUsername.setStyle(inputStyle);
        Label lblUserV = createValLabel(350);
        VBox userGrp = new VBox(2, txtUsername, lblUserV);
        userGrp.setAlignment(Pos.CENTER);

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Buat Password");
        txtPassword.setMaxWidth(350);
        txtPassword.setPrefHeight(44);
        txtPassword.setStyle(inputStyle);
        Label lblPassV = createValLabel(350);
        VBox passGrp = new VBox(2, txtPassword, lblPassV);
        passGrp.setAlignment(Pos.CENTER);

        txtConfirmPassword = new PasswordField();
        txtConfirmPassword.setPromptText("Konfirmasi Password");
        txtConfirmPassword.setMaxWidth(350);
        txtConfirmPassword.setPrefHeight(44);
        txtConfirmPassword.setStyle(inputStyle);
        Label lblConfV = createValLabel(350);
        VBox confGrp = new VBox(2, txtConfirmPassword, lblConfV);
        confGrp.setAlignment(Pos.CENTER);

        btnRegister = new Button("DAFTAR SEKARANG");
        btnRegister.setMinWidth(350);
        btnRegister.setPrefHeight(46);
        String btnBase = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.4), 15, 0, 0, 4);";
        String btnHover = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.6), 20, 0, 0, 6);";
        String btnDis = "-fx-background-color: rgba(124,58,237,0.3);" +
                "-fx-text-fill: rgba(255,255,255,0.4); -fx-font-weight: bold; -fx-font-size: 15;" +
                "-fx-background-radius: 12; -fx-cursor: default;";
        btnRegister.setStyle(btnBase);
        btnRegister.setOnMouseEntered(e -> { if (!btnRegister.isDisabled()) btnRegister.setStyle(btnHover); });
        btnRegister.setOnMouseExited(e -> { if (!btnRegister.isDisabled()) btnRegister.setStyle(btnBase); });

        Runnable validate = () -> {
            String user = txtUsername.getText() == null ? "" : txtUsername.getText();
            String pass = txtPassword.getText() == null ? "" : txtPassword.getText();
            String conf = txtConfirmPassword.getText() == null ? "" : txtConfirmPassword.getText();
            boolean uOk = true, pOk = true, cOk = true;

            if (!user.isEmpty()) {
                if (user.length() < 5) {
                    lblUserV.setText("Username minimal 5 karakter (" + user.length() + "/5)");
                    txtUsername.setStyle(inputErrorStyle); uOk = false;
                } else if (user.length() > 20) {
                    lblUserV.setText("Username maksimal 20 karakter (" + user.length() + "/20)");
                    txtUsername.setStyle(inputErrorStyle); uOk = false;
                } else if (!user.matches("^[a-zA-Z0-9_]+$")) {
                    lblUserV.setText("Username hanya boleh huruf, angka, dan underscore");
                    txtUsername.setStyle(inputErrorStyle); uOk = false;
                } else {
                    lblUserV.setText(""); txtUsername.setStyle(inputStyle);
                }
            } else { lblUserV.setText(""); txtUsername.setStyle(inputStyle); uOk = false; }

            if (!pass.isEmpty()) {
                if (pass.length() < 6) {
                    lblPassV.setText("Password minimal 6 karakter (" + pass.length() + "/6)");
                    txtPassword.setStyle(inputErrorStyle); pOk = false;
                } else if (pass.length() > 30) {
                    lblPassV.setText("Password maksimal 30 karakter (" + pass.length() + "/30)");
                    txtPassword.setStyle(inputErrorStyle); pOk = false;
                } else {
                    lblPassV.setText(""); txtPassword.setStyle(inputStyle);
                }
            } else { lblPassV.setText(""); txtPassword.setStyle(inputStyle); pOk = false; }

            if (!conf.isEmpty()) {
                if (!conf.equals(pass)) {
                    lblConfV.setText("Konfirmasi password tidak cocok");
                    txtConfirmPassword.setStyle(inputErrorStyle); cOk = false;
                } else {
                    lblConfV.setText(""); txtConfirmPassword.setStyle(inputStyle);
                }
            } else { lblConfV.setText(""); txtConfirmPassword.setStyle(inputStyle); cOk = false; }

            boolean ok = uOk && pOk && cOk;
            btnRegister.setDisable(!ok);
            btnRegister.setStyle(ok ? btnBase : btnDis);
        };

        txtUsername.textProperty().addListener((o, a, b) -> validate.run());
        txtPassword.textProperty().addListener((o, a, b) -> validate.run());
        txtConfirmPassword.textProperty().addListener((o, a, b) -> validate.run());

        btnRegister.setDisable(true);
        btnRegister.setStyle(btnDis);

        linkBack = new Hyperlink("Sudah punya akun? Login");
        linkBack.setTextFill(Color.rgb(200, 200, 255, 0.8));
        linkBack.setFont(Font.font(13));
        linkBack.setStyle("-fx-border-color: transparent;");

        registerCard.getChildren().setAll(lblIcon, lblTitle, lblSubtitle, spacer1,
                userGrp, passGrp, confGrp, btnRegister, linkBack);
        this.layout.getChildren().setAll(registerCard);
    }

    private Label createValLabel(double maxW) {
        Label lbl = new Label();
        lbl.setFont(Font.font(11));
        lbl.setTextFill(Color.rgb(252, 165, 165));
        lbl.setMaxWidth(maxW);
        lbl.setWrapText(true);
        lbl.setMinHeight(14);
        lbl.setPrefHeight(14);
        lbl.setMaxHeight(14);
        lbl.setText("");
        return lbl;
    }

    public String getUsername() { return txtUsername.getText().trim(); }
    public String getPassword() { return txtPassword.getText(); }
    public String getConfirmPassword() { return txtConfirmPassword.getText(); }
    public Button getBtnRegister() { return btnRegister; }
    public Hyperlink getBtnBack() { return linkBack; }
}