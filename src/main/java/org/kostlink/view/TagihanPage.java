package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TagihanPage extends BasePage {

    private String noKamar;
    private boolean isSudahBayar;
    private Button btnBayarSekarang;

    // Konstruktor menerima nomor kamar dan status bayar dari Main
    public TagihanPage(String noKamar, boolean isSudahBayar) {
        this.noKamar = noKamar;
        this.isSudahBayar = isSudahBayar;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear(); // Bersihkan layout lama
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setPadding(new Insets(10, 0, 0, 0));
        this.layout.setStyle("-fx-background-color: transparent;"); // Supaya menyatu dengan content area dashboard

        Label lblTitle = new Label("Halaman Riwayat Tagihan & Invoice");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-text-fill: #2D033B;");

        VBox boxStatus = new VBox(15);
        boxStatus.setPadding(new Insets(25));
        boxStatus.setMaxWidth(700);
        boxStatus.setStyle("-fx-background-radius: 12; -fx-border-radius: 12;");

        if (!isSudahBayar) {
            // Jika BELUM BAYAR
            boxStatus.setStyle("-fx-background-color: #FFF5F5; -fx-border-color: #FFCCCC; -fx-background-radius: 12;");

            Label lblKet = new Label("📋 Tagihan Kamar " + noKamar + " | Periode Bulan Ini");
            lblKet.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            Label lblStatus = new Label("STATUS: BELUM LUNAS");
            lblStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 14;");

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            btnBayarSekarang = new Button("Bayar Sekarang 💰");
            btnBayarSekarang.setStyle("-fx-background-color: #FF4B4B; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20; -fx-background-radius: 8;");

            boxStatus.getChildren().addAll(lblKet, lblStatus, btnBayarSekarang);
        } else {
            // Jika SUDAH BAYAR
            boxStatus.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: #BBF7D0; -fx-background-radius: 12;");

            Label lblKet = new Label("📋 Tagihan Kamar " + noKamar + " | Periode Bulan Ini");
            lblKet.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            Label lblStatus = new Label("STATUS: LUNAS / TERVERIFIKASI");
            lblStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 14;");

            boxStatus.getChildren().addAll(lblKet, lblStatus);
        }

        this.layout.getChildren().addAll(lblTitle, new Separator(), boxStatus);
    }

    // --- METHOD POP-UP KUSTOM PINDAHAN DARI CONTROLLER ---
    // Menggunakan Runnable agar logika klik tombol "SAYA TELAH MEMBAYAR" bisa diatur di Controller
    public static void tampilkanPopUpPembayaran(Runnable onKonfirmasiLunas) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // Mengunci layar utama saat pop-up aktif
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

        // Ketika tombol konfirmasi di pop-up diklik
        btnKonfirmasiBayar.setOnAction(event -> {
            window.close(); // Tutup jendela pop-up pembayaran
            if (onKonfirmasiLunas != null) {
                onKonfirmasiLunas.run(); // Menjalankan kelanjutan logika di Controller (Ubah data & refresh)
            }
        });

        popUpLayout.getChildren().addAll(lblTitle, lblDetail, btnKonfirmasiBayar);
        Scene scene = new Scene(popUpLayout);
        window.setScene(scene);
        window.showAndWait();
    }

    // Getter untuk tombol aksi
    public Button getBtnBayarSekarang() {
        return btnBayarSekarang;
    }
}