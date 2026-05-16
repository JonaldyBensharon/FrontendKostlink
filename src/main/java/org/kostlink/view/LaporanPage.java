package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LaporanPage extends BasePage {
    private Button btnKirimLaporan;
    private TextArea txtKeluhan;

    public LaporanPage() {
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear();
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setPadding(new Insets(10, 0, 0, 0));
        this.layout.setStyle("-fx-background-color: transparent;");

        Label lblTitle = new Label("⚠️ Laporan / Keluhan Fasilitas");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-text-fill: #2D033B;");

        VBox boxForm = new VBox(15);
        boxForm.setPadding(new Insets(25));
        boxForm.setMaxWidth(700);
        boxForm.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0;");

        Label lblKet = new Label("Ada masalah di kamar Anda? Laporkan ke pengelola:");
        lblKet.setStyle("-fx-font-size: 14;");

        txtKeluhan = new TextArea();
        txtKeluhan.setPromptText("Tuliskan detail keluhan Anda di sini... (contoh: Lampu kamar mandi mati)");
        txtKeluhan.setPrefHeight(150);

        btnKirimLaporan = new Button("Kirim Laporan Keluhan");
        btnKirimLaporan.setStyle("-fx-background-color: #2D033B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");

        boxForm.getChildren().addAll(lblKet, txtKeluhan, btnKirimLaporan);
        this.layout.getChildren().addAll(lblTitle, new Separator(), boxForm);
    }

    public Button getBtnKirimLaporan() { return btnKirimLaporan; }
    public TextArea getTxtKeluhan() { return txtKeluhan; }
}