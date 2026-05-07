package org.kostlink;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.kostlink.view.DashboardPage;
import org.kostlink.view.LoginPage;
import org.kostlink.view.RegisterPage;
import java.util.HashMap;

public class Main extends Application {

    private static Stage stage;
    private static HashMap<String, String> userDatabase = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        // AGAR TIDAK NGEZOOM: Buat jendela otomatis maksimal sesuai layar laptop
        stage.setMaximized(true);

        userDatabase.put("admin", "admin123");
        showLogin();
    }

    public static void showLogin() {
        LoginPage loginPage = new LoginPage();
        loginPage.getLinkDaftar().setOnAction(e -> showRegister());

        loginPage.getBtnLogin().setOnAction(e -> {
            String user = loginPage.getUsername();
            String pass = loginPage.getPassword();

            if (userDatabase.containsKey(user) && userDatabase.get(user).equals(pass)) {
                showDashboard();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Gagal");
                alert.setHeaderText(null);
                alert.setContentText("Akun tidak ditemukan atau password salah!");
                alert.initOwner(stage); // Alert menempel pada jendela utama
                alert.showAndWait();
            }
        });

        // Scene dibuat tanpa angka kaku agar responsif
        Scene scene = new Scene(loginPage.getLayout());
        stage.setTitle("KOSTLINK - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void showRegister() {
        RegisterPage regPage = new RegisterPage();
        regPage.getLinkBack().setOnAction(e -> showLogin());

        regPage.getBtnRegister().setOnAction(e -> {
            String user = regPage.getUsername();
            String pass = regPage.getPassword();
            String confirm = regPage.getConfirmPassword();

            if (!user.isEmpty() && !pass.isEmpty() && pass.equals(confirm)) {
                userDatabase.put(user, pass);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registrasi Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Akun " + user + " berhasil didaftarkan.");
                alert.initOwner(stage);
                alert.showAndWait();
                showLogin();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Registrasi Gagal");
                alert.setHeaderText(null);
                alert.setContentText("Periksa kembali inputan Anda.");
                alert.initOwner(stage);
                alert.showAndWait();
            }
        });

        Scene scene = new Scene(regPage.getLayout());
        stage.setTitle("KOSTLINK - Register Akun");
        stage.setScene(scene);
        stage.show();
    }

    public static void showDashboard() {
        DashboardPage dashboard = new DashboardPage();

        dashboard.getBtnLogout().setOnAction(e -> {
            System.out.println("User Logout...");
            showLogin();
        });

        Scene scene = new Scene(dashboard.getLayout());
        stage.setTitle("KOSTLINK - Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}