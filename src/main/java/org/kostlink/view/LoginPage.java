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
        VBox loginCard = new VBox(22);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(50, 45, 45, 45));
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
        Label lblIcon = new Label("🏠");
        lblIcon.setFont(Font.font(48));

        lblTitle = new Label("KOSTLINK");
        lblTitle.setTextFill(Color.WHITE);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 34));

        Label lblSubtitle = new Label("Sistem Manajemen Kost Modern");
        lblSubtitle.setTextFill(Color.rgb(200, 200, 220, 0.7));
        lblSubtitle.setFont(Font.font("System", 13));

        // Spacer
        Region spacer1 = new Region();
        spacer1.setPrefHeight(8);

        txtUsername = new TextField();
        txtUsername.setPromptText("👤  Username");
        txtUsername.setMaxWidth(340);
        txtUsername.setPrefHeight(46);
        txtUsername.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12 18;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(200,200,220,0.5);" +
            "-fx-border-color: rgba(255,255,255,0.15);" +
            "-fx-border-radius: 12;" +
            "-fx-font-size: 14;"
        );

        txtPassword = new PasswordField();
        txtPassword.setPromptText("🔒  Password");
        txtPassword.setMaxWidth(340);
        txtPassword.setPrefHeight(46);
        txtPassword.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12 18;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(200,200,220,0.5);" +
            "-fx-border-color: rgba(255,255,255,0.15);" +
            "-fx-border-radius: 12;" +
            "-fx-font-size: 14;"
        );

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
        btnLogin.setStyle(btnBaseStyle);
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle(btnHoverStyle));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle(btnBaseStyle));

        linkDaftar = new Hyperlink("Belum punya akun? Daftar di sini");
        linkDaftar.setTextFill(Color.rgb(200, 200, 255, 0.8));
        linkDaftar.setFont(Font.font(13));
        linkDaftar.setStyle("-fx-border-color: transparent;");

        loginCard.getChildren().setAll(lblIcon, lblTitle, lblSubtitle, spacer1, txtUsername, txtPassword, btnLogin, linkDaftar);
        this.layout.getChildren().setAll(loginCard);
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