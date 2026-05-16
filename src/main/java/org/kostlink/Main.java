package org.kostlink;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.kostlink.view.*;
import java.util.HashMap;

public class Main extends Application {

    private static Stage stage;
    private static Scene mainScene;
    // Database sementara untuk menyimpan user admin dan hasil registrasi
    private static HashMap<String, String> userDatabase = new HashMap<>();

    private static String sessionUser = "";
    private static String namaLengkapPenghuni = "";
    private static String nomorKamarPenghuni = "-";
    private static boolean statusAktif = false;
    private static boolean isSudahBayar = false;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        // Data Login Testing
        userDatabase.put("admin", "admin123");
        userDatabase.put("zaskiah", "123");

        mainScene = new Scene(new Pane());
        stage.setScene(mainScene);
        stage.setMaximized(true);

        showLogin();
        stage.show();
    }

    public static void setRoot(Pane layoutPane, String title) {
        mainScene.setRoot(layoutPane);
        stage.setTitle(title);
        stage.setMaximized(true);
    }

    public static void showLogin() {
        LoginPage loginPage = new LoginPage();
        loginPage.setupComponents();

        // Logika Tombol Login
        loginPage.getBtnLogin().setOnAction(e -> {
            if (userDatabase.containsKey(loginPage.getUsername()) &&
                    userDatabase.get(loginPage.getUsername()).equals(loginPage.getPassword())) {
                sessionUser = loginPage.getUsername();
                showDashboard();
            } else {
                showAlert("Username atau Password salah!");
            }
        });

        // Menambahkan aksi klik pada "Belum punya akun? Daftar di sini"
        loginPage.getLinkDaftar().setOnAction(e -> {
            showRegister();
        });

        setRoot(loginPage.getLayout(), "KOSTLINK - Login");
    }

    // Tambahkan Method showRegister ini agar bisa pindah ke halaman daftar
    public static void showRegister() {
        RegisterPage regPage = new RegisterPage();
        regPage.setupComponents();

        // Logika ketika user mengklik tombol daftar di halaman Register
        regPage.getBtnRegister().setOnAction(e -> {
            String userBaru = regPage.getUsername();
            String passBaru = regPage.getPassword();

            if (!userBaru.isEmpty() && !passBaru.isEmpty()) {
                // Memasukkan data baru ke HashMap agar bisa digunakan login
                userDatabase.put(userBaru, passBaru);
                showAlert("Registrasi Berhasil! Silakan Login.");
                showLogin();
            } else {
                showAlert("Username dan Password tidak boleh kosong!");
            }
        });

        // Tombol kembali ke Login
        regPage.getBtnBack().setOnAction(e -> showLogin());

        setRoot(regPage.getLayout(), "KOSTLINK - Registrasi");
    }

    public static void showDashboard() {
        DashboardPage dbPage = new DashboardPage(sessionUser, namaLengkapPenghuni, nomorKamarPenghuni, statusAktif, 16);
        new DashboardController(dbPage);

        dbPage.getLblUser().setOnMouseClicked(e -> {
            if (statusAktif) showHomePenghuni();
            else showFormulir();
        });

        setRoot(dbPage.getLayout(), "KOSTLINK - Dashboard");
    }

    public static void showFormulir() {
        FormPenghuniPage formPage = new FormPenghuniPage();
        formPage.setupComponents();
        formPage.getBtnBatal().setOnAction(e -> showDashboard());
        formPage.getBtnKonfirmasi().setOnAction(e -> {
            if (!formPage.getNamaLengkap().isEmpty()) {
                namaLengkapPenghuni = formPage.getNamaLengkap();
                nomorKamarPenghuni = formPage.getNoKamar();
                statusAktif = true;
                showDashboard();
            }
        });
        setRoot(formPage.getLayout(), "KOSTLINK - Lengkapi Data");
    }

    public static void showHomePenghuni() {
        HomePenghuniPage profilePage = new HomePenghuniPage(namaLengkapPenghuni, sessionUser, nomorKamarPenghuni);
        profilePage.getBtnBack().setOnAction(e -> showDashboard());
        setRoot(profilePage.getLayout(), "KOSTLINK - Profil Penghuni");
    }

    // --- FITUR BARU: GETTER & SETTER GLOBAL UNTUK SINKRONISASI STATUS BAYAR ---
    public static boolean getIsSudahBayar() {
        return isSudahBayar;
    }

    public static void setIsSudahBayar(boolean status) {
        isSudahBayar = status;
    }

    public static void backToLogin() { showLogin(); }
    public static void goToFormulir() { showFormulir(); }

    private static void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }

    public static void main(String[] args) { launch(args); }
}