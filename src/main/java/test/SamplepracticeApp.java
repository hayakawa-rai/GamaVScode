package test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import test.test2.GameController;
import test.test2.MapData;
import test.test2.MapView;

public class SamplepracticeApp extends Application {


    @Override
    public void start(Stage stage) {
        // 起動したら練習モードへ
        showPractice(stage);
    }

    // 練習モード画面へ遷移
    public void showPractice(Stage stage) {
        try {
            sample.practice practiceScreen = new sample.practice();
            practiceScreen.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // STAGE1（ゲーム本編）へ遷移
    public void starts(Stage stage) {
    	    // STAGE1 のゲームロジック（エサ復活つき）
        MapData model = new MapData(true);
        // 描画処理（ステージ・キャラ・アイテム）
        MapView view = new MapView(model);

        Group root = new Group();
        // マップサイズに合わせてウィンドウサイズを決定
        int viewWidth = model.getMap()[0].length * MapData.TILE_SIZE;
        int viewHeight = model.getMap().length * MapData.TILE_SIZE;

        // 画面（Scene）を作成
        Scene scene = new Scene(root, viewWidth, viewHeight, Color.BLACK);
        Canvas canvas = new Canvas(viewWidth, viewHeight);
        root.getChildren().add(canvas);
        // コントローラーを起動（ゲームループ開始

		// GameController を起動
		new GameController(model, view, canvas, scene, stage);

		stage.setTitle("JavaFX Pacman Stage MVC");
		stage.setScene(scene);
		stage.show();
	}
}
