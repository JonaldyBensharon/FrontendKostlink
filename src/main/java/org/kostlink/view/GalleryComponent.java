package org.kostlink.view;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class GalleryComponent {

    public static VBox createPhotoGallerySection() {
        VBox sectionContainer = new VBox(35); // Jarak vertikal longgar antara judul dan grid gambar
        sectionContainer.setAlignment(Pos.CENTER); // Menjaga simetris kontainer di tengah area putih
        // Berikan padding kanan-kiri yang tipis (10) agar gambar bisa merenggang sampai ke dekat ujung background putih
        sectionContainer.setPadding(new Insets(40, 10, 40, 10));

        // ===== 1. JUDUL DI TENGAH DENGAN ANIMASI DENYUT CAHAYA =====
        Label lblTitle = new Label("\uD83C\uDFE1 SNAPSHOT KENYAMANAN KOSTLINK \uD83C\uDFE1");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 26)); // Font judul dibuat lebih besar dan mantap
        lblTitle.setTextFill(Color.web("#2D033B"));
        lblTitle.setAlignment(Pos.CENTER);

        // Efek Animasi Font Glow
        Glow glowEffect = new Glow(0.0);
        lblTitle.setEffect(glowEffect);

        Timeline titleAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glowEffect.levelProperty(), 0.1)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(glowEffect.levelProperty(), 0.6)),
                new KeyFrame(Duration.seconds(3.0), new KeyValue(glowEffect.levelProperty(), 0.1))
        );
        titleAnimation.setCycleCount(Animation.INDEFINITE);
        titleAnimation.play();


        // ===== 2. GRID FOTO MAKSIMAL MELEBAR (MEMENUHI BACKGROUND PUTIH) =====
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        // Jarak horizontal (Hgap) dinaikkan drastis ke 65 agar foto merenggang maksimal ke kanan & kiri
        grid.setHgap(65);
        // Jarak vertikal (Vgap) disesuaikan menjadi 40 agar seimbang
        grid.setVgap(40);

        // Array path gambar
        String[] imagePaths = {
                "/assets/kamar_1.jpg",
                "/assets/wastafel_1.jpg",
                "/assets/toilet_1.jpg",
                "/assets/toilet_2.jpg",
                "/assets/wastafel_2.jpg",
                "/assets/kamar_2.jpg"
        };

        // UKURAN BARU YANG LEBIH MELEBAR DAN PROPORSIONAL
        double targetWidth = 360;  // Lebar gambar dinaikkan ke 360 agar memenuhi ruang
        double targetHeight = 200; // Tinggi gambar dinaikkan ke 200 (Rasio bioskop yang cinematic)
        double cornerRadius = 24;  // Kelengkungan sudut rounded corner gambar

        int index = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                if (index >= imagePaths.length) break;

                try {
                    // Load Gambar
                    Image img = new Image(GalleryComponent.class.getResourceAsStream(imagePaths[index]));
                    ImageView imageView = new ImageView(img);

                    // Set Ukuran Baru yang Besar dan Lebar
                    imageView.setFitWidth(targetWidth);
                    imageView.setFitHeight(targetHeight);
                    imageView.setPreserveRatio(false);

                    // Membuat sudut melengkung halus
                    Rectangle clip = new Rectangle(targetWidth, targetHeight);
                    clip.setArcWidth(cornerRadius);
                    clip.setArcHeight(cornerRadius);
                    imageView.setClip(clip);

                    imageView.setStyle("-fx-cursor: hand;");

                    // Animasi Smooth Zoom saat hover (membesar halus)
                    ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), imageView);
                    scaleUp.setToX(1.04); // Membesar sedikit saja karena ukuran gambar dasarnya sudah besar
                    scaleUp.setToY(1.04);

                    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), imageView);
                    scaleDown.setToX(1.0);
                    scaleDown.setToY(1.0);

                    imageView.setOnMouseEntered(e -> {
                        scaleDown.stop();
                        scaleUp.playFromStart();
                    });

                    imageView.setOnMouseExited(e -> {
                        scaleUp.stop();
                        scaleDown.playFromStart();
                    });

                    grid.add(imageView, col, row);
                    index++;

                } catch (Exception e) {
                    // Sistem cadangan Box Abu-abu berukuran besar jika gambar belum dimasukkan
                    Rectangle placeholder = new Rectangle(targetWidth, targetHeight);
                    placeholder.setFill(Color.LIGHTGRAY);
                    placeholder.setArcWidth(cornerRadius);
                    placeholder.setArcHeight(cornerRadius);
                    placeholder.setStyle("-fx-cursor: hand;");

                    ScaleTransition scaleUpP = new ScaleTransition(Duration.millis(200), placeholder);
                    scaleUpP.setToX(1.04); scaleUpP.setToY(1.04);
                    ScaleTransition scaleDownP = new ScaleTransition(Duration.millis(200), placeholder);
                    scaleDownP.setToX(1.0); scaleDownP.setToY(1.0);

                    placeholder.setOnMouseEntered(ev -> { scaleDownP.stop(); scaleUpP.playFromStart(); });
                    placeholder.setOnMouseExited(ev -> { scaleUpP.stop(); scaleDownP.playFromStart(); });

                    grid.add(placeholder, col, row);
                    index++;
                }
            }
        }

        // Satukan Judul dan Grid ke kontainer utama
        sectionContainer.getChildren().addAll(lblTitle, grid);
        return sectionContainer;
    }
}