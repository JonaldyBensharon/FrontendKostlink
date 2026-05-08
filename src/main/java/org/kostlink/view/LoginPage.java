package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Pos;
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
        this.layout.setSpacing(20);
        this.layout.setAlignment(Pos.CENTER);
        this.layout.setStyle("-fx-background-color: #2D033B;");

        lblTitle = new Label("KOSTLINK SYSTEM");
        lblTitle.setTextFill(Color.WHITE);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 36));

        txtUsername = new TextField();
        txtUsername.setPromptText("Username");
        txtUsername.setMaxWidth(350);
        txtUsername.setStyle("-fx-background-radius: 10; -fx-padding: 12;");

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        txtPassword.setMaxWidth(350);
        txtPassword.setStyle("-fx-background-radius: 10; -fx-padding: 12;");

        btnLogin = new Button("MASUK");
        // Memastikan tombol bisa dipicu dengan ENTER
        btnLogin.setDefaultButton(true);
        btnLogin.setMinWidth(350);
        btnLogin.setStyle("-fx-background-color: #C147E9; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 12; -fx-cursor: hand;");

        linkDaftar = new Hyperlink("Belum punya akun? Daftar di sini");
        linkDaftar.setTextFill(Color.WHITE);

        // Menggunakan setAll untuk memastikan tidak ada duplikasi komponen
        this.layout.getChildren().setAll(lblTitle, txtUsername, txtPassword, btnLogin, linkDaftar);
    }

    // Getter dengan proteksi Null
    public String getUsername() {
        return (txtUsername.getText() == null) ? "" : txtUsername.getText().trim();
    }

    public String getPassword() {
        return (txtPassword.getText() == null) ? "" : txtPassword.getText();
    }

    public Button getBtnLogin() { return btnLogin; }
    public Hyperlink getLinkDaftar() { return linkDaftar; }
}