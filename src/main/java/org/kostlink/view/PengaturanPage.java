package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PengaturanPage extends BasePage {
    private Button btnSimpanSesi;

    public PengaturanPage() {
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear();
        this.layout.setAlignment(Pos.TOP_LEFT);
        this.layout.setPadding(new Insets(10, 0, 0, 0));
        this.layout.setStyle("-fx-background-color: transparent;");

        Label lblTitle = new Label("⚙️ Pengaturan Aplikasi");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-text-fill: #2D033B;");

        VBox boxSetting = new VBox(15);
        boxSetting.setPadding(new Insets(25));
        boxSetting.setMaxWidth(700);
        boxSetting.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0;");

        CheckBox chkNotif = new CheckBox("Aktifkan Pengingat WhatsApp otomatis saat jatuh tempo");
        chkNotif.setSelected(true);

        CheckBox chkDarkMode = new CheckBox("Gunakan Mode Gelap (Dark Mode)");

        btnSimpanSesi = new Button("Simpan Pengaturan");
        btnSimpanSesi.setStyle("-fx-background-color: #2D033B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-cursor: hand;");

        boxSetting.getChildren().addAll(chkNotif, chkDarkMode, new Separator(), btnSimpanSesi);
        this.layout.getChildren().addAll(lblTitle, new Separator(), boxSetting);
    }

    public Button getBtnSimpanSesi() { return btnSimpanSesi; }
}