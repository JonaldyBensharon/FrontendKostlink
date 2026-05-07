package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashboardPage extends BasePage {

    private HBox mainContainer;
    private VBox sidebar;
    private VBox contentArea;
    private HBox topBar;
    private Button btnLogout;

    @Override
    public void setupComponents() {
        // Root Layout menggunakan HBox
        mainContainer = new HBox();

        // SIDEBAR (KIRI)
        sidebar = new VBox(20);
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250); // Agar tidak menciut
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: #F8F9FA; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 0 0;");

        Label lblLogo = new Label("LORGI");
        lblLogo.setFont(Font.font("System", FontWeight.BOLD, 24));

        Button btnDash = createMenuButton("🏠 Dashboard");
        Button btnKontrak = createMenuButton("📄 Kontrak Saya");
        Button btnTagihan = createMenuButton("💳 Riwayat Tagihan");
        Button btnKeluhan = createMenuButton("🕒 Laporan Keluhan");
        Button btnSetting = createMenuButton("⚙️ Pengaturan");

        sidebar.getChildren().addAll(lblLogo, new Separator(), btnDash, btnKontrak, btnTagihan, btnKeluhan, btnSetting);

        // BAGIAN KANAN (TopBar + Content)
        VBox rightSide = new VBox();
        HBox.setHgrow(rightSide, Priority.ALWAYS); // PENTING: Agar bagian kanan memenuhi layar

        // TOPBAR
        topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15, 30, 15, 30));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0;");

        Label lblUser = new Label("Zaskiah A.");
        lblUser.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        btnLogout = new Button("🚪");
        btnLogout.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 18;");

        topBar.getChildren().addAll(lblUser, btnLogout);

        // CONTENT AREA
        contentArea = new VBox(25);
        contentArea.setPadding(new Insets(30));
        contentArea.setStyle("-fx-background-color: #FFFFFF;");
        VBox.setVgrow(contentArea, Priority.ALWAYS); // Memenuhi tinggi layar

        Label lblTitle = new Label("Halaman Penghuni");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 24));

        // Row Kartu Statistik
        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
                createStatCard("Kamar Anda", "(Kamar 304)", "🏠"),
                createStatCard("Status Sewa", "Aktif", "💳"),
                createStatCard("Hari Sewa Ke-", "25", "📅")
        );

        Label lblTableTitle = new Label("Tagihan Terakhir & Riwayat Pembayaran");
        lblTableTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        TableView<String> table = new TableView<>();
        table.setPlaceholder(new Label("Belum ada data tagihan"));
        VBox.setVgrow(table, Priority.ALWAYS); // Tabel melebar mengikuti layar

        contentArea.getChildren().addAll(lblTitle, statsRow, lblTableTitle, table);

        // Satukan TopBar dan ContentArea ke RightSide
        rightSide.getChildren().addAll(topBar, contentArea);

        // Satukan Sidebar dan RightSide ke MainContainer
        mainContainer.getChildren().addAll(sidebar, rightSide);

        // Pasangkan ke BasePage layout
        this.layout.getChildren().clear();
        this.layout.getChildren().add(mainContainer);

        // Binding agar ukuran mainContainer mengikuti window secara otomatis
        mainContainer.prefWidthProperty().bind(this.layout.widthProperty());
        mainContainer.prefHeightProperty().bind(this.layout.heightProperty());
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-font-size: 14; -fx-cursor: hand;");
        return btn;
    }

    private VBox createStatCard(String title, String value, String icon) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setMinWidth(200);
        HBox.setHgrow(card, Priority.ALWAYS); // Kartu ikut melebar
        card.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label lblIcon = new Label(icon);
        Label lblT = new Label(title);
        lblT.setTextFill(Color.GRAY);
        Label lblV = new Label(value);
        lblV.setFont(Font.font("System", FontWeight.BOLD, 18));

        card.getChildren().addAll(lblIcon, lblT, lblV);
        return card;
    }

    public Button getBtnLogout() { return btnLogout; }
}