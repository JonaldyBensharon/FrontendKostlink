package org.kostlink.core;

import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public abstract class BasePage {
    protected VBox layout;

    public BasePage() {
        this.layout = new VBox(20);
        this.layout.setAlignment(Pos.CENTER);
        // Tema warna gelap KostLink
        this.layout.setStyle("-fx-background-color: #2D033B;");

        // setupComponents() TIDAK dipanggil di sini agar tidak dobel
    }

    public abstract void setupComponents();

    public VBox getLayout() {
        return layout;
    }
}