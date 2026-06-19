package test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import test.controller.SampleController;
import test.model.SampleModel;
import test.view.SampleView;

public class SampleMainApp extends Application {
	@Override
    public void start(Stage stage) {
        // 起動した瞬間に、ボタン操作なしでコントローラーの遷移処理を呼び出す
        SampleController.switchToStart(stage);
    }
    
    public void starts(Stage stage) {
        SampleModel model = new SampleModel();
        SampleView view = new SampleView(model);

        Group root = new Group();
        int viewWidth = model.getMap()[0].length * SampleModel.TILE_SIZE;
        int viewHeight = model.getMap().length * SampleModel.TILE_SIZE;

        Scene scene = new Scene(root, viewWidth, viewHeight, Color.BLACK);
        Canvas canvas = new Canvas(viewWidth, viewHeight);
        root.getChildren().add(canvas);

        // SampleController を起動
        new SampleController(model, view, canvas, scene);

        stage.setTitle("JavaFX Pacman Stage MVC");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
