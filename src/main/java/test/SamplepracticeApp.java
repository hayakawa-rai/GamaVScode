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

public class SamplepracticeApp extends Application {
<<<<<<< HEAD
	@Override
	public void start(Stage stage) {
		// 起動した瞬間に、ボタン操作なしでコントローラーの遷移処理を呼び出す
		SampleController.switchToStart(stage);
	}
=======

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
        SampleModel model = new SampleModel();
        // 描画処理（ステージ・キャラ・アイテム）
        SampleView view = new SampleView(model);
>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git

<<<<<<< HEAD
	public void starts(Stage stage) {
		SampleModel model = new SampleModel();
		SampleView view = new SampleView(model);
=======
        Group root = new Group();
        // マップサイズに合わせてウィンドウサイズを決定
        int viewWidth = model.getMap()[0].length * SampleModel.TILE_SIZE;
        int viewHeight = model.getMap().length * SampleModel.TILE_SIZE;
>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git

<<<<<<< HEAD
		Group root = new Group();
		int viewWidth = model.getMap()[0].length * SampleModel.TILE_SIZE;
		int viewHeight = model.getMap().length * SampleModel.TILE_SIZE;

		Scene scene = new Scene(root, viewWidth, viewHeight, Color.BLACK);
		Canvas canvas = new Canvas(viewWidth, viewHeight);
		root.getChildren().add(canvas);
=======
        // 画面（Scene）を作成
        Scene scene = new Scene(root, viewWidth, viewHeight, Color.BLACK);
        Canvas canvas = new Canvas(viewWidth, viewHeight);
        root.getChildren().add(canvas);
        // コントローラーを起動（ゲームループ開始
        
        new SampleController(model, view, canvas, scene);
>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git

<<<<<<< HEAD
		// SampleController を起動
		new SampleController(model, view, canvas, scene);

		stage.setTitle("JavaFX Pacman Stage MVC");
		stage.setScene(scene);
		stage.show();
	}
=======
        // ウィンドウ設定
        stage.setTitle("JavaFX Pacman Stage MVC");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git
}
