package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminDashboardPage extends BasePage {

    private String adminName;
    private Label lblAdminProfile;
    private Button btnLogout;

    // Tombol Sidebar Khusus Admin
    private Button btnDataPenghuni, btnValidasiBayar, btnKeluhanPenghuni;
    private Button currentActiveBtn;

    // Area Konten Utama yang bakal berubah-ubah
    private VBox contentArea;

    public AdminDashboardPage(String adminName) {
        this.adminName = adminName;
        setupComponents();
    }

    @Override
    public void setupComponents() {
        this.layout.getChildren().clear();
        this.layout.setAlignment(Pos.TOP_LEFT);

        // Kita pakai warna Navy Gelap (#1E1B4B) supaya beda sama tampilan penghuni
        this.layout.setStyle("-fx-background-color: #1E1B4B;");

        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setPrefSize(2000, 2000);

        // --- SIDEBAR ADMIN ---
        VBox sidebar = new VBox(25);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);
        sidebar.setPrefHeight(Double.MAX_VALUE);
        sidebar.setPadding(new Insets(40, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: #1E1B4B;");

        Label lblLogo = new Label("KOSTLINK ADMIN");
        lblLogo.setTextFill(Color.WHITE);
        lblLogo.setFont(Font.font("System", FontWeight.BOLD, 22));

        VBox menuBox = new VBox(10);
        btnDataPenghuni = createMenuButton("👥  Data Penghuni");
        btnValidasiBayar = createMenuButton("✅  Validasi Bayar");
        btnKeluhanPenghuni = createMenuButton("📩  Kotak Keluhan");

        // Set Data Penghuni as initially active
        currentActiveBtn = btnDataPenghuni;
        setActiveButton(btnDataPenghuni);

        menuBox.getChildren().addAll(btnDataPenghuni, btnValidasiBayar, btnKeluhanPenghuni);
        sidebar.getChildren().addAll(lblLogo, new Separator(), menuBox);

        // --- SISI KANAN (KONTEN) ---
        VBox rightSide = new VBox();
        HBox.setHgrow(rightSide, Priority.ALWAYS);
        rightSide.setStyle("-fx-background-color: #F8F9FA;");

        // Top Bar Admin
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15, 40, 15, 40));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #EEEEEE; -fx-border-width: 0 0 1 0;");

        lblAdminProfile = new Label("Ibu Kost (" + adminName + ") 👑");
        lblAdminProfile.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblAdminProfile.setStyle("-fx-text-fill: #1E1B4B;");

        btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #FF4B4B; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 8;");
        topBar.getChildren().addAll(lblAdminProfile, btnLogout);

        // Content Area Utama (dibungkus ScrollPane agar bisa scroll)
        contentArea = new VBox(30);
        contentArea.setPadding(new Insets(40));

        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: #F8F9FA; -fx-background-color: #F8F9FA; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        rightSide.getChildren().addAll(topBar, scrollPane);
        mainContainer.getChildren().addAll(sidebar, rightSide);
        this.layout.getChildren().setAll(mainContainer);
    }

    private static final String MENU_BASE_STYLE = "-fx-background-color: transparent; -fx-text-fill: #C7D2FE; -fx-font-size: 14; -fx-cursor: hand; -fx-background-radius: 8;";
    private static final String MENU_HOVER_STYLE = "-fx-background-color: #312E81; -fx-text-fill: white; -fx-font-size: 14; -fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String MENU_ACTIVE_STYLE = "-fx-background-color: #312E81; -fx-text-fill: white; -fx-font-size: 14; -fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #818CF8; -fx-border-radius: 8; -fx-border-width: 0 0 0 3;";

    // Fungsi pembantu buat bikin tombol menu sidebar yang cantik
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 15, 12, 15));
        btn.setStyle(MENU_BASE_STYLE);
        btn.setOnMouseEntered(e -> { if (btn != currentActiveBtn) btn.setStyle(MENU_HOVER_STYLE); });
        btn.setOnMouseExited(e -> { if (btn != currentActiveBtn) btn.setStyle(MENU_BASE_STYLE); });
        return btn;
    }

    public void setActiveButton(Button btn) {
        if (currentActiveBtn != null && currentActiveBtn != btn) {
            currentActiveBtn.setStyle(MENU_BASE_STYLE);
        }
        currentActiveBtn = btn;
        btn.setStyle(MENU_ACTIVE_STYLE);
    }

    // --- GETTERS (Supaya bisa dipakai Controller) ---
    public Button getBtnDataPenghuni() { return btnDataPenghuni; }
    public Button getBtnValidasiBayar() { return btnValidasiBayar; }
    public Button getBtnKeluhanPenghuni() { return btnKeluhanPenghuni; }
    public Button getBtnLogout() { return btnLogout; }
    public VBox getContentArea() { return contentArea; }
}