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

    // Penambahan UserService untuk persiapan backend
    private static final UserService userService =
            UserService.getInstance();

    // Penambahan PenghuniService
    private static final PenghuniService penghuniService =
            PenghuniService.getInstance();

    private static Penghuni getPenghuni() {
        return appState.getCurrentPenghuni();
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

                if (user instanceof Penghuni) {
                    appState.setCurrentPenghuni((Penghuni) user);
                }

                user.bukaDashboard();
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
            if (!formPage.getNamaLengkap().isEmpty()
                    && appState.getCurrentPenghuni() != null) {

                Penghuni penghuni = appState.getCurrentPenghuni();

                String noKamar = formPage.getNoKamar() == null ? "" : formPage.getNoKamar();

                penghuniService.lengkapiData(
                        penghuni,
                        formPage.getNamaLengkap(),
                        noKamar
                );

                showDashboard();
            }
        });
        setRoot(formPage.getLayout(), "KOSTLINK - Lengkapi Data");
    }

    public static void showHomePenghuni() {
        Penghuni penghuni = appState.getCurrentPenghuni();
        User user = appState.getCurrentUser();

        if (penghuni != null && user != null) {
            HomePenghuniPage profilePage = new HomePenghuniPage(
                    penghuni.getNamaLengkap(),
                    user.getUsername(),
                    penghuni.getNomorKamar()
            );

            profilePage.getBtnBack().setOnAction(e -> showDashboard());
            setRoot(profilePage.getLayout(), "KOSTLINK - Profil Penghuni");
        }
    }

    // =========================================================================
    // JEMBATAN GETTER & SETTER GLOBAL (Penyelamat Semua Halaman View)
    // =========================================================================
    public static void showDashboard() {
        if (appState.getCurrentUser() != null) {
            appState.getCurrentUser().bukaDashboard();
        }
    }

    // --- STATUS PEMBAYARAN 3 TAHAP ---
    public static String getStatusPembayaran() {
        return penghuniService.getStatusPembayaran(getPenghuni());
    }

    // Temporary bridge for legacy UI compatibility
    public static void setStatusPembayaran(String status) {
        if (getPenghuni() != null) {
            getPenghuni().setStatusPembayaran(status);
        }
    }

    // User mengirim bukti pembayaran → status jadi MENUNGGU_VERIFIKASI
    public static void kirimBuktiPembayaran(String buktiPath) {
        penghuniService.kirimBukti(getPenghuni(), buktiPath);
    }

    // Overload backward compatibility
    public static void kirimBuktiPembayaran() {
        kirimBuktiPembayaran(null);
    }

    // Admin mengonfirmasi pembayaran → status jadi LUNAS + catat tanggal konfirmasi
    public static void konfirmasiPembayaranAdmin() {
        penghuniService.konfirmasiPembayaran(getPenghuni());
    }

    // Admin menolak pembayaran → status kembali ke BELUM_BAYAR
    public static void tolakPembayaranAdmin() {
        penghuniService.tolakPembayaran(getPenghuni());
    }

    // Reset semua status pembayaran (untuk siklus baru)
    public static void resetPembayaran() {
        penghuniService.resetPembayaran(getPenghuni());
    }

    // --- TANGGAL-TANGGAL ---
    public static LocalDate getTanggalKirimBukti() {
        return penghuniService.getTanggalKirimBukti(getPenghuni());
    }

    public static LocalDate getTanggalKonfirmasiAdmin() {
        return penghuniService.getTanggalKonfirmasi(getPenghuni());
    }

    // --- BUKTI PEMBAYARAN ---
    public static String getBuktiPembayaranPath() {
        return penghuniService.getBuktiPath(getPenghuni());
    }

    public static void setBuktiPembayaranPath(String path) {
        if (getPenghuni() != null) {
            getPenghuni().setBuktiPembayaranPath(path);
        }
    }

    // Jatuh tempo berikutnya = tanggal konfirmasi admin + 30 hari
    // Akan dipindahkan karena mengandung logika
    public static LocalDate getJatuhTempoBerikutnya() {
        if (getPenghuni() != null && getPenghuni().getTanggalKonfirmasiAdmin() != null) {
            return getPenghuni().getTanggalKonfirmasiAdmin().plusDays(30);
        }
        return null;
    }

    // Backward compatibility
    public static boolean getIsSudahBayar() {
        return "LUNAS".equals(getStatusPembayaran());
    }
    public static void setIsSudahBayar(boolean status) {
        if (getPenghuni() != null) {
            if (status) {
                konfirmasiPembayaranAdmin();
            } else {
                resetPembayaran();
            }
        }
    }

    // --- DATA PENGHUNI ---
    public static String getNamaLengkapPenghuni() {
        return getPenghuni() != null ? getPenghuni().getNamaLengkap() : "";
    }
    public static String getNomorKamarPenghuni() {
        return getPenghuni() != null ? getPenghuni().getNomorKamar() : "-";
    }
    public static boolean getStatusAktif() {
        return getPenghuni() != null && getPenghuni().isStatusAktif();
    }
    public static int getTanggalSiklusKost() {
        return getPenghuni() != null ? getPenghuni().getTanggalSiklusKost() : 1;
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