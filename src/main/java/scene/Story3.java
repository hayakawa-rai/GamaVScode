package scene;

import Items.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Story3 {
    private final GameController controller;

    public Story3(GameController controller) {
        this.controller = controller;
    }

    public Scene createScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #331111;"); // 3ページ目は緊迫の赤茶色

        Label label = new Label("【ストーリー 3/3】");
        label.setStyle("-fx-text-fill: yellow; -fx-font-size: 18px;");

        Button skipBtn = new Button("⏩ スキップ");
        skipBtn.setOnAction(e -> controller.skipToGame());

        layout.getChildren().addAll(label, skipBtn);

        Scene scene = new Scene(layout, 800, 600);
        scene.setOnKeyPressed(e -> controller.advanceStory()); // ここでキーを押すと4ページ目（存在しない）になり、ゲームが始まる
        return scene;
    }
}