package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.scene.control.*;
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
        lblTitle = new Label("KOSTLINK SYSTEM");
        lblTitle.setTextFill(Color.WHITE);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 30));

        txtUsername = new TextField();
        txtUsername.setPromptText("Username");
        txtUsername.setMaxWidth(300);
        txtUsername.setStyle("-fx-background-radius: 10; -fx-padding: 10;");

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        txtPassword.setMaxWidth(300);
        txtPassword.setStyle("-fx-background-radius: 10; -fx-padding: 10;");

        btnLogin = new Button("MASUK");
        btnLogin.setMinWidth(300);
        btnLogin.setStyle("-fx-background-color: #C147E9; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10;");

        linkDaftar = new Hyperlink("Belum punya akun? Daftar di sini");
        linkDaftar.setTextFill(Color.LIGHTBLUE);

        this.layout.getChildren().addAll(lblTitle, txtUsername, txtPassword, btnLogin, linkDaftar);
    }

    // --- GETTER (ENCAPSULATION) ---
    public String getUsername() { return txtUsername.getText(); }

    // TAMBAHKAN INI: Agar Main.java bisa mengecek password
    public String getPassword() { return txtPassword.getText(); }

    public Button getBtnLogin() { return btnLogin; }
    public Hyperlink getLinkDaftar() { return linkDaftar; }
}