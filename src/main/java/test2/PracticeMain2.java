package test2;

import control.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import test2.model.MapData;
import test2.view.MapView;

// パックマン・練習用ステージの起動クラス
public class PracticeMain2 extends Application {

	private GameController controller;

	@Override
	public void start(Stage stage) {
		starts(stage);
	}


	public void starts(Stage stage) {
		// 多重起動を確実に防止
		if (this.controller != null) {
			this.controller.stop();
		}

		// ストーリーモードはエサ復活なし
        MapData model = new MapData(false);
        Pane root = new Pane();

		MapView view = new MapView(model, root);


		int viewWidth = model.getMap()[0].length * MapData.TILE_SIZE;
		int viewHeight = model.getMap().length * MapData.TILE_SIZE;

		Scene scene = new Scene(root, viewWidth, viewHeight);
		scene.getStylesheets().add(
				getClass().getResource("/css/test.css").toExternalForm());

		root.getStyleClass().add("stage1");

		// ★背景用Pane（CSSを効かせる対象）
		Pane bg = new Pane();
		bg.getStyleClass().add("game-bg");
		bg.setPrefSize(viewWidth, viewHeight);
		bg.setMouseTransparent(true);

		try {
			// src/main/resources/picture/companyroom.jpg から画像を読み込む
			//Image backgroundImage = new Image(getClass().getResourceAsStream("/picture/companyroom.jpg"));
			Image backgroundImage = new Image(getClass().getResourceAsStream("/picture/insert.png"));
			ImageView backgroundView = new ImageView(backgroundImage);

			// 画像のサイズも、ウィンドウ（root）のサイズに完全に連動（バインド）させる
			backgroundView.fitWidthProperty().bind(root.widthProperty());
            backgroundView.fitHeightProperty().bind(root.heightProperty());
            backgroundView.setPreserveRatio(false);

			// 背景用Paneに画像を追加
			bg.getChildren().add(backgroundView);
		} catch (Exception e) {
			System.out.println("⚠️ 背景画像の読み込みに失敗しました。パスを確認してください: " + e.getMessage());
		}

		// ★ゲーム描画Canvas
		Canvas canvas = new Canvas();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

		root.getChildren().addAll(bg, canvas);

		//敵描画呼び出し
		model.initEnemy(new javafx.scene.image.ImageView());

		//  準備ができたコントローラーを生成
		this.controller = new GameController(model, view, canvas, scene, stage, 1);

		stage.setTitle("JavaFX Pacman Stage MVC");
		stage.setScene(scene);
		stage.show();

		canvas.requestFocus();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
