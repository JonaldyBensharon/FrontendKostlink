package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginPage extends BasePage {

    private Label lblTitle;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLogin;
    private Hyperlink linkDaftar;

    @Override
    public void setupComponents() {
        this.layout.setSpacing(0);
        this.layout.setAlignment(Pos.CENTER);
        this.layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a0025, #2D033B, #3b0764);");

        // Card Login
        VBox loginCard = new VBox(14);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(44, 45, 40, 45));
        loginCard.setMaxWidth(420);
        loginCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 24;" +
            "-fx-border-color: rgba(255,255,255,0.12);" +
            "-fx-border-radius: 24;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 40, 0, 0, 15);"
        );

        // Logo Icon
        Label lblIcon = new Label("\ud83c\udfe0");
        lblIcon.setFont(Font.font(48));

        lblTitle = new Label("KOSTLINK");
        lblTitle.setTextFill(Color.WHITE);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 34));

        Label lblSubtitle = new Label("Sistem Manajemen Kost Modern");
        lblSubtitle.setTextFill(Color.rgb(200, 200, 220, 0.7));
        lblSubtitle.setFont(Font.font("System", 13));

        // Spacer
        Region spacer1 = new Region();
        spacer1.setPrefHeight(4);

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

        // === Username Field Group ===
        txtUsername = new TextField();
        txtUsername.setPromptText("Username");
        txtUsername.setMaxWidth(340);
        txtUsername.setPrefHeight(44);
        txtUsername.setStyle(inputStyle);

        Label lblUserValidation = createValidationLabel(340);

        VBox usernameGroup = new VBox(2);
        usernameGroup.setAlignment(Pos.CENTER);
        usernameGroup.getChildren().addAll(txtUsername, lblUserValidation);

        // === Password Field Group ===
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        txtPassword.setMaxWidth(340);
        txtPassword.setPrefHeight(44);
        txtPassword.setStyle(inputStyle);

        Label lblPassValidation = createValidationLabel(340);

        VBox passwordGroup = new VBox(2);
        passwordGroup.setAlignment(Pos.CENTER);
        passwordGroup.getChildren().addAll(txtPassword, lblPassValidation);

        // === Button ===
        btnLogin = new Button("MASUK");
        btnLogin.setDefaultButton(true);
        btnLogin.setMinWidth(340);
        btnLogin.setPrefHeight(46);
        String btnBaseStyle = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.4), 15, 0, 0, 4);";
        String btnHoverStyle = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA);" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15;" +
                "-fx-background-radius: 12; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.6), 20, 0, 0, 6);";
        String btnDisabledStyle = "-fx-background-color: rgba(124,58,237,0.3);" +
                "-fx-text-fill: rgba(255,255,255,0.4); -fx-font-weight: bold; -fx-font-size: 15;" +
                "-fx-background-radius: 12; -fx-cursor: default;";
        btnLogin.setStyle(btnBaseStyle);
        btnLogin.setOnMouseEntered(e -> { if (!btnLogin.isDisabled()) btnLogin.setStyle(btnHoverStyle); });
        btnLogin.setOnMouseExited(e -> { if (!btnLogin.isDisabled()) btnLogin.setStyle(btnBaseStyle); });

        // Validasi real-time
        Runnable validateInputs = () -> {
            String user = txtUsername.getText() == null ? "" : txtUsername.getText();
            String pass = txtPassword.getText() == null ? "" : txtPassword.getText();
            boolean userValid = true;
            boolean passValid = true;

            // Validasi username: 5-20 karakter, huruf/angka/underscore
            if (!user.isEmpty()) {
                if (user.length() < 5) {
                    lblUserValidation.setText("Username minimal 5 karakter (" + user.length() + "/5)");
                    txtUsername.setStyle(inputErrorStyle);
                    userValid = false;
                } else if (user.length() > 20) {
                    lblUserValidation.setText("Username maksimal 20 karakter (" + user.length() + "/20)");
                    txtUsername.setStyle(inputErrorStyle);
                    userValid = false;
                } else if (!user.matches("^[a-zA-Z0-9_]+$")) {
                    lblUserValidation.setText("Username hanya boleh huruf, angka, dan underscore");
                    txtUsername.setStyle(inputErrorStyle);
                    userValid = false;
                } else {
                    lblUserValidation.setText("");
                    txtUsername.setStyle(inputStyle);
                }
            } else {
                lblUserValidation.setText("");
                txtUsername.setStyle(inputStyle);
                userValid = false;
            }

            // Validasi password: 6-30 karakter
            if (!pass.isEmpty()) {
                if (pass.length() < 6) {
                    lblPassValidation.setText("Password minimal 6 karakter (" + pass.length() + "/6)");
                    txtPassword.setStyle(inputErrorStyle);
                    passValid = false;
                } else if (pass.length() > 30) {
                    lblPassValidation.setText("Password maksimal 30 karakter (" + pass.length() + "/30)");
                    txtPassword.setStyle(inputErrorStyle);
                    passValid = false;
                } else {
                    lblPassValidation.setText("");
                    txtPassword.setStyle(inputStyle);
                }
            } else {
                lblPassValidation.setText("");
                txtPassword.setStyle(inputStyle);
                passValid = false;
            }

            boolean allValid = userValid && passValid;
            btnLogin.setDisable(!allValid);
            if (!allValid) btnLogin.setStyle(btnDisabledStyle);
            else btnLogin.setStyle(btnBaseStyle);
        };

        txtUsername.textProperty().addListener((obs, o, n) -> validateInputs.run());
        txtPassword.textProperty().addListener((obs, o, n) -> validateInputs.run());

        // Set initial state
        btnLogin.setDisable(true);
        btnLogin.setStyle(btnDisabledStyle);

        linkDaftar = new Hyperlink("Belum punya akun? Daftar di sini");
        linkDaftar.setTextFill(Color.rgb(200, 200, 255, 0.8));
        linkDaftar.setFont(Font.font(13));
        linkDaftar.setStyle("-fx-border-color: transparent;");

        loginCard.getChildren().setAll(lblIcon, lblTitle, lblSubtitle, spacer1,
                usernameGroup, passwordGroup,
                btnLogin, linkDaftar);
        this.layout.getChildren().setAll(loginCard);
    }

    private Label createValidationLabel(double maxWidth) {
        Label lbl = new Label();
        lbl.setFont(Font.font(11));
        lbl.setTextFill(Color.rgb(252, 165, 165));
        lbl.setMaxWidth(maxWidth);
        lbl.setWrapText(true);
        lbl.setMinHeight(14);
        lbl.setPrefHeight(14);
        lbl.setMaxHeight(14);
        lbl.setText("");
        return lbl;
    }

    public String getUsername() {
        return (txtUsername.getText() == null) ? "" : txtUsername.getText().trim();
    }

    public String getPassword() {
        return (txtPassword.getText() == null) ? "" : txtPassword.getText();
    }

    public Button getBtnLogin() { return btnLogin; }
    public Hyperlink getLinkDaftar() { return linkDaftar; }
}