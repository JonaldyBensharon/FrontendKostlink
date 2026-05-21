package org.kostlink.view;

import org.kostlink.Main;
import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TagihanPage extends BasePage {

    private String noKamar;
    private boolean isSudahBayar;
    private Button btnBayarSekarang;
    private Button btnDetailBayar;

    public TagihanPage(String noKamar, boolean isSudahBayar) {
        this.noKamar = noKamar;
        this.isSudahBayar = isSudahBayar;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear();
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setPadding(new Insets(10, 0, 0, 0));
        this.layout.setStyle("-fx-background-color: transparent;");

        Label lblTitle = new Label("Halaman Riwayat Tagihan & Invoice");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-text-fill: #2D033B;");

        VBox boxStatus = new VBox(15);
        boxStatus.setPadding(new Insets(25));
        boxStatus.setMaxWidth(700);
        boxStatus.setStyle("-fx-background-radius: 12; -fx-border-radius: 12;");

        if (!isSudahBayar) {
            // Jika BELUM BAYAR (Tampilan Merah)
            boxStatus.setStyle("-fx-background-color: #FFF5F5; -fx-border-color: #FFCCCC; -fx-background-radius: 12;");

            Label lblKet = new Label("📋 Tagihan Kamar " + noKamar + " | Periode Bulan Ini");
            lblKet.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            Label lblStatus = new Label("STATUS: BELUM LUNAS");
            lblStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 14;");

            btnBayarSekarang = new Button("Bayar Sekarang 💰");
            btnBayarSekarang.setStyle("-fx-background-color: #FF4B4B; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20; -fx-background-radius: 8;");

            boxStatus.getChildren().addAll(lblKet, lblStatus, btnBayarSekarang);
        } else {
            // Jika SUDAH BAYAR (Tampilan Hijau dengan Tombol Detail di Sisi Kanan)
            boxStatus.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: #BBF7D0; -fx-background-radius: 12;");

            HBox susunanHorisontal = new HBox();
            susunanHorisontal.setAlignment(Pos.CENTER_LEFT);

            VBox textSisiKiri = new VBox(5);
            Label lblKet = new Label("📋 Tagihan Kamar " + noKamar + " | Periode Bulan Ini");
            lblKet.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            Label lblStatus = new Label("STATUS: LUNAS / TERVERIFIKASI");
            lblStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 14;");
            textSisiKiri.getChildren().addAll(lblKet, lblStatus);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // --- TOMBOL DETAIL PEMBAYARAN ---
            btnDetailBayar = new Button("Detail Pembayaran 🔍");
            btnDetailBayar.setStyle("-fx-background-color: #2D033B; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 6;");

            susunanHorisontal.getChildren().addAll(textSisiKiri, spacer, btnDetailBayar);
            boxStatus.getChildren().add(susunanHorisontal);
        }

        this.layout.getChildren().addAll(lblTitle, new Separator(), boxStatus);
    }

    // --- METHOD POP-UP PEMBAYARAN DIGITAL ---
    public static void tampilkanPopUpPembayaran(Runnable onKonfirmasiLunas) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("KOSTLINK - Pembayaran Digital");
        window.setMinWidth(400);
        window.setMinHeight(300);

        VBox popUpLayout = new VBox(15);
        popUpLayout.setPadding(new Insets(30));
        popUpLayout.setAlignment(Pos.CENTER);
        popUpLayout.setStyle("-fx-background-color: white;");

        Label lblTitle = new Label("Transfer Pembayaran Kost");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: #2D033B;");

        Label lblDetail = new Label(
                "Silakan lakukan transfer ke nomor VA berikut:\n\n" +
                        "BANK MANDIRI VIRTUAL ACCOUNT\n" +
                        "👉  8830-1234-5678-9101\n\n" +
                        "Nominal: Rp 1.200.000"
        );
        lblDetail.setStyle("-fx-text-alignment: center; -fx-font-size: 14;");

        Button btnKonfirmasiBayar = new Button("SAYA TELAH MEMBAYAR ✅");
        btnKonfirmasiBayar.setStyle("-fx-background-color: #2D033B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");

        btnKonfirmasiBayar.setOnAction(event -> {
            window.close();
            if (onKonfirmasiLunas != null) {
                onKonfirmasiLunas.run();
            }
        });

        popUpLayout.getChildren().addAll(lblTitle, lblDetail, btnKonfirmasiBayar);
        Scene scene = new Scene(popUpLayout);
        window.setScene(scene);
        window.showAndWait();
    }

    // --- METHOD POP-UP JENDELA DETAIL INVOICE BARU
    public static void tampilkanPopUpDetailInvoice(String nomorKamar) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("KOSTLINK - Detail Transaksi");
        window.setMinWidth(420);

        VBox rootLayout = new VBox(15);
        rootLayout.setPadding(new Insets(25));
        rootLayout.setStyle("-fx-background-color: white;");

        Label lblHeader = new Label("RINCIAN DETAIL INVOICE");
        lblHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #2D033B;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);

        // Ambil tanggal dan jam saat ini secara real-time
        String tanggalValidasi = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String jamValidasi = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        grid.add(new Label("Nomor Ruangan"), 0, 0);
        grid.add(new Label(": Kamar " + nomorKamar), 1, 0);

        grid.add(new Label("Total Nominal"), 0, 1);
        grid.add(new Label(": Rp 1.200.000"), 1, 1);

        grid.add(new Label("Metode Bayar"), 0, 2);
        grid.add(new Label(": Transfer Virtual Account (Mandiri)"), 1, 2);

        grid.add(new Label("Waktu Validasi"), 0, 3);
        Label lblTgl = new Label(": " + tanggalValidasi + " - " + jamValidasi + " WIB");
        lblTgl.setStyle("-fx-font-weight: bold; -fx-text-fill: #16A34A;");
        grid.add(lblTgl, 1, 3);

        grid.add(new Label("Kode Transaksi"), 0, 4);
        grid.add(new Label(": KST-VA-" + (System.currentTimeMillis() / 100000)), 1, 4);

        Button btnTutup = new Button("Tutup Jendela");
        btnTutup.setStyle("-fx-background-color: #FF4B4B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 5; -fx-cursor: hand;");
        btnTutup.setOnAction(e -> window.close());

        HBox areaTombol = new HBox();
        areaTombol.setAlignment(Pos.CENTER_RIGHT);
        areaTombol.setPadding(new Insets(10, 0, 0, 0));
        areaTombol.getChildren().add(btnTutup);

        rootLayout.getChildren().addAll(lblHeader, new Separator(), grid, new Separator(), areaTombol);

        Scene scene = new Scene(rootLayout);
        window.setScene(scene);
        window.showAndWait();
    }

    // --- GETTERS ---
    public Button getBtnBayarSekarang() {
        return btnBayarSekarang;
    }
    public Button getBtnDetailBayar() {
        return btnDetailBayar;
    }
}