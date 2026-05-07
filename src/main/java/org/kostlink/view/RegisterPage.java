package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.scene.control.*;
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
        lblTitle = new Label("DAFTAR AKUN KOSTLINK");
        lblTitle.setTextFill(Color.WHITE);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 24));

        txtUsername = new TextField();
        txtUsername.setPromptText("Buat Username");
        txtUsername.setMaxWidth(300);
        txtUsername.setStyle("-fx-background-radius: 10; -fx-padding: 10;");

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Buat Password");
        txtPassword.setMaxWidth(300);
        txtPassword.setStyle("-fx-background-radius: 10; -fx-padding: 10;");

        txtConfirmPassword = new PasswordField();
        txtConfirmPassword.setPromptText("Konfirmasi Password");
        txtConfirmPassword.setMaxWidth(300);
        txtConfirmPassword.setStyle("-fx-background-radius: 10; -fx-padding: 10;");

        btnRegister = new Button("DAFTAR SEKARANG");
        btnRegister.setMinWidth(300);
        btnRegister.setStyle("-fx-background-color: #C147E9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10;");

        linkBack = new Hyperlink("Sudah punya akun? Login");
        linkBack.setTextFill(Color.LIGHTBLUE);

        this.layout.getChildren().addAll(lblTitle, txtUsername, txtPassword, txtConfirmPassword, btnRegister, linkBack);
    }

    // Getter untuk mengambil data saat tombol daftar diklik
    public String getUsername() { return txtUsername.getText(); }
    public String getPassword() { return txtPassword.getText(); }
    public String getConfirmPassword() { return txtConfirmPassword.getText(); }
    public Hyperlink getLinkBack() { return linkBack; }
    public Button getBtnRegister() { return btnRegister; }
}