package org.kostlink.core;

import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public abstract class BasePage {
    protected VBox layout; // Protected agar bisa diakses kelas anak

    public BasePage() {
        this.layout = new VBox(20);
        this.layout.setAlignment(Pos.CENTER);
        // Warna ungu gelap sesuai desain Canva LORGI
        this.layout.setStyle("-fx-background-color: #2D033B;");

        setupComponents();
    }

    // Method abstrak yang wajib diisi oleh LoginPage, DashboardPage, dll.
    public abstract void setupComponents();

    public VBox getLayout() {
        return layout;
    }
}