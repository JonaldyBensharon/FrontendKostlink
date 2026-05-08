package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomePenghuniPage extends BasePage {

    private String nama, username, noKamar;
    private Button btnBack;

    public HomePenghuniPage(String nama, String username, String noKamar) {
        this.nama = (nama == null || nama.isEmpty()) ? "Penghuni" : nama;
        this.username = username;
        this.noKamar = noKamar;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.setAlignment(Pos.CENTER);
        this.layout.setStyle("-fx-background-color: #F0F2F5;");

        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        container.setMaxWidth(550);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 30, 0, 0, 15);");

        // Header Profil
        Label avatar = new Label(nama.substring(0, 1).toUpperCase());
        avatar.setFont(Font.font("System", FontWeight.BOLD, 50));
        avatar.setTextFill(Color.WHITE);
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(120, 120);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #5D3FD3, #2D033B); -fx-background-radius: 100;");

        Label lblNama = new Label(nama);
        lblNama.setFont(Font.font("System", FontWeight.BOLD, 28));

        // Grid Informasi
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(15);
        infoGrid.setAlignment(Pos.CENTER);

        addInfoRow(infoGrid, 0, "Username", username);
        addInfoRow(infoGrid, 1, "Nomor Kamar", noKamar);
        addInfoRow(infoGrid, 2, "Status Akun", "Terverifikasi");

        btnBack = new Button("KEMBALI KE DASHBOARD");
        btnBack.setPrefWidth(Double.MAX_VALUE);
        btnBack.setStyle("-fx-background-color: #2D033B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 15; -fx-background-radius: 15; -fx-cursor: hand;");

        container.getChildren().addAll(avatar, lblNama, new Separator(), infoGrid, btnBack);
        this.layout.getChildren().add(container);
    }

    private void addInfoRow(GridPane grid, int row, String label, String value) {
        Label lblL = new Label(label + " :");
        lblL.setTextFill(Color.GRAY);
        Label lblV = new Label(value);
        lblV.setStyle("-fx-font-weight: bold;");
        grid.add(lblL, 0, row);
        grid.add(lblV, 1, row);
    }

    public Button getBtnBack() { return btnBack; }
}