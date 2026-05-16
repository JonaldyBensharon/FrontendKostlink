package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class KontrakPage extends BasePage {
    private String noKamar;

    public KontrakPage(String noKamar) {
        this.noKamar = noKamar;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear();
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setPadding(new Insets(10, 0, 0, 0));
        this.layout.setStyle("-fx-background-color: transparent;");

        Label lblTitle = new Label("📄 Kontrak Sewa Kamar");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-text-fill: #2D033B;");

        VBox boxKontrak = new VBox(15);
        boxKontrak.setPadding(new Insets(25));
        boxKontrak.setMaxWidth(700);
        boxKontrak.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0;");

        Label lblKamar = new Label("Nomor Kamar: Kamar " + noKamar);
        lblKamar.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(12);

        grid.add(new Label("Jenis Kontrak:"), 0, 0);
        grid.add(new Label("Bulanan"), 1, 0);
        grid.add(new Label("Harga Sewa:"), 0, 1);
        grid.add(new Label("Rp 1.200.000 / bulan"), 1, 1);
        grid.add(new Label("Tanggal Mulai:"), 0, 2);
        grid.add(new Label("16 Mei 2026"), 1, 2);
        grid.add(new Label("Status Kontrak:"), 0, 3);

        Label lblStatus = new Label("Aktif berjalan");
        lblStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        grid.add(lblStatus, 1, 3);

        boxKontrak.getChildren().addAll(lblKamar, new Separator(), grid);
        this.layout.getChildren().addAll(lblTitle, new Separator(), boxKontrak);
    }
}