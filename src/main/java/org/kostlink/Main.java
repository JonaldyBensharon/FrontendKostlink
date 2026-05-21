package org.kostlink;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.kostlink.model.*;
import org.kostlink.view.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {

    private static Stage stage;
    private static Scene mainScene;

    // Database berbasis Objek Polimorfisme
    private static HashMap<String, User> userDatabase = new HashMap<>();
    private static User currentUserActive = null;

    // Penyelamat logika lama agar halaman-halaman View lain tidak ikut error
    private static Penghuni dataPenghuniGlobal = null;
    private static ArrayList<String> listKeluhan = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        // Mendaftarkan akun bawaan menggunakan instansiasi objek anak
        userDatabase.put("admin", new PemilikKos("admin", "admin123"));
        userDatabase.put("zaskiah", new PemilikKos("zaskiah", "123"));

        // Akun simulasi penghuni bawaan
        Penghuni defaultPenghuni = new Penghuni("budi", "123");
        userDatabase.put("budi", defaultPenghuni);
        dataPenghuniGlobal = defaultPenghuni;

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

        loginPage.getBtnLogin().setOnAction(e -> {
            String inputUser = loginPage.getUsername();
            String inputPass = loginPage.getPassword();

            // 1. Cek apakah username terdaftar di database objek
            if (userDatabase.containsKey(inputUser)) {
                User user = userDatabase.get(inputUser);

                // 2. KONDISI KHUSUS ADMIN / PEMILIK KOS
                boolean isAdminValid = (inputUser.equals("admin") && inputPass.equals("admin123")) ||
                        (inputUser.equals("zaskiah") && inputPass.equals("123"));

                // 3. KONDISI UNTUK PENGHUNI (Membaca password dari objek)
                boolean isPenghuniValid = (user instanceof Penghuni) && user.getPassword().equals(inputPass);

                // Jalankan login jika salah satu kondisi valid
                if (isAdminValid || isPenghuniValid) {
                    currentUserActive = user;

                    // Jika tipe Penghuni, ikat ke dataPenghuniGlobal agar fiturnya sinkron
                    if (user instanceof Penghuni) {
                        dataPenghuniGlobal = (Penghuni) user;
                    }

                    // Polimorfisme mengarahkan ke dashboard yang sesuai role
                    currentUserActive.bukaDashboard();
                    return;
                }
            }
            showAlert("Username atau Password salah!");
        });

        loginPage.getLinkDaftar().setOnAction(e -> showRegister());
        setRoot(loginPage.getLayout(), "KOSTLINK - Login");
    }

    public static void showRegister() {
        RegisterPage regPage = new RegisterPage();
        regPage.setupComponents();

        regPage.getBtnRegister().setOnAction(e -> {
            String userBaru = regPage.getUsername();
            String passBaru = regPage.getPassword();

            if (!userBaru.isEmpty() && !passBaru.isEmpty()) {
                Penghuni penghuniBaru = new Penghuni(userBaru, passBaru);
                userDatabase.put(userBaru, penghuniBaru);
                dataPenghuniGlobal = penghuniBaru;

                showAlert("Registrasi Berhasil! Silakan Login.");
                showLogin();
            } else {
                showAlert("Username dan Password tidak boleh kosong!");
            }
        });

        regPage.getBtnBack().setOnAction(e -> showLogin());
        setRoot(regPage.getLayout(), "KOSTLINK - Registrasi");
    }

    public static void jalankanDashboardAdmin(PemilikKos admin) {
        AdminDashboardPage adminPage = new AdminDashboardPage(admin.getUsername());
        new AdminDashboardController(adminPage);
        setRoot(adminPage.getLayout(), "KOSTLINK - Panel Utama Ibu Kost");
    }

    public static void jalankanDashboardPenghuni(Penghuni p) {
        DashboardPage dbPage = new DashboardPage(
                p.getUsername(),
                p.getNamaLengkap(),
                p.getNomorKamar(),
                p.isStatusAktif(),
                p.getTanggalSiklusKost()
        );
        new DashboardController(dbPage);

        dbPage.getLblUser().setOnMouseClicked(e -> {
            if (p.isStatusAktif()) showHomePenghuni();
            else showFormulir();
        });

        setRoot(dbPage.getLayout(), "KOSTLINK - Dashboard Penghuni");
    }

    public static void showFormulir() {
        FormPenghuniPage formPage = new FormPenghuniPage();
        formPage.setupComponents();
        formPage.getBtnBatal().setOnAction(e -> showDashboard());

        formPage.getBtnKonfirmasi().setOnAction(e -> {
            if (!formPage.getNamaLengkap().isEmpty() && dataPenghuniGlobal != null) {
                dataPenghuniGlobal.setNamaLengkap(formPage.getNamaLengkap());
                dataPenghuniGlobal.setNomorKamar(formPage.getNoKamar());
                dataPenghuniGlobal.setStatusAktif(true);
                dataPenghuniGlobal.setTanggalSiklusKost(LocalDate.now().getDayOfMonth());

                showDashboard();
            }
        });
        setRoot(formPage.getLayout(), "KOSTLINK - Lengkapi Data");
    }

    public static void showHomePenghuni() {
        if (dataPenghuniGlobal != null && currentUserActive != null) {
            HomePenghuniPage profilePage = new HomePenghuniPage(
                    dataPenghuniGlobal.getNamaLengkap(),
                    currentUserActive.getUsername(),
                    dataPenghuniGlobal.getNomorKamar()
            );
            profilePage.getBtnBack().setOnAction(e -> showDashboard());
            setRoot(profilePage.getLayout(), "KOSTLINK - Profil Penghuni");
        }
    }

    // =========================================================================
    // JEMBATAN GETTER & SETTER GLOBAL (Penyelamat Semua Halaman View)
    // =========================================================================
    public static void showDashboard() {
        if (currentUserActive != null) {
            currentUserActive.bukaDashboard();
        }
    }

    public static boolean getIsSudahBayar() {
        return dataPenghuniGlobal != null && dataPenghuniGlobal.isSudahBayar();
    }
    public static void setIsSudahBayar(boolean status) {
        if (dataPenghuniGlobal != null) dataPenghuniGlobal.setSudahBayar(status);
    }
    public static String getNamaLengkapPenghuni() {
        return dataPenghuniGlobal != null ? dataPenghuniGlobal.getNamaLengkap() : "";
    }
    public static String getNomorKamarPenghuni() {
        return dataPenghuniGlobal != null ? dataPenghuniGlobal.getNomorKamar() : "-";
    }
    public static boolean getStatusAktif() {
        return dataPenghuniGlobal != null && dataPenghuniGlobal.isStatusAktif();
    }
    public static int getTanggalSiklusKost() {
        return dataPenghuniGlobal != null ? dataPenghuniGlobal.getTanggalSiklusKost() : 1;
    }

    public static ArrayList<String> getListKeluhan() { return listKeluhan; }
    public static void tambahKeluhan(String keluhan) { listKeluhan.add(keluhan); }

    public static void backToLogin() { showLogin(); }
    public static void goToFormulir() { showFormulir(); }

    private static void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }

    public static void main(String[] args) { launch(args); }
}