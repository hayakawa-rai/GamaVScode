package scene;

import Item.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Story2 {
    private final GameController controller;

    public Story2(GameController controller) {
        this.controller = controller;
    }

    public Scene createScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #111133;"); // 2ページ目は少し青みがかった背景に

        Label label = new Label("【ストーリー 2/3】");
        label.setStyle("-fx-text-fill: #ff9999; -fx-font-size: 18px;"); // 文字色を不穏な赤に

        Button skipBtn = new Button("⏩ スキップ");
        skipBtn.setOnAction(e -> controller.skipToGame());

        layout.getChildren().addAll(label, skipBtn);

        Scene scene = new Scene(layout, 800, 600);
        scene.setOnKeyPressed(e -> controller.advanceStory());
        return scene;
    }
}