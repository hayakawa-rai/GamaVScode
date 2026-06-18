package scene;

import Items.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Story1 {
    private final GameController controller;

    public Story1(GameController controller) {
        this.controller = controller;
    }

    public Scene createScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #111111;"); // 1ページ目は暗い黒

        Label label = new Label("【ストーリー 1/3】");
        label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        Button skipBtn = new Button("⏩ スキップ");
        skipBtn.setOnAction(e -> controller.skipToGame()); // コントローラーにスキップを命じる

        layout.getChildren().addAll(label, skipBtn);

        Scene scene = new Scene(layout, 800, 600);
        scene.setOnKeyPressed(e -> controller.advanceStory()); // キーで次ページへ
        return scene;
    }
}