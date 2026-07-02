package test3;

import control.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import test1.model.MapData;
import test1.view.MapView;

public class Main3 extends Application {

	private GameController controller;

	@Override
	public void start(Stage stage) {
		starts(stage);
	}

	public static void createAndStart(Stage stage) {
		Main3 app = new Main3();
		app.starts(stage);
	}

	public void starts(Stage stage) {
		// 多重起動を確実に防止
		if (this.controller != null) {
			this.controller.stop();
		}

		MapData model = new MapData();
		Pane root = new Pane();

		MapView view = new MapView(model, root);

		int viewWidth = model.getMap()[0].length * MapData.TILE_SIZE;
		int viewHeight = model.getMap().length * MapData.TILE_SIZE;

		//Scene scene = new Scene(root, viewWidth, viewHeight);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(
				getClass().getResource("/css/test.css").toExternalForm());

		root.getStyleClass().add("stage1");

		// ★背景用Pane（CSSを効かせる対象）
		Pane bg = new Pane();
		bg.getStyleClass().add("game-bg");
		//bg.setPrefSize(viewWidth, viewHeight);

		bg.prefWidthProperty().bind(scene.widthProperty());
		bg.prefHeightProperty().bind(scene.heightProperty());

		bg.setMouseTransparent(true);

		try {
			// src/main/resources/picture/companyroom.jpg から画像を読み込む
			Image backgroundImage = new Image(getClass().getResourceAsStream("/picture/emd-nottori.jpg"));
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

		model.initEnemy(null);
		/*コメントで隠してるのが前の描写方法  
		// 先に空の ImageView を用意
		javafx.scene.image.ImageView redImageView = new javafx.scene.image.ImageView();
		
		// モデル側で敵を生成し、内部で画像（Image）を確実にセットさせる
		model.initEnemy(redImageView);
		
		//  画像が入った「後」に、ビューを通してサイズを30x30にフィットさせる
		view.setupEnemyView(redImageView);
		
		//  敵の ImageView を画面に登録
		root.getChildren().add(redImageView);
		*/
		//敵描画呼び出し　成田
		model.initEnemy(new javafx.scene.image.ImageView());

		//完璧に準備ができた最後にコントローラーを1回だけ生成
		this.controller  = new GameController(model, view, canvas, scene, stage, 3, false);

		stage.setTitle("JavaFX Pacman Stage MVC");
		stage.setScene(scene);
		// ★追加
		stage.show();

		stage.setMaximized(false);
		javafx.application.Platform.runLater(() ->{
			stage.setMaximized(true);
		});
		canvas.requestFocus();

	}
		

	public static void main(String[] args) {
		launch(args);
	}
}
