package org.kostlink.view;

import org.kostlink.core.BasePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FormPenghuniPage extends BasePage {

    private TextField txtNamaLengkap, txtNoKamar, txtNoHP, txtNIK, txtAlamat;
    private Button btnKonfirmasi, btnBatal;

    @Override
    public void setupComponents() {
        this.layout.setSpacing(15);
        this.layout.setPadding(new Insets(30));
        this.layout.setAlignment(Pos.CENTER);
        this.layout.setStyle("-fx-background-color: white;");

        VBox formCard = new VBox(15);
        formCard.setPadding(new Insets(30));
        formCard.setMaxWidth(500);
        formCard.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-background-radius: 15; -fx-border-radius: 15;");

        Label lblTitle = new Label("Lengkapi Profil & Kamar Anda");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 20));

        // Input Fields (Sesuai Gambar 2)
        txtNIK = new TextField(); txtNIK.setPromptText("NIK");
        txtNamaLengkap = new TextField(); txtNamaLengkap.setPromptText("Nama Lengkap");
        txtNoHP = new TextField(); txtNoHP.setPromptText("Nomor HP");
        txtAlamat = new TextField(); txtAlamat.setPromptText("Alamat");
        txtNoKamar = new TextField(); txtNoKamar.setPromptText("Nomor Kamar");

        btnKonfirmasi = new Button("Simpan & Masuk Dashboard");
        btnKonfirmasi.setMaxWidth(Double.MAX_VALUE);
        btnKonfirmasi.setStyle("-fx-background-color: #2D033B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12;");

        btnBatal = new Button("Batal");
        btnBatal.setTextFill(javafx.scene.paint.Color.RED);

        formCard.getChildren().addAll(lblTitle,
                new Label("NIK"), txtNIK,
                new Label("Nama"), txtNamaLengkap,
                new Label("Nomor HP"), txtNoHP,
                new Label("Alamat"), txtAlamat,
                new Label("Nomor Kamar"), txtNoKamar,
                btnKonfirmasi, btnBatal);

        this.layout.getChildren().setAll(formCard);
    }

    public String getNamaLengkap() { return txtNamaLengkap.getText(); }
    public String getNoKamar() { return txtNoKamar.getText(); }
    public Button getBtnKonfirmasi() { return btnKonfirmasi; }
    public Button getBtnBatal() { return btnBatal; }
}