package org.kostlink;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.kostlink.model.*;
import org.kostlink.service.ComplaintService;
import org.kostlink.view.*;
import java.time.LocalDate;
import java.util.ArrayList;
import org.kostlink.service.AppStateService;
import org.kostlink.service.UserService;
import org.kostlink.service.PenghuniService;
import org.kostlink.config.JPAUtil;
import org.h2.tools.Server;

public class Main extends Application {

    private static Stage stage;
    private static Scene mainScene;

    // Penambahan AppStateService
    private static final AppStateService appState =
            AppStateService.getInstance();

    // Penambahan UserService untuk memungkinkan integrasi backend
    private static final UserService userService =
            UserService.getInstance();

    // Penambahan PenghuniService
    private static final PenghuniService penghuniService =
            PenghuniService.getInstance();

    private static Penghuni getCurrentPenghuni() {
        User user = appState.getCurrentUser();
        return (user instanceof Penghuni) ? (Penghuni) user : null;
    }

    public static UserService getUserService() {
        return userService;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

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

            User user = userService.login(inputUser, inputPass);

            if (user != null) {
                appState.setCurrentUser(user);

                if (user.getRole() == Role.PEMILIK_KOST) {
                    jalankanDashboardAdmin((PemilikKos) user);
                } else if (user.getRole() == Role.PENGHUNI) {
                    jalankanDashboardPenghuni((Penghuni) user);
                }
            } else {
                showAlert("Username atau Password salah!");
            }
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

            if (userBaru.isEmpty() || passBaru.isEmpty()) {
                showAlert("Username dan Password tidak boleh kosong!");
                return;
            }

            Penghuni penghuniBaru =
                    userService.registerPenghuni(userBaru, passBaru);

            if (penghuniBaru != null) {
                showAlert("Registrasi Berhasil! Silakan Login.");
                showLogin();
            } else {
                showAlert("Username sudah digunakan!");
            }
        });

        regPage.getBtnBack().setOnAction(e -> showLogin());
        setRoot(regPage.getLayout(), "KostLink - Registrasi");
    }

    public static void jalankanDashboardAdmin(PemilikKos admin) {
        AdminDashboardPage adminPage = new AdminDashboardPage(admin.getUsername());
        new AdminDashboardController(adminPage);
        setRoot(adminPage.getLayout(), "KostLink - Panel Pemilik Kos");
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

        setRoot(dbPage.getLayout(), "KostLink - Panel Penghuni");
    }

    public static void showFormulir() {
        FormPenghuniPage formPage = new FormPenghuniPage();
        formPage.setupComponents();
        formPage.getBtnBatal().setOnAction(e -> showDashboard());

        formPage.getBtnKonfirmasi().setOnAction(e -> {
            User user = appState.getCurrentUser();

            if (user instanceof Penghuni penghuni
                    && !formPage.getNamaLengkap().isEmpty()) {

                String noKamar = formPage.getNoKamar() == null ? "" : formPage.getNoKamar();

                penghuniService.lengkapiData(
                        penghuni,
                        formPage.getNamaLengkap(),
                        noKamar
                );

                showDashboard();
            }
        });
        setRoot(formPage.getLayout(), "KostLink - Lengkapi Data");
    }

    public static void showHomePenghuni() {
        User user = appState.getCurrentUser();

        if (user instanceof Penghuni penghuni) {
            HomePenghuniPage profilePage = new HomePenghuniPage(
                    penghuni.getNamaLengkap(),
                    user.getUsername(),
                    penghuni.getNomorKamar()
            );

            profilePage.getBtnBack().setOnAction(e -> showDashboard());
            setRoot(profilePage.getLayout(), "KostLink - Profil Penghuni");
        }
    }

    // JEMBATAN GETTER & SETTER GLOBAL
    public static void showDashboard() {
        if (appState.getCurrentUser() != null) {
            appState.getCurrentUser().bukaDashboard();
        }
    }

    // STATUS PEMBAYARAN 3 TAHAP
    public static String getStatusPembayaran() {
        User user = appState.getCurrentUser();

        if (user instanceof Penghuni p) {
            return penghuniService.getStatusPembayaran(p);
        }

        return "-";
    }

    public static void setStatusPembayaran(String status) {
        User user = appState.getCurrentUser();
        if (!(user instanceof Penghuni p)) return;

        p.setStatusPembayaran(status);
    }

    // User mengirim bukti pembayaran → status jadi MENUNGGU_VERIFIKASI
    public static void kirimBuktiPembayaran(String buktiPath) {
        User user = appState.getCurrentUser();
        if (!(user instanceof Penghuni p)) return;

        penghuniService.kirimBukti(p, buktiPath);
    }

    public static void kirimBuktiPembayaran() {
        kirimBuktiPembayaran(null);
    }

    // Admin mengonfirmasi pembayaran → status jadi LUNAS + catat tanggal konfirmasi
    public static void konfirmasiPembayaranAdmin() {
        User user = appState.getCurrentUser();
        if (!(user instanceof Penghuni p)) return;

        penghuniService.konfirmasiPembayaran(p);

    }

    // Admin menolak pembayaran → status kembali ke BELUM_BAYAR
    public static void tolakPembayaranAdmin() {
        User user = appState.getCurrentUser();
        if (!(user instanceof Penghuni p)) return;

        penghuniService.tolakPembayaran(p);

    }

    // Reset semua status pembayaran untuk siklus baru
    public static void resetPembayaran() {
        User user = appState.getCurrentUser();
        if (!(user instanceof Penghuni p)) return;

        penghuniService.resetPembayaran(p);

    }

    // Tanggal terkait operasional kos
    public static LocalDate getTanggalKirimBukti() {
        Penghuni p = getCurrentPenghuni();
        return (p != null) ? penghuniService.getTanggalKirimBukti(p) : null;
    }

    public static LocalDate getTanggalKonfirmasiAdmin() {
        Penghuni p = getCurrentPenghuni();
        return (p != null) ? penghuniService.getTanggalKonfirmasi(p) : null;
    }

    // --- BUKTI PEMBAYARAN ---
    public static String getBuktiPembayaranPath() {
        Penghuni p = getCurrentPenghuni();
        return (p != null) ? penghuniService.getBuktiPath(p) : null;
    }

    public static void setBuktiPembayaranPath(String path) {
        Penghuni p = getCurrentPenghuni();
        if (p != null) {
            p.setBuktiPembayaranPath(path);
        }
    }

    // Jatuh tempo berikutnya
    // Perlu dipindahkan karena mengandung logika
    public static LocalDate getJatuhTempoBerikutnya() {
        Penghuni p = getCurrentPenghuni();
        if (p != null && p.getTanggalKonfirmasiAdmin() != null) {
            return p.getTanggalKonfirmasiAdmin().plusDays(30);
        }
        return null;
    }

    // Backward compatibility
    public static boolean getIsSudahBayar() {
        return "LUNAS".equals(getStatusPembayaran());
    }

    public static void setIsSudahBayar(boolean status) {
        Penghuni p = getCurrentPenghuni();
        if (p != null) {
            if (status) {
                konfirmasiPembayaranAdmin();
            } else {
                resetPembayaran();
            }
        }
    }

    // --- DATA PENGHUNI ---
    public static String getNamaLengkapPenghuni() {
        Penghuni p = getCurrentPenghuni();
        return (p != null) ? p.getNamaLengkap() : "";
    }

    public static String getNomorKamarPenghuni() {
        Penghuni p = getCurrentPenghuni();
        return (p != null) ? p.getNomorKamar() : "-";
    }

    public static boolean getStatusAktif() {
        Penghuni p = getCurrentPenghuni();
        return p != null && p.isStatusAktif();
    }

    public static int getTanggalSiklusKost() {
        Penghuni p = getCurrentPenghuni();
        return (p != null) ? p.getTanggalSiklusKost() : 1;
    }

    public static ArrayList<String> getListKeluhan() {
        return new ArrayList<>(ComplaintService.getInstance().getKeluhanList());
    }

    public static void tambahKeluhan(String keluhan) {
        ComplaintService.getInstance().tambahKeluhan(keluhan);
    }

    public static void backToLogin() {
        appState.resetSession();
        showLogin();
    }

    public static void goToFormulir() { showFormulir(); }

    private static void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        try {
            JPAUtil.getEntityManager().close();
            Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort",
                    "8082"
            ).start();
            System.out.println("Hibernate beserta H2 berhasil terinisialisasi:");
            System.out.println("http://localhost:8082");

        } catch (Exception e) {
            e.printStackTrace();
        }

        launch(args); }
}