package org.kostlink.view;

import org.kostlink.Main;
import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TagihanPage extends BasePage {

    private String noKamar;
    private String statusPembayaran;
    private Button btnBayarSekarang;
    private Button btnDetailBayar;

    public TagihanPage(String noKamar, String statusPembayaran) {
        this.noKamar = noKamar;
        this.statusPembayaran = statusPembayaran;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear();
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setPadding(new Insets(10, 0, 0, 0));
        this.layout.setStyle("-fx-background-color: transparent;");

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Label lblIconHeader = new Label("💳");
        lblIconHeader.setFont(Font.font(26));
        Label lblTitle = new Label("Riwayat Tagihan & Invoice");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-text-fill: #1F2937;");
        headerBox.getChildren().addAll(lblIconHeader, lblTitle);

        // =========================================================================
        // HITUNG JATUH TEMPO
        // =========================================================================
        LocalDate hariIni = LocalDate.now();
        LocalDate jatuhTempoBerikutnya = Main.getJatuhTempoBerikutnya();
        LocalDate jatuhTempoTampil;

        if ("LUNAS".equals(statusPembayaran) && jatuhTempoBerikutnya != null) {
            jatuhTempoTampil = jatuhTempoBerikutnya;
        } else {
            int tanggalSiklus = Main.getTanggalSiklusKost();
            int maxHari = hariIni.lengthOfMonth();
            int tanggalSiklusValid = Math.min(tanggalSiklus, maxHari);
            jatuhTempoTampil = LocalDate.of(hariIni.getYear(), hariIni.getMonth(), tanggalSiklusValid);
            if (jatuhTempoTampil.isBefore(hariIni)) {
                jatuhTempoTampil = jatuhTempoTampil.plusMonths(1);
            }
        }

        // =========================================================================
        // NOTIFIKASI JATUH TEMPO (hanya jika BELUM_BAYAR)
        // =========================================================================
        VBox notifikasiBox = null;
        if ("BELUM_BAYAR".equals(statusPembayaran)) {
            long selisihHari = ChronoUnit.DAYS.between(hariIni, jatuhTempoTampil);
            notifikasiBox = new VBox(8);
            notifikasiBox.setMaxWidth(750);
            notifikasiBox.setPadding(new Insets(16, 20, 16, 20));

            String bgColor, borderColor, textColor, pesanNotifikasi, iconNotif;
            if (selisihHari < 0) {
                bgColor = "#FEF2F2"; borderColor = "#FECACA"; textColor = "#991B1B"; iconNotif = "🚨";
                pesanNotifikasi = "Jatuh tempo terlewat " + Math.abs(selisihHari) + " hari! Segera lakukan pembayaran.";
            } else if (selisihHari == 0) {
                bgColor = "#FEF2F2"; borderColor = "#FCA5A5"; textColor = "#B91C1C"; iconNotif = "🔴";
                pesanNotifikasi = "HARI INI batas jatuh tempo pembayaran kost Anda!";
            } else if (selisihHari <= 3) {
                bgColor = "#FFFBEB"; borderColor = "#FDE68A"; textColor = "#92400E"; iconNotif = "⏰";
                pesanNotifikasi = "PENGINGAT: " + selisihHari + " hari lagi jatuh tempo!";
            } else if (selisihHari <= 7) {
                bgColor = "#EFF6FF"; borderColor = "#BFDBFE"; textColor = "#1E40AF"; iconNotif = "📢";
                pesanNotifikasi = "INFO: Jatuh tempo " + selisihHari + " hari lagi (" + jatuhTempoTampil.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + ")";
            } else {
                bgColor = "#F0FDF4"; borderColor = "#BBF7D0"; textColor = "#166534"; iconNotif = "📅";
                pesanNotifikasi = "Jatuh tempo selanjutnya: " + jatuhTempoTampil.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            }

            notifikasiBox.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: " + borderColor + "; -fx-background-radius: 12; -fx-border-radius: 12;");
            
            HBox contentNotif = new HBox(10);
            contentNotif.setAlignment(Pos.CENTER_LEFT);
            Label lblIcon = new Label(iconNotif);
            lblIcon.setFont(Font.font(18));
            Label lblNotif = new Label(pesanNotifikasi);
            lblNotif.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: " + textColor + ";");
            contentNotif.getChildren().addAll(lblIcon, lblNotif);
            notifikasiBox.getChildren().add(contentNotif);
        }

        // =========================================================================
        // BOX STATUS TAGIHAN (3 KONDISI)
        // =========================================================================
        VBox boxStatus = new VBox(20);
        boxStatus.setPadding(new Insets(30));
        boxStatus.setMaxWidth(750);
        boxStatus.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 4);");

        if ("BELUM_BAYAR".equals(statusPembayaran)) {
            boxStatus.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FECACA; -fx-background-radius: 16; -fx-border-radius: 16;");

            HBox headerInv = new HBox(10);
            Label lblKet = new Label("📋 Tagihan Bulan Ini — Kamar " + noKamar);
            lblKet.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #1F2937;");
            headerInv.getChildren().add(lblKet);

            VBox detilTagihan = new VBox(8);
            Label lblStatus = new Label("STATUS: BELUM LUNAS");
            lblStatus.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold; -fx-font-size: 14;");
            
            Label lblTotal = new Label("Total Pembayaran: Rp 1.200.000");
            lblTotal.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

            Label lblJatuhTempo = new Label("Batas Waktu: " + jatuhTempoTampil.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            lblJatuhTempo.setStyle("-fx-font-size: 13; -fx-text-fill: #991B1B;");
            
            detilTagihan.getChildren().addAll(lblStatus, lblTotal, lblJatuhTempo);

            btnBayarSekarang = new Button("Bayar Sekarang 💰");
            String payBase = "-fx-background-color: linear-gradient(to right, #DC2626, #EF4444); -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 12 25; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(220,38,38,0.3), 10, 0, 0, 3);";
            String payHover = "-fx-background-color: linear-gradient(to right, #B91C1C, #DC2626); -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 12 25; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(220,38,38,0.5), 14, 0, 0, 5);";
            btnBayarSekarang.setStyle(payBase);
            btnBayarSekarang.setOnMouseEntered(e -> btnBayarSekarang.setStyle(payHover));
            btnBayarSekarang.setOnMouseExited(e -> btnBayarSekarang.setStyle(payBase));

            boxStatus.getChildren().addAll(headerInv, new Separator(), detilTagihan, btnBayarSekarang);

        } else if ("MENUNGGU_VERIFIKASI".equals(statusPembayaran)) {
            boxStatus.setStyle("-fx-background-color: #FFFBEB; -fx-border-color: #FDE68A; -fx-background-radius: 16; -fx-border-radius: 16;");

            Label lblKet = new Label("📋 Tagihan Bulan Ini — Kamar " + noKamar);
            lblKet.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

            Label lblStatus = new Label("STATUS: MENUNGGU VERIFIKASI ADMIN ⏳");
            lblStatus.setStyle("-fx-text-fill: #D97706; -fx-font-weight: bold; -fx-font-size: 14;");

            LocalDate tglKirim = Main.getTanggalKirimBukti();
            String tglKirimStr = (tglKirim != null) ? tglKirim.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : hariIni.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            
            VBox infoBox = new VBox(6);
            infoBox.setPadding(new Insets(12));
            infoBox.setStyle("-fx-background-color: rgba(217,119,6,0.1); -fx-background-radius: 8;");
            Label lblDetail = new Label("📤 Bukti pembayaran berhasil dikirim pada " + tglKirimStr);
            lblDetail.setStyle("-fx-font-size: 13; -fx-text-fill: #92400E; -fx-font-weight: bold;");
            Label lblInfo = new Label("Pembayaran Anda sedang dalam proses verifikasi oleh Ibu Kost.\nMohon tunggu hingga status berubah menjadi LUNAS.");
            lblInfo.setStyle("-fx-font-size: 12; -fx-text-fill: #92400E;");
            lblInfo.setWrapText(true);
            infoBox.getChildren().addAll(lblDetail, lblInfo);

            boxStatus.getChildren().addAll(lblKet, new Separator(), lblStatus, infoBox);

        } else {
            boxStatus.setStyle("-fx-background-color: #ECFDF5; -fx-border-color: #A7F3D0; -fx-background-radius: 16; -fx-border-radius: 16;");

            HBox headerInv = new HBox(10);
            headerInv.setAlignment(Pos.CENTER_LEFT);
            Label lblKet = new Label("📋 Tagihan Bulan Ini — Kamar " + noKamar);
            lblKet.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #065F46;");
            headerInv.getChildren().add(lblKet);

            HBox mainRow = new HBox();
            mainRow.setAlignment(Pos.CENTER_LEFT);

            VBox textSisiKiri = new VBox(8);
            Label lblStatus = new Label("STATUS: LUNAS / TERVERIFIKASI ✅");
            lblStatus.setStyle("-fx-text-fill: #059669; -fx-font-weight: bold; -fx-font-size: 14;");

            LocalDate tglKonfirmasi = Main.getTanggalKonfirmasiAdmin();
            String tglStr = (tglKonfirmasi != null) ? tglKonfirmasi.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : hariIni.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            Label lblKonfirmasi = new Label("Dikonfirmasi Admin pada: " + tglStr);
            lblKonfirmasi.setStyle("-fx-font-size: 13; -fx-text-fill: #065F46;");

            Label lblInfoBerikutnya = new Label("📅 Tagihan berikutnya: " + jatuhTempoTampil.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            lblInfoBerikutnya.setStyle("-fx-font-size: 13; -fx-text-fill: #1E3A8A; -fx-font-weight: bold;");

            textSisiKiri.getChildren().addAll(lblStatus, lblKonfirmasi, lblInfoBerikutnya);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            btnDetailBayar = new Button("Detail Invoice 🔍");
            String detBase = "-fx-background-color: white; -fx-text-fill: #059669; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20; -fx-background-radius: 8; -fx-border-color: #059669; -fx-border-radius: 8;";
            String detHover = "-fx-background-color: #059669; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20; -fx-background-radius: 8; -fx-border-color: #059669; -fx-border-radius: 8;";
            btnDetailBayar.setStyle(detBase);
            btnDetailBayar.setOnMouseEntered(e -> btnDetailBayar.setStyle(detHover));
            btnDetailBayar.setOnMouseExited(e -> btnDetailBayar.setStyle(detBase));

            mainRow.getChildren().addAll(textSisiKiri, spacer, btnDetailBayar);
            boxStatus.getChildren().addAll(headerInv, new Separator(), mainRow);
        }

        this.layout.getChildren().add(headerBox);
        Separator sep = new Separator();
        sep.setStyle("-fx-padding: 10 0;");
        this.layout.getChildren().add(sep);
        
        if (notifikasiBox != null) {
            this.layout.getChildren().add(notifikasiBox);
            Region spacing = new Region();
            spacing.setPrefHeight(10);
            this.layout.getChildren().add(spacing);
        }
        this.layout.getChildren().add(boxStatus);
    }

    // --- POPUP PEMBAYARAN DENGAN UPLOAD BUKTI ---
    public static void tampilkanPopUpPembayaran(java.util.function.Consumer<String> onKonfirmasiLunas) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("KOSTLINK - Pembayaran Digital");

        VBox popUpLayout = new VBox(18);
        popUpLayout.setPadding(new Insets(30));
        popUpLayout.setAlignment(Pos.CENTER);
        popUpLayout.setStyle("-fx-background-color: white;");

        Label lblIcon = new Label("🏦");
        lblIcon.setFont(Font.font(36));

        Label lblTitle = new Label("Transfer Virtual Account");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: #1F2937;");

        VBox bankBox = new VBox(8);
        bankBox.setAlignment(Pos.CENTER);
        bankBox.setPadding(new Insets(16));
        bankBox.setStyle("-fx-background-color: #F8F9FA; -fx-border-color: #E5E7EB; -fx-background-radius: 12; -fx-border-radius: 12;");
        Label lblBank = new Label("BANK MANDIRI");
        lblBank.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E3A8A; -fx-font-size: 14;");
        Label lblVA = new Label("8830 1234 5678 9101");
        lblVA.setStyle("-fx-font-weight: bold; -fx-font-size: 22; -fx-text-fill: #1F2937;");
        Label lblNominal = new Label("Total Bayar: Rp 1.200.000");
        lblNominal.setStyle("-fx-font-size: 14; -fx-text-fill: #4B5563;");
        bankBox.getChildren().addAll(lblBank, lblVA, lblNominal);

        // === AREA UPLOAD BUKTI PEMBAYARAN ===
        VBox uploadArea = new VBox(10);
        uploadArea.setAlignment(Pos.CENTER);
        uploadArea.setPadding(new Insets(16));
        uploadArea.setStyle("-fx-background-color: #F5F3FF; -fx-border-color: #DDD6FE; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12;");

        Label lblUploadTitle = new Label("📎 Upload Bukti Pembayaran");
        lblUploadTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #5B21B6;");

        Label lblUploadHint = new Label("Format: JPG, PNG (Maks 5MB)");
        lblUploadHint.setStyle("-fx-font-size: 11; -fx-text-fill: #7C3AED;");

        ImageView imgPreview = new ImageView();
        imgPreview.setFitWidth(200);
        imgPreview.setFitHeight(150);
        imgPreview.setPreserveRatio(true);
        imgPreview.setVisible(false);

        Label lblFileName = new Label("");
        lblFileName.setStyle("-fx-font-size: 11; -fx-text-fill: #065F46; -fx-font-weight: bold;");
        lblFileName.setVisible(false);

        final String[] selectedFilePath = {null};

        Button btnPilihFile = new Button("Pilih File 📁");
        String pilihBase = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 8; -fx-cursor: hand;";
        String pilihHover = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 8; -fx-cursor: hand;";
        btnPilihFile.setStyle(pilihBase);
        btnPilihFile.setOnMouseEntered(e -> btnPilihFile.setStyle(pilihHover));
        btnPilihFile.setOnMouseExited(e -> btnPilihFile.setStyle(pilihBase));

        btnPilihFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Pilih Bukti Pembayaran");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
            );
            File file = fileChooser.showOpenDialog(window);
            if (file != null) {
                selectedFilePath[0] = file.getAbsolutePath();
                Image img = new Image(file.toURI().toString(), 200, 150, true, true);
                imgPreview.setImage(img);
                imgPreview.setVisible(true);
                lblFileName.setText("✅ " + file.getName());
                lblFileName.setVisible(true);
                uploadArea.setStyle("-fx-background-color: #ECFDF5; -fx-border-color: #A7F3D0; -fx-border-style: solid; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12;");
            }
        });

        uploadArea.getChildren().addAll(lblUploadTitle, lblUploadHint, btnPilihFile, imgPreview, lblFileName);

        // === TOMBOL KONFIRMASI ===
        Button btnKonfirmasiBayar = new Button("KIRIM BUKTI PEMBAYARAN ✅");
        String btnBase = "-fx-background-color: linear-gradient(to right, #7C3AED, #A855F7); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 10; -fx-cursor: hand;";
        String btnHover = "-fx-background-color: linear-gradient(to right, #6D28D9, #9333EA); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 10; -fx-cursor: hand;";
        btnKonfirmasiBayar.setStyle(btnBase);
        btnKonfirmasiBayar.setOnMouseEntered(e -> btnKonfirmasiBayar.setStyle(btnHover));
        btnKonfirmasiBayar.setOnMouseExited(e -> btnKonfirmasiBayar.setStyle(btnBase));

        btnKonfirmasiBayar.setOnAction(event -> {
            if (selectedFilePath[0] == null) {
                Alert warn = new Alert(Alert.AlertType.WARNING, "Harap upload bukti pembayaran terlebih dahulu!");
                warn.setHeaderText("Bukti Belum Di-upload");
                warn.showAndWait();
                return;
            }
            window.close();
            if (onKonfirmasiLunas != null) onKonfirmasiLunas.accept(selectedFilePath[0]);
        });

        popUpLayout.getChildren().addAll(lblIcon, lblTitle, bankBox, uploadArea, btnKonfirmasiBayar);

        ScrollPane scrollPane = new ScrollPane(popUpLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        window.setScene(new Scene(scrollPane, 480, 620));
        window.showAndWait();
    }

    public static void tampilkanPopUpDetailInvoice(String nomorKamar) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("KOSTLINK - Detail Invoice");

        VBox rootLayout = new VBox(20);
        rootLayout.setPadding(new Insets(30));
        rootLayout.setStyle("-fx-background-color: white;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("🧾");
        icon.setFont(Font.font(24));
        Label lblHeader = new Label("BUKTI PEMBAYARAN RESMI");
        lblHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: #1F2937;");
        header.getChildren().addAll(icon, lblHeader);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #F8F9FA; -fx-background-radius: 10; -fx-border-color: #E5E7EB; -fx-border-radius: 10;");

        LocalDate tglKonfirmasi = Main.getTanggalKonfirmasiAdmin();
        String tanggalValidasi = (tglKonfirmasi != null) ? tglKonfirmasi.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) : LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        
        // Helper untuk styling label
        String lblStyle = "-fx-text-fill: #6B7280; -fx-font-size: 13;";
        String valStyle = "-fx-text-fill: #1F2937; -fx-font-weight: bold; -fx-font-size: 13;";

        Label l1 = new Label("Kamar"); l1.setStyle(lblStyle);
        Label v1 = new Label(nomorKamar); v1.setStyle(valStyle);
        grid.add(l1, 0, 0); grid.add(v1, 1, 0);

        Label l2 = new Label("Total Bayar"); l2.setStyle(lblStyle);
        Label v2 = new Label("Rp 1.200.000"); v2.setStyle(valStyle);
        grid.add(l2, 0, 1); grid.add(v2, 1, 1);

        Label l3 = new Label("Metode"); l3.setStyle(lblStyle);
        Label v3 = new Label("Virtual Account Mandiri"); v3.setStyle(valStyle);
        grid.add(l3, 0, 2); grid.add(v3, 1, 2);

        Label l4 = new Label("Tgl. Lunas"); l4.setStyle(lblStyle);
        Label v4 = new Label(tanggalValidasi); v4.setStyle("-fx-text-fill: #059669; -fx-font-weight: bold; -fx-font-size: 13;");
        grid.add(l4, 0, 3); grid.add(v4, 1, 3);

        Label l5 = new Label("No. Ref"); l5.setStyle(lblStyle);
        Label v5 = new Label("KST-VA-" + (System.currentTimeMillis() / 100000)); v5.setStyle(valStyle);
        grid.add(l5, 0, 4); grid.add(v5, 1, 4);

        Button btnTutup = new Button("Tutup");
        btnTutup.setStyle("-fx-background-color: #E5E7EB; -fx-text-fill: #374151; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 8; -fx-cursor: hand;");
        btnTutup.setOnAction(e -> window.close());

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.getChildren().add(btnTutup);

        rootLayout.getChildren().addAll(header, grid, bottom);
        window.setScene(new Scene(rootLayout, 450, 420));
        window.showAndWait();
    }

    public Button getBtnBayarSekarang() { return btnBayarSekarang; }
    public Button getBtnDetailBayar() { return btnDetailBayar; }
}