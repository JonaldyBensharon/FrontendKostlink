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

        // Card Register — glassmorphism style
        VBox registerCard = new VBox(18);
        registerCard.setAlignment(Pos.CENTER);
        registerCard.setPadding(new Insets(45, 45, 40, 45));
        registerCard.setMaxWidth(440);
        registerCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.07);" +
            "-fx-background-radius: 24;" +
            "-fx-border-color: rgba(255,255,255,0.12);" +
            "-fx-border-radius: 24;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 40, 0, 0, 15);"
        );

        // Icon
        Label lblIcon = new Label("📝");
        lblIcon.setFont(Font.font(44));

        lblTitle = new Label("DAFTAR AKUN");
        lblTitle.setTextFill(Color.WHITE);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 30));

        Label lblSubtitle = new Label("Buat akun baru untuk mulai menggunakan KostLink");
        lblSubtitle.setTextFill(Color.rgb(200, 200, 220, 0.7));
        lblSubtitle.setFont(Font.font("System", 12));

        Region spacer1 = new Region();
        spacer1.setPrefHeight(6);

        // Input Fields — translucent style
        String inputStyle =
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12 18;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(200,200,220,0.5);" +
            "-fx-border-color: rgba(255,255,255,0.15);" +
            "-fx-border-radius: 12;" +
            "-fx-font-size: 14;";

        txtUsername = new TextField();
        txtUsername.setPromptText("👤  Buat Username");
        txtUsername.setMaxWidth(350);
        txtUsername.setPrefHeight(46);
        txtUsername.setStyle(inputStyle);

        txtPassword = new PasswordField();
        txtPassword.setPromptText("🔒  Buat Password");
        txtPassword.setMaxWidth(350);
        txtPassword.setPrefHeight(46);
        txtPassword.setStyle(inputStyle);

        txtConfirmPassword = new PasswordField();
        txtConfirmPassword.setPromptText("🔐  Konfirmasi Password");
        txtConfirmPassword.setMaxWidth(350);
        txtConfirmPassword.setPrefHeight(46);
        txtConfirmPassword.setStyle(inputStyle);

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
        btnRegister.setStyle(btnBase);
        btnRegister.setOnMouseEntered(e -> btnRegister.setStyle(btnHover));
        btnRegister.setOnMouseExited(e -> btnRegister.setStyle(btnBase));

        linkBack = new Hyperlink("Sudah punya akun? Login");
        linkBack.setTextFill(Color.rgb(200, 200, 255, 0.8));
        linkBack.setFont(Font.font(13));
        linkBack.setStyle("-fx-border-color: transparent;");

        registerCard.getChildren().setAll(lblIcon, lblTitle, lblSubtitle, spacer1,
                txtUsername, txtPassword, txtConfirmPassword, btnRegister, linkBack);
        this.layout.getChildren().setAll(registerCard);
    }

    public String getUsername() { return txtUsername.getText().trim(); }
    public String getPassword() { return txtPassword.getText(); }
    public String getConfirmPassword() { return txtConfirmPassword.getText(); }
    public Button getBtnRegister() { return btnRegister; }

    // Getter tambahan
    public Hyperlink getBtnBack() { return linkBack; }
}